package bolsointeligente.entities;

import java.awt.Color;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;


public class TabelaDespesas implements TableModelListener{
	
	public static final String[] NOME_COLUNAS = {"Data", "Dia", "Tipo", "Descrição", "Valor", "Paga" };
	
	private final int QUANTIDADE_COLUNAS_INICIAL = 2;
	
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
		modeloTabelaDespesa.addTableModelListener(this);
		tabelaDespesas = new JTable(modeloTabelaDespesa);
		
		
		definirRenderizadoresColunas();
		definirLarguraColunas();
		definirAlturaLinhas();
		exibirLinhasVerticaisHorizontais();
		definirFonte();
		gerarAutoRowSorter();
		definirCorFundo();
		definirTabelaAjustaTamanhoViewport();
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
		JComboBox<Integer> quantidadeDiasMes = new JComboBox<>();
		tabelaDespesas.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(quantidadeDiasMes));
		tabelaDespesas.setDefaultRenderer(Float.class, new RenderizadorFloat());
	}

	public void atualizarDadosTabela(String categoria, int numeroMes) {

		List<Despesa> despesas;
		
		try {
			despesas = BolsoInteligente.obterDespesasCategoriaMes(categoria, numeroMes);
		} catch (SQLException e) {
			despesas = new ArrayList<>();
		}
		limparLinhasTabela();
		modeloTabelaDespesa.setRowCount(QUANTIDADE_COLUNAS_INICIAL);
		Object[] valores = null;
		int linha = 0;
		for(Despesa despesa : despesas) {
			valores = new Object[] {
				despesa.getData(),
				despesa.getDiaPagamento().getDayOfMonth(),
				despesa.getFormaPagamento(),
				despesa.getDescricao(),
				despesa.getValor(),
				despesa.getSituacao()
			};
			inserirLinhaTabela(valores, linha);
			linha++;
		}
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getType() == TableModelEvent.UPDATE) {
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        DefaultTableModel model = (DefaultTableModel)e.getSource();
	        String columnName = model.getColumnName(column);
	        Object data = model.getValueAt(row, column);
	        Container container = tabelaDespesas.getParent();
	        
	        
	        System.out.println(String.format("Linha = %d Coluna = %d\nNome coluna = %s\n\n" + data, row,column,columnName));
	        System.out.println("nome componente = " + container.getName());
		}
		else if(e.getType() == TableModelEvent.INSERT) {
			LocalDate data = (LocalDate)modeloTabelaDespesa.getValueAt(e.getFirstRow(), 0);
			if(data != null) {
				int diaSelecionado = (int) modeloTabelaDespesa.getValueAt(e.getFirstRow(), 1);
				int daysInMonth = YearMonth.of(2023, data.getMonthValue()).lengthOfMonth();
				@SuppressWarnings("unchecked")
				JComboBox<Integer> comboBox = (JComboBox<Integer>)((DefaultCellEditor) tabelaDespesas.getCellEditor(e.getFirstRow(), 1)).getComponent();
				comboBox.removeAllItems();
		        for (int i = 1; i <= daysInMonth; i++) {
		        	comboBox.addItem(i);
		        }
		        comboBox.setSelectedItem(diaSelecionado);
			}
		}
	}

	private void limparLinhasTabela() {
		this.modeloTabelaDespesa.setRowCount(0);
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
			
			if(i == 0) {
				coluna.setPreferredWidth(150);
			}
			else if(i == 1) {
				coluna.setPreferredWidth(50);
			}
			else if(i == 3) {
				coluna.setPreferredWidth(200);
			}
			else if(i == 4) {
				coluna.setPreferredWidth(100);
			}
			else {
				coluna.setPreferredWidth(70);
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

	public void inserirLinhaTabela(Object[] dadosInserir, int linha) {
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
