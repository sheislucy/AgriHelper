package org.helper.ui;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.helper.domain.PackageUnitDomain;
import org.helper.enums.EmPackageType;

public class PackageTableRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setToolTipText("双击种子行来修改[作物]下拉框中的种子");
		if (value != null) {
			PackageUnitDomain packageUnit = (PackageUnitDomain) value;
			if (column == 0) {
				setText(packageUnit.getName());
			} else {
				setText(packageUnit.getAmount());
			}
			if (packageUnit.getType() != EmPackageType.SEED) {
				setBackground(java.awt.Color.pink);
				setOpaque(true);
			} else {
				setOpaque(false);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
			if (isSelected) {
				setOpaque(true);
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			}
		}
		return this;
	}

}
