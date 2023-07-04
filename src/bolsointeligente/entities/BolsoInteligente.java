package bolsointeligente.entities;

import java.io.File;


import java.sql.SQLException;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bolsointeligente.bd.ConexaoBancoDeDados;
import bolsointeligente.dao.DaoCategoria;
import bolsointeligente.dao.DaoDespesa;
import bolsointeligente.dao.DaoInvestimento;
import bolsointeligente.dao.DaoReceita;
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
	
	public static List<String> obterCategorias() throws SQLException {
		return new DaoCategoria(conexaoBancodeDados.getConexaoBanco()).select();
	}
	
	public static List<Despesa> obterDespesas() throws SQLException{
		return new DaoDespesa(conexaoBancodeDados.getConexaoBanco()).select();
	}
	
	public static List<Float> obterValoresReceitas() throws SQLException{
		return new DaoReceita(conexaoBancodeDados.getConexaoBanco()).selectValoresReceita();
	}
	
	public static List<Object> obterValoresDespesas() throws SQLException{
		return new DaoDespesa(conexaoBancodeDados.getConexaoBanco()).selectValoresSituacaoDespesas();
	}
	
	public static List<Despesa> obterDespesasCategoriaMes(String categoria, int numeroMes) throws SQLException {
		return new DaoDespesa(conexaoBancodeDados.getConexaoBanco()).selectDespesaPorCategoria(categoria, numeroMes);
	}
	
	public static List<Float> obterValoresReceitaMensal(int numeroMes) throws SQLException{
		return new DaoReceita(conexaoBancodeDados.getConexaoBanco()).selectValoresReceitasMensal(numeroMes);
	}
	
	public static List<Object> obterCategoriasValoresMensal(int numeroMes) throws SQLException{
		return new DaoDespesa(conexaoBancodeDados.getConexaoBanco()).selectCategoriasValoresMensal(numeroMes);
	}
	
	public static Float obterValorTotalInvestido() throws SQLException{
		return new DaoInvestimento(conexaoBancodeDados.getConexaoBanco()).selectValorTotalInvestido();
	}
	
	public static List<Investimento> obterInvestimentos() throws SQLException{
		return new DaoInvestimento(conexaoBancodeDados.getConexaoBanco()).select();
	}
	
	public static Float obterValorTotalAcumulado() throws SQLException{
		return new DaoInvestimento(conexaoBancodeDados.getConexaoBanco()).selectValorTotalAcumulado();
	}
	
	public static boolean alterarDespesa(Despesa despesaAlterar) throws SQLException {
		new DaoDespesa(conexaoBancodeDados.getConexaoBanco()).update(despesaAlterar);
		return true;
	}
	
	
	
	public ConexaoBancoDeDados getBancoDeDados() {
		return conexaoBancodeDados;
	}

	public static void main(String[] args) {
		new BolsoInteligente();
	}

	

}
