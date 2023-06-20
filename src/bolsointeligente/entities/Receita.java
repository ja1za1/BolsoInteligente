package bolsointeligente.entities;

import java.time.LocalDate;

import bolsointeligente.utils.DataHora;

public class Receita {
	
	private LocalDate data;
	private String descricao;
	private float valor;
	
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
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return String.format("%s - %s - %,.2f", descricao, DataHora.obterDataFormatada(data), valor);
	}
	
	

}
