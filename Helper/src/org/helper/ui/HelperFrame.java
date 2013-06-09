package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;

/**
 * @author luxixu
 * 
 */
public class HelperFrame extends JFrame {
	private static final long serialVersionUID = -6344590535790274762L;
	private JTable farmTable;
	private JMenu menuLogin;
	private JPanel mainBar;
	private JTabbedPane tab;
	private JScrollPane manuallyPanel;
	private JPanel manuallyWrapper;
	private JScrollPane scrollTablePanel;
	private JPanel controlPanel;
	private JPanel footerWrapper;
	private JTabbedPane consoleTab;
	private JTabbedPane infoPane;
	private CheckTableModel tableModel;

	public HelperFrame() {
		this.setTitle("Helper Version 0.0.1");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);

		this.setJMenuBar(constructMenuBar(this));
		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	private JPanel constructMainPanel() {
		mainBar = new JPanel();
		mainBar.setPreferredSize(new Dimension(780, 650));

		tab = new JTabbedPane();
		tab.addTab("手动操作", constructManuallyPanel());
		tab.addTab("好友列表", constructManuallyPanel2());
		tab.addTab("自动操作", constructManuallyPanel2());
		mainBar.add(tab);
		return mainBar;

	}

	private JScrollPane constructManuallyPanel2() {
		manuallyPanel = new JScrollPane();
		manuallyPanel.setPreferredSize(new Dimension(780, 600));
		return manuallyPanel;
	}

	private JPanel constructManuallyPanel() {
		manuallyWrapper = new JPanel();
		BoxLayout lo = new BoxLayout(manuallyWrapper, BoxLayout.Y_AXIS);
		manuallyWrapper.setLayout(lo);

		scrollTablePanel = new JScrollPane(constructFarmFieldTable());
		scrollTablePanel.setPreferredSize(new Dimension(780, 300));

		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(780, 50));

		List<JCheckBox> controlCheckboxes = constructControlCheckbox();
		for (JCheckBox cb : controlCheckboxes) {
			controlPanel.add(cb);
		}
		controlPanel.add(new JLabel("作物"));
		controlPanel.add(new JComboBox<String>(crops()));
		controlPanel.add(new JButton("执行护理"));
		controlPanel.add(new JButton("刷新"));

		footerWrapper = new JPanel();
		consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(500, 250));
		consoleTab.addTab("操作日志", new JScrollPane());

		infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(280, 250));
		// JScrollPane userInfoPane = new JScrollPane(new
		// JLabel("用户名：qqqqqqqqqqqqqqqqqqqqqq ",
		// SwingConstants.LEFT));
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
		tableModel = new CheckTableModel(new Object[] { "", "土地", "名称", "花期",
				"产量", "杂草", "虫害", "阳光", "收获时间" }, 0);
		// Object[][] cellData = {
		// { new Boolean(true), "1", "玉米", "2day", "50", "true", "true",
		// "true", "2013-6-7" },
		// { new Boolean(true), "2", "西瓜", "2day", "44", "true", "true",
		// "true", "2013-6-7" } };
		// for (Object[] o : cellData) {
		// tableModel.addRow(o);
		// }

		farmTable = new JTable();
		farmTable.setModel(tableModel);
		farmTable.getTableHeader().setDefaultRenderer(
				new TableHeaderCheckboxRender(farmTable));

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
		menuLogin = new JMenu("登录L");
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

	public void changeLoginMenuName() {
		menuLogin.setText("登出O");
		menuLogin.setMnemonic(KeyEvent.VK_O);
		menuLogin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// new LoginDialog(frame);
			}
		});
		menuLogin.repaint();
	}

	public void refreshAccount() {
		JScrollPane userInfoPane = (JScrollPane) infoPane.getComponentAt(0);
		JPanel tempPanel = new JPanel();
		BoxLayout lo = new BoxLayout(tempPanel, BoxLayout.Y_AXIS);
		tempPanel.setLayout(lo);
		tempPanel.add(new JLabel("用户名： "
				+ FarmDomain.getInstance().getUserName(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("ID： "
				+ FarmDomain.getInstance().getUserId(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("金币： " + FarmDomain.getInstance().getMoney(),
				SwingConstants.LEFT));
		tempPanel.add(new JLabel("经验： " + FarmDomain.getInstance().getExp(),
				SwingConstants.LEFT));
		tempPanel.add(new JLabel("魅力值： " + FarmDomain.getInstance().getCharm(),
				SwingConstants.LEFT));
		userInfoPane.setViewportView(tempPanel);
		infoPane.repaint();

		tableModel.setRowCount(0);
		List<FieldUnitDomain> fieldList = FarmDomain.getInstance()
				.getFieldList();
		// Object[][] cellData = {
		// { new Boolean(true), "1", "玉米", "2day", "50", "true", "true",
		// "true", "2013-6-7" },
		// { new Boolean(true), "2", "西瓜", "2day", "44", "true", "true",
		// "true", "2013-6-7" } };
		int i = 0;
		for (FieldUnitDomain unit : fieldList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(true));
			entry.add(i++);
			entry.add(unit.getA());
			entry.add("1H");
			entry.add(unit.getK());
			entry.add(new Boolean(unit.getS()) ? "YES" : "-");
			entry.add(new Boolean(unit.getT()) ? "YES;" : "-");
			entry.add(new Boolean(unit.getU()) ? "YES;" : "-");
			tableModel.addRow(entry);
		}
		farmTable.setModel(tableModel);
		farmTable.repaint();
	}

}
