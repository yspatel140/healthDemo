package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
   
	@Autowired
	HelloService helloService;
	
	@RequestMapping("/hello")
	public Person hello(){
		return helloService.getPerson();
	}
}
