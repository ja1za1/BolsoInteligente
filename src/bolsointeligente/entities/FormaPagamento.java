package bolsointeligente.entities;

public enum FormaPagamento {
	
	CC("Cartão de Crédito"),
	CD("Cartão de Débito"),
	DINHEIRO("Dinheiro"),
	PIX("Pix");
	
	private String descricao;
	
	private FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
	public static FormaPagamento obterFormaPagamento(String formaPagamento) {
		if(formaPagamento.equalsIgnoreCase(CC.name())) {
			return CC;
		}
		else if(formaPagamento.equalsIgnoreCase(CD.name())) {
			return CD;
		}
		else if(formaPagamento.equalsIgnoreCase(DINHEIRO.descricao)) {
			return DINHEIRO;
		}
		else if(formaPagamento.equalsIgnoreCase(PIX.descricao)) {
			return PIX;
		}
		else {
			return null;
		}
	}
	
}
