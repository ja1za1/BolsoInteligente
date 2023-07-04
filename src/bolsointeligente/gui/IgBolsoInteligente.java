package bolsointeligente.gui;



import javax.swing.JFrame;



import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.Meses;
import bolsointeligente.entities.graphics.GraficoBarras;
import bolsointeligente.entities.graphics.GraficoPizza3D;
import bolsointeligente.entities.tables.TabelaDespesas;
import mos.es.InputOutput;

import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;



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
				   lblValorTotalPagar,
				   lblNaoHaDadosGrafico;
				   
	private JComboBox<String> cmbBoxCategoriaOrcamento,
						      cmbBoxMesOrcamento;

	
	public IgBolsoInteligente() {
		definirPropriedadesJanelaPrincipal();
		gerarPainelCabecalho();
		gerarPainelOrcamento();
		gerarPainelRodape();
		balancoMensal();
		definirListeners();
		atualizarTabelaDespesa(obterCategoriaSelecionada(), obterNumeroMesSelecionado());
		exibirJanela();
	}
	
	public String obterCategoriaSelecionada() {
		return (String)cmbBoxCategoriaOrcamento.getSelectedItem();
	}
	
	private String obterNomeMesSelecionado() {
		return (String)cmbBoxMesOrcamento.getSelectedItem();
	}
	
	public int obterNumeroMesSelecionado() {
		return Meses.obterNumeroMes(obterNomeMesSelecionado());
	}
	
	private void balancoMensal() {

		float valorTotalPago,
			  valorTotalPagar,
		      valorDespesas,
		      valorReceitas,
		      valorSaldo,
		      valorInvestimentos;
		
		valorReceitas = valorSaldo = valorTotalPago = valorTotalPagar = valorDespesas = 0f;
		List<Float> valoresReceitas;
		List<Object> valoresSituacaoDespesas;
		try {
			valoresReceitas = BolsoInteligente.obterValoresReceitas();
		} catch (SQLException sqlException) {
			valoresReceitas = new ArrayList<>();
		}
		
		try {
			valoresSituacaoDespesas = BolsoInteligente.obterValoresDespesas();
		} catch (SQLException sqlException) {
			valoresSituacaoDespesas = new ArrayList<>();
		}
		
		
		for(Float valorReceita : valoresReceitas) {
			valorReceitas += valorReceita;
		}
		
		for(int i = 0; i < valoresSituacaoDespesas.size(); i++) {
			Object valorOuSituacao = valoresSituacaoDespesas.get(i); 
			if( valorOuSituacao instanceof Float) {
				valorDespesas += (Float)valorOuSituacao;
			}else {
				if((Boolean)valorOuSituacao) {
					valorTotalPago += (Float)valoresSituacaoDespesas.get(i-1);
				}else {
					valorTotalPagar += (Float)valoresSituacaoDespesas.get(i-1);
				}
			}
		}
		
		valorSaldo = valorReceitas - valorDespesas;
		try {
			valorInvestimentos = BolsoInteligente.obterValorTotalAcumulado();
		} catch (SQLException e) {
			valorInvestimentos = 0;
		}
		
		lblValorInvestimentos.setText(String.format("R$ %,.2f", valorInvestimentos));
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
		btnPesquisarDespesa.addActionListener((e) -> new IgPesquisarDespesa(frmBolsoInteligente,this.frmBolsoInteligente, this.tabelaDespesa));
		btnInvestimentos.addActionListener((e) -> new IgInvestimentos());
		btnImportar.addActionListener((e) -> operacoesImportacao());
		btnGraficoBarra.addActionListener((e) -> exibirGrafico(graficoBarras.getGraficoBarras()));
		btnGraficoPizza.addActionListener((e) -> exibirGrafico(graficoPizza3D.getGraficoPizza3D()));
		cmbBoxCategoriaOrcamento.addActionListener((e) -> atualizarDadosDeAcordoCategoriaMes());
		cmbBoxMesOrcamento.addActionListener((e) -> atualizarDadosDeAcordoCategoriaMes());
	}

	
	public void atualizarDadosDeAcordoCategoriaMes() {
		exibirDadosTabelaDeAcordoCategoriaMes();
		inserirDadosGraficoBarra((DefaultCategoryDataset)graficoBarras.getPlotGraficoBarras().getDataset());
		inserirDadosGraficoPizza3D((DefaultPieDataset)graficoPizza3D.getPlotGraficoPizza3D().getDataset());
		balancoMensal();
	}

	private void exibirDadosTabelaDeAcordoCategoriaMes() {
		tabelaDespesa.atualizarDadosTabela(obterCategoriaSelecionada(), obterNumeroMesSelecionado());
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
		
		cmbBoxMesOrcamento = new JComboBox<>();
		cmbBoxMesOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMesOrcamento.setLabelFor(cmbBoxMesOrcamento);
		DefaultComboBoxModel<String> modeloComboBoxMeses = new DefaultComboBoxModel<>();
		for(Meses mes : Meses.values()) {
			modeloComboBoxMeses.addElement(mes.getNomeMes());
		}
		modeloComboBoxMeses.setSelectedItem(Meses.obterNomeMes(LocalDate.now().getMonthValue()));
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
		
		DefaultComboBoxModel<String> modeloComboBoxCategorias = new DefaultComboBoxModel<>();
		modeloComboBoxCategorias.addAll(obterCategoriasCadastradas());
		modeloComboBoxCategorias.setSelectedItem("Todas");
		cmbBoxCategoriaOrcamento.setModel(modeloComboBoxCategorias);
		cmbBoxCategoriaOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		cmbBoxCategoriaOrcamento.setBounds(218, 24, 119, 26);
		pnlOrcamento.add(cmbBoxCategoriaOrcamento);
		
		lblNaoHaDadosGrafico = new JLabel();
		lblNaoHaDadosGrafico.setForeground(new Color(255, 0, 0));
		lblNaoHaDadosGrafico.setHorizontalAlignment(SwingConstants.CENTER);
		lblNaoHaDadosGrafico.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblNaoHaDadosGrafico.setBounds(78, 135, 402, 35);
		
		
		
        tabelaDespesa = new TabelaDespesas(this);
		JScrollPane scrllPaneTabelaOrcamento = new JScrollPane(tabelaDespesa.getTabelaDespesas());
		scrllPaneTabelaOrcamento.setBounds(20, 62, 643, 244);
		pnlOrcamento.add(scrllPaneTabelaOrcamento);
		
		DefaultPieDataset dadosGraficoPizza3D = new DefaultPieDataset();
				
		inserirDadosGraficoPizza3D(dadosGraficoPizza3D);
		
		graficoPizza3D = new GraficoPizza3D(gerarGraficoPizza3D(dadosGraficoPizza3D,null,true,true,false));
		
		DefaultCategoryDataset dadosGraficoBarras = new DefaultCategoryDataset();
				
		inserirDadosGraficoBarra(dadosGraficoBarras);
		
		graficoBarras = new GraficoBarras(gerarGraficoBarra(null, null, null, dadosGraficoBarras, PlotOrientation.VERTICAL, true, true, false));
		
		chrtPnlGrafico = new ChartPanel(graficoBarras.getGraficoBarras());
		
		
		chrtPnlGrafico.setMouseWheelEnabled(true);
		chrtPnlGrafico.setBorder(null);
        chrtPnlGrafico.setBounds(675, 24, 505, 322);
        chrtPnlGrafico.setLayout(null);
		pnlOrcamento.add(chrtPnlGrafico);
		chrtPnlGrafico.add(lblNaoHaDadosGrafico);
		
		
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
	
    private void inserirDadosGraficoPizza3D(DefaultPieDataset dadosGraficoPizza) {
    	dadosGraficoPizza.clear();
    	
    	List<Object> categoriaValoresMensal;
    	try {
			categoriaValoresMensal = BolsoInteligente.obterCategoriasValoresMensal(obterNumeroMesSelecionado());
		} catch (SQLException e) {
			categoriaValoresMensal = new ArrayList<>();
		}
    	List<Float> receitasMensais;
    	try {
			receitasMensais = BolsoInteligente.obterValoresReceitaMensal(obterNumeroMesSelecionado());
		} catch (SQLException sqlException) {
			receitasMensais = new ArrayList<>();
		}
    	
    	float valorTotalGanhoMes = 0f;
    	
    	for(Float receita : receitasMensais) {
    		valorTotalGanhoMes += receita;
    	}
    	
    	if(categoriaValoresMensal.size() == 0) {
    		lblNaoHaDadosGrafico.setText("Não há despesas suficientes para exibir o gráfico");
			lblNaoHaDadosGrafico.setVisible(true);
			return;
    	}
    	
    	for(int i = 0; i < categoriaValoresMensal.size(); i++) {
    		Object categoriaOuValor = categoriaValoresMensal.get(i);
    		if(categoriaOuValor instanceof String) {
    			continue;
    		}
    		else if(categoriaOuValor instanceof Float){
    			Float porcentagemGastaMensalCategoria = null;
    			if(valorTotalGanhoMes == 0) {
    				lblNaoHaDadosGrafico.setText("Não há receitas suficientes para exibir o gráfico");
    				lblNaoHaDadosGrafico.setVisible(true);
    				return;
    			}
    			else {
    				if(lblNaoHaDadosGrafico.isVisible()) {
    					lblNaoHaDadosGrafico.setVisible(false);
    				}
    				porcentagemGastaMensalCategoria = (float)categoriaOuValor/valorTotalGanhoMes * 100;
    			}
    			if(porcentagemGastaMensalCategoria != null) {
    				categoriaValoresMensal.set(i, porcentagemGastaMensalCategoria);
    			}
    			
    		}
    	}
    	
    	for(int i = 0; i < categoriaValoresMensal.size(); i+=2) {
    		dadosGraficoPizza.setValue((String)categoriaValoresMensal.get(i), (Float)categoriaValoresMensal.get(i+1));
    	}
    	
	}
    
    private void inserirDadosGraficoBarra(DefaultCategoryDataset dadosGraficoBarra) {
    	dadosGraficoBarra.clear();
    	List<Object> categoriaValoresMensal;
    	try {
			categoriaValoresMensal = BolsoInteligente.obterCategoriasValoresMensal(obterNumeroMesSelecionado());
		} catch (SQLException e) {
			categoriaValoresMensal = new ArrayList<>();
		}
    	List<Float> receitasMensais;
    	try {
			receitasMensais = BolsoInteligente.obterValoresReceitaMensal(obterNumeroMesSelecionado());
		} catch (SQLException sqlException) {
			receitasMensais = new ArrayList<>();
		}
    	
    	float valorTotalGanhoMes = 0f;
    	
    	for(Float receita : receitasMensais) {
    		valorTotalGanhoMes += receita;
    	}
    	
    	for(int i = 0; i < categoriaValoresMensal.size(); i++) {
    		Object categoriaOuValor = categoriaValoresMensal.get(i);
    		if(categoriaOuValor instanceof String) {
    			continue;
    		}
    		else if(categoriaOuValor instanceof Float){
    			Float porcentagemGastaMensalCategoria = null;
    			if(valorTotalGanhoMes == 0) {
    				lblNaoHaDadosGrafico.setVisible(true);
    				return;
    			}
    			else {
    				if(lblNaoHaDadosGrafico.isVisible()) {
    					lblNaoHaDadosGrafico.setVisible(false);
    				}
    				porcentagemGastaMensalCategoria = (float)categoriaOuValor/valorTotalGanhoMes;
    			}
    			if(porcentagemGastaMensalCategoria != null) {
    				categoriaValoresMensal.set(i, porcentagemGastaMensalCategoria);
    			}
    			
    		}
    	}
    	
    	for(int i = 0; i < categoriaValoresMensal.size(); i+=2) {
    		dadosGraficoBarra.setValue((Float)categoriaValoresMensal.get(i+1), (String)categoriaValoresMensal.get(i), "");
    	}
    	
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

	private void atualizarCategoriasExibidas() {
		final List<String> categoriasCadastradas = obterCategoriasCadastradas();
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
	
	private List<String> obterCategoriasCadastradas() {
		List<String> categoriasCadastradas;
		try {
			categoriasCadastradas = BolsoInteligente.obterCategorias();
		} catch (SQLException sqlException) {
			categoriasCadastradas = new ArrayList<>();
		}
		categoriasCadastradas.add(0, "Todas");
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
	
	public void atualizarTabelaDespesa(String categoria, int numeroMes) {
		tabelaDespesa.atualizarDadosTabela(categoria, numeroMes);
	}

	private void operacoesImportacao() {
		importarArquivos();
		atualizarCategoriasExibidas();
		atualizarDadosDeAcordoCategoriaMes();
	}
	
	private void importarArquivos() {
		final String TITULO_IMPORTACAO = "Importação";
		if(flChserImportar == null) {
			criarJanelaImportar();
		}
		int opcao = flChserImportar.showOpenDialog(frmBolsoInteligente);
		
		if(opcao == JFileChooser.APPROVE_OPTION) {
			InputOutput.showInfo(BolsoInteligente.importarArquivos(flChserImportar.getSelectedFiles()), TITULO_IMPORTACAO); ;
		}
	}
}
