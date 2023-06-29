package bolsointeligente.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

public class IgInvestimentos{
	
	private JDialog janelaInvestimentos;
	private JButton btnGraficoBarras,
				    btnGraficoColunas,
				    btnFechar;

	/**
	 * Create the dialog.
	 */
	public IgInvestimentos() {
		janelaInvestimentos = new JDialog();
		janelaInvestimentos.setTitle("Bolso Inteligente - Investimentos");
		janelaInvestimentos.setModalityType(ModalityType.APPLICATION_MODAL);
		janelaInvestimentos.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE );
		janelaInvestimentos.setBounds(100, 100, 1004, 442);
		
		JPanel pnlCabecalho = new JPanel();
		pnlCabecalho.setBackground(new Color(255, 255, 255));
		janelaInvestimentos.getContentPane().add(pnlCabecalho, BorderLayout.NORTH);
		pnlCabecalho.setLayout(new MigLayout("", "40[]80[]80[]40", "[][]"));
		
		JLabel lblTotalInvestido = new JLabel("Total Investido");
		lblTotalInvestido.setForeground(new Color(0, 0, 160));
		lblTotalInvestido.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblTotalInvestido, "cell 0 0");
		
		JLabel lblTotalAcumulado = new JLabel("Total Acumulado");
		lblTotalAcumulado.setForeground(new Color(0, 0, 160));
		lblTotalAcumulado.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblTotalAcumulado, "cell 1 0");
		
		JLabel lblRendimentoBruto = new JLabel("Rendimento Bruto");
		lblRendimentoBruto.setForeground(new Color(0, 0, 160));
		lblRendimentoBruto.setFont(new Font("Arial Black", Font.PLAIN, 16));
		pnlCabecalho.add(lblRendimentoBruto, "cell 2 0");
		
		JLabel lblValorTotaInvestido = new JLabel("R$ 6.000,00");
		lblValorTotaInvestido.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorTotaInvestido, "cell 0 1");
		
		JLabel lblValorTotalAcumulado = new JLabel("R$ 10.440,79");
		lblValorTotalAcumulado.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorTotalAcumulado, "cell 1 1");
		
		JLabel lblValorRendimentoBruto = new JLabel("R$ 4.350,79");
		lblValorRendimentoBruto.setFont(new Font("Arial Black", Font.PLAIN, 18));
		pnlCabecalho.add(lblValorRendimentoBruto, "cell 2 1");
		
		JPanel pnlInvestimentos = new JPanel();
		pnlInvestimentos.setBackground(new Color(255, 255, 255));
		pnlInvestimentos.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Investimentos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		janelaInvestimentos.getContentPane().add(pnlInvestimentos, BorderLayout.CENTER);
		pnlInvestimentos.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(new JTable());
		pnlInvestimentos.add(scrollPane, BorderLayout.CENTER);
		
		JPanel pnlRodape = new JPanel();
		pnlRodape.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) pnlRodape.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		janelaInvestimentos.getContentPane().add(pnlRodape, BorderLayout.SOUTH);
		
		btnGraficoBarras = new JButton("Gráfico em Barras");
		btnGraficoBarras.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarras.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlRodape.add(btnGraficoBarras);
		
		btnGraficoColunas = new JButton("Gráfico em Colunas");
		btnGraficoColunas.setFont(new Font("Arial", Font.PLAIN, 12));
		btnGraficoColunas.setMnemonic(KeyEvent.VK_C);
		pnlRodape.add(btnGraficoColunas);
		
		btnFechar = new JButton("Fechar");
		btnFechar.setMnemonic(KeyEvent.VK_F);
		btnFechar.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlRodape.add(btnFechar);
		adicionarListeners();
		
		janelaInvestimentos.setVisible(true);
		
	}

	private void adicionarListeners() {
		btnFechar.addActionListener((e) -> fecharJanela());
	}

	private void fecharJanela() {
		janelaInvestimentos.dispose();
	}

}
