package com.hjg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LhEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LhEurekaServerApplication.class, args);
	}

}
