package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bolsointeligente.entities.Receita;
import bolsointeligente.utils.DataHora;

public class DaoReceita extends Dao<Receita> {

	public DaoReceita(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Receita receita) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		
		long codigoRenda = obterCodigoReceita(receita.getDescricao());
		
		if(codigoRenda == 0) {
			inserirRenda(preparedStatement, conexaoBanco, receita);
			codigoRenda = obterCodigoReceita(receita.getDescricao());
		}
		inserirRendaMensal(preparedStatement, conexaoBanco, receita, codigoRenda);
	}
	
	private void inserirRenda(PreparedStatement preparedStatement, Connection conexaoBanco, Receita receita) throws SQLException {
		String sqlRenda = "INSERT INTO renda (descricao) "
		   				+ "VALUES (?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlRenda);
		preparedStatement.setString(1, receita.getDescricao());
		preparedStatement.execute();
	}
	
	private void inserirRendaMensal(PreparedStatement preparedStatement, Connection conexaoBanco, Receita receita, long codigoRenda) throws SQLException{
		String sqlRendaMensal = "INSERT INTO renda_mensal (cod_renda, data, valor) "
				  			  + "VALUES (?, ?, ?)";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlRendaMensal);
		preparedStatement.setLong(1, codigoRenda);
		preparedStatement.setDate(2, DataHora.converterLocalDateParaDate(receita.getData()));
		preparedStatement.setFloat(3, receita.getValor());
		preparedStatement.execute();
	}
	
	private long obterCodigoReceita(String descricao) throws SQLException{
		String sqlObterCodigo = "SELECT codigo "
							  + "FROM renda "
							  + "WHERE descricao = ?";
		
		PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlObterCodigo);
		preparedStatement.setString(1, descricao);
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : 0l;
		
	}
	
	@Override
	public ResultSet select(Receita receita) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void update(Receita receita) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Receita receita) throws SQLException {
		// TODO Auto-generated method stub
		
	}



}
