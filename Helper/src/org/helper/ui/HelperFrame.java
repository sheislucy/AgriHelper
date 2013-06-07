package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.helper.util.FontUtil;

/**
 * @author luxixu
 * 
 */
public class HelperFrame extends JFrame {
	private static final long serialVersionUID = -6344590535790274762L;

	public HelperFrame() {
		this.setTitle("Helper Version 0.0.1");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		// TODO
		this.setJMenuBar(constructMenuBar());
		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	private JPanel constructMainPanel() {
		JPanel mainBar = new JPanel();
		mainBar.setPreferredSize(new Dimension(780, 650));

		JTabbedPane tab = new JTabbedPane();
		tab.addTab("手动操作", constructManuallyPanel());
		tab.addTab("自动操作", constructManuallyPanel());
		mainBar.add(tab);
		return mainBar;

	}
	
	private JPanel constructManuallyPanel(){
		JPanel manuallyPanel = new JPanel();
		manuallyPanel.setPreferredSize(new Dimension(780, 600));
		return manuallyPanel;
	}

	private JMenuBar constructMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuLogin = new JMenu("登录_L");
		menuLogin.setMnemonic(KeyEvent.VK_L);

		JMenu menuShop = new JMenu("商店_S");
		menuShop.setMnemonic(KeyEvent.VK_S);

		JMenu menuStore = new JMenu("仓库_T");
		menuStore.setMnemonic(KeyEvent.VK_T);

		menuBar.add(menuLogin);
		menuBar.add(menuShop);
		menuBar.add(menuStore);

		return menuBar;
	}

	public static void main(String[] args) {
		HelperFrame frame = new HelperFrame();
		frame.setVisible(true);
	}

}
