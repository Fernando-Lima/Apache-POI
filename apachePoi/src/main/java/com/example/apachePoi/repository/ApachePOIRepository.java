package com.example.apachePoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apachePoi.model.Pessoa;

@Repository
public interface ApachePOIRepository extends JpaRepository<Pessoa, Long>{

}
