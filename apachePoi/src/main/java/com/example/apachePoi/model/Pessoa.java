package com.example.apachePoi.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pessoa {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	private String nome;
	private Double ramal;
	private Double chamadasEfetuadas;
	private Double chamadasRecebidas;
	@Enumerated(EnumType.STRING)
	private TipoLicacao tipoLigacao;
	private String chamadasDeEntrada;
	private String chamadasDeSaida;
	
	public String getChamadasDeEntrada() {
		return chamadasDeEntrada;
	}
	public void setChamadasDeEntrada(String chamadasDeEntrada) {
		this.chamadasDeEntrada = chamadasDeEntrada;
	}
	public String getChamadasDeSaida() {
		return chamadasDeSaida;
	}
	public void setChamadasDeSaida(String chamadasDeSaida) {
		this.chamadasDeSaida = chamadasDeSaida;
	}
	public Double getChamadasEfetuadas() {
		return chamadasEfetuadas;
	}
	public void setChamadasEfetuadas(Double chamadasEfetuadas) {
		this.chamadasEfetuadas = chamadasEfetuadas;
	}
	public Double getChamadasRecebidas() {
		return chamadasRecebidas;
	}
	public void setChamadasRecebidas(Double chamadasRecebidas) {
		this.chamadasRecebidas = chamadasRecebidas;
	}
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getRamal() {
		return ramal;
	}
	public void setRamal(Double ramal) {
		this.ramal = ramal;
	}
	public TipoLicacao getTipoLigacao() {
		return tipoLigacao;
	}
	public void setTipoLigacao(TipoLicacao tipoLigacao) {
		this.tipoLigacao = tipoLigacao;
	}
	
	public Pessoa(Long codigo, String nome, Double ramal, Double chamadasEfetuadas, Double chamadasRecebidas,
			TipoLicacao tipoLigacao) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.ramal = ramal;
		this.chamadasEfetuadas = chamadasEfetuadas;
		this.chamadasRecebidas = chamadasRecebidas;
		this.tipoLigacao = tipoLigacao;
	}
	public Pessoa() {
		super();
		// TODO Auto-generated constructor stub
	}
}
