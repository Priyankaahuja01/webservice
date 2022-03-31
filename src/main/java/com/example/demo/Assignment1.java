package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.UserRepository;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
public class Assignment1 {

	public static void main(String[] args) {
		SpringApplication.run(Assignment1.class, args);
	}

}

@RestController
class A1Controller{
	@GetMapping(value="/healthz")
	public ResponseEntity<String> getStatus() {
		return new ResponseEntity<>("Hellooooooooooooooo", org.springframework.http.HttpStatus.OK);
	}
	
}
