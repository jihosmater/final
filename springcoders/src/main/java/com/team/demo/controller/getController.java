package com.team.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sss/*")
public class getController {
	

	@GetMapping("sss")
	public void ss() {

	}
}
