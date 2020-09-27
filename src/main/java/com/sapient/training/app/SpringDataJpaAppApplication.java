package com.sapient.training.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sapient.training")
@EnableJpaRepositories(basePackages = "com.sapient.training.data")
@EntityScan(basePackages = "com.sapient.training.entity")
public class SpringDataJpaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaAppApplication.class, args);
	}

}
