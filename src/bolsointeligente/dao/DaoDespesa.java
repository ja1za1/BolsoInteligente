package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bolsointeligente.entities.Despesa;
import bolsointeligente.utils.DataHora;

public class DaoDespesa extends Dao<Despesa> {
	
	public DaoDespesa(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Despesa despesa) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();

		long codigoFormaPagamento = obterCodigoFormaPagamento(despesa.getFormaPagamento().toString());
		
		if(codigoFormaPagamento == 0) {
			inserirFormaPagamento(preparedStatement,conexaoBanco,despesa);
			codigoFormaPagamento = obterCodigoFormaPagamento(despesa.getFormaPagamento().toString());
		}
		
		long codigoCategoria = obterCodigoCategoria(despesa.getCategoria().toString());
		
		if(codigoCategoria == 0) {
			inserirCategoria(preparedStatement,conexaoBanco,despesa);
			codigoCategoria = obterCodigoCategoria(despesa.getCategoria().toString());
		}
		
		long codigoDespesa = obterCodigoDespesa(despesa.getDescricao(),codigoCategoria);
		
		if(codigoDespesa == 0) {
			inserirDespesa(preparedStatement,conexaoBanco,despesa,codigoCategoria);
			codigoDespesa = obterCodigoDespesa(despesa.getDescricao(), codigoCategoria);
		}
		
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
		preparedStatement.setBoolean(7, despesa.getPago());
		preparedStatement.execute();
		
	}

	private void inserirDespesa(PreparedStatement preparedStatement, Connection conexaoBanco, Despesa despesa, long codigoCategoria) throws SQLException {
		String sqlDespesa = "INSERT INTO despesa (descricao, cod_categoria) "
						  + "VALUES (?, ?)";
		preparedStatement = conexaoBanco.prepareStatement(sqlDespesa);
		preparedStatement.setString(1, despesa.getDescricao());
		preparedStatement.setLong(2, codigoCategoria);
		preparedStatement.execute();
	}

	private void inserirCategoria(PreparedStatement preparedStatement, Connection conexaoBanco, Despesa despesa) throws SQLException {
		String sqlCategoria = "INSERT INTO categoria (descricao) "
							+ "VALUES (?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlCategoria);
		preparedStatement.setString(1, despesa.getCategoria().toString());
		preparedStatement.execute();
	}

	private void inserirFormaPagamento(PreparedStatement preparedStatement, Connection conexaoBanco, Despesa despesa) throws SQLException {
		String sqlFormaPagamento = "INSERT INTO forma_pagamento (descricao) "
								 + "VALUES (?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlFormaPagamento);
		preparedStatement.setString(1, despesa.getFormaPagamento().toString());
		preparedStatement.execute();
	}

	private long obterCodigoFormaPagamento(String descricaoFormaPagamento) throws SQLException {
		String sqlObterCodigo = "SELECT codigo "
							  + "FROM forma_pagamento "
							  + "WHERE descricao = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricaoFormaPagamento);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : 0l;
	}
	
	private long obterCodigoCategoria(String descricaoCategoria) throws SQLException {
		String sqlObterCodigo = "SELECT codigo "
							  + "FROM categoria "
							  + "WHERE descricao = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricaoCategoria);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : 0l;
	}
	
	private long obterCodigoDespesa(String descricaoDespesa, long codigoCategoria) throws SQLException {
		String sqlObterCodigo = "SELECT codigo "
							  + "FROM despesa "
							  + "WHERE descricao = ? "
							  + "AND cod_categoria = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricaoDespesa);
		preparedStatement.setLong(2, codigoCategoria);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : 0l;
	}

	@Override
	public void update(Despesa despesa) throws SQLException {
		
	}

	@Override
	public void delete(Despesa despesa) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ResultSet select(Despesa despesa) throws SQLException {
		String consultaSQL = "SELECT * FROM despesa";
		return null;
	}

}
