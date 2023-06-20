package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import bolsointeligente.entities.Despesa;

public class DaoDespesa extends Dao<Despesa> {
	
	public DaoDespesa(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public void update(Despesa dadosAtualizar) throws SQLException {
		
	}

	@Override
	public void delete(Despesa dadosDeletar) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(Despesa dadosInserir) throws SQLException {
		
	}

	@Override
	public ResultSet select(Despesa dadosSelecionar) throws SQLException {
		String consultaSQL = "SELECT * FROM despesa";
		return null;
	}

}
