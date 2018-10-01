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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.apachePoi.model.Pessoa;
import com.example.apachePoi.model.Resposta;
import com.example.apachePoi.model.TipoLicacao;
import com.example.apachePoi.repository.ApachePOIRepository;

@Controller
@RequestMapping("/apachePoi")
public class ApachePOIController{

	/*
	 * TODO Para pegar o tipo String ou Numérico de um celula utiliza-se o metodo
	 * getCellType se retornar 0 = Numérico e se retornar 1 = String
	 */

	/*
	 * TODO Script SQL para calcular o tempo de ligações
	 * 
	 * select pessoa.ramal as 'Ramal',
	 * DATE_FORMAT(SEC_TO_TIME(SUM(pessoa.chamadas_recebidas)), '%H:%i:%s') as 'ChamadasDeEntrada', 
	 * DATE_FORMAT(SEC_TO_TIME(SUM(pessoa.chamadas_efetuadas)), '%H:%i:%s') as
	 * 'ChamadasDeSaida' from pessoa where pessoa.ramal is not null group by
	 * pessoa.ramal;
	 * 
	 */

	// System.out.println("Linha >> " + row.getRowNum() + "| Coluna >> " +
	// cell.getColumnIndex() + "Tipo >> " + cell.getCellType());

	private static String UPLOADED_FOLDER = "/home/fernando/ApachePOI/";

	private static Path path;
	private String mensagem = "Erro ao salvar arquivo";
	
	@Autowired
	ApachePOIRepository repository;
	

	@RequestMapping
	public String index() {
		return "index";
	}

	//Metodo para fazer o upload do arquivo, deve ser formato xml
	@PostMapping
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Por favor selecione um arquivo");
			return "redirect:/apachePoi";
		}

		try {
			// Pega o arquivo e salva em algum lugar
			byte[] bytes = file.getBytes();
			path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			
			try {
				salvar();
				redirectAttributes.addFlashAttribute("messageOk", mensagem);
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message", mensagem);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/apachePoi";
	}

	//Metodo para salvar os dados no Banco de dados, pega as informações das colunas 1,2,12 e 19
	//Tanto a coluna quanto linha começa em 0 "Zero".
	public String salvar() throws IOException {
		List<Pessoa> listaPessoas = new ArrayList<Pessoa>();

		try {
			FileInputStream arquivo = new FileInputStream(new File(String.valueOf(ApachePOIController.path)));

			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);

			HSSFSheet sheetAlunos = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheetAlunos.iterator();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				if (row.getRowNum() != 0) { // pula a primeira linha (Cabeçalho da tabela)
					Iterator<Cell> cellIterator = row.cellIterator();

					Pessoa pessoa = new Pessoa();
					pessoa.setTipoLigacao(TipoLicacao.EFETUADA);

					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						listaPessoas.add(pessoa);

						listaPessoas.add(pessoa);

						if (cell.getColumnIndex() == 1 && cell.getCellType() == 1) {
							rowIterator.next();
							break;
						} else {
							switch (cell.getColumnIndex()) {

							case 1: 
								if (cell.getNumericCellValue() < 20 || cell.getNumericCellValue() > 43) {
									pessoa.setTipoLigacao(TipoLicacao.RECEBIDA);
									break;
								} else {
									pessoa.setRamal(Integer.valueOf((int) cell.getNumericCellValue()));
									break;
								}
							case 2:
								if (cell.getCellType() != 1) {
									if (pessoa.getTipoLigacao().equals(TipoLicacao.RECEBIDA)) {
										if (cell.getNumericCellValue() >= 20 && cell.getNumericCellValue() <= 43) {
											pessoa.setRamal(Integer.valueOf((int) cell.getNumericCellValue()));
										}
									}
								}
								break;
							case 12:
								if (pessoa.getTipoLigacao().equals(TipoLicacao.RECEBIDA)) {
									pessoa.setChamadasRecebidas(cell.getNumericCellValue());
									pessoa.setChamadasEfetuadas(0.0);
								} else {
									pessoa.setChamadasEfetuadas(cell.getNumericCellValue());
									pessoa.setChamadasRecebidas(0.0);
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
			
			mensagem = "Arquivo enviado com sucesso! ";
			arquivo.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mensagem = "Arquivo Excel não encontrado!";
		}

		if (listaPessoas.size() == 0) {
			mensagem = "Nenhuma Pessoa encontrada!";
		} else {
			try {
				repository.saveAll(listaPessoas);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mensagem;
	}
	
	@RequestMapping("/calcular")
	public ModelAndView buscarCalculo() {
		ModelAndView mv;
		try {
			List<Resposta> respostas = repository.findByRamal();
			Resposta resposta = null;
			List<Pessoa> pessoas = new ArrayList<>();
			for(int i = 0; i< respostas.size(); i++) {
				resposta = respostas.get(i);
				Pessoa pessoa = new Pessoa();
				pessoa.setRamal(resposta.getRamal());
				pessoa.setChamadasDeSaida(resposta.getChamadasDeSaida().toString());
				pessoa.setChamadasDeEntrada(resposta.getChamadasDeEntrada().toString());
				pessoas.add(pessoa);
			}
			mv = new ModelAndView("index");
			mv.addObject("calculo", pessoas);
		} catch (Exception e) {
			e.printStackTrace();
			mv = new ModelAndView("index");
		}
		return mv;
	}
}
