package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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
//		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	private JPanel constructMainPanel() {
		JPanel mainBar = new JPanel();
		mainBar.setPreferredSize(new Dimension(780, 650));
		// TODO add main panel
		return mainBar;

	}

	private JMenuBar constructMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuLogin = new JMenu("µÇÂ¼");
		
		JMenu menuShop = new JMenu("ÉÌµê");
		
		JMenu menuStore = new JMenu("²Ö¿â");
		
		menuBar.add(menuLogin);
		menuBar.add(menuShop);
		menuBar.add(menuStore);
		
		return menuBar;
	}

	public static void main(String[] args) {
		HelperFrame frame = new HelperFrame();
		frame.setVisible(true);
	}
	
//	public static void main(final String args[]) {   
//	    JFrame frame = new JFrame("MenuSample Example");   
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
//	    JMenuBar menuBar = new JMenuBar();   
//	  
//	    // File Menu, F - Mnemonic   
//	    JMenu fileMenu = new JMenu("File");   
//	    fileMenu.setMnemonic(KeyEvent.VK_F);   
//	    menuBar.add(fileMenu);   
//	  
//	    // File->New, N - Mnemonic   
//	    JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);   
//	    fileMenu.add(newMenuItem);   
//	  
//	    JCheckBoxMenuItem caseMenuItem = new JCheckBoxMenuItem("Case Sensitive");   
//	    caseMenuItem.setMnemonic(KeyEvent.VK_C);   
//	    fileMenu.add(caseMenuItem);   
//	      
//	    frame.setJMenuBar(menuBar);   
//	    frame.setSize(350, 250);   
//	    frame.setVisible(true);   
//	  }   
}
