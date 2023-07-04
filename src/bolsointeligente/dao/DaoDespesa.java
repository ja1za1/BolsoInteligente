package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

import bolsointeligente.entities.Despesa;
import bolsointeligente.utils.DataHora;

public class DaoDespesa extends Dao<Despesa> {
	
	private final static long NAO_CADASTRADO = 0;
	
	public DaoDespesa(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Despesa despesa) throws SQLException {



		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();

		long codigoFormaPagamento = obterCodigoFormaPagamento(despesa.getFormaPagamento());
		
		if(codigoFormaPagamento == NAO_CADASTRADO) {
			inserirFormaPagamento(preparedStatement,conexaoBanco,despesa.getFormaPagamento());
			codigoFormaPagamento = obterCodigoFormaPagamento(despesa.getFormaPagamento());
		}
		
		long codigoCategoria = obterCodigoCategoria(despesa.getCategoria());
		
		if(codigoCategoria == NAO_CADASTRADO) {
			inserirCategoria(preparedStatement,conexaoBanco,despesa);
			codigoCategoria = obterCodigoCategoria(despesa.getCategoria());
		}
		
		
		inserirDespesa(preparedStatement,conexaoBanco,despesa.getDescricao(),codigoCategoria);
		long codigoDespesa = obterUltimoCodigoInseridoDespesa();
		
		inserirOrcamento(preparedStatement, conexaoBanco, despesa, codigoDespesa,codigoFormaPagamento);
	}
	
	private void inserirOrcamento(PreparedStatement preparedStatement, Connection conexaoBanco, Despesa despesa,
			long codigoDespesa, long codigoFormaPagamento) throws SQLException {
		
		String sqlOrcamento = "INSERT INTO orcamento (mes_ano,cod_despesa,data_despesa,data_pagamento,cod_forma_pagamento,valor,situacao) "
							+ "VALUES (?,?,?,?,?,?,?)";
		preparedStatement = conexaoBanco.prepareStatement(sqlOrcamento);
		preparedStatement.setInt(1, despesa.getDiaPagamento().getMonth().getValue());
		preparedStatement.setLong(2, codigoDespesa);
		preparedStatement.setDate(3, DataHora.converterLocalDateParaDate(despesa.getData()));
		preparedStatement.setDate(4, DataHora.converterMonthDayParaDate(despesa.getDiaPagamento()));
		preparedStatement.setLong(5, codigoFormaPagamento);
		preparedStatement.setFloat(6, despesa.getValor());
		preparedStatement.setBoolean(7, despesa.getSituacao());
		preparedStatement.execute();
		
	}

	private void inserirDespesa(PreparedStatement preparedStatement, Connection conexaoBanco, String descricaoDespesa, long codigoCategoria) throws SQLException {



		String sqlDespesa = "INSERT INTO despesa (descricao, cod_categoria) "
						  + "VALUES (?, ?)";
		preparedStatement = conexaoBanco.prepareStatement(sqlDespesa);
		preparedStatement.setString(1, descricaoDespesa);
		preparedStatement.setLong(2, codigoCategoria);
		preparedStatement.execute();
	}

	private void inserirCategoria(PreparedStatement preparedStatement, Connection conexaoBanco, Despesa despesa) throws SQLException {
		String sqlCategoria = "INSERT INTO categoria (descricao) "
							+ "VALUES (?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlCategoria);
		preparedStatement.setString(1, despesa.getCategoria());
		preparedStatement.execute();
	}

	private void inserirFormaPagamento(PreparedStatement preparedStatement, Connection conexaoBanco, String formaPagamento) throws SQLException {
		

		String sqlFormaPagamento = "INSERT INTO forma_pagamento (descricao) "
								 + "VALUES (?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlFormaPagamento);
		preparedStatement.setString(1, formaPagamento);
		preparedStatement.execute();
	}

	private long obterCodigoFormaPagamento(String descricaoFormaPagamento) throws SQLException {


		String sqlObterCodigo = "SELECT codigo "
							  + "FROM forma_pagamento "
							  + "WHERE descricao = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricaoFormaPagamento);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : NAO_CADASTRADO;
	}
	
	private long obterCodigoCategoria(String descricaoCategoria) throws SQLException {
		String sqlObterCodigo = "SELECT codigo "
							  + "FROM categoria "
							  + "WHERE descricao = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricaoCategoria);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : NAO_CADASTRADO;
	}
	
	private long obterUltimoCodigoInseridoDespesa() throws SQLException {
		String sqlObterCodigo = "SELECT MAX(codigo) AS codigo FROM despesa";
							  
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : NAO_CADASTRADO;
	}
	

	
	@Override
	public void update(Despesa despesaAtualizar) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		Despesa despesaCadastrada = selectDespesaPorCodigo(despesaAtualizar.getCodigo());
		
		long codigoDespesaAtualizar = -1;
		long codigoFormaPagamentoAtualizar = -1;
		
		
		if(despesaCadastrada.getFormaPagamento().compareToIgnoreCase(despesaAtualizar.getFormaPagamento()) != 0) {
			codigoFormaPagamentoAtualizar = obterCodigoFormaPagamento(despesaAtualizar.getFormaPagamento());
			if(codigoFormaPagamentoAtualizar == NAO_CADASTRADO) {
				inserirFormaPagamento(preparedStatement, conexaoBanco, despesaAtualizar.getFormaPagamento());
				codigoFormaPagamentoAtualizar = obterCodigoFormaPagamento(despesaAtualizar.getFormaPagamento());
			}
			
		}
		else if(despesaCadastrada.getDescricao().compareTo(despesaAtualizar.getDescricao()) != 0){
			long codigoCategoriaDespesaCadastrada = obterCodigoCategoria(despesaCadastrada.getCategoria());
			inserirDespesa(preparedStatement, conexaoBanco, despesaAtualizar.getDescricao(), codigoCategoriaDespesaCadastrada);
			codigoDespesaAtualizar = obterUltimoCodigoInseridoDespesa();
		}
		if(codigoDespesaAtualizar != -1) {
			// desc despesa é diferente da cadastrada
			String sqlAtualizarOrcamentoCodDespesa = "UPDATE orcamento "
												   + "SET cod_despesa = ?, data_despesa = ?, data_pagamento = ?, valor = ?, situacao = ? "
												   + "WHERE cod_despesa = ?";
			
			preparedStatement = conexaoBanco.prepareStatement(sqlAtualizarOrcamentoCodDespesa);
			preparedStatement.setLong(1, codigoDespesaAtualizar);
			preparedStatement.setDate(2, DataHora.converterLocalDateParaDate(despesaAtualizar.getData()));
			preparedStatement.setDate(3, DataHora.converterMonthDayParaDate(despesaAtualizar.getDiaPagamento()));
			preparedStatement.setFloat(4, despesaAtualizar.getValor());
			preparedStatement.setBoolean(5, despesaAtualizar.getSituacao());
			preparedStatement.setLong(6, despesaCadastrada.getCodigo());
			
		}else if(codigoFormaPagamentoAtualizar != -1) {
			// desc forma pagamento é diferente da cadastrada
			String sqlAtualizarOrcamentoCodFormaPagamento = "UPDATE orcamento "
					   							   + "SET cod_forma_pagamento = ?, data_despesa = ?, data_pagamento = ?, valor = ?, situacao = ? "
					   							   + "WHERE cod_despesa = ?";
			
			
			preparedStatement = conexaoBanco.prepareStatement(sqlAtualizarOrcamentoCodFormaPagamento);
			preparedStatement.setLong(1, codigoFormaPagamentoAtualizar);
			preparedStatement.setDate(2, DataHora.converterLocalDateParaDate(despesaAtualizar.getData()));
			preparedStatement.setDate(3, DataHora.converterMonthDayParaDate(despesaAtualizar.getDiaPagamento()));
			preparedStatement.setFloat(4, despesaAtualizar.getValor());
			preparedStatement.setBoolean(5, despesaAtualizar.getSituacao());
			preparedStatement.setLong(6, despesaAtualizar.getCodigo());
			
		}else {
			// nem a forma de pagamento foi alterada e nem a descrição da despesa.
			String sqlAtualizarOrcamentoCodFormaPagamento = "UPDATE orcamento "
					   + "SET data_despesa = ?, data_pagamento = ?, valor = ?, situacao = ? "
					   + "WHERE cod_despesa = ?";


			preparedStatement = conexaoBanco.prepareStatement(sqlAtualizarOrcamentoCodFormaPagamento);
			preparedStatement.setDate(1, DataHora.converterLocalDateParaDate(despesaAtualizar.getData()));
			preparedStatement.setDate(2, DataHora.converterMonthDayParaDate(despesaAtualizar.getDiaPagamento()));
			preparedStatement.setFloat(3, despesaAtualizar.getValor());
			preparedStatement.setBoolean(4, despesaAtualizar.getSituacao());
			preparedStatement.setLong(5, despesaAtualizar.getCodigo());
		}
		preparedStatement.execute();
		preparedStatement.close();
	}


	@Override
	public void delete(Despesa despesa) throws SQLException {
		
		
	}


	@Override
	public List<Despesa> select() throws SQLException {

		List<Despesa> despesas = new ArrayList<>();
		String sqlConsultaDespesa = "SELECT orc.data_despesa, orc.data_pagamento, pag.descricao AS forma_pagamento, desp.descricao AS descricao, orc.valor, orc.situacao, cat.descricao, orc.cod_despesa AS categoria "
							+ "FROM orcamento AS orc "
							+ "INNER JOIN despesa AS desp "
							+ "ON orc.cod_despesa = desp.codigo "
							+ "INNER JOIN forma_pagamento AS pag "
							+ "ON orc.cod_forma_pagamento = pag.codigo "
							+ "INNER JOIN categoria AS cat "
							+ "ON desp.cod_categoria = cat.codigo";
		
		try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultaDespesa)){
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				Despesa despesa = new Despesa();
				despesa.setData(tabelaDados.getDate("data_despesa").toLocalDate());
				LocalDate dataPagamento = tabelaDados.getDate("data_pagamento").toLocalDate();
				despesa.setDiaPagamento(MonthDay.of(dataPagamento.getMonthValue(), dataPagamento.getDayOfMonth()));
				despesa.setFormaPagamento(tabelaDados.getString("forma_pagamento"));
				despesa.setDescricao(tabelaDados.getString("descricao"));
				despesa.setValor(tabelaDados.getFloat("valor"));
				despesa.setSituacao(tabelaDados.getBoolean("situacao"));
				despesa.setCategoria(tabelaDados.getString("categoria"));
				despesa.setCodigo(tabelaDados.getLong("cod_despesa"));
				despesas.add(despesa);
			}
		}
		return despesas;
	}
	
	public List<Object> selectValoresSituacaoDespesas() throws SQLException{

		List<Object> valoresSituacaoDespesas = new ArrayList<>();
		String sqlConsultarValoresDespesa = "SELECT valor,situacao FROM orcamento";
		try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultarValoresDespesa)){
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				valoresSituacaoDespesas.add(tabelaDados.getFloat("valor"));
				valoresSituacaoDespesas.add(tabelaDados.getBoolean("situacao"));
			}
		}
		return valoresSituacaoDespesas;
	}
	
	private Despesa selectDespesaPorCodigo(long codigoDespesa) throws SQLException{
		String sqlDespesaPorCodigo = "SELECT orc.mes_ano, orc.data_despesa, orc.data_pagamento, pag.descricao AS forma_pagamento, desp.descricao AS descricao, orc.valor, orc.situacao, cat.descricao AS categoria "
								   + "FROM orcamento AS orc "
								   + "INNER JOIN despesa AS desp "
								   + "ON orc.cod_despesa = desp.codigo "
								   + "INNER JOIN forma_pagamento AS pag "
								   + "ON orc.cod_forma_pagamento = pag.codigo "
								   + "INNER JOIN categoria AS cat "
								   + "ON desp.cod_categoria = cat.codigo "
								   + "WHERE orc.cod_despesa = ?";
		
		Despesa despesa = new Despesa();
		try(PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlDespesaPorCodigo)){
			preparedStatement.setLong(1, codigoDespesa);
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				despesa.setCategoria(tabelaDados.getString("categoria"));
				despesa.setData(tabelaDados.getDate("data_despesa").toLocalDate());
				despesa.setDescricao(tabelaDados.getString("descricao"));
				LocalDate dataPagamento = tabelaDados.getDate("data_pagamento").toLocalDate();
				despesa.setDiaPagamento(MonthDay.of(dataPagamento.getMonthValue(), dataPagamento.getDayOfMonth()));
				despesa.setFormaPagamento(tabelaDados.getString("forma_pagamento"));
				despesa.setSituacao(tabelaDados.getBoolean("situacao"));
				despesa.setValor(tabelaDados.getFloat("valor"));
				despesa.setCodigo(codigoDespesa);
			}
		}
		
		
		return despesa;
	}
	
	public List<Despesa> selectDespesaPorCategoria(String categoria, int numeroMes) throws SQLException{
		List<Despesa>despesasCategoria = new ArrayList<>();
		
		String sqlDespesaPorCategoria = "SELECT orc.mes_ano, orc.data_despesa, orc.data_pagamento, pag.descricao AS forma_pagamento, desp.descricao AS descricao, orc.valor, orc.situacao, cat.descricao AS categoria, orc.cod_despesa "
				+ "FROM orcamento AS orc "
				+ "INNER JOIN despesa AS desp "
				+ "ON orc.cod_despesa = desp.codigo "
				+ "INNER JOIN forma_pagamento AS pag "
				+ "ON orc.cod_forma_pagamento = pag.codigo "
				+ "INNER JOIN categoria AS cat "
				+ "ON desp.cod_categoria = cat.codigo "
				+ "WHERE cat.descricao = ? AND orc.mes_ano = ?";
		
		if(categoria.equalsIgnoreCase("todas")) {
			return selectTodasDespesasMensais(numeroMes);
		}
		else {
			try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlDespesaPorCategoria)){
				preparedStatement.setString(1, categoria);
				preparedStatement.setInt(2, numeroMes);
				ResultSet tabelaDados = preparedStatement.executeQuery();
				while(tabelaDados.next()) {
					Despesa despesa = new Despesa();
					despesa.setData(tabelaDados.getDate("data_despesa").toLocalDate());
					LocalDate dataPagamento = tabelaDados.getDate("data_pagamento").toLocalDate();
					despesa.setDiaPagamento(MonthDay.of(dataPagamento.getMonthValue(), dataPagamento.getDayOfMonth()));
					despesa.setFormaPagamento(tabelaDados.getString("forma_pagamento"));
					despesa.setDescricao(tabelaDados.getString("descricao"));
					despesa.setValor(tabelaDados.getFloat("valor"));
					despesa.setSituacao(tabelaDados.getBoolean("situacao"));
					despesa.setCategoria(tabelaDados.getString("categoria"));
					despesa.setCodigo(tabelaDados.getLong("cod_despesa"));
					despesasCategoria.add(despesa);
				}
			}
		}
		return despesasCategoria;
	}
	
	private List<Despesa> selectTodasDespesasMensais(int numeroMes) throws SQLException{
		String sqlTodasDespesasMes = "SELECT orc.mes_ano, orc.data_despesa, orc.data_pagamento, pag.descricao AS forma_pagamento, desp.descricao AS descricao, orc.valor, orc.situacao, cat.descricao AS categoria, orc.cod_despesa "
				+ "FROM orcamento AS orc "
				+ "INNER JOIN despesa AS desp "
				+ "ON orc.cod_despesa = desp.codigo "
				+ "INNER JOIN forma_pagamento AS pag "
				+ "ON orc.cod_forma_pagamento = pag.codigo "
				+ "INNER JOIN categoria AS cat "
				+ "ON desp.cod_categoria = cat.codigo "
				+ "WHERE orc.mes_ano = ?";
		
		List<Despesa> despesasMensais = new ArrayList<>();
		try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlTodasDespesasMes)){
			preparedStatement.setInt(1, numeroMes);
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				Despesa despesa = new Despesa();
				despesa.setData(tabelaDados.getDate("data_despesa").toLocalDate());
				LocalDate dataPagamento = tabelaDados.getDate("data_pagamento").toLocalDate();
				despesa.setDiaPagamento(MonthDay.of(dataPagamento.getMonthValue(), dataPagamento.getDayOfMonth()));
				despesa.setFormaPagamento(tabelaDados.getString("forma_pagamento"));
				despesa.setDescricao(tabelaDados.getString("descricao"));
				despesa.setValor(tabelaDados.getFloat("valor"));
				despesa.setSituacao(tabelaDados.getBoolean("situacao"));
				despesa.setCategoria(tabelaDados.getString("categoria"));
				despesa.setCodigo(tabelaDados.getLong("cod_despesa"));
				despesasMensais.add(despesa);
			}
		}
		return despesasMensais;
	}
	
	public List<Object> selectCategoriasValoresMensal(int numeroMes) throws SQLException{
		List<Object> categoriaValores = new ArrayList<>();
		String sqlConsultaCategoriaValoresMensal = "SELECT cat.descricao, SUM(orc.valor) AS total_valor "
										 		 + "FROM orcamento AS orc "
										 		 + "INNER JOIN despesa AS desp ON desp.codigo = orc.cod_despesa "
										 		 + "INNER JOIN categoria AS cat ON desp.cod_categoria = cat.codigo "
										 		 + "WHERE orc.mes_ano = ? "
										 		 + "GROUP BY cat.descricao";
		try(PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultaCategoriaValoresMensal)){
			preparedStatement.setInt(1, numeroMes);
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				categoriaValores.add(tabelaDados.getString("descricao"));
				categoriaValores.add(tabelaDados.getFloat("total_valor"));
			}
		}
		
		return categoriaValores;
	}
}
