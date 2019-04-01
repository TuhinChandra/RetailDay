package com.tcs.KingfisherDay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tcs.KingfisherDay")
public class KingfisherDayApplication {

	public static void main(String[] args) {
		SpringApplication.run(KingfisherDayApplication.class, args);
	}
}
