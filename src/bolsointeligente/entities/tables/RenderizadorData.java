package bolsointeligente.entities.tables;

import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorData extends DefaultTableCellRenderer {
	
	public RenderizadorData() {
		setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if(value instanceof LocalDate)
			value = ((LocalDate)value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
	
	
}
