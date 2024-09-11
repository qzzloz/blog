package com.assgn.yourssu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YourssuApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourssuApplication.class, args);
	}

}
