package com.demo.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IoTApplication {
	public static void main(String[] args) {
		SpringApplication.run(IoTApplication.class, args);
	}
}