package bolsointeligente.entities;

public enum Categoria {
	// Alimentação, Educação, Energia Elétrica, Esporte, Lazer, Habitação e Plano de Saúde.
	
	ALIMENTACAO("Alimentação"),
	EDUCACAO("Educação"),
	ENERGIA_ELETRICA("Energia Elétrica"),
	ESPORTE("Esporte"),
	LAZER("Lazer"),
	HABITACAO("Habitação"),
	PLANO_SAUDE("Plano de Saúde");
	
	private String descricao;

	private Categoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
	public static Categoria obterCategoria(String categoria) {
		if(categoria.equalsIgnoreCase(ALIMENTACAO.descricao)) {
			return ALIMENTACAO;
		}
		else if(categoria.equalsIgnoreCase(EDUCACAO.descricao)) {
			return EDUCACAO;
		}
		else if(categoria.equalsIgnoreCase(ENERGIA_ELETRICA.descricao)) {
			return ENERGIA_ELETRICA;
		}
		else if(categoria.equalsIgnoreCase(ESPORTE.descricao)) {
			return ESPORTE;
		}
		else if(categoria.equalsIgnoreCase(LAZER.descricao)) {
			return LAZER;
		}
		else if(categoria.equalsIgnoreCase(HABITACAO.descricao)) {
			return HABITACAO;
		}
		else if(categoria.equalsIgnoreCase(PLANO_SAUDE.descricao)) {
			return PLANO_SAUDE;
		}
		else {
			return null;
		}
	}
}
