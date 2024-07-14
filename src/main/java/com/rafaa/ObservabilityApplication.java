package com.rafaa;

import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservabilityApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}


	@RestController
	class HelloController {

		private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
		private final RestTemplate restTemplate;
		private final SleepService sleepService;

		HelloController(RestTemplate restTemplate, SleepService sleepService) {
			this.restTemplate = restTemplate;
            this.sleepService = sleepService;
        }

		@GetMapping("/hello")
		public String hello() {
			LOGGER.info("---------Hello method started---------");
			LOGGER.error("---------Hello method started, id missing!---------");
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello, Cloud!", String.class);
			return responseEntity.getBody();
		}

		@GetMapping("/sleep")
		public Long sleep(@RequestParam Long ms) {
			Long result = this.sleepService.doSleep(ms);
			return result;
		}


		@GetMapping("/exception")
		public String exception() {
			throw new IllegalArgumentException("This id is invalid");
		}

		@ExceptionHandler(value = { IllegalArgumentException.class })
		protected ResponseEntity<String> handleConflict(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().body(ex.getMessage());
		}

	}

	@Service
	class SleepService{

		@Timed(value = "do_sleep_method_timed")
		public Long doSleep(Long ms) {
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return ms;
		}

	}

}
