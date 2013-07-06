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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.helper.domain.AccountDomain;
import org.helper.domain.CropDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.domain.StoreUnitDomain;
import org.helper.domain.UserPreferenceDomain;
import org.helper.domain.login.UserDomain;
import org.helper.service.ExecuteService;
import org.helper.service.RefreshFarmStep4Service;
import org.helper.service.RefreshStorageService;
import org.helper.service.SellAllService;
import org.helper.service.SellOneService;
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
	private CheckTableModel farmTableModel;
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
	private JButton executeBtn;
	private JComboBox<CropDomain> seedCombo;
	private JButton sellSelectedBtn;
	private MouseAdapter loginEvent;
	private MouseAdapter logoutEvent;
	private Map<String, Timer> scheduleTimerList = new HashMap<String, Timer>();;
	private JButton autoCareBtn;
	private JTable storageTable;
	private DefaultTableModel storageTableModel;
	private JTable accountTable;
	private DefaultTableModel accountTableModel;

	private UserDomain userDomain;
	private FarmDomain farmDomain;

	private Map<String, AccountDomain> accountList = new HashMap<String, AccountDomain>();

	private List<EmOperations> operationList;
	private List<String> checkedFieldIdList;
	private List<Integer> checkedStoreCropList;

	private boolean auto = false;
	private int accountRowId;

	public HelperFrame() {
		this.refreshBtn = new JButton("刷新");
		this.executeBtn = new JButton("执行护理");
		this.executeBtn.setToolTipText("会根据土地状态，判断执行复选框内选中的操作");
		this.autoCareBtn = new JButton("开启自动护理");
		this.autoCareBtn.setEnabled(false);
		this.autoCareBtn.setToolTipText("自动护理将除三害，并自动收/铲/种");
		this.sellSelectedBtn = new JButton("一键卖出选中");
		this.sellSelectedBtn.setToolTipText("卖出仓库内选中的果实，默认全选");

		this.loggerArea = new JTextArea();
		this.loggerArea.setLineWrap(true);
		this.operationList = new ArrayList<EmOperations>();
		this.checkedFieldIdList = new ArrayList<String>();
		this.checkedStoreCropList = new ArrayList<Integer>();

		this.setTitle("Dual Helper - Version 0.0.4");
		this.setSize(1010, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.loginEvent = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loginDialog.showIt();
			}
		};
		this.logoutEvent = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				stopSchedule(FarmDomain.getInstance().getUserId());
				accountList.remove(FarmDomain.getInstance().getUserId());
				accountTableModel.removeRow(accountRowId);
				accountTable.setRowSelectionInterval(
						accountTableModel.getRowCount() - 1,
						accountTableModel.getRowCount() - 1);
				HelperLoggerAppender.writeLog("登出成功");
				// TODO
			}
		};
		HelperLoggerAppender.setInstance(this);

		this.setJMenuBar(constructMenuBar(this));
		this.getContentPane().add(constructAccountsPane(), BorderLayout.WEST);
		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	public void enableAutoCare() {
		this.autoCareBtn.setEnabled(true);
	}

	private JScrollPane constructAccountsPane() {
		JScrollPane accoutsPane = new JScrollPane(constructAccountsTable());
		accoutsPane.setPreferredSize(new Dimension(300, 650));
		return accoutsPane;
	}

	private JTable constructAccountsTable() {
		accountTableModel = new CheckTableModel(new Object[] { "Id", "用户名",
				"域", "状态" }, 0);

		accountTable = new JTable();
		accountTable.setModel(accountTableModel);
		accountTable.setSize(400, 650);

		TableColumnModel tcm = accountTable.getColumnModel();
		tcm.getColumn(0).setWidth(50);// id
		tcm.getColumn(1).setMaxWidth(150);// name
		tcm.getColumn(1).setMaxWidth(100);// domain
		tcm.getColumn(2).setMaxWidth(100);// status

		accountTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					// 右键事件
					JPopupMenu popMenu = null;
					JTable table = (JTable) e.getComponent();
					// 获取鼠标右键选中的行
					int row = table.rowAtPoint(e.getPoint());
					if (row == -1) {
						return;
					}
					// 获取已选中的行
					int[] rows = table.getSelectedRows();
					boolean inSelected = false;
					// 判断当前右键所在行是否已选中
					for (int r : rows) {
						if (row == r) {
							inSelected = true;
							break;
						}
					}
					accountRowId = row;
					// 当前鼠标右键点击所在行不被选中则高亮显示选中行
					if (!inSelected) {
						table.setRowSelectionInterval(accountRowId,
								accountRowId);
					}
					// 生成右键菜单
					String userId = (String) accountTableModel.getValueAt(
							accountRowId, 0);
					userDomain = accountList.get(userId).getUserDomain();
					farmDomain = accountList.get(userId).getFarmDomain();
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					popMenu = makePopup();
					popMenu.show(e.getComponent(), e.getX(), e.getY());
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					// 左键事件
					JTable table = (JTable) e.getComponent();
					accountRowId = table.rowAtPoint(e.getPoint());
					String userId = (String) accountTableModel.getValueAt(
							accountRowId, 0);
					userDomain = accountList.get(userId).getUserDomain();
					farmDomain = accountList.get(userId).getFarmDomain();
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					auto = accountList.get(userId).isAutoCareEnable();
					if (auto) {
						autoCareBtn.setText("停止自动护理");
					} else {
						autoCareBtn.setText("开启自动护理");
					}
					refreshAccount();
				}
			}
		});
		return this.accountTable;
	}

	private JPopupMenu makePopup() {
		JPopupMenu rightClickPop = new JPopupMenu();
		JButton logoutBtn = new JButton("登出");
		logoutBtn.addMouseListener(logoutEvent);
		rightClickPop.add(logoutBtn);
		return rightClickPop;
	}

	private JPanel constructMainPanel() {
		mainBar = new JPanel();
		mainBar.setPreferredSize(new Dimension(680, 650));

		tab = new JTabbedPane();
		tab.addTab("手动操作", constructManuallyPanel());
		tab.addTab("好友列表", constructManuallyPanel2());
		tab.addTab("自动操作", constructManuallyPanel2());
		mainBar.add(tab);
		return mainBar;

	}

	private JScrollPane constructManuallyPanel2() {
		manuallyPanel = new JScrollPane();
		manuallyPanel.setPreferredSize(new Dimension(680, 600));
		return manuallyPanel;
	}

	private JPanel constructManuallyPanel() {
		manuallyWrapper = new JPanel();
		BoxLayout lo = new BoxLayout(manuallyWrapper, BoxLayout.Y_AXIS);
		manuallyWrapper.setLayout(lo);

		// 表格区域
		scrollTablePanel = new JScrollPane(constructFarmFieldTable());
		scrollTablePanel.setPreferredSize(new Dimension(680, 325));

		// 控制面板
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(680, 90));

		JPanel ctrlWrapper1 = new JPanel();// control 面板第一行，checkbox和dropdown
		ctrlWrapper1.setPreferredSize(new Dimension(680, 30));
		List<JCheckBox> controlCheckboxes = constructControlCheckbox();
		for (JCheckBox cb : controlCheckboxes) {
			ctrlWrapper1.add(cb);
		}
		ctrlWrapper1.add(new JLabel("作物"));
		ctrlWrapper1.add(constructSeedCombo());
		controlPanel.add(ctrlWrapper1);

		JPanel ctrlWrapper2 = new JPanel();// control 面板第二行，按钮
		ctrlWrapper2.setPreferredSize(new Dimension(680, 35));
		ctrlWrapper2.add(executeBtn);
		ctrlWrapper2.add(refreshBtn);
		ctrlWrapper2.add(autoCareBtn);
		ctrlWrapper2.add(sellSelectedBtn);
		controlPanel.add(ctrlWrapper2);

		bindRefreshEvent();
		bindExecuteEvent();
		bindAutoEvent();
		bindSellEvent();

		// 日志和仓库
		footerWrapper = new JPanel();
		consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(490, 190));
		JScrollPane loggerScroll = new JScrollPane(loggerArea);
		loggerScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		consoleTab.addTab("操作日志", loggerScroll);

		JScrollPane storageScroll = new JScrollPane(constructStorageTable());
		storageScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		consoleTab.addTab("仓库", storageScroll);

		// 个人信息
		infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(200, 190));
		infoPane.addTab("个人信息", new JScrollPane());
		footerWrapper.add(consoleTab);
		footerWrapper.add(infoPane);

		manuallyWrapper.add(scrollTablePanel);
		manuallyWrapper.add(controlPanel);
		manuallyWrapper.add(footerWrapper);
		return manuallyWrapper;
	}

	private JTable constructStorageTable() {
		storageTableModel = new CheckTableModel(new Object[] { "", "名称", "数量",
				"单价", "总价" }, 0);

		storageTable = new JTable();
		storageTable.setModel(storageTableModel);
		storageTable.getTableHeader().setDefaultRenderer(
				new TableHeaderCheckboxRender(storageTable, false));

		TableColumnModel tcm = storageTable.getColumnModel();
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new CheckboxInTableRenderer());
		tcm.getColumn(0).setMaxWidth(20);

		tcm.getColumn(1).setMaxWidth(100);// NAME
		tcm.getColumn(2).setMaxWidth(100);// number
		tcm.getColumn(3).setMaxWidth(50);// price per
		tcm.getColumn(4).setWidth(50);// price total

		return storageTable;
	}

	@SuppressWarnings("unchecked")
	private JComboBox<CropDomain> constructSeedCombo() {
		seedCombo = new JComboBox<CropDomain>(ShopDomain.getCropList());
		seedCombo.setRenderer(new SeedComboBoxRenderer());
		seedCombo.setSelectedIndex(Integer.parseInt(UserPreferenceDomain
				.getSeedComboIndex()));
		return seedCombo;
	}

	private void bindSellEvent() {
		this.sellSelectedBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -5343809425474458881L;

			@Override
			public void actionPerformed(ActionEvent e) {
				UserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				collectStorage();
				if (checkedStoreCropList.size() == storageTableModel
						.getRowCount()) {
					SellAllService sellAllService = ServiceFactory
							.getService(SellAllService.class);
					try {
						sellAllService.sellAll();
					} catch (IOException | ParseException e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				} else {
					SellOneService sellOneService = ServiceFactory
							.getService(SellOneService.class);
					for (int index : checkedStoreCropList) {
						StoreUnitDomain storeUnit = FarmDomain.getInstance()
								.getStoreUnitDomainByIndex(index);
						try {
							sellOneService.sellOne(storeUnit.getAmount(),
									storeUnit.getcId());
						} catch (IOException | ParseException e1) {
							e1.printStackTrace();
							HelperLoggerAppender.writeLog(e1.getMessage());
						}
					}
				}
				refreshBtn.doClick();
			}
		});
	}

	private void bindAutoEvent() {
		this.autoCareBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				auto = !auto;
				if (auto) {
					autoCareBtn.setText("停止自动护理");
					accountTableModel.setValueAt("已开启自动护理", accountRowId, 3);
					startSchedule();
				} else {
					autoCareBtn.setText("开启自动护理");
					accountTableModel.setValueAt("未开启自动护理", accountRowId, 3);
					stopSchedule(FarmDomain.getInstance().getUserId());
				}
				this.mouseReleased(e);
			}
		});
	}

	private void bindExecuteEvent() {
		this.executeBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1619866963615403453L;

			@Override
			public void actionPerformed(ActionEvent e) {
				UserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				collectOperations();
				collectFields();
				ExecuteService executeService = ServiceFactory
						.getService(ExecuteService.class);
				executeService.execute(operationList, checkedFieldIdList,
						((CropDomain) seedCombo.getSelectedItem()).getcId());
				UserPreferenceDomain.saveToFile(water.isSelected(),
						worm.isSelected(), weed.isSelected(),
						harvest.isSelected(), plow.isSelected(),
						buy.isSelected(), plant.isSelected(),
						String.valueOf(seedCombo.getSelectedIndex()));
				refreshBtn.doClick();
			}
		});
	}

	private void collectStorage() {
		checkedStoreCropList.clear();
		for (int i = 0; i < storageTableModel.getRowCount(); i++) {
			if ((Boolean) storageTableModel.getValueAt(i, 0)) {
				checkedStoreCropList.add(i);
			}
		}
	}

	private void collectFields() {
		checkedFieldIdList.clear();
		for (int i = 0; i < farmTableModel.getRowCount(); i++) {
			if ((Boolean) farmTableModel.getValueAt(i, 0)) {
				checkedFieldIdList.add(String.valueOf(farmTableModel
						.getValueAt(i, 1)));
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
		if (harvest.isSelected()) {
			operationList.add(EmOperations.HARVEST);
		}
		if (plow.isSelected()) {
			operationList.add(EmOperations.PLOW);
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
		water = new JCheckBox("浇水", null, UserPreferenceDomain.isWater());
		worm = new JCheckBox("杀虫", null, UserPreferenceDomain.isWorm());
		weed = new JCheckBox("除草", null, UserPreferenceDomain.isWeed());
		harvest = new JCheckBox("收获", null, UserPreferenceDomain.isHarvest());
		plow = new JCheckBox("翻地", null, UserPreferenceDomain.isPlow());
		buy = new JCheckBox("自动买种", null, UserPreferenceDomain.isBuy());
		plant = new JCheckBox("播种", null, UserPreferenceDomain.isPlant());
		checkboxSet.add(water);
		checkboxSet.add(worm);
		checkboxSet.add(weed);
		checkboxSet.add(harvest);
		checkboxSet.add(plow);
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
				RefreshStorageService storeService = ServiceFactory
						.getService(RefreshStorageService.class);
				UserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				try {
					farmService.refreshFarm();
					storeService.refreshStorage();
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
					HelperLoggerAppender.writeLog(e1.getMessage());
				}
				refreshAccount(UserDomain.getInstance(),
						FarmDomain.getInstance());
			}
		});

	}

	private void startSchedule() {
		TimerTask refreshTask = new TimerTask() {
			public void run() {
				ExecuteService executeService = ServiceFactory
						.getService(ExecuteService.class);
				refreshBtn.doClick();
				UserDomain.setInstance(userDomain);
				FarmDomain.setInstance(farmDomain);
				executeService.executeAll(((CropDomain) seedCombo
						.getSelectedItem()).getcId());
			}
		};

		TimerTask operationTask = new TimerTask() {
			public void run() {
				// TODO
			}
		};

		Timer scheduleTimer = new Timer(true);
		long random = 120000L + (long) (10000 * (new Random()).nextFloat());
		scheduleTimer.schedule(refreshTask, random, random);
		scheduleTimerList.put(farmDomain.getUserId(), scheduleTimer);
		accountList.get(farmDomain.getUserId()).setAutoCareEnable(true);
		HelperLoggerAppender.writeLog("自动护理开启，间隔时间" + random + "毫秒");
	}

	private void stopSchedule(String userId) {
		if (null != userId && null != scheduleTimerList.get(userId)) {
			scheduleTimerList.get(userId).cancel();
			scheduleTimerList.remove(userId);
			HelperLoggerAppender.writeLog("自动护理关闭");
		}
	}

	private JTable constructFarmFieldTable() {
		farmTableModel = new CheckTableModel(new Object[] { "", "土地", "名称",
				"阶段  当前季/总季", "(花期)第一季/每季", "产量", "杂草", "虫害", "干旱", "收获时间" }, 0);

		farmTable = new JTable();
		farmTable.setModel(farmTableModel);
		farmTable.getTableHeader().setDefaultRenderer(
				new TableHeaderCheckboxRender(farmTable, true));

		TableColumnModel tcm = farmTable.getColumnModel();
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new CheckboxInTableRenderer());
		tcm.getColumn(0).setMaxWidth(20);

		tcm.getColumn(1).setMaxWidth(50);

		tcm.getColumn(2).setMaxWidth(100);// NAME
		tcm.getColumn(3).setWidth(150);// status

		tcm.getColumn(4).setWidth(100);
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
		menuLogin.addMouseListener(loginEvent);

		JMenu menuShop = new JMenu("商店S");
		menuShop.setMnemonic(KeyEvent.VK_S);

		menuBar.add(menuLogin);
		menuBar.add(menuShop);

		return menuBar;
	}

	private void refreshInfoPane() {
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
	}

	private void refreshFarmTable() {
		farmTableModel.setRowCount(0);
		List<FieldUnitDomain> fieldList = FarmDomain.getInstance()
				.getFieldList();
		int i = 0;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (FieldUnitDomain unit : fieldList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(true));
			entry.add(i++);
			entry.add(ShopDomain.getCropName(unit.getA()).replace("种子", ""));
			StringBuilder status = new StringBuilder(
					EmCropStatus.getStatusName(unit.getB()));
			status.append(" ").append(unit.getJ()).append("/")
					.append(ShopDomain.getSeasonNuber(unit.getA()));
			entry.add(status);// 阶段 当前季/总季
			int season = Integer
					.parseInt(ShopDomain.getSeasonNuber(unit.getA()));
			long cycle = Long.parseLong(ShopDomain.getGrowthCycle(unit.getA()));
			StringBuilder cycleTime = new StringBuilder();
			if (season == 0) {
				cycleTime.append("-");
			} else if (season > 1) {
				long reMaturingCycle = Long.parseLong(ShopDomain
						.getReMaturingTime(unit.getA()));
				cycleTime.append(formatCycle(cycle)).append(" / ")
						.append(formatCycle(reMaturingCycle));
			} else {
				cycleTime.append(formatCycle(cycle)).append(" / ").append("-");
			}
			entry.add(cycleTime);// 花期(第一季/每季)
			entry.add(unit.getK());
			entry.add(Integer.parseInt(unit.getF()) > 0 ? Integer.parseInt(unit
					.getF()) : "-");// "杂草"
			entry.add(Integer.parseInt(unit.getG()) > 0 ? Integer.parseInt(unit
					.getG()) : "-");// "虫害"
			entry.add(Integer.parseInt(unit.getH()) == 0 ? "旱" : "-");// "干旱"
			long harvest = Long.parseLong(unit.getQ());
			if (harvest > 0L) {
				Date harvestDate = new Date(
						(Long.parseLong(unit.getQ()) + cycle) * 1000);
				entry.add(sdf.format(harvestDate));
			} else {
				entry.add("-");
			}
			farmTableModel.addRow(entry);
		}
		farmTable.setModel(farmTableModel);
		farmTable.repaint();
	}

	private void refreshStorage() {
		storageTableModel.setRowCount(0);
		List<StoreUnitDomain> storeList = FarmDomain.getInstance()
				.getStoreList();
		for (StoreUnitDomain unit : storeList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(false));
			entry.add(unit.getcName());
			entry.add(unit.getAmount());
			entry.add(unit.getPrice());
			int total = Integer.parseInt(unit.getAmount())
					* Integer.parseInt(unit.getPrice());
			entry.add(total);
			storageTableModel.addRow(entry);
		}
		storageTable.setModel(storageTableModel);
		storageTable.repaint();
	}

	private void refreshAccount() {
		refreshInfoPane();
		refreshFarmTable();
		refreshStorage();
		HelperLoggerAppender.writeLog("刷新状态成功");
	}

	public void refreshAccount(UserDomain userDomain, FarmDomain farmDomain) {
		this.userDomain = userDomain;
		this.farmDomain = farmDomain;
		refreshAccount();
	}

	public void addAccountToAccountList(UserDomain userDomain,
			FarmDomain farmDomain) {
		AccountDomain account = new AccountDomain();
		account.setFarmDomain(farmDomain);
		account.setUserDomain(userDomain);
		account.setAutoCareEnable(false);
		this.accountList.put(FarmDomain.getInstance().getUserId(), account);
		Vector<Object> accountRow = new Vector<Object>(); // "Id", "用户名", "域",
															// "状态"
		accountRow.add(FarmDomain.getInstance().getUserId());
		accountRow.add(FarmDomain.getInstance().getUserName());
		if (UserDomain.getInstance().isVeryCD()) {
			accountRow.add("通过VeryCD网关登录");
		} else {
			accountRow.add("通过职内网关登录");
		}
		accountRow.add("未开启自动护理");
		this.accountTableModel.addRow(accountRow);
		this.accountTable.setRowSelectionInterval(
				accountTableModel.getRowCount() - 1,
				accountTableModel.getRowCount() - 1);
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

	public Map<String, AccountDomain> getAccountList() {
		return accountList;
	}
}
