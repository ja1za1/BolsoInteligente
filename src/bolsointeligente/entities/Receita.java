package bolsointeligente.entities;

import java.time.LocalDate;



public class Receita {
	
	private LocalDate data;
	private String descricao;
	private float valor;
	
	public Receita() {}
	
	public Receita(LocalDate data, String descricao, float valor) {
		this.data = data;
		this.descricao = descricao;
		this.valor = valor;
	}
	
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	

}
