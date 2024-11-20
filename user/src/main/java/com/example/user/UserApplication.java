package com.example.user;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.user.mapper")
public class UserApplication {

	public static void main(String[] args) {
		System.setProperty("jdk.xml.accessExternalDTD", "all");
		SpringApplication.run(UserApplication.class, args);
	}

}
