package bolsointeligente.entities.tables;

import java.awt.Color;




import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import bolsointeligente.dao.Dao;
import bolsointeligente.dao.DaoDespesa;
import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.Despesa;
import bolsointeligente.entities.tables.EditorCelula.VerificadorFloatValido;
import bolsointeligente.entities.tables.EditorCelula.VerificadorTextoVazio;
import bolsointeligente.gui.IgBolsoInteligente;
import mos.es.InputOutput;


public class TabelaDespesas implements TableModelListener{
	
	public static final String[] NOME_COLUNAS = {"Data", "Dia", "Tipo", "Descrição", "Valor", "Paga", "Codigo"};
	public static final int  COLUNA_DATA 		   			 = 0,
							 COLUNA_DIA_PAGAMENTO  			 = 1,
							 COLUNA_TIPO_PAGAMENTO 			 = 2,
							 COLUNA_DESCRICAO      			 = 3,
							 COLUNA_VALOR 		   			 = 4,
							 COLUNA_PAGA 		   			 = 5,
							 COLUNA_INVISIVEL_CODIGO_DESPESA = 6;
	
	private final int QUANTIDADE_COLUNAS_INICIAL = 1;
	
	
	private JTable tabelaDespesas;
	private DefaultTableModel modeloTabelaDespesa;
	private IgBolsoInteligente interfaceGrafica;
	DateTableEditor datePicker;
	
	public TabelaDespesas(IgBolsoInteligente interfaceGrafica) {
		modeloTabelaDespesa = new DefaultTableModel(NOME_COLUNAS,QUANTIDADE_COLUNAS_INICIAL){
			
			private final Class<?>[] CLASSES_COLUNAS = {LocalDate.class, Integer.class, String.class, String.class, Float.class, Boolean.class, Object.class};
			
			@Override
			public Class<?> getColumnClass(int coluna) {
				return CLASSES_COLUNAS[coluna];
			}

		};
		
		this.interfaceGrafica = interfaceGrafica;
		modeloTabelaDespesa.addTableModelListener(this);
		tabelaDespesas = new JTable(modeloTabelaDespesa);
		definirRenderizadoresColunas();
		definirEditoresColunas();
		definirLarguraColunas();
		definirAlturaLinhas();
		exibirLinhasVerticaisHorizontais();
		definirFonte();
		gerarAutoRowSorter();
		definirCorFundo();
		definirTabelaAjustaTamanhoViewport();
	}
	
	private void definirEditoresColunas() {
		datePicker = new DateTableEditor(false, true, true);
		datePicker.getDatePicker().setDate(LocalDate.of(2023, this.interfaceGrafica.obterNumeroMesSelecionado(), 1));
		datePicker.getDatePickerSettings().setFormatForDatesCommonEra("dd/MM/uuuu");
		datePicker.getDatePickerSettings().setAllowEmptyDates(false);
		datePicker.clickCountToEdit = 2;
		tabelaDespesas.getColumnModel().getColumn(COLUNA_DATA).setCellEditor(datePicker);
		tabelaDespesas.getColumnModel().getColumn(COLUNA_DESCRICAO).setCellEditor(new EditorCelula(new VerificadorTextoVazio()));
		tabelaDespesas.getColumnModel().getColumn(COLUNA_TIPO_PAGAMENTO).setCellEditor(new EditorCelula(new VerificadorTextoVazio()));
		tabelaDespesas.getColumnModel().getColumn(COLUNA_VALOR).setCellEditor(new EditorCelula(new VerificadorFloatValido()));
		tabelaDespesas.getColumnModel().getColumn(COLUNA_DIA_PAGAMENTO).setCellEditor(new DefaultCellEditor(new JComboBox<Integer>()));
	}

	private void definirAlturaLinhas() {
		tabelaDespesas.setRowHeight(30);
	}
	
	
	private void definirRenderizadoresColunas() {
		tabelaDespesas.setDefaultRenderer(LocalDate.class, new RenderizadorData());
		tabelaDespesas.setDefaultRenderer(Integer.class, new RenderizadorInteger());
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
		atualizarMesDefaultDatePicker();
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
				despesa.getSituacao(),
				despesa.getCodigo()
			};
			inserirLinhaTabela(valores, linha);
			linha++;
		}
	}
	
	private void atualizarMesDefaultDatePicker() {
		datePicker.getDatePicker().setDate(LocalDate.of(2023, this.interfaceGrafica.obterNumeroMesSelecionado(), 1));
	}

	@Override
	public void tableChanged(TableModelEvent eventoAlteracaoTabela) {
		if(eventoAlteracaoTabela.getType() == TableModelEvent.UPDATE) {
			int linhaAlteracao = eventoAlteracaoTabela.getFirstRow();
			if(modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_INVISIVEL_CODIGO_DESPESA) != null) {
				// alteração em linha que já tem despesa cadastrada
				atualizarDespesa(linhaAlteracao);
				interfaceGrafica.atualizarDadosDeAcordoCategoriaMes();
			}else {
				// alteração em uma linha "vazia" (não tem despesa cadastrada correspondente)
				
				// tentando inserir despesa com categoria "todas" selecionada
				if(this.interfaceGrafica.obterCategoriaSelecionada().equalsIgnoreCase("todas")) {
					modeloTabelaDespesa.removeRow(linhaAlteracao);
					modeloTabelaDespesa.addRow(new Object[] {});
					InputOutput.showError("Impossível inserir despesa dentro da categoria 'todas'.\nFavor selecionar uma categoria.", BolsoInteligente.NOME_PROGRAMA);
				}
				// verificando se todos os campos da linha foram preenchidos para poder realizar inserção no banco.
				else if(verificarLinhaPreenchida(linhaAlteracao)) {
					inserirNovaDespesaBanco(linhaAlteracao);
				}
			}
	        
		}
		else if(eventoAlteracaoTabela.getType() == TableModelEvent.INSERT) {
			atualizarNumeroDiasMes(eventoAlteracaoTabela.getFirstRow());
		}
	}

	
	private void inserirNovaDespesaBanco(int linhaAlteracao) {
		Despesa despesaNova = new Despesa();
		
		despesaNova.setCategoria(this.interfaceGrafica.obterCategoriaSelecionada());
		despesaNova.setData((LocalDate)modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_DATA));
		despesaNova.setDiaPagamento(MonthDay.of(this.interfaceGrafica.obterNumeroMesSelecionado(), (int)modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_DIA_PAGAMENTO)));
		despesaNova.setDescricao((String)modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_DESCRICAO));
		despesaNova.setFormaPagamento((String)modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_TIPO_PAGAMENTO));
		
		Object situacao = modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_PAGA);
		if(situacao == null) {
			despesaNova.setSituacao(false);
		}else {
			despesaNova.setSituacao((Boolean)situacao);
		}
		
		Object valor = modeloTabelaDespesa.getValueAt(linhaAlteracao, COLUNA_VALOR);
		if(valor instanceof String) {
			despesaNova.setValor(Float.parseFloat((String)valor));
		}
		else if(valor instanceof Float) {
			despesaNova.setValor((Float)valor);
		}
		try {
			Dao.inserirDadosBancoDeDados(new DaoDespesa(BolsoInteligente.conexaoBancodeDados.getConexaoBanco()), despesaNova);
		} catch (SQLException sqlException) {
			InputOutput.showError(String.format("\nErro ao inserir linha %d no banco de dados!", linhaAlteracao), BolsoInteligente.NOME_PROGRAMA);
		}
		this.interfaceGrafica.atualizarDadosDeAcordoCategoriaMes();
	}

	private boolean verificarLinhaPreenchida(int linha) {
		if(modeloTabelaDespesa.getValueAt(linha, COLUNA_DATA) == null) {
			return false;
		}
		else if(modeloTabelaDespesa.getValueAt(linha, COLUNA_DIA_PAGAMENTO) == null) {
			return false;
		}
		else if(modeloTabelaDespesa.getValueAt(linha, COLUNA_TIPO_PAGAMENTO) == null) {
			return false;
		}
		else if(modeloTabelaDespesa.getValueAt(linha, COLUNA_DESCRICAO) == null) {
			return false;
		}
		else if(modeloTabelaDespesa.getValueAt(linha, COLUNA_VALOR) == null) {
			return false;
		}
		else {
			return true;
		}
	}

	public void atualizarNumeroDiasMes(int linhaDadosInseridos) {
		int numeroMes = this.interfaceGrafica.obterNumeroMesSelecionado();
		Integer diaSelecionado = (Integer) modeloTabelaDespesa.getValueAt(linhaDadosInseridos, COLUNA_DIA_PAGAMENTO);
		int daysInMonth = YearMonth.of(2023, numeroMes).lengthOfMonth();
		@SuppressWarnings("unchecked")
		JComboBox<Integer> comboBox = (JComboBox<Integer>)((DefaultCellEditor) tabelaDespesas.getCellEditor(linhaDadosInseridos, COLUNA_DIA_PAGAMENTO)).getComponent();
		comboBox.removeAllItems();
		comboBox.addItem(null);
	    for (int i = 1; i <= daysInMonth; i++) {
	        comboBox.addItem(i);
	    }
	    comboBox.setRenderer((ListCellRenderer<? super Integer>) new DefaultListCellRenderer() {
	         @Override
	         public Component getListCellRendererComponent(JList<?> list, Object valor, int index, boolean isSelected, boolean cellHasFocus) {
	             if (valor == null) {
	                 return new JLabel(); // Retorna um JLabel vazio para ocultar o valor nulo
	             }
	             return super.getListCellRendererComponent(list, valor, index, isSelected, cellHasFocus);
	         }
	    });
	    comboBox.setSelectedItem(diaSelecionado);
	}
	
	private void atualizarDespesa(int linhaAlteracao) {
		Despesa despesaAlterar = obterDespesaAtravesDadosTabela(linhaAlteracao);
		try {
			BolsoInteligente.alterarDespesa(despesaAlterar);
		} catch (SQLException e) {
			InputOutput.showError("\nErro ao inserir despesa atualizada no banco.", BolsoInteligente.NOME_PROGRAMA);
		}
		
	}

	private Despesa obterDespesaAtravesDadosTabela(int linhaContendoDados) {
		LocalDate dataDespesa = (LocalDate)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_DATA);
		MonthDay diaMesPagamento = MonthDay.of(this.interfaceGrafica.obterNumeroMesSelecionado(), (int)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_DIA_PAGAMENTO));
		String formaPagamento = (String)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_TIPO_PAGAMENTO);
		String descricaoDespesa = (String)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_DESCRICAO);
		String categoriaDespesa = this.interfaceGrafica.obterCategoriaSelecionada();
		
		Object valor = modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_VALOR);
		Float valorDespesa = 0f;
		if(valor instanceof Float) {
			valorDespesa = (Float)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_VALOR);
		}
		else if(valor instanceof String) {
			valorDespesa = Float.parseFloat((String)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_VALOR));
		}
		 
		boolean situacao = (boolean)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_PAGA);
		long codigoDespesa = (long)modeloTabelaDespesa.getValueAt(linhaContendoDados, COLUNA_INVISIVEL_CODIGO_DESPESA);
		
		return new Despesa(descricaoDespesa, categoriaDespesa, formaPagamento, diaMesPagamento, dataDespesa, valorDespesa, situacao, codigoDespesa);
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
			
			
			if(i == COLUNA_DATA) {
				coluna.setPreferredWidth(150);
			}
			else if(i == COLUNA_DIA_PAGAMENTO) {
				coluna.setPreferredWidth(50);
			}
			else if(i == COLUNA_DESCRICAO) {
				coluna.setPreferredWidth(200);
			}
			else if(i == COLUNA_VALOR) {
				coluna.setPreferredWidth(100);
			}
			else if(i == COLUNA_INVISIVEL_CODIGO_DESPESA) {
				tabelaDespesas.getColumnModel().removeColumn(coluna);
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

	public void inserirLinhaTabela(Object[] dadosInserir, int linha) {
		modeloTabelaDespesa.insertRow(linha, dadosInserir);
	}

	
}
