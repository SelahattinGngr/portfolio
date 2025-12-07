package com.selahattindev.portfolio.service.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.request.SigninRequestDto;
import com.selahattindev.portfolio.dto.request.SignupRequestDto;
import com.selahattindev.portfolio.dto.request.VerifyTwoFaRequestDto;
import com.selahattindev.portfolio.dto.response.CookieDto;
import com.selahattindev.portfolio.dto.response.EmailMessageDto;
import com.selahattindev.portfolio.dto.response.GoogleTwoFaResponseDto;
import com.selahattindev.portfolio.dto.response.SigninResponseDto;
import com.selahattindev.portfolio.exception.UserAlreadyExistsException;
import com.selahattindev.portfolio.model.User;
import com.selahattindev.portfolio.repository.UserRepository;
import com.selahattindev.portfolio.security.UserDetailsImpl;
import com.selahattindev.portfolio.security.UserDetailsServiceImpl;
import com.selahattindev.portfolio.security.jwt.TokenProvider;
import com.selahattindev.portfolio.service.infra.CookieService;
import com.selahattindev.portfolio.service.infra.OtpService;
import com.selahattindev.portfolio.service.infra.RedisQueueService;
import com.selahattindev.portfolio.service.infra.TokenService;
import com.selahattindev.portfolio.service.infra.TwoFactorService;
import com.selahattindev.portfolio.utils.cookie.CookieFactory;
import com.selahattindev.portfolio.utils.enums.Roles;
import com.selahattindev.portfolio.utils.enums.TwoFaType;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final UserDetailsServiceImpl userDetailsService;
        private final TokenProvider tokenProvider;
        private final CookieFactory cookieFactory;
        private final CookieService cookieService;
        private final TokenService tokenService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final OtpService otpService;
        private final RedisQueueService queueService;
        private final TwoFactorService twoFactorService;

        public SigninResponseDto signin(SigninRequestDto dto, HttpServletResponse response) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(dto.getSignin(), dto.getPassword()));

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                User user = userDetails.getUser();

                if (user.getTwoFaType() != TwoFaType.NONE) {
                        String otp = ""; // test amaçlı burada if içine tanımlanacak
                        if (user.getTwoFaType() == TwoFaType.EMAIL) {
                                otp = otpService.generateOtp();
                                otpService.saveOtp(user.getUsername(), otp);

                                EmailMessageDto emailDto = EmailMessageDto.builder()
                                                .to(user.getEmail())
                                                .subject("Giriş Kodunuz")
                                                .content("Kodunuz: " + otp)
                                                .build();
                                queueService.enqueueEmail(emailDto);
                        }

                        return SigninResponseDto.builder()
                                        .username(user.getUsername())
                                        .isTwoFaRequired(true)
                                        .twoFaType(user.getTwoFaType().toString())
                                        .otp(otp)
                                        .build();
                }

                return generateTokensAndReturnResponse(userDetails, response);
        }

        public void signup(SignupRequestDto dto) {
                if (userRepository.existsByUsername(dto.getUsername())) {
                        throw new UserAlreadyExistsException(dto.getUsername());
                }

                var user = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .roles(Roles.ROLE_USER.toString())
                                .build();

                userRepository.save(user);
        }

        public void signout(String deviceId, String refreshToken, HttpServletResponse response) {
                String username = tokenProvider.extractUsernameFromRefreshToken(refreshToken);
                tokenService.deleteToken(username, deviceId);
                cookieService.clearCookies(response);
        }

        public void refreshToken(String refreshToken, String deviceId, HttpServletResponse response) {
                if (!tokenProvider.validateRefreshToken(refreshToken)) {
                        throw new RuntimeException("Invalid refresh token");
                }

                String username = tokenProvider.extractUsernameFromRefreshToken(refreshToken);
                String storedToken = tokenService.getToken(username, deviceId);

                if (storedToken == null || !storedToken.equals(refreshToken)) {
                        throw new RuntimeException("Token not found or invalid device");
                }

                tokenService.deleteToken(username, deviceId);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
                CookieDto cookieDto = cookieFactory.create(userDetails);
                tokenService.storeToken(userDetails.getUsername(), cookieDto);

                cookieService.refreshCookies(cookieDto, response);
        }

        public SigninResponseDto verifyTwoFa(VerifyTwoFaRequestDto dto, HttpServletResponse response) {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                                .loadUserByUsername(dto.getUsername());
                User user = userDetails.getUser();

                boolean isValid = false;

                if (user.getTwoFaType() == TwoFaType.EMAIL) {
                        isValid = otpService.validateOtp(user.getUsername(), dto.getCode());
                } else if (user.getTwoFaType() == TwoFaType.GOOGLE) {
                        try {
                                int code = Integer.parseInt(dto.getCode());
                                isValid = twoFactorService.isOtpValid(user.getTwoFaSecret(), code);
                        } catch (NumberFormatException e) {
                                isValid = false;
                        }
                }

                if (!isValid) {
                        throw new RuntimeException("Geçersiz Kod!");
                }

                return generateTokensAndReturnResponse(userDetails, response);
        }

        private SigninResponseDto generateTokensAndReturnResponse(UserDetailsImpl userDetails,
                        HttpServletResponse response) {
                CookieDto cookieDto = cookieFactory.create(userDetails);
                tokenService.storeToken(userDetails.getUsername(), cookieDto);
                cookieService.createCookies(cookieDto, response);

                return SigninResponseDto.builder()
                                .username(userDetails.getUsername())
                                .role(userDetails.getRole())
                                .isTwoFaRequired(false)
                                .build();
        }

        public GoogleTwoFaResponseDto setupGoogleTwoFa(String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

                String secret = twoFactorService.generateNewSecret();
                String qrUrl = twoFactorService.generateQrCodeImageUri(secret, username);

                user.setTwoFaSecret(secret);
                user.setTwoFaType(TwoFaType.GOOGLE);
                userRepository.save(user);

                return new GoogleTwoFaResponseDto(secret, qrUrl);
        }
}