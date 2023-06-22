package bolsointeligente.entities.importfiles;

import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.List;

import bolsointeligente.dao.Dao;
import bolsointeligente.dao.DaoDespesa;
import bolsointeligente.dao.DaoInvestimento;
import bolsointeligente.dao.DaoReceita;
import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.Categoria;
import bolsointeligente.entities.Despesa;
import bolsointeligente.entities.FormaPagamento;
import bolsointeligente.entities.Investimento;
import bolsointeligente.entities.Receita;
import bolsointeligente.utils.DataHora;
import bolsointeligente.utils.ValidarCampos;
import mos.reader.Line;
import mos.reader.Reader;

public class ImportarArquivos implements ImportacaoArquivo {
	
	private final static int QUANTIDADE_COLUNAS_RECEITA 	 = 3,
		  					 QUANTIDADE_COLUNAS_DESPESA 	 = 7,
		  					 QUANTIDADE_COLUNAS_INVESTIMENTO = 8,
		  					 COLUNAS_VAZIAS 				 = 0,
		  		 
		  					 COLUNA_DESCRICAO_RECEITA = 0,
		  					 COLUNA_DATA_RECEITA      = 1,
		  					 COLUNA_VALOR_RECEITA     = 2,
				 
		  					 COLUNA_DATA_DESPESA       = 0,
		  					 COLUNA_DIA_PAGAMENTO      = 1,
		  					 COLUNA_FORMA_PAGAMENTO    = 2,
		  					 COLUNA_DESCRICAO_DESPESA  = 3,
		  					 COLUNA_CATEGORIA          = 4,
		  					 COLUNA_VALOR_DESPESA      = 5,
		  					 COLUNA_SITUACAO           = 6,
		  					 
							 COLUNA_OBJETIVO 			 = 0,
							 COLUNA_ESTRATEGIA 			 = 1,
							 COLUNA_NOME 				 = 2,
							 COLUNA_VALOR_INVESTIDO 	 = 3,
							 COLUNA_POSICAO_INVESTIMENTO = 4,
							 COLUNA_RENDIMENTO_BRUTO 	 = 5,
							 COLUNA_RENTABILIDADE 	     = 6,
							 COLUNA_VENCIMENTO 			 = 7;
							
	
	private final static String 	TEXTO_COLUNA_DESCRICAO_RECEITA = "Tipo",
									TEXTO_COLUNA_DATA_RECEITA      = "Data",
									TEXTO_COLUNA_VALOR_RECEITA     = "Valor",
				
									TEXTO_COLUNA_DATA_DESPESA      = "DataDespesa",
									TEXTO_COLUNA_DIA_PAGAMENTO     = "DiaPagamento",
									TEXTO_COLUNA_FORMA_PAGAMENTO   = "FormaPagamento",
									TEXTO_COLUNA_DESCRICAO_DESPESA = "Descrição",
									TEXTO_COLUNA_CATEGORIA         = "Categoria",
									TEXTO_COLUNA_VALOR_DESPESA     = "Valor",
									TEXTO_COLUNA_SITUACAO          = "Situação",
									
									TEXTO_COLUNA_OBJETIVO 			  = "Objetivo",
									TEXTO_COLUNA_ESTRATEGIA 		  = "Estratégia",
									TEXTO_COLUNA_NOME 				  = "Nome",
									TEXTO_COLUNA_VALOR_INVESTIDO 	  = "Valor Investido",
									TEXTO_COLUNA_POSICAO_INVESTIMENTO = "Posição",
									TEXTO_COLUNA_RENDIMENTO_BRUTO 	  = "Rendimento Bruto",
									TEXTO_COLUNA_RENTABILIDADE 		  = "Rentabilidade",
									TEXTO_COLUNA_VENCIMENTO 		  = "Vencimento";
	
	
	
	public ImportarArquivos() {}
	

	@Override
	public String importarArquivos(File...arquivosImportar) {

		final int QUANTIDADE_ARQUIVOS_IMPORTAR = arquivosImportar.length;
		StringBuilder relatorioImportacaoArquivos = new StringBuilder();
		for(int i = 0; i < QUANTIDADE_ARQUIVOS_IMPORTAR; i++) {
			String pathArquivo;
			try {
				pathArquivo = arquivosImportar[i].getCanonicalPath();
				List<Line> linhasArquivo = Reader.read(pathArquivo, Reader.SEMICOLON);
				String caminhoArquivo[] = pathArquivo.split("\\\\");
				String nomeArquivo = caminhoArquivo[caminhoArquivo.length-1];
				if(linhasArquivo == null) {
					relatorioImportacaoArquivos.append(String.format("\n\nO arquivo %s não existe.", nomeArquivo));
				}
				else {
					relatorioImportacaoArquivos.append(lerDadosArquivo(linhasArquivo, nomeArquivo)); 
				}
			} catch (IOException ioExcpetion) {
				relatorioImportacaoArquivos.append(String.format("\n\nFalha ao obter path de arquivo.\nERRO: %s",ioExcpetion.getMessage()));
			}
			
		}
		return relatorioImportacaoArquivos.toString();
	}

	private String lerDadosArquivo(List<Line> linhasArquivo, String nomeArquivo) {

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
			linhasImportadasSucesso = lerArquivoInvestimento(linhasArquivo, nomeArquivo, problemasImportacaoArquivo);
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

	private int lerArquivoDespesa(List<Line> linhasArquivo, String nomeArquivo,
			StringBuilder problemasImportacaoArquivo) {
		
		int linhasImportadasSucesso = 0;
		
		final String CABECALHO[] = linhasArquivo.get(0).getLine();
		
		if(!CABECALHO[COLUNA_DATA_DESPESA].equalsIgnoreCase(TEXTO_COLUNA_DATA_DESPESA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DATA_DESPESA+1,TEXTO_COLUNA_DATA_DESPESA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DIA_PAGAMENTO].equalsIgnoreCase(TEXTO_COLUNA_DIA_PAGAMENTO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DIA_PAGAMENTO+1,TEXTO_COLUNA_DIA_PAGAMENTO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_FORMA_PAGAMENTO].equalsIgnoreCase(TEXTO_COLUNA_FORMA_PAGAMENTO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_FORMA_PAGAMENTO+1,TEXTO_COLUNA_FORMA_PAGAMENTO));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DESCRICAO_DESPESA].equalsIgnoreCase(TEXTO_COLUNA_DESCRICAO_DESPESA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO_DESPESA+1,TEXTO_COLUNA_DESCRICAO_DESPESA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_CATEGORIA].equalsIgnoreCase(TEXTO_COLUNA_CATEGORIA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_CATEGORIA+1,TEXTO_COLUNA_CATEGORIA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_VALOR_DESPESA].equalsIgnoreCase(TEXTO_COLUNA_VALOR_DESPESA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VALOR_DESPESA+1,TEXTO_COLUNA_VALOR_DESPESA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_SITUACAO].equalsIgnoreCase(TEXTO_COLUNA_SITUACAO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO_DESPESA+1,TEXTO_COLUNA_DESCRICAO_DESPESA));
			return linhasImportadasSucesso;
		}
		
		for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
			try {
				String dataDespesaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DATA_DESPESA);
				if(!ValidarCampos.verificarDataValida(dataDespesaString)) {
					// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
					final String FORMATO_DATA_INVALIDO = String.format("\n'%s': A data da despesa na linha %d coluna %d está formatada errada.", nomeArquivo,linhaCorrente,COLUNA_DATA_DESPESA+1);
					problemasImportacaoArquivo.append(FORMATO_DATA_INVALIDO);
					continue;
				}
				LocalDate dataDespesa;
				try {
					dataDespesa = DataHora.obterLocalDateIsoDateStringDiaMesAno(dataDespesaString);
				} catch (DateTimeParseException dateTimeParseException) {
					// DATA INVALIDA (VALORES DE DIA OU MES ERRADO)
					final String DATA_INVALIDA = String.format("\n'%s': A data da despesa na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_DATA_DESPESA+1);
					problemasImportacaoArquivo.append(DATA_INVALIDA);
					continue;
				}
				
				String diaPagamentoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DIA_PAGAMENTO);
				if(!ValidarCampos.verificarDiaMesValido(diaPagamentoString)) {
					// DIA PAGAMENTO FORMATADO ERRONEAMENTE (Fora do padrão DD/MM ou MM/DD)
					final String FORMATO_DIA_PAGAMENTO_INVALIDO = String.format("\n'%s': O dia do pagamento na linha %d coluna %d está formatado errado.", nomeArquivo,linhaCorrente,COLUNA_DIA_PAGAMENTO+1);
					problemasImportacaoArquivo.append(FORMATO_DIA_PAGAMENTO_INVALIDO);
					continue;
				}
				MonthDay diaPagamento;
				try {
					diaPagamento = DataHora.obterMonthDayStringDiaMes(diaPagamentoString);
				} catch (DateTimeParseException dateTimeParseException) {
					// DIA DO MES INVÁLIDO (VALORES DE DIA OU MES ERRADO)
					final String DIA_PAGAMENTO_INVALIDO = String.format("\n'%s': O dia de pagamento na linha %d coluna %d é inválido.", nomeArquivo, linhaCorrente, COLUNA_DIA_PAGAMENTO+1);
					problemasImportacaoArquivo.append(DIA_PAGAMENTO_INVALIDO);
					continue;
				}
				
				String formaPagamentoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_FORMA_PAGAMENTO);
				FormaPagamento formaPagamento;
				formaPagamento = FormaPagamento.obterFormaPagamento(formaPagamentoString);
				if(formaPagamento == null) {
					final String FORMA_PAGAMENTO_INVALIDA = String.format("\n'%s': A forma de pagamento na linha %d coluna %d é inválida.", nomeArquivo,linhaCorrente,COLUNA_FORMA_PAGAMENTO+1);
					problemasImportacaoArquivo.append(FORMA_PAGAMENTO_INVALIDA);
					continue;
				}
				
				String descricao = linhasArquivo.get(linhaCorrente).getData(COLUNA_DESCRICAO_DESPESA);
				if(descricao.isBlank()) {
					// STRING VAZIA
					final String DESCRICAO_VAZIA = String.format("\n'%s': A descrição na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_DESCRICAO_DESPESA+1);
					problemasImportacaoArquivo.append(DESCRICAO_VAZIA);
					continue;
				}
				
				String categoriaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_CATEGORIA);
				Categoria categoria;
				categoria = Categoria.obterCategoria(categoriaString);
				if(categoria == null) {
					final String CATEGORIA_INVALIDA = String.format("\n'%s': A categoria na linha %d coluna %d é inválida", nomeArquivo, linhaCorrente, COLUNA_CATEGORIA+1);
					problemasImportacaoArquivo.append(CATEGORIA_INVALIDA);
					continue;
				}
				
				String valorDespesaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR_DESPESA).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(valorDespesaString)) {
					// FLOAT INVÁLIDO
					final String VALOR_INVALIDO = String.format("\n'%s': O valor na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR_DESPESA+1);
					problemasImportacaoArquivo.append(VALOR_INVALIDO);
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
						problemasImportacaoArquivo.append(SITUACAO_INVALIDA);
						continue;
					}
				}
				Despesa despesa = new Despesa(descricao, diaPagamento, categoria, formaPagamento, dataDespesa, valorDespesa, pago);
				Dao.inserirDadosBancoDeDados(new DaoDespesa(BolsoInteligente.conexaoBancodeDados.getConexaoBanco()), despesa);
				linhasImportadasSucesso++;
				
			}catch (SQLException sqlException) {
				final String INSERCAO_FALHOU = String.format("\n'%s': Houve um problema ao inserir a linha %d no banco de dados.",nomeArquivo,linhaCorrente);
				problemasImportacaoArquivo.append(INSERCAO_FALHOU);
			}
			
		}
		
		return linhasImportadasSucesso;
	}

	private int lerArquivoReceita(List<Line> linhasArquivo, String nomeArquivo,
			StringBuilder problemasImportacaoArquivo) {
		
		int linhasImportadasSucesso = 0;
		
		final String CABECALHO[] = linhasArquivo.get(0).getLine();
		
		if(!CABECALHO[COLUNA_DESCRICAO_RECEITA].equalsIgnoreCase(TEXTO_COLUNA_DESCRICAO_RECEITA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DESCRICAO_RECEITA+1,TEXTO_COLUNA_DESCRICAO_RECEITA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_DATA_RECEITA].equalsIgnoreCase(TEXTO_COLUNA_DATA_RECEITA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_DATA_RECEITA+1,TEXTO_COLUNA_DATA_RECEITA));
			return linhasImportadasSucesso;
		}
		else if(!CABECALHO[COLUNA_VALOR_RECEITA].equalsIgnoreCase(TEXTO_COLUNA_VALOR_RECEITA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VALOR_RECEITA+1,TEXTO_COLUNA_VALOR_RECEITA));
			return linhasImportadasSucesso;
		}
		
		for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
			try {
				String descricaoReceita = linhasArquivo.get(linhaCorrente).getData(COLUNA_DESCRICAO_RECEITA);
				if(descricaoReceita.isBlank()) {
					// STRING VAZIA
					final String DESCRICAO_VAZIA = String.format("\n'%s': A descrição na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_DESCRICAO_RECEITA+1);
					problemasImportacaoArquivo.append(DESCRICAO_VAZIA);
					continue;
				}
				
				String dataReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_DATA_RECEITA);
				if(!ValidarCampos.verificarDataValida(dataReceitaString)) {
					// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
					final String FORMATO_DATA_INVALIDO = String.format("\n'%s': A data na linha %d coluna %d está formatada errada.", nomeArquivo,linhaCorrente,COLUNA_DATA_RECEITA+1);
					problemasImportacaoArquivo.append(FORMATO_DATA_INVALIDO);
					continue;
				}
				LocalDate dataReceita = DataHora.obterLocalDateIsoDateStringDiaMesAno(dataReceitaString);
				
				String valorReceitaString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR_RECEITA).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(valorReceitaString)) {
					// FLOAT INVÁLIDO
					final String VALOR_INVALIDO = String.format("\n'%s': O valor na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR_RECEITA+1);
					problemasImportacaoArquivo.append(VALOR_INVALIDO);
					continue;
				}
				Float valorReceita = Float.parseFloat(valorReceitaString);
				
				Receita receita = new Receita(dataReceita, descricaoReceita, valorReceita);
				Dao.inserirDadosBancoDeDados(new DaoReceita(BolsoInteligente.conexaoBancodeDados.getConexaoBanco()), receita);
				linhasImportadasSucesso++;
				
			} catch (SQLException sqlException) {
				final String INSERCAO_FALHOU = String.format("\n'%s': Houve um problema ao inserir a linha %d no banco de dados.",nomeArquivo,linhaCorrente);
				problemasImportacaoArquivo.append(INSERCAO_FALHOU);
			}catch (DateTimeParseException dateTimeException) {
				// DATA INVALIDA (VALORES DE DIA OU MES ERRADO)
				final String DATA_INVALIDA = String.format("\n'%s': A data na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_DATA_RECEITA+1);
				problemasImportacaoArquivo.append(DATA_INVALIDA);
				continue;
			}
			
		}
		return linhasImportadasSucesso;
	}

	private int lerArquivoInvestimento(List<Line> linhasArquivo, String nomeArquivo, StringBuilder problemasImportacaoArquivo) {
		int linhasImportadasSucesso = 0;
		
		final String CABECALHO[] = linhasArquivo.get(0).getLine();
		if(!CABECALHO[COLUNA_OBJETIVO].equalsIgnoreCase(TEXTO_COLUNA_OBJETIVO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_OBJETIVO+1,TEXTO_COLUNA_OBJETIVO));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_ESTRATEGIA].equalsIgnoreCase(TEXTO_COLUNA_ESTRATEGIA)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_ESTRATEGIA+1,TEXTO_COLUNA_ESTRATEGIA));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_NOME].equalsIgnoreCase(TEXTO_COLUNA_NOME)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_NOME+1,TEXTO_COLUNA_NOME));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_VALOR_INVESTIDO].equalsIgnoreCase(TEXTO_COLUNA_VALOR_INVESTIDO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VALOR_INVESTIDO+1,TEXTO_COLUNA_VALOR_INVESTIDO));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_POSICAO_INVESTIMENTO].equalsIgnoreCase(TEXTO_COLUNA_POSICAO_INVESTIMENTO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_POSICAO_INVESTIMENTO+1,TEXTO_COLUNA_POSICAO_INVESTIMENTO));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_RENDIMENTO_BRUTO].equalsIgnoreCase(TEXTO_COLUNA_RENDIMENTO_BRUTO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_RENDIMENTO_BRUTO+1,TEXTO_COLUNA_RENDIMENTO_BRUTO));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_RENTABILIDADE].equalsIgnoreCase(TEXTO_COLUNA_RENTABILIDADE)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_RENTABILIDADE+1,TEXTO_COLUNA_RENTABILIDADE));
			return linhasImportadasSucesso;
		}else if(!CABECALHO[COLUNA_VENCIMENTO].equalsIgnoreCase(TEXTO_COLUNA_VENCIMENTO)) {
			problemasImportacaoArquivo.append(String.format("\n'%s': O cabeçalho da coluna %d deveria ser '%s'", nomeArquivo,COLUNA_VENCIMENTO+1,TEXTO_COLUNA_VENCIMENTO));
			return linhasImportadasSucesso;
		}
		// Objetivo;Estratégia;Nome;Valor Investido;Posição; Rendimento Bruto;Rentabilidade;Vencimento
		for(int linhaCorrente = 1; linhaCorrente < linhasArquivo.size(); linhaCorrente++) {
			try {
				String objetivo = linhasArquivo.get(linhaCorrente).getData(COLUNA_OBJETIVO);
				if(objetivo.isBlank()) {
					// STRING VAZIA
					final String OBJETIVO_VAZIO = String.format("\n'%s': O objetivo na linha %d coluna %d está vazio.",nomeArquivo,linhaCorrente,COLUNA_OBJETIVO+1);
					problemasImportacaoArquivo.append(OBJETIVO_VAZIO);
					continue;
				}
				
				String estrategia = linhasArquivo.get(linhaCorrente).getData(COLUNA_ESTRATEGIA);
				if(estrategia.isBlank()) {
					// STRING VAZIA
					final String ESTRATEGIA_VAZIA = String.format("\n'%s': A estratégia na linha %d coluna %d está vazia.",nomeArquivo,linhaCorrente,COLUNA_ESTRATEGIA+1);
					problemasImportacaoArquivo.append(ESTRATEGIA_VAZIA);
					continue;
				}
				
				String nomeInvestimento = linhasArquivo.get(linhaCorrente).getData(COLUNA_NOME);
				if(nomeInvestimento.isBlank()) {
					final String NOME_VAZIO = String.format("\n'%s': O nome na linha %d coluna %d está vazio.",nomeArquivo,linhaCorrente,COLUNA_NOME+1);
					problemasImportacaoArquivo.append(NOME_VAZIO);
					continue;
				}
				
				String valorInvestidoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VALOR_INVESTIDO).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(valorInvestidoString)) {
					final String VALOR_INVESTIDO_INVALIDO = String.format("\n'%s': O valor investido na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_VALOR_INVESTIDO+1);
					problemasImportacaoArquivo.append(VALOR_INVESTIDO_INVALIDO);
					continue;
				}
				float valorInvestido = Float.parseFloat(valorInvestidoString);
				
				String posicaoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_POSICAO_INVESTIMENTO).replace(".","").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(posicaoString)) {
					final String POSICAO_INVALIDA = String.format("\n'%s': A posição na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_POSICAO_INVESTIMENTO+1);
					problemasImportacaoArquivo.append(POSICAO_INVALIDA);
					continue;
				}
				float posicao = Float.parseFloat(posicaoString);
				
				String rendimentoBrutoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_RENDIMENTO_BRUTO).replace(".", "").replace(",", ".");
				if(!ValidarCampos.verificarFloatValido(rendimentoBrutoString)) {
					final String RENDIMENTO_BRUTO_INVALIDO = String.format("\n'%s': O rendimento bruto na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_RENDIMENTO_BRUTO+1);
					problemasImportacaoArquivo.append(RENDIMENTO_BRUTO_INVALIDO);
					continue;
				}
				float rendimentoBruto = Float.parseFloat(rendimentoBrutoString);
				
				String rentabilidadeString = linhasArquivo.get(linhaCorrente).getData(COLUNA_RENTABILIDADE).replace(".", "").replace(",", ".").replace("%", "");
				
				if(!ValidarCampos.verificarFloatValido(rentabilidadeString)) {
					final String RENTABILIDADE_INVALIDA = String.format("\n'%s': A rentabilidade na linha %d coluna %d é inválido.",nomeArquivo,linhaCorrente,COLUNA_RENTABILIDADE+1);
					problemasImportacaoArquivo.append(RENTABILIDADE_INVALIDA);
					continue;
				}
				float rentabilidade = Float.parseFloat(rentabilidadeString);
				
				String vencimentoString = linhasArquivo.get(linhaCorrente).getData(COLUNA_VENCIMENTO);
				if(!ValidarCampos.verificarDataValida(vencimentoString)) {
					// DATA FORMATADA ERRONEAMENTE (Fora do padrao DD/MM/YYYY ou MM/DD/YYYY)
					final String VENCIMENTO_FORMATADO_ERRADO = String.format("\n'%s': O vencimento na linha %d coluna %d está formatado errado.", nomeArquivo,linhaCorrente,COLUNA_VENCIMENTO+1);
					problemasImportacaoArquivo.append(VENCIMENTO_FORMATADO_ERRADO);
					continue;
				}
				LocalDate vencimento = DataHora.obterLocalDateIsoDateStringDiaMesAno(vencimentoString);
				
				Investimento investimento = new Investimento(objetivo, estrategia, nomeInvestimento, valorInvestido, posicao, rendimentoBruto, rentabilidade, vencimento);
				Dao.inserirDadosBancoDeDados(new DaoInvestimento(BolsoInteligente.conexaoBancodeDados.getConexaoBanco()), investimento);
				linhasImportadasSucesso++;
				
			} catch (SQLException e) {
				final String INSERCAO_FALHOU = String.format("\n'%s': Houve um problema ao inserir a linha %d no banco de dados.",nomeArquivo,linhaCorrente);
				problemasImportacaoArquivo.append(INSERCAO_FALHOU);
			}catch (DateTimeParseException dateTimeParseException) {
				// DATA INVALIDA (VALORES DE DIA OU MES ERRADO)
				final String DATA_VENCIMENTO_INVALIDO = String.format("\n'%s': A data de vencimento na linha %d coluna %d é inválida.",nomeArquivo,linhaCorrente,COLUNA_DATA_RECEITA+1);
				problemasImportacaoArquivo.append(DATA_VENCIMENTO_INVALIDO);
				continue;
			}
		}
		
		return linhasImportadasSucesso;
	}
	

}
