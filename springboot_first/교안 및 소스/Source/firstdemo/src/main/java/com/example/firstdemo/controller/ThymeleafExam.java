package com.example.firstdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafExam {

	@GetMapping("/thleaf")
	public String thExam( Model model ) {
		
		String username = "Spring Boot with Thymeleaf Template Engine";
		model.addAttribute( "username", username );
		
		String message = "<b>Hello World!</b>";
		model.addAttribute( "message", message );
		
		return "thymeleaf";  	// thymeleaf.html
	}
}





