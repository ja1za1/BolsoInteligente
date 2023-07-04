package bolsointeligente.entities.tables;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorFloat extends DefaultTableCellRenderer {
		
		public RenderizadorFloat() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(value instanceof Float)
				value = String.format("R$%,.2f", (Float)value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		
}