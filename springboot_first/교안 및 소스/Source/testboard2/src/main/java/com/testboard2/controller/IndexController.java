package com.testboard2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/fragments")
	public String indexFragments() {
		
		return "/tpl/tpl_fragments";  // tpl_fragments.html
	}

	@GetMapping("/main")
	public String indexMain() {
		
		return "/tpl/tpl_main";  // tpl_main.html
	}

	@GetMapping("/sub")
	public String indexSub() {
		
		return "/tpl/tpl_sub";  // tpl_sub.html
	}
	
	@GetMapping("/fragments_bs_topmenu")
	public String indexBsTopmenu() {
		
		return "/tpl/tpl_fragments_bs_topmenu";
	} 
	
	@GetMapping("/fragments_bs_topmenu2")
	public String indexBsTopmenu2() {
		
		return "/tpl/tpl_fragments_bs_topmenu2";
	}
	
	@GetMapping("mainpage")
	public String indexMainPage() {
		
		return "/tpl2/mainpage";  // mainpage.html
	}

}


























