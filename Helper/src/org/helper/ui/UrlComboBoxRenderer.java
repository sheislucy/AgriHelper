package org.helper.ui;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.helper.enums.EmUrlDomain;

public class UrlComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = -5776263185614313566L;

	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		if (value != null) {
			EmUrlDomain urlDomain = (EmUrlDomain) value;
			setText(urlDomain.getValue());
		}

		return this;
	}
}
