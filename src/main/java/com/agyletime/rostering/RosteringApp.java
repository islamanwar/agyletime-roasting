package com.agyletime.rostering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class RosteringApp extends SpringBootServletInitializer{

	private static Class<RosteringApp> applicationClass = RosteringApp.class;
	
	public static void main(String[] args) {
		SpringApplication.run(RosteringApp.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(applicationClass);
	}
}
