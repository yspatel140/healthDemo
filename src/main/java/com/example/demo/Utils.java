package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Utils {
	
	@Bean
	RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
