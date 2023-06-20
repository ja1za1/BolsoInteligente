package bolsointeligente.gui;

import java.awt.FlowLayout;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.event.KeyEvent;

public class IgPesquisarDespesa extends JDialog {

	private final boolean MODAL = true;
	private final String TITULO_JANELA = "Pesquisar Despesa";
	private final int LARGURA_JANELA = 489, 
					  ALTURA_JANELA  = 167;
			
	
	private JDialog jDialogPesquisarDespesa;
	/**
	 * Create the dialog.
	 */
	public IgPesquisarDespesa(JFrame framePai,JButton botaoClicado) {
		jDialogPesquisarDespesa = new JDialog(framePai, TITULO_JANELA, MODAL);
		jDialogPesquisarDespesa.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jDialogPesquisarDespesa.setResizable(false);
		jDialogPesquisarDespesa.setBounds(100,100, LARGURA_JANELA, ALTURA_JANELA);
		jDialogPesquisarDespesa.setLocationRelativeTo(botaoClicado);
		jDialogPesquisarDespesa.getContentPane().setLayout(null);
		
		JPanel pnlPesquisarDespesa = new JPanel();
		pnlPesquisarDespesa.setBounds(0, 0, 473, 96);
		pnlPesquisarDespesa.setBorder(new EmptyBorder(5, 5, 5, 5));
		jDialogPesquisarDespesa.getContentPane().add(pnlPesquisarDespesa);
		pnlPesquisarDespesa.setLayout(null);
		
		JLabel lblItemDespesa = new JLabel("Item de despesa:");
		lblItemDespesa.setDisplayedMnemonic(KeyEvent.VK_I);
		lblItemDespesa.setBounds(10, 14, 96, 14);
		lblItemDespesa.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlPesquisarDespesa.add(lblItemDespesa);
		
		JTextField txtFieldItemDespesa = new JTextField();
		lblItemDespesa.setLabelFor(txtFieldItemDespesa);
		txtFieldItemDespesa.setBounds(115, 11, 185, 20);
		txtFieldItemDespesa.setColumns(10);
		pnlPesquisarDespesa.add(txtFieldItemDespesa);
		
		JLabel lblProcurarPor = new JLabel("Procurar por:");
		lblProcurarPor.setBounds(10, 61, 71, 14);
		lblProcurarPor.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlPesquisarDespesa.add(lblProcurarPor);
		
		JRadioButton rdbtnData = new JRadioButton("Data");
		rdbtnData.setBounds(87, 57, 51, 23);
		rdbtnData.setMnemonic(KeyEvent.VK_D);
		rdbtnData.setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		JRadioButton rdbtnDescricao = new JRadioButton("Descrição");
		rdbtnDescricao.setBounds(147, 57, 81, 23);
		rdbtnDescricao.setMnemonic(KeyEvent.VK_E);
		rdbtnDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		JRadioButton rdbtnValor = new JRadioButton("Valor");
		rdbtnValor.setBounds(236, 57, 53, 23);
		rdbtnValor.setMnemonic(KeyEvent.VK_V);
		rdbtnValor.setFont(new Font("Arial", Font.PLAIN, 12));
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnData);
		buttonGroup.add(rdbtnDescricao);
		buttonGroup.add(rdbtnValor);
		
		pnlPesquisarDespesa.add(rdbtnData);
		pnlPesquisarDespesa.add(rdbtnDescricao);
		pnlPesquisarDespesa.add(rdbtnValor);
		
		
		
		
		JPanel btnPaneRodape = new JPanel();
		btnPaneRodape.setBounds(0, 96, 473, 33);
		FlowLayout fl_btnPaneRodape = new FlowLayout(FlowLayout.RIGHT);
		btnPaneRodape.setLayout(fl_btnPaneRodape);
		jDialogPesquisarDespesa.getContentPane().add(btnPaneRodape);
		
		JButton btnProximaDespesa = new JButton("Próxima Despesa");
		btnProximaDespesa.setMnemonic(KeyEvent.VK_P);
		btnProximaDespesa.setFont(new Font("Arial", Font.PLAIN, 12));
		btnPaneRodape.add(btnProximaDespesa);
		jDialogPesquisarDespesa.getRootPane().setDefaultButton(btnProximaDespesa);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.setMnemonic(KeyEvent.VK_F);
		btnFechar.setFont(new Font("Arial", Font.PLAIN, 12));
		btnFechar.setActionCommand("Cancel");
		btnPaneRodape.add(btnFechar);
		
		jDialogPesquisarDespesa.setVisible(true);
	}
}
