package com.example.ActLikeMinions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ActLikeMinionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActLikeMinionsApplication.class, args);
	}

}
