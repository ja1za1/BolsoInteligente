package bolsointeligente.entities;

import java.time.LocalDate;

public class Investimento {
// Objetivo;Estratégia;Nome;Valor Investido;Posição; Rendimento Bruto;Rentabilidade;Vencimento
// Viagem;Pós-fixado; CDB Original Jan/2026; 2.000; 2.789,45; 789,45; 39,47% ; 14/01/2026
	
	private String objetivo,
				   estrategia,
				   nome;
	
	private float valorInvestido,
				  posicao,
				  rendimentoBruto,
				  rentabilidade;
	
	private LocalDate vencimento;

	public Investimento(String objetivo, String estrategia, String nome, float valorInvestido, float posicao,
			float rendimentoBruto, float rentabilidade, LocalDate vencimento) {
		this.objetivo = objetivo;
		this.estrategia = estrategia;
		this.nome = nome;
		this.valorInvestido = valorInvestido;
		this.posicao = posicao;
		this.rendimentoBruto = rendimentoBruto;
		this.rentabilidade = rentabilidade;
		this.vencimento = vencimento;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getValorInvestido() {
		return valorInvestido;
	}

	public void setValorInvestido(float valorInvestido) {
		this.valorInvestido = valorInvestido;
	}

	public float getPosicao() {
		return posicao;
	}

	public void setPosicao(float posicao) {
		this.posicao = posicao;
	}

	public float getRendimentoBruto() {
		return rendimentoBruto;
	}

	public void setRendimentoBruto(float rendimentoBruto) {
		this.rendimentoBruto = rendimentoBruto;
	}

	public float getRentabilidade() {
		return rentabilidade;
	}

	public void setRentabilidade(float rentabilidade) {
		this.rentabilidade = rentabilidade;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}
	
	
	
	
}
