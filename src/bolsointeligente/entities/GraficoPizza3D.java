package bolsointeligente.entities;

import java.awt.Color;
import java.text.AttributedString;
import java.text.DecimalFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;

public class GraficoPizza3D{
	
	private JFreeChart graficoPizza3D;
	
	private PiePlot3D plotGraficoPizza3D;
	
	private final Color COR_FUNDO          = Color.WHITE,
						COR_FUNDO_LEGENDAS = Color.WHITE;
	
	private final boolean EXIBIR_BORDAS_GRAFICO = false,
						  LADOS_ESCUROS         = true;
	
	private final static DecimalFormat FORMATO_DECIMAL = new DecimalFormat("0.00");
	private final PieSectionLabelGenerator GERADOR_LEGENDA = new StandardPieSectionLabelGenerator("{0}: {1}%",FORMATO_DECIMAL,FORMATO_DECIMAL);

	private final PieLabelLinkStyle ESTILO_LINHAS_CONEXAO_LEGENDA = PieLabelLinkStyle.QUAD_CURVE;
	
	private final PieToolTipGenerator GERADOR_TEXTO_AJUDA = new StandardPieToolTipGenerator("{0}: {1}%",FORMATO_DECIMAL,FORMATO_DECIMAL);
	
	private final float TRANSPARENCIA_GRAFICO = 0.6f;

	public GraficoPizza3D(JFreeChart graficoPizza3D) throws ClassCastException{
		this.graficoPizza3D = graficoPizza3D;
		plotGraficoPizza3D = (PiePlot3D)graficoPizza3D.getPlot();
		definirAtributosPadrão();
	}
	
	
	
	public JFreeChart getGraficoPizza3D() {
		return graficoPizza3D;
	}
	
	
	
	public PiePlot3D getPlotGraficoPizza3D() {
		return plotGraficoPizza3D;
	}


	private void definirAtributosPadrão() {
		alterarCorFundoGrafico(COR_FUNDO);
		exibirBordasGrafico(EXIBIR_BORDAS_GRAFICO);
		definirGeradorLegenda(GERADOR_LEGENDA);
		definirCorFundoLegendas(COR_FUNDO_LEGENDAS);
		definirEstiloLinhasConexaoLegenda(ESTILO_LINHAS_CONEXAO_LEGENDA);
		definirTransparenciaGrafico(TRANSPARENCIA_GRAFICO);
		definirGeradorTextoAjuda(GERADOR_TEXTO_AJUDA);
		definirLadosEscuros(LADOS_ESCUROS);
	}

	public GraficoPizza3D alterarCorFundoGrafico(Color cor) {
		plotGraficoPizza3D.setBackgroundPaint(cor);
		return this;
	}
	
	public GraficoPizza3D exibirBordasGrafico(boolean valor) {
		plotGraficoPizza3D.setOutlineVisible(valor);
		return this;
	}
	
	public GraficoPizza3D definirGeradorLegenda(PieSectionLabelGenerator geradorLegenda) {
		plotGraficoPizza3D.setLabelGenerator(geradorLegenda);
		return this;
	}
	
	public GraficoPizza3D definirCorFundoLegendas(Color cor) {
		plotGraficoPizza3D.setLabelBackgroundPaint(cor);
		return this;
	}
	
	public GraficoPizza3D definirEstiloLinhasConexaoLegenda(PieLabelLinkStyle estilo) {
		plotGraficoPizza3D.setLabelLinkStyle(estilo);
		return this;
	}
	
	public GraficoPizza3D definirTransparenciaGrafico(float valorTransparencia) {
		plotGraficoPizza3D.setForegroundAlpha(valorTransparencia);
		return this;
	}
	
	public GraficoPizza3D definirGeradorTextoAjuda(PieToolTipGenerator geradorTextoAjuda) {
		plotGraficoPizza3D.setToolTipGenerator(geradorTextoAjuda);
		return this;
	}
	
	public GraficoPizza3D definirLadosEscuros(boolean valor) {
		plotGraficoPizza3D.setDarkerSides(valor);
		return this;
	}
	
}
