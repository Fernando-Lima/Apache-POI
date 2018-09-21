package com.example.apachePoi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.apachePoi.model.Pessoa;
import com.example.apachePoi.model.Resposta;

@Repository
public interface ApachePOIRepository extends JpaRepository<Pessoa,Long>{
//	@Query(value = "select pessoa.ramal as 'Ramal',\n" + 
//			"DATE_FORMAT(SEC_TO_TIME(SUM(chamadas_recebidas)), '%H:%i:%s') as 'Chamadas de entrada',\n" + 
//			"DATE_FORMAT(SEC_TO_TIME(SUM(chamadas_efetuadas)), '%H:%i:%s') as 'Chamadas de saida'\n" + 
//			"from pessoa where pessoa.ramal is not null \n" + 
//			"group by pessoa.ramal;",
//			nativeQuery = true)
	@Query(value = "select pessoa.ramal from pessoa where pessoa.codigo = 8",
			nativeQuery = true)
			Resposta findByRamal();
}
