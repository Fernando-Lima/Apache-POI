package com.example.apachePoi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.apachePoi.model.Pessoa;
import com.example.apachePoi.model.Resposta;

public interface ApachePOIRepository extends JpaRepository<Pessoa,Long>{
	@Query(value = "select pessoa.ramal as 'Ramal',\n" + 
			"DATE_FORMAT(SEC_TO_TIME(SUM(pessoa.chamadas_recebidas)), '%H:%i:%s') as 'ChamadasDeEntrada',\n" + 
			"DATE_FORMAT(SEC_TO_TIME(SUM(pessoa.chamadas_efetuadas)), '%H:%i:%s') as 'ChamadasDeSaida'\n" + 
			"from pessoa where pessoa.ramal is not null \n" + 
			"group by pessoa.ramal;",
			nativeQuery = true)
			List<Resposta> findByRamal();
	
}
