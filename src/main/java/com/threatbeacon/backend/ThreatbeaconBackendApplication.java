package com.threatbeacon.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThreatbeaconBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreatbeaconBackendApplication.class, args);
	}

}
