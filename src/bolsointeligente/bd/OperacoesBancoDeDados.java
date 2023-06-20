package bolsointeligente.bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OperacoesBancoDeDados<T> {
	
	void update(T dadosAtualizar) throws SQLException;
	
	void delete(T dadosDeletar) throws SQLException;
	
	void insert(T dadosInserir) throws SQLException;
	
	ResultSet select(T dadosSelecionar) throws SQLException;
	
}
