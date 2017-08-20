package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

	Person getPerson(){
		Person person=new Person("yoge", "12");
		return person;
	}
}
