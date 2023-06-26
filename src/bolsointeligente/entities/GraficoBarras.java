package bolsointeligente.entities;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;


public class GraficoBarras {
	
	private final static Color COR_FUNDO_PADRAO = Color.WHITE;
	private final static boolean EXIBIR_LINHAS_BORDA = false;
	private final static DecimalFormat FORMATO_DECIMAL = new DecimalFormat("0.00%");
	private final static StandardCategoryToolTipGenerator TEXTO_AJUDA = new StandardCategoryToolTipGenerator("{0} - {1}: {2}", FORMATO_DECIMAL);
	private final static StandardCategoryItemLabelGenerator TEXTO_LEGENDA = new StandardCategoryItemLabelGenerator("{2}", FORMATO_DECIMAL);
	
	private JFreeChart graficoBarras;
	
	private CategoryPlot plotGraficoBarras;
	
	
	public GraficoBarras(JFreeChart graficoBarras) throws ClassCastException{
		this.graficoBarras = graficoBarras;
		plotGraficoBarras = graficoBarras.getCategoryPlot();
		definirAtributosPadrao();
	}

	private void definirAtributosPadrao() {
		gerarGeradorLegenda();
		definirCorDeFundo(COR_FUNDO_PADRAO);
		exibirLinhasBorda(EXIBIR_LINHAS_BORDA);
		definirFormatacaoValores(FORMATO_DECIMAL);
		definirFormatoTextoAjuda(TEXTO_AJUDA);
	}
	
	private void gerarGeradorLegenda() {
		BarRenderer renderizadorBarra = new BarRenderer();
		renderizadorBarra.setDefaultItemLabelGenerator(new CategoryItemLabelGenerator() {
			
			private static final NumberFormat format = NumberFormat.getNumberInstance();
			@Override
			public String generateRowLabel(CategoryDataset dataset, int row) {
				return null;
			}
			
			@Override
			public String generateLabel(CategoryDataset dataset, int row, int column) {
				Number valor = dataset.getValue(row, column);
	            return format.format(valor);
			}
			
			@Override
			public String generateColumnLabel(CategoryDataset dataset, int column) {
				return null;
			}
		});
		renderizadorBarra.setDefaultItemLabelsVisible(true);
		plotGraficoBarras.setRenderer(renderizadorBarra);
		plotGraficoBarras.getRenderer().setDefaultItemLabelGenerator(TEXTO_LEGENDA);
	}

	public GraficoBarras definirFormatoTextoAjuda(StandardCategoryToolTipGenerator textoAjuda) {
		plotGraficoBarras.getRenderer().setDefaultToolTipGenerator(textoAjuda);
		return this;
	}

	public GraficoBarras definirFormatacaoValores(NumberFormat formatacao) {
		NumberAxis yAxis = (NumberAxis) plotGraficoBarras.getRangeAxis();
		yAxis.setNumberFormatOverride(formatacao);
		return this;
	}

	public JFreeChart getGraficoBarras() {
		return graficoBarras;
	}

	public GraficoBarras definirCorDeFundo(Color cor) {
		plotGraficoBarras.setBackgroundPaint(cor);
		return this;
	}
	
	public GraficoBarras exibirLinhasBorda(boolean exibir) {
		plotGraficoBarras.setOutlineVisible(exibir);
		return this;
	}
	
}
