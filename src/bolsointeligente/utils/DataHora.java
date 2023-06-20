package bolsointeligente.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.PatternSyntaxException;

public class DataHora {
	
	/**
	 * Gera uma <code>String</code> formatada no estilo DD/MM/YYYY a partir de um objeto <code>LocalDate</code>.
	 * 
	 * @param data O objeto <code>LocalDate</code> contendo a data encapsulada.
	 * @return Uma <code>String</code> formatada no estilo DD/MM/YYYY.
	 */
	public static String obterDataFormatada(LocalDate data) {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}
	
	/**
	 * Gera um objeto do tipo <code>LocalDate</code> formatado no padrão ISO_DATE a partir de uma 
	 * <code>String</code> que já esteja no padrão ISO_DATE (YYYY-MM-DD).
	 * 
	 * @param data A <code>String</code> formatada no estilo YYYY-MM-DD.
	 * @return Um objeto <code>LocalDate</code> formatado no padrão ISO_DATE.
	 * @throws DateTimeParseException Caso a <code>string</code> não esteja no formato YYYY-MM-DD.
	 */
	
	public static LocalDate obterLocalDateIsoDate(String data) throws DateTimeParseException{
		return LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
	}
	
	
	/**
	 * Gera um objeto do tipo <code>LocalDate</code> formatado no padrão ISO_DATE (YYYY-MM-DD) a partir de uma <code>String</code> que esteja formatada
	 * no estilo DD/MM/YYYY.
	 * 
	 * 
	 * @param diaMesAno A <code>String</code> formatada no estilo DD/MM/YYYY.
	 * @return Um objeto <code>LocalDate</code> formatado no padrão ISO_DATE.
	 * @throws PatternSyntaxException Caso a data não esteja separada pelo caractere '/'.
	 * 
	 * 
	 */
	public static LocalDate obterLocalDateIsoDateStringDiaMesAno(String diaMesAno) throws PatternSyntaxException{
		String camposData[] = diaMesAno.split("/");
		StringBuilder dataISO_DATE = new StringBuilder(10);
		for(int i = camposData.length; i > 0; i--) {
			dataISO_DATE.append(camposData[i-1]);
			if(i != 1)
				dataISO_DATE.append('-');
		}
		return obterLocalDateIsoDate(dataISO_DATE.toString());
	}
}
