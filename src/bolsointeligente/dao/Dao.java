package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.SQLException;

import bolsointeligente.bd.OperacoesBancoDeDados;



public abstract class Dao<T> implements OperacoesBancoDeDados<T> {
	
	private Connection conexaoBanco;
	
	public Dao(Connection conexaoBanco) {
		this.conexaoBanco = conexaoBanco;
	}

	public Connection getConexaoBanco() {
		return conexaoBanco;
	}
	
	public static <T> void inserirDadosBancoDeDados(Dao<T> dao, T dadosInserir) throws SQLException {
		dao.insert(dadosInserir);
	}

}
