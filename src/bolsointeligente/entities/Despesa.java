package bolsointeligente.entities;

import java.time.LocalDate;

import java.time.MonthDay;


public class Despesa {
	private String descricao,
				   categoria,
				   formaPagamento;
	private MonthDay diaPagamento;
	private LocalDate data;
	private Float valor;
	private boolean situacao;
	private long codigo;
	
	public Despesa () {}
	
	public Despesa(String descricao, MonthDay diaPagamento, String categoria, String formaPagamento,
			LocalDate data, Float valor, boolean pago) {
		this.descricao = descricao;
		this.diaPagamento = diaPagamento;
		this.categoria = categoria;
		this.formaPagamento = formaPagamento;
		this.data = data;
		this.valor = valor;
		this.situacao = pago;
	}
	
	public Despesa(String descricao, String categoria, String formaPagamento, MonthDay diaPagamento, LocalDate data,
			Float valor, boolean situacao, long codigo) {
		this.descricao = descricao;
		this.categoria = categoria;
		this.formaPagamento = formaPagamento;
		this.diaPagamento = diaPagamento;
		this.data = data;
		this.valor = valor;
		this.situacao = situacao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
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

	public boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

	
}
