package bolsointeligente.entities;

import java.time.LocalDate;

import bolsointeligente.utils.DataHora;

public class Despesa {
	private String descricao;
	private LocalDate data;
	private Float valor;
	
	public Despesa() {
		descricao = null;
		data = null;
		valor = null;
	}
	
	public Despesa(String descricao, LocalDate data, Float valor) {
		this.descricao = descricao;
		this.data = data;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return String.format("%s - %s  R$ %,.2f]", descricao, DataHora.obterDataFormatada(data), valor);
	}
	
	
	
	
	
}
