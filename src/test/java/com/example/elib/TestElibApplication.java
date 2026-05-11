package com.example.elib;

import org.springframework.boot.SpringApplication;

public class TestElibApplication {

	public static void main(String[] args) {
		SpringApplication.from(ElibApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
