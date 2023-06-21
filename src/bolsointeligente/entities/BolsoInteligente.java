package bolsointeligente.entities;

import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bolsointeligente.bd.ConexaoBancoDeDados;
import bolsointeligente.dao.Dao;
import bolsointeligente.dao.DaoDespesa;
import bolsointeligente.dao.DaoReceita;
import bolsointeligente.gui.IgBolsoInteligente;
import bolsointeligente.utils.DataHora;
import bolsointeligente.utils.ValidarCampos;
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
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            InputOutput.showError(String.format("Falha ao alterar Look and Feel para nimbus.\nERRO: %s", exception.getMessage()), NOME_PROGRAMA);
        }
 
	}
	
	public static String importarArquivos(File... arquivos) {
		final int QUANTIDADE_ARQUIVOS = arquivos.length;
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
		return relatorioImportacaoArquivos.toString();
	}
	
	private static String lerDadosArquivo(List<Line> linhasArquivo, String nomeArquivo) {

		final int QUANTIDADE_LINHAS  = linhasArquivo.size(),
				  QUANTIDADE_COLUNAS = (QUANTIDADE_LINHAS != 0) ? linhasArquivo.get(0).quantityOfData() : COLUNAS_VAZIAS;
		
		int linhasImportadasSucesso = 0;
		
		StringBuilder relatorioImportacaoArquivo = new StringBuilder(String.format("\n\nArquivo '%s': ", nomeArquivo)),
				      problemasImportacaoArquivo = new StringBuilder();
		
		
		switch (QUANTIDADE_COLUNAS) {
		case QUANTIDADE_COLUNAS_RECEITA:
			linhasImportadasSucesso = lerArquivoReceita(linhasArquivo, nomeArquivo, problemasImportacaoArquivo);
			break;
		case QUANTIDADE_COLUNAS_DESPESA:
			linhasImportadasSucesso = lerArquivoDespesa(linhasArquivo, nomeArquivo, problemasImportacaoArquivo);
			break;
		
		case QUANTIDADE_COLUNAS_INVESTIMENTO:
			System.out.println("INVESTIMENTO");
			break;
		case COLUNAS_VAZIAS:
			final String ARQUIVO_VAZIO = String.format("\nO arquivo %s está vazio.", nomeArquivo);
			problemasImportacaoArquivo.append(ARQUIVO_VAZIO);
			break;
		default:
			final String CABECALHO_INVALIDO = String.format("\n'%s': O cabeçalho está inválido.", nomeArquivo);
			problemasImportacaoArquivo.append(CABECALHO_INVALIDO);
			break;
		}
		relatorioImportacaoArquivo.append(String.format("%d de %d linhas importadas com sucesso.", linhasImportadasSucesso,(QUANTIDADE_LINHAS != 0) ? QUANTIDADE_LINHAS-1: 0));
		relatorioImportacaoArquivo.append(problemasImportacaoArquivo);
		return relatorioImportacaoArquivo.toString();
	}
	
	private static int lerArquivoReceita(List<Line>linhasArquivo, String nomeArquivo, StringBuilder problemasImportacao) {
		final int COLUNA_DESCRICAO = 0,
				  COLUNA_DATA      = 1,
				  COLUNA_VALOR     = 2;
		
		final String TEXTO_COLUNA_DESCRICAO = "Tipo",
				     TEXTO_COLUNA_DATA      = "Data",
				     TEXTO_COLUNA_VALOR     = "Valor";
		
		int linhasImportadasSucesso = 0;
		
		final String CABECALHO[] = linhasArquivo.get(0).getLine();
		
		if(!CABECALHO[COLUNA_DESCRICAO].equalsIgnoreCase(TEXTO_COLUNA_DESCRICAO)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO+1,TEXTO_COLUNA_DESCRICAO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DATA].equalsIgnoreCase(TEXTO_COLUNA_DATA)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DATA+1,TEXTO_COLUNA_DATA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_VALOR].equalsIgnoreCase(TEXTO_COLUNA_VALOR)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VALOR+1,TEXTO_COLUNA_VALOR));
			return linhasImportadasSucesso;
		}
		
		for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
			try {
				String descricaoReceita = linhasArquivo.get(linhaCorrente).getData(COLUNA_DESCRICAO);
				if(descricaoReceita.isBlank()) {
					// STRING VAZIA
					final String DESCRICAO_VAZIA = String.format("\n'%s': A descrição na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_DESCRICAO+1);
					problemasImportacao.append(DESCRICAO_VAZIA);
					continue;
				}
				
				String dataReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DATA);
				if(!ValidarCampos.verificarDataValida(dataReceitaString)) {
					// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
					final String FORMATO_DATA_INVALIDO = String.format("\n'%s': A data na linha %d coluna %d está formatada errada.", nomeArquivo,linhaCorrente,COLUNA_DATA+1);
					problemasImportacao.append(FORMATO_DATA_INVALIDO);
					continue;
				}
				LocalDate dataReceita = DataHora.obterLocalDateIsoDateStringDiaMesAno(dataReceitaString);
				
				String valorReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(valorReceitaString)) {
					// FLOAT INVÁLIDO
					final String VALOR_INVALIDO = String.format("\n'%s': O valor na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR+1);
					problemasImportacao.append(VALOR_INVALIDO);
					continue;
				}
				Float valorReceita = Float.parseFloat(valorReceitaString);
				
				Receita receita = new Receita(dataReceita, descricaoReceita, valorReceita);
				inserirDadosBancoDeDados(new DaoReceita(conexaoBancodeDados.getConexaoBanco()), receita);
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
		return linhasImportadasSucesso;
	}
	
	private static int lerArquivoDespesa(List<Line>linhasArquivo, String nomeArquivo, StringBuilder problemasImportacao) {
		final int COLUNA_DATA_DESPESA    = 0,
				  COLUNA_DIA_PAGAMENTO   = 1,
				  COLUNA_FORMA_PAGAMENTO = 2,
				  COLUNA_DESCRICAO       = 3,
				  COLUNA_CATEGORIA       = 4,
				  COLUNA_VALOR           = 5,
				  COLUNA_SITUACAO        = 6;
		
		final String TEXTO_COLUNA_DATA_DESPESA    = "DataDespesa",
			     	 TEXTO_COLUNA_DIA_PAGAMENTO   = "DiaPagamento",
			     	 TEXTO_COLUNA_FORMA_PAGAMENTO = "FormaPagamento",
			     	 TEXTO_COLUNA_DESCRICAO       = "Descrição",
			     	 TEXTO_COLUNA_CATEGORIA       = "Categoria",
			     	 TEXTO_COLUNA_VALOR           = "Valor",
			     	 TEXTO_COLUNA_SITUACAO        = "Situação";
		
		int linhasImportadasSucesso = 0;
		
		final String CABECALHO[] = linhasArquivo.get(0).getLine();
		
		if(!CABECALHO[COLUNA_DATA_DESPESA].equalsIgnoreCase(TEXTO_COLUNA_DATA_DESPESA)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DATA_DESPESA+1,TEXTO_COLUNA_DATA_DESPESA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DIA_PAGAMENTO].equalsIgnoreCase(TEXTO_COLUNA_DIA_PAGAMENTO)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DIA_PAGAMENTO+1,TEXTO_COLUNA_DIA_PAGAMENTO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_FORMA_PAGAMENTO].equalsIgnoreCase(TEXTO_COLUNA_FORMA_PAGAMENTO)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_FORMA_PAGAMENTO+1,TEXTO_COLUNA_FORMA_PAGAMENTO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DESCRICAO].equalsIgnoreCase(TEXTO_COLUNA_DESCRICAO)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO+1,TEXTO_COLUNA_DESCRICAO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_CATEGORIA].equalsIgnoreCase(TEXTO_COLUNA_CATEGORIA)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_CATEGORIA+1,TEXTO_COLUNA_CATEGORIA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_VALOR].equalsIgnoreCase(TEXTO_COLUNA_VALOR)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VALOR+1,TEXTO_COLUNA_VALOR));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_SITUACAO].equalsIgnoreCase(TEXTO_COLUNA_SITUACAO)) {
			problemasImportacao.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO+1,TEXTO_COLUNA_DESCRICAO));
			return linhasImportadasSucesso;
		}
		
		for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
			try {
				String dataDespesaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DATA_DESPESA);
				if(!ValidarCampos.verificarDataValida(dataDespesaString)) {
					// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
					final String FORMATO_DATA_INVALIDO = String.format("\n'%s': A data da despesa na linha %d coluna %d está formatada errada.", nomeArquivo,linhaCorrente,COLUNA_DATA_DESPESA+1);
					problemasImportacao.append(FORMATO_DATA_INVALIDO);
					continue;
				}
				LocalDate dataDespesa;
				try {
					dataDespesa = DataHora.obterLocalDateIsoDateStringDiaMesAno(dataDespesaString);
				} catch (DateTimeParseException dateTimeParseException) {
					// DATA INVALIDA (VALORES DE DIA OU MES ERRADO)
					final String DATA_INVALIDA = String.format("\n'%s': A data da despesa na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_DATA_DESPESA+1);
					problemasImportacao.append(DATA_INVALIDA);
					continue;
				}
				
				String diaPagamentoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DIA_PAGAMENTO);
				if(!ValidarCampos.verificarDiaMesValido(diaPagamentoString)) {
					// DIA PAGAMENTO FORMATADO ERRONEAMENTE (Fora do padrão DD/MM ou MM/DD)
					final String FORMATO_DIA_PAGAMENTO_INVALIDO = String.format("\n'%s': O dia do pagamento na linha %d coluna %d está formatado errado.", nomeArquivo,linhaCorrente,COLUNA_DIA_PAGAMENTO+1);
					problemasImportacao.append(FORMATO_DIA_PAGAMENTO_INVALIDO);
					continue;
				}
				MonthDay diaPagamento;
				try {
					diaPagamento = DataHora.obterMonthDayStringDiaMes(diaPagamentoString);
				} catch (DateTimeParseException dateTimeParseException) {
					// DIA DO MES INVÁLIDO (VALORES DE DIA OU MES ERRADO)
					final String DIA_PAGAMENTO_INVALIDO = String.format("\n'%s': O dia de pagamento na linha %d coluna %d é inválido.", nomeArquivo, linhaCorrente, COLUNA_DIA_PAGAMENTO+1);
					problemasImportacao.append(DIA_PAGAMENTO_INVALIDO);
					continue;
				}
				
				String formaPagamentoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_FORMA_PAGAMENTO);
				FormaPagamento formaPagamento;
				formaPagamento = FormaPagamento.obterFormaPagamento(formaPagamentoString);
				if(formaPagamento == null) {
					final String FORMA_PAGAMENTO_INVALIDA = String.format("\n'%s': A forma de pagamento na linha %d coluna %d é inválida.", nomeArquivo,linhaCorrente,COLUNA_FORMA_PAGAMENTO+1);
					problemasImportacao.append(FORMA_PAGAMENTO_INVALIDA);
					continue;
				}
				
				String descricao = linhasArquivo.get(linhaCorrente).getData(COLUNA_DESCRICAO);
				if(descricao.isBlank()) {
					// STRING VAZIA
					final String DESCRICAO_VAZIA = String.format("\n'%s': A descrição na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_DESCRICAO+1);
					problemasImportacao.append(DESCRICAO_VAZIA);
					continue;
				}
				
				String categoriaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_CATEGORIA);
				Categoria categoria;
				categoria = Categoria.obterCategoria(categoriaString);
				if(categoria == null) {
					final String CATEGORIA_INVALIDA = String.format("\n'%s': A categoria na linha %d coluna %d é inválida", nomeArquivo, linhaCorrente, COLUNA_CATEGORIA+1);
					problemasImportacao.append(CATEGORIA_INVALIDA);
					continue;
				}
				
				String valorDespesaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(valorDespesaString)) {
					// FLOAT INVÁLIDO
					final String VALOR_INVALIDO = String.format("\n'%s': O valor na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR+1);
					problemasImportacao.append(VALOR_INVALIDO);
					continue;
				}
				Float valorDespesa = Float.parseFloat(valorDespesaString);
				
				boolean pago = false;
				
				if(linhasArquivo.get(linhaCorrente).quantityOfData() == QUANTIDADE_COLUNAS_DESPESA) {
					if(linhasArquivo.get(linhaCorrente).getData(COLUNA_SITUACAO).equalsIgnoreCase("Paga") ||
					   linhasArquivo.get(linhaCorrente).getData(COLUNA_SITUACAO).equalsIgnoreCase("Pago")) {
						pago = true;
					}
					else {
						final String SITUACAO_INVALIDA = String.format("\n'%s': A situação na linha %d coluna %d é inválida.", nomeArquivo,linhaCorrente,COLUNA_SITUACAO+1);
						problemasImportacao.append(SITUACAO_INVALIDA);
						continue;
					}
				}
				Despesa despesa = new Despesa(descricao, diaPagamento, categoria, formaPagamento, dataDespesa, valorDespesa, pago);
				inserirDadosBancoDeDados(new DaoDespesa(conexaoBancodeDados.getConexaoBanco()), despesa);
				linhasImportadasSucesso++;
				
			}catch (SQLException sqlException) {
				final String INSERCAO_FALHOU = String.format("\n'%s': Houve um problema ao inserir a linha %d no banco de dados.",nomeArquivo,linhaCorrente);
				problemasImportacao.append(INSERCAO_FALHOU);
			}
			
		}
		
		return linhasImportadasSucesso;
	}
	
	
	private static <T> void inserirDadosBancoDeDados(Dao<T> dao, T dadosInserir ) throws SQLException{
		dao.insert(dadosInserir);
	}

	public ConexaoBancoDeDados getBancoDeDados() {
		return conexaoBancodeDados;
	}

	public static void main(String[] args) {
		new BolsoInteligente();
	}

}
