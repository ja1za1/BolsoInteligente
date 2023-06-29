package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.SQLException;

import bolsointeligente.bd.OperacoesBancoDeDados;


/**
 * Esta classe é uma abstração de um DAO (Data Access Object) e extende a classe {@link DaoAdapter} que é uma classe 
 * que fornece implementações vazias dos métodos de acesso ao banco de dados definidos pela enumeração {@link OperacoesBancoDeDados}
 * para que o usuário tenha a flexibilidade de implementar apenas os métodos necessários nas subclasses de Dao.
 * 
 * @author João Lucas
 *
 * @param <T> o objeto que será utilizado nas operações do banco de dados.
 */

public abstract class Dao<T> extends DaoAdapter<T> {
	
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
