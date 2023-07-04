package bolsointeligente.entities.tables;

import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import bolsointeligente.entities.BolsoInteligente;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import bolsointeligente.entities.Investimento;
import bolsointeligente.entities.tables.EditorCelula.VerificadorFloatValido;
import bolsointeligente.entities.tables.EditorCelula.VerificadorTextoVazio;

public class TabelaInvestimentos implements TableModelListener{
	
	
	public static final String[] NOME_COLUNAS = {"Objetivo", "Estratégia", "Nome", "Valor Investido", "Posição", "Rendimento Bruto", "Rentabilidade", "Vencimento"};
	public static final int  COLUNA_OBJETIVO 		 = 0,
							 COLUNA_ESTRATEGIA       = 1,
							 COLUNA_NOME 			 = 2,
							 COLUNA_VALOR_INVESTIDO  = 3,
							 COLUNA_POSICAO 		 = 4,
							 COLUNA_RENDIMENTO_BRUTO = 5,
							 COLUNA_RENTABILIDADE 	 = 6,
							 COLUNA_VENCIMENTO 		 = 7,
							 COLUNA_INVISIVEL_CODIGO = 8;
	
	private JTable tabelaInvestimentos;
	private DefaultTableModel modeloTabelaInvestimentos;
	
	private final int QUANTIDADE_COLUNAS_INICIAL = 1;
	
	public TabelaInvestimentos() {
		
		modeloTabelaInvestimentos = new DefaultTableModel(NOME_COLUNAS,QUANTIDADE_COLUNAS_INICIAL){
			
		private final Class<?>[] CLASSES_COLUNAS = {String.class, String.class, String.class, Float.class, Float.class, Float.class,Float.class, LocalDate.class, Object.class };
			
			@Override
			public Class<?> getColumnClass(int coluna) {
				return CLASSES_COLUNAS[coluna];
			}

			@Override
			public boolean isCellEditable(int linha, int coluna) {
				switch (coluna) {
				case COLUNA_RENTABILIDADE:
					return false;
				case COLUNA_RENDIMENTO_BRUTO:
					return false;
				default:
					return true;
				}
			}
		};
		tabelaInvestimentos = new JTable(modeloTabelaInvestimentos);
		modeloTabelaInvestimentos.addTableModelListener(this);
		definirPropriedadesPadrao();
	}

	
	private void definirPropriedadesPadrao() {
		definirLarguraColunas();
		definirAlturaColunas();
		exibirLinhasVerticaisHorizontais();
		gerarAutoRowSorter();
		definirTabelaAjustaTamanhoViewport();
		definirCorFundo();
		definirFonte();
		definirRenderizadoresColunas();
		definirEditoresColunas();
	}
	


	@Override
	public void tableChanged(TableModelEvent eventoAlteracaoTabela) {
		if(eventoAlteracaoTabela.getType() == TableModelEvent.UPDATE) {
			
		}
		else if(eventoAlteracaoTabela.getType() == TableModelEvent.INSERT) {
			
		}
	}
	
	private void definirRenderizadoresColunas() {
		tabelaInvestimentos.setDefaultRenderer(LocalDate.class, new RenderizadorData());
		tabelaInvestimentos.setDefaultRenderer(Float.class, new RenderizadorFloat());
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_RENTABILIDADE).setCellRenderer(new RenderizadorPorcentagem());
	}
	
	private void definirEditoresColunas() {
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_OBJETIVO).setCellEditor(new EditorCelula(new VerificadorTextoVazio()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_ESTRATEGIA).setCellEditor(new EditorCelula(new VerificadorTextoVazio()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_NOME).setCellEditor(new EditorCelula(new VerificadorTextoVazio()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_VALOR_INVESTIDO).setCellEditor(new EditorCelula(new VerificadorFloatValido()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_POSICAO).setCellEditor(new EditorCelula(new VerificadorFloatValido()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_RENDIMENTO_BRUTO).setCellEditor(new EditorCelula(new VerificadorFloatValido()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_RENTABILIDADE).setCellEditor(new EditorCelula(new VerificadorFloatValido()));
		tabelaInvestimentos.getColumnModel().getColumn(COLUNA_VENCIMENTO).setCellEditor(gerarDatePicker());
	}
	
	private DateTableEditor gerarDatePicker() {
		DateTableEditor datePicker = new DateTableEditor(false, true, true);
		datePicker.getDatePickerSettings().setFormatForDatesCommonEra("dd/MM/uuuu");
		datePicker.getDatePickerSettings().setAllowEmptyDates(false);
		datePicker.clickCountToEdit = 2;
		return datePicker;
	}

	private void definirAlturaColunas() {
		tabelaInvestimentos.setRowHeight(30);
	}
	
	public void atualizarTabela() {
		List<Investimento> investimentos;
		
		try {
			investimentos = BolsoInteligente.obterInvestimentos();
		} catch (SQLException e) {
			investimentos = new ArrayList<>();
		}
		limparLinhasTabela();
		modeloTabelaInvestimentos.setRowCount(QUANTIDADE_COLUNAS_INICIAL);
		Object[] valores = null;
		int linha = 0;
		for(Investimento investimento : investimentos) {
			valores = new Object[]{
				investimento.getObjetivo(),
				investimento.getEstrategia(),
				investimento.getNome(),
				investimento.getValorInvestido(),
				investimento.getPosicao(),
				investimento.getRendimentoBruto(),
				investimento.getRentabilidade(),
				investimento.getVencimento()
			};
			inserirLinhaTabela(valores,linha);
			linha++;
		}
		
	}


	private void inserirLinhaTabela(Object[] dadosInserir, int linha) {
		modeloTabelaInvestimentos.insertRow(linha, dadosInserir);
	}


	private void definirLarguraColunas() {
		
		for(int i = 0; i < tabelaInvestimentos.getModel().getColumnCount(); i++) {
			TableColumn coluna = tabelaInvestimentos.getColumnModel().getColumn(i);
			
			if(i == COLUNA_INVISIVEL_CODIGO) {
				tabelaInvestimentos.getColumnModel().removeColumn(coluna);
			}
			else {
				coluna.setWidth(200);
			}
			
		}
	}
	
	private void exibirLinhasVerticaisHorizontais() {
		tabelaInvestimentos.setShowVerticalLines(true);
        tabelaInvestimentos.setShowHorizontalLines(true);
	}
	
	private void limparLinhasTabela() {
		this.modeloTabelaInvestimentos.setRowCount(0);
	}

	private void definirTabelaAjustaTamanhoViewport() {
		tabelaInvestimentos.setFillsViewportHeight(true);
	}

	private void definirCorFundo() {
		tabelaInvestimentos.setBackground(Color.WHITE);
	}

	private void gerarAutoRowSorter() {
		tabelaInvestimentos.setAutoCreateRowSorter(true);
	}

	private void definirFonte() {
		tabelaInvestimentos.setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
	public JTable getTabelaInvestimentos() {
		return tabelaInvestimentos;
	}

	public DefaultTableModel getModeloTabelaInvestimentos() {
		return modeloTabelaInvestimentos;
	}

}
