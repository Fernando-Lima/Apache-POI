package com.example.apachePoi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.apachePoi.model.Pessoa;
import com.example.apachePoi.model.TipoLicacao;
import com.example.apachePoi.repository.ApachePOIRepository;

@Controller
@RequestMapping("/apachePoi")
public class ApachePOIController {
	
	private static String UPLOADED_FOLDER = "/home/fernando/ApachePOI/";

	private static Path path;
	
	@Autowired
	ApachePOIRepository repository;

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
			
			salvar(redirectAttributes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/apachePoi";
	}
	
	public String salvar(RedirectAttributes attributes) throws IOException {
		List<Pessoa> listaPessoas = new ArrayList<Pessoa>();

		try {
			FileInputStream arquivo = new FileInputStream(new File(String.valueOf(ApachePOIController.path)));

			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);

			HSSFSheet sheetAlunos = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheetAlunos.iterator();

			while (rowIterator.hasNext()) {
				
				Row row = rowIterator.next();
				if (row.getRowNum() != 0) {
					Iterator<Cell> cellIterator = row.cellIterator();

					Pessoa pessoa = new Pessoa();
					pessoa.setTipoLigacao(TipoLicacao.EFETUADA);
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						listaPessoas.add(pessoa);
						
						if(cell.getColumnIndex() == 1 && cell.getCellType() == 1) {
							rowIterator.next();
							break;
						}else {
							listaPessoas.add(pessoa);
							switch (cell.getColumnIndex()) {
							
							case 1:
								if(cell.getNumericCellValue()>43) {
									//camada de entrada
									pessoa.setTipoLigacao(TipoLicacao.RECEBIDA);
									break;
								}else {
									pessoa.setRamal(cell.getNumericCellValue());
									break;
								}
							
							case 2:
								if (pessoa.getTipoLigacao().equals(TipoLicacao.RECEBIDA) && cell.getNumericCellValue() < 43) {
									pessoa.setRamal(cell.getNumericCellValue());
								}
								break;
							case 12:
								if(pessoa.getTipoLigacao().equals(TipoLicacao.RECEBIDA)) {
									pessoa.setChamadasRecebidas(cell.getNumericCellValue());
								}else {
									pessoa.setChamadasEfetuadas(cell.getNumericCellValue());
								}
								break;
							case 19:
								pessoa.setNome(cell.getStringCellValue());
								break;
							}
						}
					}
				}
			}
			arquivo.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			attributes.addFlashAttribute("message", "Arquivo Excel não encontrado!");
			System.out.println("Arquivo Excel não encontrado!");
		}

		if (listaPessoas.size() == 0) {
			attributes.addFlashAttribute("message", "Nenhuma Pessoa encontrada!");
			System.out.println("Nenhuma Pessoa encontrada!");
		} else {
			try {
				repository.saveAll(listaPessoas);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return "redirect:/tinsul";
	}
	
}
