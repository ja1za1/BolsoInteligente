package bolsointeligente.entities;

import java.util.HashMap;
import java.util.Map;

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
	
	private String nomeMes;
	private int numeroMes;
	private static final Map<Integer, String> mapaNumeroMesParaNome = inicializarMapaNumeroParaMes();
	private static final Map<String, Integer> mapaNomeMesParaNumero = inicializarMapaNomeParaNumero();
	
	
	private Meses(String mes, int numeroMes) {
		this.nomeMes = mes;
		this.numeroMes = numeroMes;
	}

	private static Map<String, Integer> inicializarMapaNomeParaNumero() {
		Map<String, Integer> mapMeses = new HashMap<>(12);
		for(Meses mes : Meses.values()) {
			mapMeses.put(mes.nomeMes,mes.numeroMes);
		}
		return mapMeses;
	}

	private static Map<Integer, String> inicializarMapaNumeroParaMes() {
		Map<Integer, String>mapMeses = new HashMap<>(12);
		
		for (Meses mes : Meses.values()) {
            mapMeses.put(mes.numeroMes, mes.nomeMes);
        }
		return mapMeses;
	}

	public String getNomeMes() {
		return nomeMes;
	}

	public int getNumeroMes() {
		return numeroMes;
	}
	
	public static String obterNomeMes(int numeroMes) {
		return mapaNumeroMesParaNome.get(numeroMes);
	}
	
	public static int obterNumeroMes(String nomeMes) {
		return mapaNomeMesParaNumero.get(nomeMes);
	}
	

}
