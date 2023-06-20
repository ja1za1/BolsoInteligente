package bolsointeligente.dao;

import java.sql.Connection;

import bolsointeligente.bd.OperacoesBancoDeDados;



public abstract class Dao<T> implements OperacoesBancoDeDados<T> {
	
	private Connection conexaoBanco;
	
	public Dao(Connection conexaoBanco) {
		this.conexaoBanco = conexaoBanco;
	}

	public Connection getConexaoBanco() {
		return conexaoBanco;
	}

}
