package com.selahattindev.portfolio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.model.User;
import com.selahattindev.portfolio.repository.UserRepository;
import com.selahattindev.portfolio.utils.Roles;

@RestController
@SpringBootApplication
public class PortfolioApplication {

	@GetMapping("/")
	public String home() {
		return "Welcome to Selahattin's Portfolio API. <a href='/h2-console'>Go to H2 Console</a> to see the database.";
	}

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner userCreater(UserRepository userRepository) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return (args) -> {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("Adminpass1."));
			admin.setRoles(Roles.ADMIN.name());
			userRepository.save(admin);
			for (var i = 1; i <= 25; i++) {
				User user = new User();
				user.setUsername("user" + i);
				user.setPassword(passwordEncoder.encode("P4ssword" + i));
				userRepository.save(user);
			}
		};
	}

}
