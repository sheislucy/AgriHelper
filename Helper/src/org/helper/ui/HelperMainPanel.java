package org.helper.ui;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author luxixu
 *
 */
public class HelperMainPanel extends JPanel {
	private static final long serialVersionUID = -2654503895987728181L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("test string in the panel.\n", 100, 100);
	}
}
