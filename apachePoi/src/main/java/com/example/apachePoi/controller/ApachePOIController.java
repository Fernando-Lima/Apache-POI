package com.example.apachePoi.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/apachePoi")
public class ApachePOIController {
	
	private static String UPLOADED_FOLDER = "/home/fernando/ApachePOI/";

	private static Path path;

	@RequestMapping
	public String index() {
		return "index";
	}
	
	@PostMapping
	public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) {
		
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Por favor selecione um arquivo");
			return "redirect:/apachePoi";
		}
		
		try {
			// Pega o arquivo e salva em algum lugar
			byte[] bytes = file.getBytes();
			path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			redirectAttributes.addFlashAttribute("message",
					"Arquivo  enviado com sucesso'" + file.getOriginalFilename() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/apachePoi";
		
	}
	
}
