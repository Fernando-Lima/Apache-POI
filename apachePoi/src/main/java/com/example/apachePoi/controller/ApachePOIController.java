package com.example.apachePoi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apachePoi")
public class ApachePOIController {

	@RequestMapping
	public String index() {
		return "index";
	}
	
}
