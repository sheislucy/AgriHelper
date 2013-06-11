package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import org.apache.http.client.ClientProtocolException;
import org.helper.domain.CropDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.domain.VeryCDUserDomain;
import org.helper.service.ExecuteService;
import org.helper.service.RefreshFarmStep4Service;
import org.helper.service.ServiceFactory;
import org.helper.util.EmCropStatus;
import org.helper.util.EmOperations;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.parser.ParseException;

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
	private LoginDialog loginDialog;
	private JButton refreshBtn;
	private JTextArea loggerArea;
	private JCheckBox water;
	private JCheckBox worm;
	private JCheckBox weed;
	private JCheckBox plow;
	private JCheckBox harvest;
	private JCheckBox buy;
	private JCheckBox plant;
	private JButton execute;
	private JComboBox seedCombo;

	private VeryCDUserDomain userDomain;
	private FarmDomain farmDomain;
	private List<EmOperations> operationList;
	private List<String> checkedFieldIdList;

	public HelperFrame() {
		this.refreshBtn = new JButton("刷新");
		this.execute = new JButton("执行护理");
		this.loggerArea = new JTextArea();
		this.loggerArea.setLineWrap(true);
		this.operationList = new ArrayList<EmOperations>();
		this.checkedFieldIdList = new ArrayList<String>();
		// FIXME dummy code
		this.loggerArea
				.append("log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1log1"
						+ "log1log1log1log1log1log1log1log1log1log1\n");
		this.loggerArea.append("log2\n");

		this.setTitle("Helper Version 0.0.1");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		HelperLoggerAppender.setInstance(this);

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
		scrollTablePanel.setPreferredSize(new Dimension(780, 350));

		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(780, 50));

		List<JCheckBox> controlCheckboxes = constructControlCheckbox();
		for (JCheckBox cb : controlCheckboxes) {
			controlPanel.add(cb);
		}
		controlPanel.add(new JLabel("作物"));
		controlPanel.add(constructSeedCombo());
		controlPanel.add(execute);
		controlPanel.add(refreshBtn);

		bindRefreshEvent();
		bindExecuteEvent();

		footerWrapper = new JPanel();
		consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(580, 200));
		consoleTab.addTab("操作日志", loggerArea);

		infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(200, 200));
		infoPane.addTab("个人信息", new JScrollPane());
		footerWrapper.add(consoleTab);
		footerWrapper.add(infoPane);

		manuallyWrapper.add(scrollTablePanel);
		manuallyWrapper.add(controlPanel);
		manuallyWrapper.add(footerWrapper);
		return manuallyWrapper;
	}

	@SuppressWarnings("unchecked")
	private JComboBox constructSeedCombo() {
		seedCombo = new JComboBox(ShopDomain.getCropList());// TODO
		seedCombo.setRenderer(new ComboBoxRenderer());
		return seedCombo;
	}

	private void bindExecuteEvent() {
		this.execute.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				VeryCDUserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				collectOperations();
				collectFields();
				// TODO
				ExecuteService executeService = ServiceFactory
						.getService(ExecuteService.class);
				executeService.execute(operationList, checkedFieldIdList,
						((CropDomain) seedCombo.getSelectedItem()).getcId());
				this.mouseReleased(e);
				refreshBtn.doClick();
			}
		});
	}

	private void collectFields() {
		checkedFieldIdList.clear();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if ((Boolean) tableModel.getValueAt(i, 0)) {
				checkedFieldIdList.add(String.valueOf(tableModel.getValueAt(i,
						1)));
			}
		}
	}

	private void collectOperations() {
		operationList.clear();
		if (water.isSelected()) {
			operationList.add(EmOperations.WATER);
		}
		if (worm.isSelected()) {
			operationList.add(EmOperations.WORM);
		}
		if (weed.isSelected()) {
			operationList.add(EmOperations.WEED);
		}
		if (plow.isSelected()) {
			operationList.add(EmOperations.PLOW);
		}
		if (harvest.isSelected()) {
			operationList.add(EmOperations.HARVEST);
		}
		if (buy.isSelected()) {
			operationList.add(EmOperations.BUY);
		}
		if (plant.isSelected()) {
			operationList.add(EmOperations.PLANT);
		}
	}

	private List<JCheckBox> constructControlCheckbox() {
		List<JCheckBox> checkboxSet = new ArrayList<JCheckBox>();
		water = new JCheckBox("浇水");
		worm = new JCheckBox("杀虫");
		weed = new JCheckBox("除草");
		plow = new JCheckBox("翻地");
		harvest = new JCheckBox("收获");
		buy = new JCheckBox("自动买种");
		plant = new JCheckBox("播种");
		checkboxSet.add(water);
		checkboxSet.add(worm);
		checkboxSet.add(weed);
		checkboxSet.add(plow);
		checkboxSet.add(harvest);
		checkboxSet.add(buy);
		checkboxSet.add(plant);

		return checkboxSet;
	}

	private void bindRefreshEvent() {
		this.refreshBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 3231109692374056419L;

			@Override
			public void actionPerformed(ActionEvent e) {
				RefreshFarmStep4Service farmService = ServiceFactory
						.getService(RefreshFarmStep4Service.class);
				VeryCDUserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				try {
					farmService.getFarmAndPlayerInfo();
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				refreshAccount();
			}
		});

		this.refreshBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				RefreshFarmStep4Service farmService = ServiceFactory
						.getService(RefreshFarmStep4Service.class);
				VeryCDUserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				try {
					farmService.getFarmAndPlayerInfo();
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				refreshAccount();
				this.mouseReleased(e);
			}
		});
	}

	private JTable constructFarmFieldTable() {
		tableModel = new CheckTableModel(new Object[] { "", "土地", "名称", "阶段",
				"花期", "产量", "杂草", "虫害", "干旱", "收获时间" }, 0);

		farmTable = new JTable();
		farmTable.setModel(tableModel);
		farmTable.getTableHeader().setDefaultRenderer(
				new TableHeaderCheckboxRender(farmTable));

		TableColumnModel tcm = farmTable.getColumnModel();
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new CheckboxInTableRenderer());
		tcm.getColumn(0).setMaxWidth(20);

		tcm.getColumn(1).setMaxWidth(50);

		tcm.getColumn(2).setWidth(300);// NAME
		tcm.getColumn(3).setMaxWidth(50);// status

		tcm.getColumn(4).setMaxWidth(100);
		tcm.getColumn(5).setMaxWidth(50);
		tcm.getColumn(6).setMaxWidth(50);
		tcm.getColumn(7).setMaxWidth(50);
		tcm.getColumn(8).setMaxWidth(150);

		return farmTable;
	}

	private JMenuBar constructMenuBar(final HelperFrame frame) {
		loginDialog = new LoginDialog(frame);
		JMenuBar menuBar = new JMenuBar();
		menuLogin = new JMenu("登录L");
		menuLogin.setMnemonic(KeyEvent.VK_L);
		menuLogin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loginDialog.showIt();
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
		tempPanel.add(new JLabel("ID： " + FarmDomain.getInstance().getUserId(),
				SwingConstants.LEFT));
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
		int i = 0;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (FieldUnitDomain unit : fieldList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(true));
			entry.add(i++);
			entry.add(ShopDomain.getCropName(unit.getA()).replace("种子", ""));
			entry.add(EmCropStatus.getStatusName(unit.getB()));// 阶段
			long cycle = Long.parseLong(ShopDomain.getGrowthCycle(unit.getA()));
			entry.add(formatCycle(cycle));
			entry.add(unit.getK());
			entry.add(Integer.parseInt(unit.getS()) > 0 ? Integer.parseInt(unit
					.getS()) : "-");
			entry.add(Integer.parseInt(unit.getT()) > 0 ? Integer.parseInt(unit
					.getT()) : "-");
			entry.add(Integer.parseInt(unit.getU()) > 0 ? Integer.parseInt(unit
					.getU()) : "-");
			long harvest = Long.parseLong(unit.getQ());
			if (harvest > 0L) {
				Date harvestDate = new Date(
						(Long.parseLong(unit.getQ()) + cycle) * 1000);
				entry.add(sdf.format(harvestDate));
			} else {
				entry.add("-");
			}
			tableModel.addRow(entry);
		}
		farmTable.setModel(tableModel);
		farmTable.repaint();
	}

	public void refreshAccount(VeryCDUserDomain userDomain,
			FarmDomain farmDomain) {
		this.userDomain = userDomain;
		this.farmDomain = farmDomain;
		refreshAccount();
	}

	private String formatCycle(long cycle) {
		StringBuilder sb = new StringBuilder();
		long min = cycle / 60;
		long hour = 0L;
		if (min > 60) {
			hour = min / 60;
			min = min % 60;
			return sb.append(hour).append("小时").append(min).append("分")
					.toString();
		}
		return sb.append(min).append("分").toString();
	}

	public JTextArea getLoggerArea() {
		return loggerArea;
	}

	public void setLoggerArea(JTextArea loggerArea) {
		this.loggerArea = loggerArea;
	}
}
