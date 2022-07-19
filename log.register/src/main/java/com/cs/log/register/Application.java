package com.cs.log.register;

import com.cs.log.register.service.ReadService;
import com.cs.log.register.service.WriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories("com.cs.log.register.repository")
@EntityScan("com.cs.log.register.entity")
@Slf4j
public class Application implements CommandLineRunner {

   @Autowired
   ReadService readService;
   @Autowired
   WriteService writeService;

	public static void main(String[] args) {
		log.info("Application started.");
		Instant start = Instant.now();
		SpringApplication.run(Application.class, args);
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		log.info("Application finished.");
		log.info("Total time : "+timeElapsed+" milisec.");
	}

	@Override
	public void run(String... args) throws Exception {
		readService.readLogFile(args[0]);
		writeService.writeEventDetails();
	}
}
