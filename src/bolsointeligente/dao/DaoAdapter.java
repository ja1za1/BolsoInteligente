package bolsointeligente.dao;

import java.sql.SQLException;
import java.util.List;

import bolsointeligente.bd.OperacoesBancoDeDados;

/**
 * Esta classe tem como objetivo fornecer implementações vazias da interface {@link OperacoesBancoDeDados} para que não seja necessário implementar todos
 * os métodos dessa interface ao criar uma classe do tipo Dao.
 * 
 * @author JoaoLucas
 *
 * @param <T> o objeto que será utilizado nas operações do banco de dados.
 */

public abstract class DaoAdapter<T> implements OperacoesBancoDeDados<T> {

	@Override
	public void update(T dadosAtualizar) throws SQLException {
		
	}

	@Override
	public void delete(T dadosDeletar) throws SQLException {
		
	}

	@Override
	public void insert(T dadosInserir) throws SQLException {
		
	}

	@Override
	public List<T> select() throws SQLException {
		return null;
	}
	
}
