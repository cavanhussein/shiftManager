package com.when.i.work.shift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController
public class ShiftApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiftApplication.class, args);
	}

	@GetMapping("/api/shift")
	public void getShifts() {
		System.out.println("test");
	}
}
