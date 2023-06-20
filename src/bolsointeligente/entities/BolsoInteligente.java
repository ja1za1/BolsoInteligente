package bolsointeligente.entities;

import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bolsointeligente.bd.ConexaoBancoDeDados;
import bolsointeligente.dao.DaoDespesa;
import bolsointeligente.dao.DaoReceita;
import bolsointeligente.gui.IgBolsoInteligente;
import bolsointeligente.utils.DataHora;
import bolsointeligente.utils.ValidarRegex;
import mos.es.InputOutput;
import mos.reader.Line;
import mos.reader.Reader;

public class BolsoInteligente {
	
	private final static String NOME_PROGRAMA = "Bolso Inteligente";
	private final static int QUANTIDADE_COLUNAS_RECEITA = 3,
							 QUANTIDADE_COLUNAS_DESPESA = 7,
							 QUANTIDADE_COLUNAS_INVESTIMENTO = 8,
							 COLUNAS_VAZIAS = 0;
	
	private final String URL_BANCO_DADOS     = "jdbc:postgresql:financialplanning",
						 USUARIO_BANCO_DADOS = "dba",
						 SENHA_BANCO_DADOS   = "fpdb@";
						 
	
	private static ConexaoBancoDeDados conexaoBancodeDados;
	
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
            // Define o Look and Feel como Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            InputOutput.showError(String.format("Falha ao alterar Look and Feel para nimbus.\nERRO: %s", exception.getMessage()), NOME_PROGRAMA);
        }
 
	}
	
	public static boolean importarArquivos(File... arquivos) {
		final int QUANTIDADE_ARQUIVOS = arquivos.length;
		final String TITULO_IMPORTACAO = "Importação";
		StringBuilder relatorioImportacaoArquivos = new StringBuilder();
		for(int i = 0; i < QUANTIDADE_ARQUIVOS; i++) {
			String pathArquivo;
			try {
				pathArquivo = arquivos[i].getCanonicalPath();
				List<Line> linhasArquivo = Reader.read(pathArquivo, Reader.SEMICOLON);
				String caminhoArquivo[] = pathArquivo.split("\\\\");
				String nomeArquivo = caminhoArquivo[caminhoArquivo.length-1];
				if(linhasArquivo == null) {
					InputOutput.showError(String.format("O arquivo %s não existe.", nomeArquivo), NOME_PROGRAMA);
				}
				else {
					relatorioImportacaoArquivos.append(lerDadosArquivo(linhasArquivo, nomeArquivo)); 
				}
			} catch (IOException ioExcpetion) {
				InputOutput.showError(String.format("Falha ao obter path de arquivo.\nERRO: %s",ioExcpetion.getMessage()), NOME_PROGRAMA);
			}
			
		}
		InputOutput.showInfo(relatorioImportacaoArquivos.toString(), TITULO_IMPORTACAO);
		return true;
	}
	
	private static String lerDadosArquivo(List<Line> linhasArquivo, String nomeArquivo) {

		final int QUANTIDADE_LINHAS  = linhasArquivo.size(),
				  QUANTIDADE_COLUNAS = (QUANTIDADE_LINHAS != 0) ? linhasArquivo.get(0).quantityOfData() : COLUNAS_VAZIAS;
		int linhasImportadasSucesso = 0;
		StringBuilder relatorioImportacao = new StringBuilder(String.format("\n\nArquivo '%s': ", nomeArquivo)),
				      problemasImportacao = new StringBuilder();
		final int COLUNA_DESCRICAO = 0,
				  COLUNA_DATA      = 1,
				  COLUNA_VALOR     = 2;
		
		switch (QUANTIDADE_COLUNAS) {
		case QUANTIDADE_COLUNAS_RECEITA:
			for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
				try {
					String descricaoReceita = linhasArquivo.get(linhaCorrente).getData(COLUNA_DESCRICAO);
					if(ValidarRegex.verificarStringVazia(descricaoReceita)) {
						// STRING VAZIA
						final String STRING_VAZIA = String.format("\n'%s': A descrição na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_DESCRICAO+1);
						problemasImportacao.append(STRING_VAZIA);
						continue;
					}
					
					String dataReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DATA);
					if(!ValidarRegex.verificarDataValida(dataReceitaString)) {
						// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
						final String FORMATO_DATA_INVALIDO = String.format("\n'%s': A data na linha %d coluna %d está formatada errada.", nomeArquivo,linhaCorrente,COLUNA_DATA+1);
						problemasImportacao.append(FORMATO_DATA_INVALIDO);
						continue;
					}
					LocalDate dataReceita = DataHora.obterLocalDateIsoDateStringDiaMesAno(dataReceitaString);
					
					String valorReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR).replace(".","").replace(",", ".");
					if(!ValidarRegex.verificarFloatValido(valorReceitaString)) {
						// FLOAT INVÁLIDO
						final String VALOR_INVALIDO = String.format("\n'%s': O valor na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR+1);
						problemasImportacao.append(VALOR_INVALIDO);
						continue;
					}
					Float valorReceita = Float.parseFloat(valorReceitaString);
					
					Receita receita = new Receita(dataReceita, descricaoReceita, valorReceita);
					DaoReceita daoReceita= new DaoReceita(BolsoInteligente.conexaoBancodeDados.getConexaoBanco());
					daoReceita.insert(receita);
					linhasImportadasSucesso++;
					
				} catch (SQLException sqlException) {
					final String INSERCAO_FALHOU = String.format("\n'%s': Houve um problema ao inserir a linha %d no banco de dados.",nomeArquivo,linhaCorrente);
					problemasImportacao.append(INSERCAO_FALHOU);
				}catch (DateTimeParseException dateTimeException) {
					// DATA INVALIDA (VALORES DE DIA OU MES ERRADO)
					final String DATA_INVALIDA = String.format("\n'%s': A data na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_DATA+1);
					problemasImportacao.append(DATA_INVALIDA);
					continue;
				}
				
			}
			
			break;
		case QUANTIDADE_COLUNAS_DESPESA:
			for(Line linha : linhasArquivo) {
				
				
			}
			break;
		
		case QUANTIDADE_COLUNAS_INVESTIMENTO:
			System.out.println("INVESTIMENTO");
			break;
		case COLUNAS_VAZIAS:
			final String ARQUIVO_VAZIO = String.format("\nO arquivo %s está vazio.", nomeArquivo);
			problemasImportacao.append(ARQUIVO_VAZIO);
		}
		relatorioImportacao.append(String.format("%d de %d linhas importadas com sucesso.", linhasImportadasSucesso,(QUANTIDADE_LINHAS != 0) ? QUANTIDADE_LINHAS-1: 0));
		relatorioImportacao.append(problemasImportacao);
		return relatorioImportacao.toString();
	}

	public ConexaoBancoDeDados getBancoDeDados() {
		return conexaoBancodeDados;
	}

	public static void main(String[] args) {
		new BolsoInteligente();
	}

}
