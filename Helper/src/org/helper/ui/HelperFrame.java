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
		tab.addTab("手动操作", constructManuallyPanel());
		tab.addTab("好友列表", constructManuallyPanel2());
		tab.addTab("自动操作", constructManuallyPanel2());
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
		controlPanel.add(new JLabel("作物"));
		controlPanel.add(new JComboBox<String>(crops()));
		controlPanel.add(new JButton("执行护理"));
		controlPanel.add(new JButton("刷新"));

		JPanel footerWrapper = new JPanel();
		JTabbedPane consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(500, 250));
		consoleTab.addTab("操作日志", new JScrollPane());

		JTabbedPane infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(280, 250));
		infoPane.addTab("个人信息", new JScrollPane());
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
		JCheckBox sunshine = new JCheckBox("阳光");
		JCheckBox worm = new JCheckBox("杀虫");
		JCheckBox weed = new JCheckBox("除草");
		JCheckBox plow = new JCheckBox("翻地");
		JCheckBox havest = new JCheckBox("收获");
		JCheckBox buy = new JCheckBox("自动买种");
		JCheckBox plant = new JCheckBox("播种");
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
				"土地", "名称", "花期", "产量", "杂草", "虫害", "阳光", "收获时间" }, 0);
		Object[][] cellData = {
				{ new Boolean(true), "1", "玉米", "2day", "50", "true", "true",
						"true", "2013-6-7" },
				{ new Boolean(true), "2", "西瓜", "2day", "44", "true", "true",
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
		JMenu menuLogin = new JMenu("登录L");
		menuLogin.setMnemonic(KeyEvent.VK_L);
		menuLogin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new LoginDialog(frame);
			}
		});

		JMenu menuShop = new JMenu("商店S");
		menuShop.setMnemonic(KeyEvent.VK_S);

		JMenu menuStore = new JMenu("仓库T");
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
