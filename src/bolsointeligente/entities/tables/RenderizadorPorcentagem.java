package bolsointeligente.entities.tables;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorPorcentagem extends DefaultTableCellRenderer{
	public RenderizadorPorcentagem(){
		setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if(value instanceof Float)
			value = String.format("%,.2f%%", (Float)value);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
