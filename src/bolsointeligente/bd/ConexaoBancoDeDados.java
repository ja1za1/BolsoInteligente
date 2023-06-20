package bolsointeligente.bd;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoBancoDeDados implements AutoCloseable {
	
	private Connection conexaoBanco;
		
	private String urlBanco, usuario, senha;
	
	public ConexaoBancoDeDados(String urlBanco, String usuario, String senha) throws SQLException {
		this.urlBanco = urlBanco;
		this.usuario = usuario;
		this.senha = senha;
		conexaoBanco = DriverManager.getConnection(urlBanco, usuario, senha);
	}

	public String getUrlBanco() {
		return urlBanco;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getSenha() {
		return senha;
	}
	
	public Connection getConexaoBanco() {
		return conexaoBanco;
	}

	@Override
	public void close() throws SQLException{
		if(conexaoBanco != null) conexaoBanco.close();
	}

}
