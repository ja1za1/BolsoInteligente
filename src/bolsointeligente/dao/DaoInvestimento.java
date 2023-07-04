package bolsointeligente.dao;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import bolsointeligente.entities.Investimento;
import bolsointeligente.utils.DataHora;

public class DaoInvestimento extends Dao<Investimento> {

	public DaoInvestimento(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void insert(Investimento investimento) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		
		String sqlInvestimento = "INSERT INTO investimento (objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento) "
							   + "VALUES (?,?,?,?,?,?,?,?)";
		preparedStatement = conexaoBanco.prepareStatement(sqlInvestimento);
		preparedStatement.setString(1, investimento.getObjetivo());
		preparedStatement.setString(2, investimento.getEstrategia());
		preparedStatement.setString(3, investimento.getNome());
		preparedStatement.setFloat(4, investimento.getValorInvestido());
		preparedStatement.setFloat(5, investimento.getPosicao());
		preparedStatement.setFloat(6, investimento.getRendimentoBruto());
		preparedStatement.setFloat(7, investimento.getRentabilidade());
		preparedStatement.setDate(8, DataHora.converterLocalDateParaDate(investimento.getVencimento()));
		preparedStatement.execute();
	}
	
	@Override
	public void update(Investimento dadosAtualizar) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Investimento dadosDeletar) throws SQLException {
		// TODO Auto-generated method stub

	}


	@Override
	public List<Investimento> select() throws SQLException {
		List<Investimento>investimentos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		String sqlObterInvestimentos = "SELECT * FROM investimento";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlObterInvestimentos);
		
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		while(tabelaDados.next()) {
			Investimento investimento = new Investimento();
			investimento.setCodigo(tabelaDados.getLong("codigo"));
			investimento.setObjetivo(tabelaDados.getString("objetivo"));
			investimento.setEstrategia(tabelaDados.getString("estrategia"));
			investimento.setNome(tabelaDados.getString("nome"));
			investimento.setValorInvestido(tabelaDados.getFloat("valor_investido"));
			investimento.setPosicao(tabelaDados.getFloat("posicao"));
			investimento.setRendimentoBruto(tabelaDados.getFloat("rendimento_bruto"));
			investimento.setRentabilidade(tabelaDados.getFloat("rentabilidade"));
			investimento.setVencimento(tabelaDados.getDate("vencimento").toLocalDate());
			investimentos.add(investimento);
			
		}
		return investimentos;
	}
	
	public Float selectValorTotalInvestido() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		String sqlObterValorTotalInvestido = "SELECT SUM(valor_investido) FROM investimento";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlObterValorTotalInvestido);
		
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getFloat("sum") : 0;
		
	}
	
	public Float selectValorTotalAcumulado() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection conexaoBanco = getConexaoBanco();
		String sqlObterValorTotalInvestido = "SELECT SUM(posicao) FROM investimento";
		
		preparedStatement = conexaoBanco.prepareStatement(sqlObterValorTotalInvestido);
		
		ResultSet tabelaDados = preparedStatement.executeQuery();
		
		return (tabelaDados.next()) ? tabelaDados.getFloat("sum") : 0;
		
	}
	
	

}
