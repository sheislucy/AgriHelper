package org.helper.ui;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.helper.domain.CropDomain;

public class SeedComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = -3305370701169717328L;

	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		if (value != null) {
			CropDomain crop = (CropDomain) value;
			setText(crop.getcName().replace("种子", ""));
		}

		return this;
	}
}
