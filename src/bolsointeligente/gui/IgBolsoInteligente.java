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
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.GraficoPizza3D;
import mos.es.InputOutput;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

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



public class IgBolsoInteligente extends JFrame{
	
	private final int LARGURA_JANELA = 1207,
					  ALTURA_JANELA  = 521;
	private final String TITULO_JANELA = "Bolso Inteligente";

	private JFrame frmBolsoInteligente;
	private JFileChooser flChserImportar;

	/**
	 * Create the application.
	 */
	public IgBolsoInteligente() {
		frmBolsoInteligente = new JFrame(TITULO_JANELA);
		frmBolsoInteligente.getContentPane().setBackground(new Color(255, 255, 255));
		frmBolsoInteligente.setPreferredSize(new Dimension(LARGURA_JANELA, ALTURA_JANELA));
		frmBolsoInteligente.getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		frmBolsoInteligente.setResizable(false);
		frmBolsoInteligente.setBounds(100, 100, LARGURA_JANELA, ALTURA_JANELA);
		frmBolsoInteligente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBolsoInteligente.getContentPane().setLayout(null);
		
		JPanel pnlOrcamento = new JPanel();
		pnlOrcamento.setBackground(new Color(255, 255, 255));
		pnlOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlOrcamento.setBounds(6, 84, 1186, 363);
		pnlOrcamento.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Or\u00E7amento", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		pnlOrcamento.setLayout(null);
		frmBolsoInteligente.getContentPane().add(pnlOrcamento);
		
		JButton btnPesquisarDespesa = new JButton("Pesquisar Despesa...");
		btnPesquisarDespesa.setMnemonic(KeyEvent.VK_P);
		btnPesquisarDespesa.setFont(new Font("Arial", Font.PLAIN, 12));
		
		btnPesquisarDespesa.setBounds(20, 318, 148, 28);
		pnlOrcamento.add(btnPesquisarDespesa);
		
		JButton btnGraficoBarra = new JButton("Gráfico em Barras");
		btnGraficoBarra.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarra.setFont(new Font("Arial", Font.PLAIN, 12));
		btnGraficoBarra.setBounds(180, 318, 128, 28);
		pnlOrcamento.add(btnGraficoBarra);
		
		JButton btnGraficoPizza = new JButton("Gráfico em Pizza");
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
		cmbBoxMesOrcamento.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		cmbBoxMesOrcamento.setBounds(52, 24, 90, 26);
		pnlOrcamento.add(cmbBoxMesOrcamento);
		
		JLabel lblCategoriaOrcamento = new JLabel("Categoria:");
		lblCategoriaOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCategoriaOrcamento.setDisplayedMnemonic(KeyEvent.VK_C);
		lblCategoriaOrcamento.setBounds(154, 29, 57, 16);
		pnlOrcamento.add(lblCategoriaOrcamento);
		
		JComboBox<String> cmbBoxCategoriaOrcamento = new JComboBox<>();
		lblCategoriaOrcamento.setLabelFor(cmbBoxCategoriaOrcamento);
		cmbBoxCategoriaOrcamento.setModel(new DefaultComboBoxModel<String>(new String[] {"Alimentação", "Educação", "Energia Elétrica", "Esporte", "Lazer", "Habitação", "Plano de Saúde"}));
		cmbBoxCategoriaOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		cmbBoxCategoriaOrcamento.setBounds(218, 24, 119, 26);
		pnlOrcamento.add(cmbBoxCategoriaOrcamento);
		
		
		// Criar uma tabela de exemplo
        JTable tblOrcamento = new JTable();
        tblOrcamento.setFillsViewportHeight(true);
        tblOrcamento.setBackground(Color.WHITE);
        tblOrcamento.setModel(new DefaultTableModel(
        	new Object[][] {
        		{"23/03/2023", new Integer(2), "Dinheiro", "Feira", new Double(52.5), null},
        		{"01/01/2023", new Integer(10), "CC", "Supermercado", new Double(93.23), null},
        		{"15/02/2023", new Integer(3), "CD", "Padaria", new Double(104.98), Boolean.FALSE},
        		{"10/04/2023", new Integer(20), "CD", "Padaria", new Double(223.57), Boolean.TRUE},
        	},
        	new String[] {
        		"Data", "Dia", "Tipo", "Descri\u00E7\u00E3o", "Valor", "Paga"
        	}
        ) {
        	Class[] columnTypes = new Class[] {
        		String.class, Integer.class, String.class, String.class, Object.class, Boolean.class
        	};
        	public Class getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        });
        
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = tblOrcamento.getColumnModel().getColumn(i);
            if (i == 3) {
                column.setPreferredWidth(200);
            }
            else if(i == 1) {
            	column.setPreferredWidth(10);
            }
            else {
                column.setPreferredWidth(50);
            }
        }
        
//     // Alinha as colunas à esquerda, exceto a coluna 'Paga' que é centralizada
//        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
//        leftRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
//
//        for (int i = 0; i < tblOrcamento.getColumnCount(); i++) {
//            TableCellRenderer renderer;
//            if (i == 5) {
//                renderer = centerRenderer; // Coluna 'Paga'
//            } else {
//                renderer = leftRenderer; // Demais colunas
//            }
//            tblOrcamento.getColumnModel().getColumn(i).setCellRenderer(renderer);
//        }
        
        tblOrcamento.setShowVerticalLines(true);
        tblOrcamento.setShowHorizontalLines(true);
//        tblOrcamento.setAutoCreateRowSorter(true);
        tblOrcamento.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JScrollPane scrllPaneTabelaOrcamento = new JScrollPane(tblOrcamento);
		scrllPaneTabelaOrcamento.setBounds(20, 62, 643, 244);
		pnlOrcamento.add(scrllPaneTabelaOrcamento);
		
		// Criar um conjunto de dados para o gráfico de pizza
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Maçã", 20.7);
        dataset.setValue("Laranja", 30.0);
        dataset.setValue("Banana", 50.0);
        dataset.setValue("Pera", 30.0);
        

//         Criar o gráfico de pizza
        JFreeChart chart = ChartFactory.createPieChart3D(
                null, // título do gráfico
                dataset, // conjunto de dados
                true, // exibir legenda
                true, // exibir dicas de ferramentas
                false // não exibir URLs
        );
        
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlineVisible(false);
     // Configurando o valor em cada fatia
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {1}%");
        PieToolTipGenerator toolTipGenerator = new StandardPieToolTipGenerator("{0}: {1}%");
        ((PiePlot3D) chart.getPlot()).setLabelGenerator(labelGenerator);
        ((PiePlot3D) chart.getPlot()).setLabelBackgroundPaint(Color.WHITE);
        ((PiePlot3D) chart.getPlot()).setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);        
        ((PiePlot3D) chart.getPlot()).setForegroundAlpha(0.4f);
        ((PiePlot3D) chart.getPlot()).setToolTipGenerator(toolTipGenerator);
        ((PiePlot3D) chart.getPlot()).setDarkerSides(true);
        
//        GraficoPizza3D graficoPizza3D = new GraficoPizza3D((PiePlot3D) ChartFactory.createPieChart3D(
//                null, // título do gráfico
//                dataset, // conjunto de dados
//                true, // exibir legenda
//                true, // exibir dicas de ferramentas
//                false // não exibir URLs
//        ).getPlot());
		// Criar um painel de gráfico e adicioná-lo ao JFrame
		ChartPanel chrtPnlGrafico = new ChartPanel(chart);
		chrtPnlGrafico.setMouseWheelEnabled(true);
		chrtPnlGrafico.setBorder(null);
        chrtPnlGrafico.setBounds(675, 29, 505, 298);
		pnlOrcamento.add(chrtPnlGrafico);
		chrtPnlGrafico.setLayout(null);
		
		JPanel pnlCabecalho = new JPanel();
		pnlCabecalho.setBackground(new Color(255, 255, 255));
		pnlCabecalho.setBounds(6, 6, 1185, 72);
		pnlCabecalho.setLayout(new MigLayout("", "[]10[]40[]40[]100[]40[]150[]", "[][]"));
		
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
		
		JLabel lblValorReceitas = new JLabel();
		lblValorReceitas.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblValorReceitas.setText("R$ 4.000,00");
		pnlCabecalho.add(lblValorReceitas, "cell 1 1");
		
		JLabel lblValorDespesas = new JLabel("R$ 2.921,97");
		lblValorDespesas.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorDespesas, "cell 2 1");
		
		JLabel lblValorSaldo = new JLabel("R$ 1.078,03");
		lblValorSaldo.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorSaldo, "cell 3 1");
		
		JLabel lblValorTotalPago = new JLabel("R$ 1.245,02");
		lblValorTotalPago.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorTotalPago, "cell 4 1");
		
		JLabel lblValorInvestimentos = new JLabel("R$ 15.847,36");
		lblValorInvestimentos.setHorizontalAlignment(SwingConstants.CENTER);
		lblValorInvestimentos.setFont(new Font("Arial Black", Font.BOLD, 18));
		pnlCabecalho.add(lblValorInvestimentos, "cell 5 1");
		
		JLabel lblValorTotalPagar = new JLabel("R$ 1.676,95");
		lblValorTotalPagar.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValorTotalPagar.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorTotalPagar, "cell 6 1,alignx left");
		
		frmBolsoInteligente.getContentPane().add(pnlCabecalho);
		
		JPanel pnlRodape = new JPanel();
		pnlRodape.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) pnlRodape.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		pnlRodape.setBounds(16, 442, 1177, 34);
		frmBolsoInteligente.getContentPane().add(pnlRodape);
		
		
		
		JButton btnImportar = new JButton("Importar...");
		pnlRodape.add(btnImportar);
		btnImportar.setMnemonic(KeyEvent.VK_O);
		btnImportar.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JButton btnInvestimentos = new JButton("Investimentos...");
		pnlRodape.add(btnInvestimentos);
		btnInvestimentos.setMnemonic(KeyEvent.VK_I);
		btnInvestimentos.setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		btnPesquisarDespesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new IgPesquisarDespesa(frmBolsoInteligente,btnPesquisarDespesa);
			}
		});
		
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importarArquivo();
			}

			
		});
		
		frmBolsoInteligente.setVisible(true);
		
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
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
		UIManager.put("FileChooser.viewMenuLabelText", "Visualizar em");
		UIManager.put("FileChooser.refreshActionLabelText", "Atualizar");
		UIManager.put("FileChooser.newFolderActionLabelText", "Nova Pasta");
		UIManager.put("FileChooser.listViewActionLabelText", "Lista");
		UIManager.put("FileChooser.detailsViewActionLabelText", "Detalhes");
		
	}

	private void importarArquivo() {
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
