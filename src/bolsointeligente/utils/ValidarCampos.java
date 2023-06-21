package bolsointeligente.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidarCampos {
	
	private final static String REGEX_DATA_VALIDA    = "^\\d{2}/\\d{2}/\\d{4}$",
					     		REGEX_FLOAT_VALIDO   = "^\\d+(\\.\\d+)?$",
					     		REGEX_DIA_MES_VALIDO = "^\\d{2}/\\d{2}$";
			
	public static boolean verificarDiaMesValido(String diaMes) {
		return verificarDadoValido(diaMes, REGEX_DIA_MES_VALIDO);
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
