package com.example.demo;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationHealthIndicator extends AbstractHealthIndicator {

	@Autowired
	private RestTemplate restTemplate;
	
//	@Autowired
//	HelloService helloService;
	
	//@Value("${external.web.service}")
	//private String url;
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		//System.out.print("URL" + url);
		try{
			restTemplate.getForObject("http://localhost:8089", String.class);
		}catch(HttpClientErrorException dce){
			builder.up().withDetail("external-web-service", "UP");
		}catch (ResourceAccessException e) {
			builder.up().withDetail("external-web-service", "DOWN");
		}
		
	}

}
