package bolsointeligente.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.MonthDay;
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
	
	public static MonthDay obterMonthDayStringDiaMes(String diaMes) throws PatternSyntaxException{
		String camposData[] = diaMes.split("/");
		StringBuilder diaMesISO_DATE = new StringBuilder(7);
		diaMesISO_DATE.append("--");
		for(int i = camposData.length; i > 0; i--) {
			diaMesISO_DATE.append(camposData[i-1]);
			if(i != 1) {
				diaMesISO_DATE.append("-");
			}
		}
		return obterMonthDayIsoDate(diaMesISO_DATE.toString());
	}
	
	private static MonthDay obterMonthDayIsoDate(String diaMes) throws DateTimeParseException{
		return MonthDay.parse(diaMes);
	}
	
	public static Date converterLocalDateParaDate(LocalDate data) {
		return Date.valueOf(data);
	}
	
	public static Date converterMonthDayParaDate(MonthDay diaMes) {
		return Date.valueOf(converterMonthDayParaLocalDate(diaMes)); 
	}
	
	public static LocalDate converterMonthDayParaLocalDate(MonthDay diaMes) {
		return diaMes.atYear(LocalDate.now().getYear());
	}
	
	public static String obterDiaMesFormatado(MonthDay diaMes) {
		return diaMes.format(DateTimeFormatter.ofPattern("dd/MM"));
	}
	
	
	
	
	
}
