package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

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

		this.setJMenuBar(constructMenuBar(this));
		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	private JPanel constructMainPanel() {
		JPanel mainBar = new JPanel();
		mainBar.setPreferredSize(new Dimension(780, 650));

		JTabbedPane tab = new JTabbedPane();
		tab.addTab("�ֶ�����", constructManuallyPanel());
		tab.addTab("�����б�", constructManuallyPanel2());
		tab.addTab("�Զ�����", constructManuallyPanel2());
		mainBar.add(tab);
		return mainBar;

	}

	private JScrollPane constructManuallyPanel2() {
		JScrollPane manuallyPanel = new JScrollPane();
		manuallyPanel.setPreferredSize(new Dimension(780, 600));
		return manuallyPanel;
	}

	private JPanel constructManuallyPanel() {
		JPanel manuallyWrapper = new JPanel();
		BoxLayout lo = new BoxLayout(manuallyWrapper, BoxLayout.Y_AXIS);
		manuallyWrapper.setLayout(lo);

		JScrollPane scrollTablePanel = new JScrollPane(
				constructFarmFieldTable());
		scrollTablePanel.setPreferredSize(new Dimension(780, 300));

		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(780, 50));

		List<JCheckBox> controlCheckboxes = constructControlCheckbox();
		for (JCheckBox cb : controlCheckboxes) {
			controlPanel.add(cb);
		}
		controlPanel.add(new JLabel("����"));
		controlPanel.add(new JComboBox<String>(crops()));
		controlPanel.add(new JButton("ִ�л���"));
		controlPanel.add(new JButton("ˢ��"));

		JPanel footerWrapper = new JPanel();
		JTabbedPane consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(500, 250));
		consoleTab.addTab("������־", new JScrollPane());

		JTabbedPane infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(280, 250));
		infoPane.addTab("������Ϣ", new JScrollPane());
		footerWrapper.add(consoleTab);
		footerWrapper.add(infoPane);

		manuallyWrapper.add(scrollTablePanel);
		manuallyWrapper.add(controlPanel);
		manuallyWrapper.add(footerWrapper);
		return manuallyWrapper;
	}

	private String[] crops() {
		return new String[] { "potato", "tomato" };
	}

	private List<JCheckBox> constructControlCheckbox() {
		List<JCheckBox> checkboxSet = new ArrayList<JCheckBox>();
		JCheckBox sunshine = new JCheckBox("����");
		JCheckBox worm = new JCheckBox("ɱ��");
		JCheckBox weed = new JCheckBox("����");
		JCheckBox plow = new JCheckBox("����");
		JCheckBox havest = new JCheckBox("�ջ�");
		JCheckBox buy = new JCheckBox("�Զ�����");
		JCheckBox plant = new JCheckBox("����");
		checkboxSet.add(sunshine);
		checkboxSet.add(worm);
		checkboxSet.add(weed);
		checkboxSet.add(plow);
		checkboxSet.add(havest);
		checkboxSet.add(buy);
		checkboxSet.add(plant);

		return checkboxSet;
	}

	private JTable constructFarmFieldTable() {
		CheckTableModel tableModel = new CheckTableModel(new Object[] { "",
				"����", "����", "����", "����", "�Ӳ�", "�溦", "����", "�ջ�ʱ��" }, 0);
		Object[][] cellData = {
				{ new Boolean(true), "1", "����", "2day", "50", "true", "true",
						"true", "2013-6-7" },
				{ new Boolean(true), "2", "����", "2day", "44", "true", "true",
						"true", "2013-6-7" } };
		for (Object[] o : cellData) {
			tableModel.addRow(o);
		}

		JTable farmTable = new JTable();
		farmTable.setModel(tableModel);
		farmTable.getTableHeader().setDefaultRenderer(
				new TableHeaderChechboxRender(farmTable));

		TableColumnModel tcm = farmTable.getColumnModel();
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new CheckboxInTableRenderer());
		tcm.getColumn(0).setMaxWidth(20);

		tcm.getColumn(1).setMaxWidth(50);

		tcm.getColumn(2).setWidth(400);

		tcm.getColumn(3).setMaxWidth(200);
		tcm.getColumn(4).setMaxWidth(50);
		tcm.getColumn(5).setMaxWidth(50);
		tcm.getColumn(6).setMaxWidth(50);
		tcm.getColumn(7).setMaxWidth(50);

		return farmTable;
	}

	private JMenuBar constructMenuBar(final HelperFrame frame) {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuLogin = new JMenu("��¼L");
		menuLogin.setMnemonic(KeyEvent.VK_L);
		menuLogin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new LoginDialog(frame);
			}
		});

		JMenu menuShop = new JMenu("�̵�S");
		menuShop.setMnemonic(KeyEvent.VK_S);

		JMenu menuStore = new JMenu("�ֿ�T");
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
