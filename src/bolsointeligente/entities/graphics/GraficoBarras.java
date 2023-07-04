package bolsointeligente.entities.graphics;

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
	private final static StandardCategoryToolTipGenerator TEXTO_AJUDA = new StandardCategoryToolTipGenerator("{0}: {2}", FORMATO_DECIMAL);
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
		BarRenderer renderizadorBarra = (BarRenderer)plotGraficoBarras.getRenderer();
		renderizadorBarra.setDefaultItemLabelGenerator(new CategoryItemLabelGenerator() {
			
			@Override
			public String generateRowLabel(CategoryDataset dataset, int row) {
				return null;
			}
			
			@Override
			public String generateLabel(CategoryDataset dataset, int row, int column) {
				Number valor = dataset.getValue(row, column);
	            return String.format("%,.2f", valor);
			}
			
			@Override
			public String generateColumnLabel(CategoryDataset dataset, int column) {
				return null;
			}
		});
		renderizadorBarra.setDefaultItemLabelsVisible(true);
		renderizadorBarra.setDrawBarOutline(true);
		renderizadorBarra.setItemLabelAnchorOffset(0.5d);
		renderizadorBarra.setShadowVisible(true);
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
	
	
	public CategoryPlot getPlotGraficoBarras() {
		return plotGraficoBarras;
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
