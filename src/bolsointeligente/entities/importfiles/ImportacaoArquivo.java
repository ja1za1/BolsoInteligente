package bolsointeligente.entities.importfiles;

import java.io.File;

public interface ImportacaoArquivo {
	
	/**
	 * Faz a importação do(s) arquivo(s).
	 * 
	 * @return um relatório contendo as informações sobre importação do(s) arquivo(s).
	 */
	
	String importarArquivos(File... arquivosImportar);
	
}
