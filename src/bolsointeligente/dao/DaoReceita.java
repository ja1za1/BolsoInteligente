package bolsointeligente.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bolsointeligente.entities.Receita;
import bolsointeligente.utils.DataHora;

public class DaoReceita extends Dao<Receita> {
	
	private final static long NAO_CADASTRADO = 0;

	public DaoReceita(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Receita receita) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		
		long codigoRenda = obterCodigoReceita(receita.getDescricao());
		
		if(codigoRenda == NAO_CADASTRADO) {
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
		
		return (tabelaDados.next()) ? tabelaDados.getLong("codigo") : NAO_CADASTRADO;
		
	}
	
	@Override
	public List<Receita> select() throws SQLException {
		List<Receita> receitas = new ArrayList<>();
		String sqlConsultaReceitas = "SELECT renda.descricao,renda_m.data,renda_m.valor "
								   + "FROM renda "
								   + "INNER JOIN renda_mensal AS renda_m "
								   + "ON renda_m.cod_renda = renda.codigo";
		try(PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultaReceitas)){
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				Receita receita = new Receita();
				receita.setDescricao(tabelaDados.getString("descricao"));
				receita.setValor(tabelaDados.getFloat("valor"));
				receita.setData(tabelaDados.getDate("data").toLocalDate());
				receitas.add(receita);
			}
		}
		return receitas;
	}
	
	public List<Float> selectValoresReceita() throws SQLException{
		String sqlConsultaValoresReceitas = "SELECT valor FROM renda_mensal";
		List<Float> valoresReceitas = new ArrayList<>();
		try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultaValoresReceitas)){
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				valoresReceitas.add(tabelaDados.getFloat("valor"));
			}
		}
		return valoresReceitas;
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
