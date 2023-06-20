package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bolsointeligente.entities.Receita;

public class DaoReceita extends Dao<Receita> {

	public DaoReceita(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Receita receita) throws SQLException {
		String sqlRenda = "INSERT INTO renda (descricao) "
				   		+ "VALUES (?)";
		PreparedStatement preparedStatement;
		
		String sqlRendaMensal = "INSERT INTO renda_mensal (cod_renda, data, valor)"
							  + "VALUES (?, ?, ?)";
		Connection conexaoBanco = getConexaoBanco();
		
		long codigoRenda = obterCodigoReceita(receita.getDescricao());
		
		if(codigoRenda == 0) {
			preparedStatement = conexaoBanco.prepareStatement(sqlRenda);
			preparedStatement.setString(1, receita.getDescricao());
			preparedStatement.execute();
			codigoRenda = obterCodigoReceita(receita.getDescricao());
		}
		
		preparedStatement = conexaoBanco.prepareStatement(sqlRendaMensal);
		preparedStatement.setLong(1, codigoRenda);
		preparedStatement.setDate(2, Date.valueOf(receita.getData()));
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
	public ResultSet select(Receita dadosSelecionar) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void update(Receita dadosAtualizar) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Receita dadosDeletar) throws SQLException {
		// TODO Auto-generated method stub
		
	}



}
