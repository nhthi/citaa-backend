package com.citaa.citaa;

import com.citaa.citaa.service.QuickstartSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CitaaApplication {
	@Autowired
	private QuickstartSample quickstartSample;

	public static void main(String[] args) {
		SpringApplication.run(CitaaApplication.class, args);
	}
}
