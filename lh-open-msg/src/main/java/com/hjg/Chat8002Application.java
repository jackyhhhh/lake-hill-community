package com.hjg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
@EnableEurekaClient
public class Chat8002Application {

	public static void main(String[] args) {
		SpringApplication.run(Chat8002Application.class, args);
	}

}
