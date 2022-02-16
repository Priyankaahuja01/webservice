package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	A1Controller a ;
	@Test
	void contextLoads() {
		a = new A1Controller();
		ResponseEntity<String> responseInvoiceResponseEntity = a.getStatus() ;
		assertEquals("200 OK",responseInvoiceResponseEntity.getStatusCode().toString());
		
	}

}
