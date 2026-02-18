package com.example.firstdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController1 {

	@GetMapping("/user/userinfo")
	public String userInfo() {
		
		return "/user/userinfo";
	}
	
	@GetMapping("/user/userdata")
	public String userData( Model model ) {
		
		model.addAttribute( "username", "홍길동" );
		
		return "/user/userinfo";
	}
	
	@GetMapping("/user/userid")
	public String userId( @RequestParam( value="userid", required=false ) String userid, Model model ) {
		
		
		model.addAttribute( "userid", userid );
		
		return "/user/userinfo";  // userinfo.html
	}
	
	@GetMapping("/user/userparams")
	public String userParams( 
			@RequestParam( value="userid" ) String uid,
			@RequestParam( value="username" ) String uname,
			@RequestParam( value="useremail" ) String uemail,
			@RequestParam( value="userhp", required=false ) String uhp,
			Model model	) {
		
		model.addAttribute( "userid", uid );
		model.addAttribute( "username", uname );
		model.addAttribute( "useremail", uemail );
		model.addAttribute( "userhp", uhp );
		
		return "/user/userinfo";
	}	
}



















