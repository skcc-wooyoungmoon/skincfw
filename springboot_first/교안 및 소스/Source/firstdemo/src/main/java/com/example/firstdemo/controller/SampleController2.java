package com.example.firstdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController2 {
	
	/*
	@GetMapping("/post")
	public String demoPost() {
		
		return "/post/post";  // post.html
	}
	*/
	
	@PostMapping("/post")
	public String demoPost( 
			@RequestParam String membername,
			@RequestParam String memberid,
			@RequestParam String memberemail, 
			Model model ) {
		
		model.addAttribute( "membername", membername );
		model.addAttribute( "memberid", memberid );
		model.addAttribute( "memberemail", memberemail );
		
		return "/post/post";  // post.html
	}
}















