package com.hjg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class LhZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(LhZuulApplication.class, args);
	}

}
