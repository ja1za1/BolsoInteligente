package bolsointeligente.entities;

import java.time.LocalDate;
import java.time.MonthDay;

import bolsointeligente.utils.DataHora;

public class Despesa {
	private String descricao;
	private Categoria categoria;
	private MonthDay diaPagamento;
	private FormaPagamento formaPagamento;
	private LocalDate data;
	private Float valor;
	private boolean pago;
	
	public Despesa(String descricao, MonthDay diaPagamento, Categoria categoria, FormaPagamento formaPagamento,
			LocalDate data, Float valor, boolean pago) {
		this.descricao = descricao;
		this.diaPagamento = diaPagamento;
		this.categoria = categoria;
		this.formaPagamento = formaPagamento;
		this.data = data;
		this.valor = valor;
		this.pago = pago;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public MonthDay getDiaPagamento() {
		return diaPagamento;
	}

	public void setDiaPagamento(MonthDay diaPagamento) {
		this.diaPagamento = diaPagamento;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
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

	public boolean getPago() {
		return pago;
	}

	public void setSituacao(boolean situacao) {
		this.pago = situacao;
	}

	@Override
	public String toString() {
		return String.format(
				"%s - %s - %s - %s - %s - %,.2f - Pago: %s",
				descricao, diaPagamento, categoria, formaPagamento, DataHora.obterDataFormatada(data), valor, pago);
	}
	
}
