package org.helper.ui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckboxInTableRenderer extends JCheckBox implements
		TableCellRenderer {
	private static final long serialVersionUID = -8264390955923094416L;


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Boolean b = (Boolean) value;
		this.setSelected(b.booleanValue());
		return this;
	}

}
