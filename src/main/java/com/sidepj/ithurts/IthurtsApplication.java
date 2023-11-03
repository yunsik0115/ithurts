package com.sidepj.ithurts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class IthurtsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IthurtsApplication.class, args);
	}

}
