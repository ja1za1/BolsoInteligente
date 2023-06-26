package bolsointeligente.gui;



import javax.swing.JFrame;



import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.Despesa;
import bolsointeligente.entities.GraficoBarras;
import bolsointeligente.entities.GraficoPizza3D;
import bolsointeligente.entities.Meses;
import bolsointeligente.entities.TabelaDespesas;
import bolsointeligente.utils.DataHora;
import mos.es.InputOutput;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;



public class IgBolsoInteligente{
	
	private final int LARGURA_JANELA = 1207,
					  ALTURA_JANELA  = 521;
	
	private final String TITULO_JANELA = "Bolso Inteligente";
	
	
	private JFrame frmBolsoInteligente;
	
	private JFileChooser flChserImportar;
	
	private TabelaDespesas tabelaDespesa;
	
	private JPanel pnlCabecalho,
				   pnlOrcamento,
				   pnlRodape;
	
	private GraficoBarras graficoBarras;
	
	private GraficoPizza3D graficoPizza3D;
	
	private ChartPanel chrtPnlGrafico;
	
	private JButton btnPesquisarDespesa,
					btnGraficoBarra,
					btnGraficoPizza,
					btnImportar,
					btnInvestimentos;
	
	private JLabel lblValorReceitas,
				   lblValorDespesas,
				   lblValorSaldo,
				   lblValorTotalPago,
				   lblValorInvestimentos,
				   lblValorTotalPagar;
				   
	private JComboBox<String> cmbBoxCategoriaOrcamento;

	/**
	 * Create the application.
	 */
	
	
	
	public IgBolsoInteligente() {
		definirPropriedadesJanelaPrincipal();
		gerarPainelCabecalho();
		gerarPainelOrcamento();
		gerarPainelRodape();
		definirValoresJLabel();
		definirListeners();
		exibirJanela();
	}
	
	private void definirValoresJLabel() {

		DefaultTableModel modeloTabelaDespesa = tabelaDespesa.getModeloTabelaDespesa();
		int numeroLinhas = modeloTabelaDespesa.getRowCount();
		int numeroColunas = modeloTabelaDespesa.getColumnCount();
		
		float valorTotalPago,
			  valorTotalPagar,
		      valorDespesas,
		      valorReceitas,
		      valorSaldo;
		
		valorReceitas = valorSaldo = valorTotalPago = valorTotalPagar = valorDespesas = 0f;
		List<Float> valoresReceitas;
		try {
			valoresReceitas = BolsoInteligente.obterValoresReceitas();
		} catch (SQLException e) {
			valoresReceitas = new ArrayList<>();
		}
		
		for(Float valorReceita : valoresReceitas) {
			valorReceitas += valorReceita;
		}
		
		for(int linha = 0; linha < numeroLinhas; linha++) {
			for(int coluna = 0; coluna < numeroColunas; coluna++) {
				if(coluna == 4) {
					Object valorDespesa = modeloTabelaDespesa.getValueAt(linha, coluna);
					if(valorDespesa instanceof Float) {
						valorDespesas += (Float)valorDespesa;
					}
					Object situacao = modeloTabelaDespesa.getValueAt(linha, coluna+1);
					if(situacao instanceof Boolean) {
						boolean pago = (Boolean)situacao;
						if(pago) {
							valorTotalPago += (Float)valorDespesa;
						}
						else {
							valorTotalPagar += (Float)valorDespesa;
						}
					}
					
					
				}
			}
		}
		valorSaldo = valorReceitas - valorDespesas;
		
		lblValorDespesas.setText(String.format("R$ %,.2f", valorDespesas));
		lblValorTotalPago.setText(String.format("R$ %,.2f", valorTotalPago));
		lblValorTotalPagar.setText(String.format("R$ %,.2f", valorTotalPagar));
		lblValorReceitas.setText(String.format("R$ %,.2f", valorReceitas));
		lblValorSaldo.setText(String.format("R$ %,.2f", valorSaldo));
		
		float valorAviso = valorReceitas * 0.2f;
		if(valorSaldo < 0) {
			lblValorSaldo.setForeground(Color.RED);
		}
		else if(valorSaldo <= valorAviso && valorSaldo != 0) {
			lblValorSaldo.setForeground(Color.YELLOW.darker());
		}
		else if(valorSaldo != 0){
			lblValorSaldo.setForeground(Color.GREEN.darker());
		}
		else {
			lblValorSaldo.setForeground(Color.BLACK);
		}
		
		
	}

	private void exibirJanela() {
		frmBolsoInteligente.setVisible(true);
	}

	
	private void definirListeners() {
		btnPesquisarDespesa.addActionListener((e) -> new IgPesquisarDespesa(frmBolsoInteligente,btnPesquisarDespesa));
		btnImportar.addActionListener((e) -> importarArquivos());
		btnGraficoBarra.addActionListener((e) -> exibirGrafico(graficoBarras.getGraficoBarras()));
		btnGraficoPizza.addActionListener((e) -> exibirGrafico(graficoPizza3D.getGraficoPizza3D()));
	}

	
	private void gerarPainelCabecalho() {
		pnlCabecalho = new JPanel();
		pnlCabecalho.setBackground(new Color(255, 255, 255));
		pnlCabecalho.setBounds(6, 6, 1185, 72);
		pnlCabecalho.setLayout(new MigLayout("", "[]10[]40[]40[]100[]40[]90[]", "[][]"));
		
		JLabel lblReceitas = new JLabel("Receitas");
		lblReceitas.setForeground(new Color(0, 0, 160));
		lblReceitas.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblReceitas, "cell 1 0");
		
		JLabel lblDespesas = new JLabel("Despesas");
		lblDespesas.setForeground(new Color(0, 0, 160));
		lblDespesas.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblDespesas, "cell 2 0");
		
		JLabel lblSaldo = new JLabel("Saldo");
		lblSaldo.setForeground(new Color(0, 0, 160));
		lblSaldo.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblSaldo, "cell 3 0");
		
		JLabel lblTotalPago = new JLabel("Total Pago");
		lblTotalPago.setForeground(new Color(0, 0, 160));
		lblTotalPago.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblTotalPago, "cell 4 0");
		
		JLabel lblTotalAPagar = new JLabel("Total A Pagar");
		lblTotalAPagar.setForeground(new Color(0, 0, 160));
		lblTotalAPagar.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblTotalAPagar, "cell 5 0");
		
		JLabel lblInvestimentos = new JLabel("Investimentos");
		lblInvestimentos.setForeground(new Color(0, 0, 160));
		lblInvestimentos.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblInvestimentos, "cell 6 0");
		
		lblValorReceitas = new JLabel();
		lblValorReceitas.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblValorReceitas.setText("R$ 4.000,00");
		pnlCabecalho.add(lblValorReceitas, "cell 1 1");
		
		lblValorDespesas = new JLabel("R$ 0,00");
		lblValorDespesas.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorDespesas, "cell 2 1");
		
		lblValorSaldo = new JLabel("R$ 0,00");
		lblValorSaldo.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorSaldo, "cell 3 1");
		
		lblValorTotalPago = new JLabel("R$ 0,00");
		lblValorTotalPago.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorTotalPago, "cell 4 1");
		
		lblValorTotalPagar = new JLabel("R$ 0,00");
		lblValorTotalPagar.setFont(new Font("Arial Black", Font.BOLD, 18));
		pnlCabecalho.add(lblValorTotalPagar, "cell 5 1");
		
		lblValorInvestimentos = new JLabel("R$ 15.847,36");
		lblValorInvestimentos.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorInvestimentos, "cell 6 1,alignx left");
		
		frmBolsoInteligente.getContentPane().add(pnlCabecalho);
	}
	
	private void gerarPainelOrcamento() {
		pnlOrcamento = new JPanel();
		pnlOrcamento.setBackground(new Color(255, 255, 255));
		pnlOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlOrcamento.setBounds(6, 84, 1186, 363);
		pnlOrcamento.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Orçamento", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		pnlOrcamento.setLayout(null);
		frmBolsoInteligente.getContentPane().add(pnlOrcamento);
		
		btnPesquisarDespesa = new JButton("Pesquisar Despesa...");
		btnPesquisarDespesa.setMnemonic(KeyEvent.VK_P);
		btnPesquisarDespesa.setFont(new Font("Arial", Font.PLAIN, 12));
		
		btnPesquisarDespesa.setBounds(20, 318, 148, 28);
		pnlOrcamento.add(btnPesquisarDespesa);
		
		btnGraficoBarra = new JButton("Gráfico em Barras");
		btnGraficoBarra.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarra.setFont(new Font("Arial", Font.PLAIN, 12));
		btnGraficoBarra.setBounds(180, 318, 128, 28);
		pnlOrcamento.add(btnGraficoBarra);
		
		btnGraficoPizza = new JButton("Gráfico em Pizza");
		btnGraficoPizza.setMnemonic(KeyEvent.VK_G);
		btnGraficoPizza.setFont(new Font("Arial", Font.PLAIN, 12));
		btnGraficoPizza.setBounds(320, 318, 119, 28);
		pnlOrcamento.add(btnGraficoPizza);
		
		JLabel lblMesOrcamento = new JLabel("Mês:");
		lblMesOrcamento.setDisplayedMnemonic(KeyEvent.VK_M);
		lblMesOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMesOrcamento.setBounds(20, 29, 31, 16);
		pnlOrcamento.add(lblMesOrcamento);
		
		JComboBox<String> cmbBoxMesOrcamento = new JComboBox<>();
		cmbBoxMesOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMesOrcamento.setLabelFor(cmbBoxMesOrcamento);
		DefaultComboBoxModel<String> modeloComboBoxMeses = new DefaultComboBoxModel<>();
		for(Meses mes : Meses.values()) {
			modeloComboBoxMeses.addElement(mes.getMes());
		}
		cmbBoxMesOrcamento.setModel(modeloComboBoxMeses);
		cmbBoxMesOrcamento.setBounds(52, 24, 90, 26);
		pnlOrcamento.add(cmbBoxMesOrcamento);
		
		JLabel lblCategoriaOrcamento = new JLabel("Categoria:");
		lblCategoriaOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCategoriaOrcamento.setDisplayedMnemonic(KeyEvent.VK_C);
		lblCategoriaOrcamento.setBounds(154, 29, 57, 16);
		pnlOrcamento.add(lblCategoriaOrcamento);
		
		cmbBoxCategoriaOrcamento = new JComboBox<>();
		lblCategoriaOrcamento.setLabelFor(cmbBoxCategoriaOrcamento);
		
		DefaultComboBoxModel<String> modeloComboBoxCategorias = new DefaultComboBoxModel<>(obterCategoriasCadastradas());
		cmbBoxCategoriaOrcamento.setModel(modeloComboBoxCategorias);
		cmbBoxCategoriaOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		cmbBoxCategoriaOrcamento.setBounds(218, 24, 119, 26);
		pnlOrcamento.add(cmbBoxCategoriaOrcamento);
		
		
        tabelaDespesa = new TabelaDespesas();
		JScrollPane scrllPaneTabelaOrcamento = new JScrollPane(tabelaDespesa.getTabelaDespesas());
		scrllPaneTabelaOrcamento.setBounds(20, 62, 643, 244);
		pnlOrcamento.add(scrllPaneTabelaOrcamento);
		
		DefaultPieDataset dadosGraficoPizza3D = gerarDadosGraficoPizza3D();
		
		graficoPizza3D = new GraficoPizza3D(gerarGraficoPizza3D(dadosGraficoPizza3D,null,true,true,false));
		
		
		DefaultCategoryDataset dadosGraficoBarras = gerarDadosGraficoBarra();
		
		graficoBarras = new GraficoBarras(gerarGraficoBarra(null, null, null, dadosGraficoBarras, PlotOrientation.VERTICAL, true, true, false));
		
        
        
		chrtPnlGrafico = new ChartPanel(graficoBarras.getGraficoBarras());
		chrtPnlGrafico.setMouseWheelEnabled(true);
		chrtPnlGrafico.setBorder(null);
        chrtPnlGrafico.setBounds(675, 29, 505, 298);
        chrtPnlGrafico.setLayout(null);
		pnlOrcamento.add(chrtPnlGrafico);
	}
	
	private void gerarPainelRodape() {
		pnlRodape = new JPanel();
		pnlRodape.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) pnlRodape.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		pnlRodape.setBounds(16, 442, 1177, 34);
		frmBolsoInteligente.getContentPane().add(pnlRodape);
		
		
		btnImportar = new JButton("Importar...");
		pnlRodape.add(btnImportar);
		btnImportar.setMnemonic(KeyEvent.VK_O);
		btnImportar.setFont(new Font("Arial", Font.PLAIN, 12));
		
		btnInvestimentos = new JButton("Investimentos...");
		pnlRodape.add(btnInvestimentos);
		btnInvestimentos.setMnemonic(KeyEvent.VK_I);
		btnInvestimentos.setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
    private DefaultPieDataset gerarDadosGraficoPizza3D() {
		// // Criar um conjunto de dados para o gráfico de pizza
        DefaultPieDataset dadosGrafico = new DefaultPieDataset();
//        dadosGrafico.setValue("Maçã", 20.7);
//        dadosGrafico.setValue("Laranja", 30.0);
//        dadosGrafico.setValue("Banana", 50.0);
//        dadosGrafico.setValue("Pera", 30.0);
		return dadosGrafico;
	}
    
    private DefaultCategoryDataset gerarDadosGraficoBarra() {
    	DefaultCategoryDataset dadosGraficoBarra = new DefaultCategoryDataset();
//        dadosGraficoBarra.addValue(0.75, "Produto A", "Janeiro");
//        dadosGraficoBarra.addValue(0.12, "Produto B", "Janeiro");
//        dadosGraficoBarra.addValue(0.3, "Produto C", "Janeiro");
//        dadosGraficoBarra.addValue(0.8, "Produto A", "Fevereiro");
//        dadosGraficoBarra.addValue(0.10, "Produto B", "Fevereiro");
//        dadosGraficoBarra.addValue(0.11, "Produto C", "Fevereiro");
//        dadosGraficoBarra.addValue(0.13, "Produto A", "Março");
//        dadosGraficoBarra.addValue(0.7, "Produto B", "Março");
//        dadosGraficoBarra.addValue(0.14, "Produto C", "Março");
        return dadosGraficoBarra;
    }
	
	private JFreeChart gerarGraficoPizza3D(DefaultPieDataset dadosGrafico, String tituloGrafico, boolean exibirLegenda, boolean exibirToolTips, boolean exibirUrls ) {
		return ChartFactory.createPieChart3D(
                tituloGrafico,  // título do gráfico
                dadosGrafico,   // conjunto de dados
                exibirLegenda,  // exibir legenda
                exibirToolTips, // exibir dicas de ferramentas
                exibirUrls      // não exibir URLs
        );
	}
	
	private JFreeChart gerarGraficoBarra(String tituloGrafico, String tituloCategorias, String tituloValores, DefaultCategoryDataset dadosGrafico, PlotOrientation orientacaoGrafico, boolean exibirLegendas, boolean exibirToolTips, boolean exibirUrls) {
        JFreeChart chart = ChartFactory.createBarChart(
                tituloGrafico,
                tituloCategorias,
                tituloValores,
                dadosGrafico,
                orientacaoGrafico,
                exibirLegendas,
                exibirToolTips,
                exibirUrls
        );
        return chart;
	}
	
	private void exibirGrafico(JFreeChart graficoExibir) {
		chrtPnlGrafico.setChart(graficoExibir);
	}

	private void definirPropriedadesJanelaPrincipal() {
		frmBolsoInteligente = new JFrame(TITULO_JANELA);
		frmBolsoInteligente.getContentPane().setBackground(new Color(255, 255, 255));
		frmBolsoInteligente.setPreferredSize(new Dimension(LARGURA_JANELA, ALTURA_JANELA));
		frmBolsoInteligente.getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		frmBolsoInteligente.setResizable(false);
		frmBolsoInteligente.setBounds(100, 100, LARGURA_JANELA, ALTURA_JANELA);
		frmBolsoInteligente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBolsoInteligente.getContentPane().setLayout(null);
	}

	private void verificarCategoriaNova() {
		final String[] categoriasCadastradas = obterCategoriasCadastradas();
		List<String> categoriasSendoExibidas = obterCategoriasSendoExibidas();
		
		
		for(String categoria : categoriasCadastradas) {
			if(!categoriasSendoExibidas.stream().anyMatch(x -> x.equalsIgnoreCase(categoria))) {
				adicionarNovaCategoria(categoria);
			}
		}
	}
	
	private List<String> obterCategoriasSendoExibidas() {
		final int numeroCategorias = cmbBoxCategoriaOrcamento.getItemCount();
		List<String> categoriasSendoExibidas = new ArrayList<>(numeroCategorias);
		
		for(int i = 0; i < numeroCategorias; i++) {
			categoriasSendoExibidas.add(cmbBoxCategoriaOrcamento.getItemAt(i));
		}
		return categoriasSendoExibidas;
	}
	
	private void adicionarNovaCategoria(String categoria) {
		DefaultComboBoxModel<String> modeloComboBoxCategoria = (DefaultComboBoxModel<String>)cmbBoxCategoriaOrcamento.getModel();
		modeloComboBoxCategoria.addElement(categoria);
	}


	private void criarJanelaImportar() {
		final String DIRETORIO_ATUAL = ".",
				     ARQUIVOS_CSV    = "CSV (separado por virgulas) (*.csv)",
				     EXTENSAO_CSV    = "csv",
				     TITULO          = "Importar receitas, despesas, investimentos";
		alterarLinguaJFileChooser();
		flChserImportar = new JFileChooser(new File(DIRETORIO_ATUAL));
		
		flChserImportar.setDialogTitle(TITULO);
		flChserImportar.setFileSelectionMode(JFileChooser.FILES_ONLY);
		flChserImportar.setMultiSelectionEnabled(true);
		flChserImportar.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter csvExtensionFilter = new FileNameExtensionFilter(ARQUIVOS_CSV, EXTENSAO_CSV);
		flChserImportar.setFileFilter(csvExtensionFilter);
		
	}
	
	private String[] obterCategoriasCadastradas() {
		String[] categoriasCadastradas = new String[] {};
		try {
			categoriasCadastradas = BolsoInteligente.obterCategorias();
		} catch (SQLException sqlException) {
			InputOutput.showError("Erro ao obter categorias cadastradas do banco.", TITULO_JANELA);
		}
		return categoriasCadastradas;
	}
	
	private void alterarLinguaJFileChooser() {
		UIManager.put("FileChooser.openButtonText","Abrir");
		UIManager.put("FileChooser.cancelButtonText","Cancelar");
		UIManager.put("FileChooser.cancelButtonToolTipText",
		"Fecha a janela de escolha de arquivos");
		UIManager.put("FileChooser.openButtonToolTipText", "Abrir arquivo(s) selecionado(s)");

		UIManager.put("FileChooser.lookInLabelText", "Procurar em:");
		UIManager.put("FileChooser.fileNameLabelText", "Nome:");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo arquivo:");
		UIManager.put("FileChooser.upFolderToolTipText", "Subir um nível");
		UIManager.put("FileChooser.homeFolderToolTipText", "Diretório Home");
		UIManager.put("FileChooser.newFolderToolTipText", "Nova Pasta");
		UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
		UIManager.put("FileChooser.viewMenuLabelText", "Visualizar em");
		UIManager.put("FileChooser.refreshActionLabelText", "Atualizar");
		UIManager.put("FileChooser.newFolderActionLabelText", "Nova Pasta");
		UIManager.put("FileChooser.listViewActionLabelText", "Lista");
		UIManager.put("FileChooser.detailsViewActionLabelText", "Detalhes");
		
	}
	
	public void atualizarTabelaDespesa(List<Despesa>despesas) {
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
		}
		tabelaDespesa.atualizarTabela(valores, linha);
		
		linha++;
	}

	private void importarArquivos() {
		final String TITULO_IMPORTACAO = "Importação";
		if(flChserImportar == null) {
			criarJanelaImportar();
		}
		int opcao = flChserImportar.showOpenDialog(frmBolsoInteligente);
		
		if(opcao == JFileChooser.APPROVE_OPTION) {
			InputOutput.showInfo(BolsoInteligente.importarArquivos(flChserImportar.getSelectedFiles()), TITULO_IMPORTACAO); ;
			definirValoresJLabel();
			verificarCategoriaNova();
		}
	}
}
