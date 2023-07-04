package bolsointeligente.entities.tables;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class EditorCelula extends DefaultCellEditor {

    private InputVerifier verificadorInput = null;

    public EditorCelula(InputVerifier verifier) {
        super(new JTextField());
        this.verificadorInput = verifier;
    }

    @Override
    public boolean stopCellEditing() {
        return verificadorInput.verify(editorComponent) && super.stopCellEditing();
    }
    
    public static class VerificadorTextoVazio extends InputVerifier {

	    @Override
	    public boolean verify(JComponent input) {
	        boolean valido = false;
	        String textoDigitadoUsuario = ((JTextField) input).getText();
	            
	        if (!textoDigitadoUsuario.isEmpty()){
	            input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
	            valido = true;
	        } else {
	        	input.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	        }
	       
	        return valido;
	    }

	}
	
	public static class VerificadorFloatValido extends InputVerifier {

	    @Override
	    public boolean verify(JComponent input) {
	        boolean valido = false;
	        String textoDigitadoUsuario = ((JTextField) input).getText();
	        if(!textoDigitadoUsuario.isEmpty()) {
		        try {
		        	Float.parseFloat(textoDigitadoUsuario);
			        input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			        valido = true;
		        }catch (NumberFormatException numberFormatException) {
		        	input.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				}
	        }
	        else {
	        	input.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	        }
	        
	       
	        return valido;
	    }

	}
}