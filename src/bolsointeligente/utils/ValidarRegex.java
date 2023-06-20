package bolsointeligente.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidarRegex {
	
	private final static String REGEX_STRING_VAZIA  = "^\\s*$",
					     		REGEX_DATA_VALIDA   = "^\\d{2}/\\d{2}/\\d{4}$",
					     		REGEX_FLOAT_VALIDO  = "^\\d+(\\.\\d+)?$";
					     		
	
	public static boolean verificarStringVazia(String string) {
		return verificarDadoValido(string, REGEX_STRING_VAZIA);
	}
	
	public static boolean verificarDataValida(String data) {
		return verificarDadoValido(data, REGEX_DATA_VALIDA);
	}
	
	public static boolean verificarFloatValido(String numero) {
       return verificarDadoValido(numero, REGEX_FLOAT_VALIDO);
    }
	
	private static boolean verificarDadoValido(String dado, String regex) {
		Pattern padraoRegex = Pattern.compile(regex);
        Matcher matcher = padraoRegex.matcher(dado);

        return matcher.matches();
	}
}
