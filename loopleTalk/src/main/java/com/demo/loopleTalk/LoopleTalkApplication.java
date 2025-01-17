package com.demo.loopleTalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LoopleTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoopleTalkApplication.class, args);
	}

}
