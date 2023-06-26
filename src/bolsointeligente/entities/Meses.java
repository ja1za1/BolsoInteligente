package bolsointeligente.entities;

public enum Meses {
	JANEIRO("Janeiro", 1),
	FEVEREIRO("Fevereiro", 2),
	MARCO("Março", 3),
	ABRIL("Abril", 4),
	MAIO("Maio", 5),
	JUNHO("Junho", 6),
	JULHO("Julho", 7),
	AGOSTO("Agosto", 8),
	SETEMBRO("Setembro", 9),
	OUTUBRO("Outubro", 10),
	NOVEMBRO("Novembro", 11),
	DEZEMBRO("Dezembro", 12);
	
	private String mes;
	private int numeroMes;
	
	private Meses(String mes, int numeroMes) {
		this.mes = mes;
		this.numeroMes = numeroMes;
	}

	public String getMes() {
		return mes;
	}

	public int getNumeroMes() {
		return numeroMes;
	}
	
	

}
