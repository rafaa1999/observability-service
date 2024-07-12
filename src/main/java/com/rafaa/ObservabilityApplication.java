package com.rafaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ObservabilityApplication {

	public static final Logger log = LoggerFactory.getLogger(ObservabilityApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ObservabilityApplication.class, args);
		log.info("Starting the project using gradle");
	}

}
