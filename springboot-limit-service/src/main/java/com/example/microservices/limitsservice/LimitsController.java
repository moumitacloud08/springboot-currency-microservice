package com.example.microservices.limitsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {
	
	@Autowired
	Configuration configuration;

	@GetMapping("/limits")
	public LimitConfiguration retrieveLimits() {
		
		return new LimitConfiguration(configuration.getMaximum(), configuration.getMinimum());
		//return new LimitConfiguration(1,1000);
	}
}
