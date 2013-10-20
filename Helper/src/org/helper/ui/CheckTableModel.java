package org.helper.ui;

import javax.swing.table.DefaultTableModel;

public class CheckTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	public CheckTableModel(Object[] os, int rows) {
		super(os, rows);
	}

	@Override
	public Class<? extends Object> getColumnClass(int c) {
		Object value = getValueAt(0, c);
		return null != value ? value.getClass() : super.getClass();
	}

	public void selectAllOrNull(boolean value) {
		for (int i = 0; i < getRowCount(); i++) {
			this.setValueAt(value, i, 0);
		}
	}
}
