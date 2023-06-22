package bolsointeligente.entities;

import java.io.File;


import java.sql.SQLException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bolsointeligente.bd.ConexaoBancoDeDados;
import bolsointeligente.entities.importfiles.ImportarArquivos;
import bolsointeligente.gui.IgBolsoInteligente;
import mos.es.InputOutput;

public class BolsoInteligente {
	
	public final static String NOME_PROGRAMA = "Bolso Inteligente";
	
	
	private final String URL_BANCO_DADOS     = "jdbc:postgresql:financialplanning",
						 USUARIO_BANCO_DADOS = "dba",
						 SENHA_BANCO_DADOS   = "fpdb@";
						 
	
	public static ConexaoBancoDeDados conexaoBancodeDados;
	
	public BolsoInteligente() {
		iniciarConexaoBancoDeDados();
		definirLookAndFeel();
		iniciarInterfaceGrafica();
	}

	private void iniciarInterfaceGrafica() {
		new IgBolsoInteligente();
	}

	private void iniciarConexaoBancoDeDados(){
		try {
			BolsoInteligente.conexaoBancodeDados = new ConexaoBancoDeDados(URL_BANCO_DADOS, USUARIO_BANCO_DADOS, SENHA_BANCO_DADOS);
		}catch (SQLException sqlExcpetion) {
			InputOutput.showError(String.format("Não foi possível realizar a conexão com banco de dados.\n\nERRO: %s\n\nO programa será encerrado.", sqlExcpetion.getMessage()), NOME_PROGRAMA);
			System.exit(0);
		} 
		
	}

	private void definirLookAndFeel() {
		try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            InputOutput.showError(String.format("Falha ao alterar Look and Feel para nimbus.\nERRO: %s", exception.getMessage()), NOME_PROGRAMA);
        }
 
	}
	
	public static String importarArquivos(File... arquivos) {		
		return new ImportarArquivos().importarArquivos(arquivos);
	}
	

	public ConexaoBancoDeDados getBancoDeDados() {
		return conexaoBancodeDados;
	}

	public static void main(String[] args) {
		new BolsoInteligente();
	}

}
