package com.selahattindev.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
