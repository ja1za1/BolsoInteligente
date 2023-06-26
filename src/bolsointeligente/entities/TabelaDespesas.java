package bolsointeligente.entities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;


public class TabelaDespesas {
	
	public static final String[] NOME_COLUNAS = {"Data", "Dia", "Tipo", "Descrição", "Valor", "Paga" };
	private final int QUANTIDADE_COLUNAS_INICIAL = 0;
	
	private JTable tabelaDespesas;
	private DefaultTableModel modeloTabelaDespesa;
	
	public TabelaDespesas() {
		modeloTabelaDespesa = new DefaultTableModel(NOME_COLUNAS, QUANTIDADE_COLUNAS_INICIAL){
			
			private final Class<?>[] CLASSES_COLUNAS = {LocalDate.class, Integer.class, String.class, String.class, Float.class, Boolean.class};
			
			@Override
			public Class<?> getColumnClass(int coluna) {
				return CLASSES_COLUNAS[coluna];
			}
			
		};
		tabelaDespesas = new JTable(modeloTabelaDespesa);
		
		definirRenderizadoresColunas();
		definirLarguraColunas();
		definirAlturaLinhas();
		exibirLinhasVerticaisHorizontais();
		definirFonte();
		gerarAutoRowSorter();
		definirCorFundo();
		definirTabelaAjustaTamanhoViewport();
		inicializarDadosTabela();
	}
	
	private void definirAlturaLinhas() {
		tabelaDespesas.setRowHeight(30);
	}

	private void definirRenderizadoresColunas() {
		tabelaDespesas.setDefaultRenderer(LocalDate.class, new RenderizadorData());
		DateTableEditor datePicker = new DateTableEditor(false, true, true);
		datePicker.getDatePickerSettings().setTranslationClear("Limpar");
		datePicker.clickCountToEdit = 2;
		tabelaDespesas.getColumnModel().getColumn(0).setCellEditor(datePicker);
		tabelaDespesas.setDefaultRenderer(Integer.class, new RenderizadorInteger());
		tabelaDespesas.setDefaultRenderer(Float.class, new RenderizadorFloat());
	}

	private void inicializarDadosTabela() {
		List<Despesa> despesasCadastradas = BolsoInteligente.despesas;
		Object[] valores = null;
		int linha = 0;
		for(Despesa despesa : despesasCadastradas) {
			valores = new Object[] {
				despesa.getData(),
				despesa.getDiaPagamento().getDayOfMonth(),
				despesa.getFormaPagamento(),
				despesa.getDescricao(),
				despesa.getValor(),
				despesa.getSituacao()
			};
			atualizarTabela(valores, linha);
			linha++;
		}
	}

	private void definirTabelaAjustaTamanhoViewport() {
		tabelaDespesas.setFillsViewportHeight(true);
	}

	private void definirCorFundo() {
		tabelaDespesas.setBackground(Color.WHITE);
	}

	private void gerarAutoRowSorter() {
		tabelaDespesas.setAutoCreateRowSorter(true);
	}

	private void definirFonte() {
		tabelaDespesas.setFont(new Font("Arial", Font.PLAIN, 12));
	}

	private void definirLarguraColunas() {
		for(int i = 0; i < tabelaDespesas.getModel().getColumnCount(); i++) {
			TableColumn coluna = tabelaDespesas.getColumnModel().getColumn(i);
			if(i == 3) {
				coluna.setPreferredWidth(200);
			}
			else if(i == 0) {
				coluna.setPreferredWidth(150);
			}
			else if(i == 1) {
				coluna.setPreferredWidth(25);
			}
			else if(i == 4) {
				coluna.setPreferredWidth(100);
			}
			else {
				coluna.setPreferredWidth(50);
			}
		}
	}
	
	private void exibirLinhasVerticaisHorizontais() {
		tabelaDespesas.setShowVerticalLines(true);
        tabelaDespesas.setShowHorizontalLines(true);
	}
	
	public JTable getTabelaDespesas() {
		return tabelaDespesas;
	}
	
	public DefaultTableModel getModeloTabelaDespesa() {
		return modeloTabelaDespesa;
	}

	public void setTabelaDespesas(JTable tabelaDespesas) {
		this.tabelaDespesas = tabelaDespesas;
	}

	public void atualizarTabela(Object[] dadosInserir, int linha) {
		modeloTabelaDespesa.insertRow(linha, dadosInserir);
	}

	private class RenderizadorInteger extends DefaultTableCellRenderer {
		
		public RenderizadorInteger() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(value instanceof Integer)
				value = String.format("%02d", (Integer)value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		
	}
	
	private class RenderizadorFloat extends DefaultTableCellRenderer {
		
		public RenderizadorFloat() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(value instanceof Float)
				value = String.format("%,.2f", (Float)value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		
	}
	private class RenderizadorData extends DefaultTableCellRenderer {
		
		public RenderizadorData() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(value instanceof LocalDate)
				value = ((LocalDate)value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		
	}
	
	
}
