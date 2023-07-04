package bolsointeligente.gui;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bolsointeligente.entities.BolsoInteligente;
import bolsointeligente.entities.tables.TabelaDespesas;
import bolsointeligente.utils.DataHora;
import bolsointeligente.utils.ValidarCampos;
import mos.es.InputOutput;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.awt.Color;

public class IgPesquisarDespesa{

	private final boolean MODAL = true;
	private final String TITULO_JANELA = "Pesquisar Despesa";
	private final int LARGURA_JANELA = 489, 
					  ALTURA_JANELA  = 167;
	private ButtonGroup buttonGroup;
	private JRadioButton rdbtnData,
						 rdbtnDescricao,
						 rdbtnValor;
	
	private JDialog jDialogPesquisarDespesa;
	private TabelaDespesas tabelaDespesa;
	private JTextField txtFieldItemDespesa;
	/**
	 * Create the dialog.
	 */
	public IgPesquisarDespesa(JFrame framePai,JFrame framePrincipal, TabelaDespesas tabela) {
		jDialogPesquisarDespesa = new JDialog(framePai, TITULO_JANELA, MODAL);
		jDialogPesquisarDespesa.getContentPane().setBackground(new Color(255, 255, 255));
		jDialogPesquisarDespesa.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jDialogPesquisarDespesa.setResizable(false);
		jDialogPesquisarDespesa.setBounds(100,100, LARGURA_JANELA, ALTURA_JANELA);
		jDialogPesquisarDespesa.setLocationRelativeTo(framePrincipal);
		jDialogPesquisarDespesa.getContentPane().setLayout(null);
		
		this.tabelaDespesa = tabela;
		JPanel pnlPesquisarDespesa = new JPanel();
		pnlPesquisarDespesa.setBackground(new Color(255, 255, 255));
		pnlPesquisarDespesa.setBounds(0, 0, 473, 96);
		pnlPesquisarDespesa.setBorder(new EmptyBorder(5, 5, 5, 5));
		jDialogPesquisarDespesa.getContentPane().add(pnlPesquisarDespesa);
		pnlPesquisarDespesa.setLayout(null);
		
		JLabel lblItemDespesa = new JLabel("Item de despesa:");
		lblItemDespesa.setDisplayedMnemonic(KeyEvent.VK_I);
		lblItemDespesa.setBounds(10, 14, 96, 14);
		lblItemDespesa.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlPesquisarDespesa.add(lblItemDespesa);
		
		JLabel lblProcurarPor = new JLabel("Procurar por:");
		lblProcurarPor.setBounds(10, 61, 71, 14);
		lblProcurarPor.setFont(new Font("Arial", Font.PLAIN, 12));
		pnlPesquisarDespesa.add(lblProcurarPor);
		
		rdbtnData = new JRadioButton("Data");
		rdbtnData.setBounds(87, 57, 51, 23);
		rdbtnData.setMnemonic(KeyEvent.VK_D);
		rdbtnData.setFont(new Font("Arial", Font.PLAIN, 12));
		rdbtnData.setSelected(true);
		
		
		
		rdbtnDescricao = new JRadioButton("Descrição");
		rdbtnDescricao.setBounds(147, 57, 81, 23);
		rdbtnDescricao.setMnemonic(KeyEvent.VK_E);
		rdbtnDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		rdbtnValor = new JRadioButton("Valor");
		rdbtnValor.setBounds(236, 57, 53, 23);
		rdbtnValor.setMnemonic(KeyEvent.VK_V);
		rdbtnValor.setFont(new Font("Arial", Font.PLAIN, 12));
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnData);
		buttonGroup.add(rdbtnDescricao);
		buttonGroup.add(rdbtnValor);
		
		pnlPesquisarDespesa.add(rdbtnData);
		pnlPesquisarDespesa.add(rdbtnDescricao);
		pnlPesquisarDespesa.add(rdbtnValor);
		
		txtFieldItemDespesa = new JTextField();
		txtFieldItemDespesa.setBounds(115, 11, 203, 28);
		pnlPesquisarDespesa.add(txtFieldItemDespesa);
		txtFieldItemDespesa.setColumns(10);
		
		
		
		
		JPanel btnPaneRodape = new JPanel();
		btnPaneRodape.setBackground(new Color(255, 255, 255));
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
		btnPaneRodape.add(btnFechar);
		
		btnProximaDespesa.addActionListener((e) -> obterItemDespesa());
		
		btnFechar.addActionListener((e) -> jDialogPesquisarDespesa.dispose());
		
		rdbtnData.addActionListener((e) -> limparCaixaTextoLimparLinhasSelecionadas());
		
		rdbtnDescricao.addActionListener((e) -> limparCaixaTextoLimparLinhasSelecionadas());
		
		rdbtnValor.addActionListener((e) -> limparCaixaTextoLimparLinhasSelecionadas());
		
		
		jDialogPesquisarDespesa.setVisible(true);
	}
	
	private void limparCaixaTextoLimparLinhasSelecionadas() {
		int linhaSelecionada = tabelaDespesa.getTabelaDespesas().getSelectedRow();
		
		if(linhaSelecionada != -1) {
			tabelaDespesa.getTabelaDespesas().removeRowSelectionInterval(linhaSelecionada, linhaSelecionada);
		}
		txtFieldItemDespesa.setText("");
	}
	
	private boolean obterItemDespesa() {
		Integer linhaEncontrouDespesa = null,
				linhaSelecionada = tabelaDespesa.getTabelaDespesas().getSelectedRow();
		String textoDigitadoUsuario = txtFieldItemDespesa.getText().strip().replace(".","").replace(",", ".");
		if(rdbtnData.isSelected()) {
			if(textoDigitadoUsuario.isEmpty()) {
				InputOutput.showError("Data não pode estar vazia!", BolsoInteligente.NOME_PROGRAMA);
				return false;
			}
			else if(!ValidarCampos.verificarDataValida(textoDigitadoUsuario)) {
				InputOutput.showError("Data inválida!", BolsoInteligente.NOME_PROGRAMA);
				return false;
			}
			else {
				linhaEncontrouDespesa = pesquisarCamposTabela(TabelaDespesas.COLUNA_DATA, textoDigitadoUsuario, linhaSelecionada);
			}
		}
		else if(rdbtnDescricao.isSelected()) {
			if(textoDigitadoUsuario.isEmpty()) {
				InputOutput.showError("Descrição não pode estar vazia!", BolsoInteligente.NOME_PROGRAMA);
				return false;
			}
			else {
				linhaEncontrouDespesa = pesquisarCamposTabela(TabelaDespesas.COLUNA_DESCRICAO, textoDigitadoUsuario, linhaSelecionada);
			}
			
		}
		else if(rdbtnValor.isSelected()) {
			if(textoDigitadoUsuario.isEmpty()) {
				InputOutput.showError("Valor não pode estar vazio!", BolsoInteligente.NOME_PROGRAMA);
				return false;
			}
			else if(!ValidarCampos.verificarFloatValido(textoDigitadoUsuario)) {
				InputOutput.showError("Valor inválido!", BolsoInteligente.NOME_PROGRAMA);
				return false;
			}
			else {
				linhaEncontrouDespesa = pesquisarCamposTabela(TabelaDespesas.COLUNA_VALOR, textoDigitadoUsuario, linhaSelecionada);
			}
		}
		
		if(linhaEncontrouDespesa == null) {
			InputOutput.showError("Nenhuma despesa com esse valor encontrada!", BolsoInteligente.NOME_PROGRAMA);
		}
		else if(linhaEncontrouDespesa == -1) {
			InputOutput.showAlert("Não há mais despesas com esse valor.", BolsoInteligente.NOME_PROGRAMA);
			tabelaDespesa.getTabelaDespesas().removeRowSelectionInterval(tabelaDespesa.getTabelaDespesas().getSelectedRow(), tabelaDespesa.getTabelaDespesas().getSelectedRow());
		}
		else {
			tabelaDespesa.getTabelaDespesas().setRowSelectionInterval(linhaEncontrouDespesa, linhaEncontrouDespesa);
		}
		return true;
	}
	
	private Integer pesquisarCamposTabela(int indiceColuna, String textoDigitadoUsuario, int linhaSelecionada) {
		String textoCampo;
		JTable tabela = tabelaDespesa.getTabelaDespesas();
		int	numeroTotalLinhas = tabela.getRowCount() - 1;
		boolean achouValor = false;
		System.out.println(textoDigitadoUsuario);
		for(int i = 0; i < numeroTotalLinhas; i++) {
			if(indiceColuna == TabelaDespesas.COLUNA_DATA)
				textoCampo = DataHora.obterDataFormatada((LocalDate)tabela.getValueAt(i, indiceColuna));
			else if (indiceColuna == TabelaDespesas.COLUNA_VALOR){
				textoCampo = String.format("%.2f", Float.parseFloat(tabela.getValueAt(i, indiceColuna).toString())).replace(",", ".");
			}
			else {
				textoCampo = tabela.getValueAt(i, indiceColuna).toString();
			}
			System.out.println(textoCampo);
			if(textoCampo.equalsIgnoreCase(textoDigitadoUsuario)) {
				achouValor = true;
				if(i > linhaSelecionada) return i;
			}
		}
		return (achouValor == false) ? null : -1;
	}
}
