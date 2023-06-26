package bolsointeligente.bd;

import java.sql.SQLException;
import java.util.List;

public interface OperacoesBancoDeDados<T> {
	
	void update(T dadosAtualizar) throws SQLException;
	
	void delete(T dadosDeletar) throws SQLException;
	
	void insert(T dadosInserir) throws SQLException;
	
	List<T> select() throws SQLException;
	
}
