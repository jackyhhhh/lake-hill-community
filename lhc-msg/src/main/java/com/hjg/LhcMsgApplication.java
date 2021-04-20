package com.hjg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableEurekaClient
public class LhcMsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(LhcMsgApplication.class, args);
	}

}
