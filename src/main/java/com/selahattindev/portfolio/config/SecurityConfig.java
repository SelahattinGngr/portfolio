package com.selahattindev.portfolio.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/h2-console/**").permitAll() // H2 console'u aç
                                                .anyRequest().authenticated() // diğer her şey için login gerektir
                                )
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**") // H2 için CSRF'yi kapat
                                )
                                .headers(headers -> headers
                                                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 iframe
                                                                                                         // izinleri
                                )
                                .formLogin(withDefaults());

                return http.build();
        }
}
