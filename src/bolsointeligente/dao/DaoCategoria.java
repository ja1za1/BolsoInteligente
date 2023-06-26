package bolsointeligente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCategoria extends Dao<String> {

	public DaoCategoria(Connection conexaoBanco) {
		super(conexaoBanco);
	}

	@Override
	public List<String> select() throws SQLException {
		String sqlConsultaCategoria = "SELECT descricao FROM categoria";
		List<String> categoriasCadastradas = new ArrayList<>();
		try (PreparedStatement preparedStatement = getConexaoBanco().prepareStatement(sqlConsultaCategoria)){
			ResultSet tabelaDados = preparedStatement.executeQuery();
			while(tabelaDados.next()) {
				categoriasCadastradas.add(tabelaDados.getString("descricao"));
			}
		}
		return categoriasCadastradas;
		
	}
	
	

}
