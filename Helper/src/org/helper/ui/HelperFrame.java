package org.helper.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.helper.domain.AccountDomain;
import org.helper.domain.BaseFarmDomain;
import org.helper.domain.CropDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.PackageUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.domain.StoreUnitDomain;
import org.helper.domain.UserPreferenceDomain;
import org.helper.domain.UserPreferenceUnit;
import org.helper.domain.login.UserDomain;
import org.helper.enums.EmCropStatus;
import org.helper.enums.EmFriendOperations;
import org.helper.enums.EmOperations;
import org.helper.enums.EmPackageType;
import org.helper.service.ExecuteService;
import org.helper.service.HelpFriendService;
import org.helper.service.PackageService;
import org.helper.service.RefreshFarmStep4Service;
import org.helper.service.RefreshFriendFarmService;
import org.helper.service.RefreshFriendService;
import org.helper.service.RefreshStorageService;
import org.helper.service.SellAllService;
import org.helper.service.SellOneService;
import org.helper.service.ServiceFactory;
import org.helper.util.HelperLoggerAppender;
import org.helper.util.ResourceLoader;
import org.json.simple.parser.ParseException;

/**
 * @author luxixu
 * 
 */
public class HelperFrame extends JDialog {
	private static final long serialVersionUID = -6344590535790274762L;
	private JTable farmTable;
	private JMenu menuLogin;
	private JPanel mainBar;
	private JPanel manuallyWrapper;
	private JScrollPane scrollTablePanel;
	private JPanel controlPanel;
	private JPanel footerWrapper;
	private JTabbedPane consoleTab;
	private JTabbedPane infoPane;
	private CheckTableModel farmTableModel;
	private LoginDialog loginDialog;
	private JPopupMenu logoutPopMenu;
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
	private JButton autoCareBtn;
	private JTable storageTable;
	private DefaultTableModel storageTableModel;
	private JTable packageTable;
	private DefaultTableModel packageTableModel;
	private JTable accountTable;
	private DefaultTableModel accountTableModel;
	private JTable friendTable;
	private DefaultTableModel friendTableModel;
	private JButton refreshFriends;
	private TitledBorder fieldTitle;

	private JCheckBox waterFriend;
	private JCheckBox wormFriend;
	private JCheckBox weedFriend;
	private JCheckBox stealFriend;
	private JButton refreshFriendField;
	private JButton helpFriend;

	private JPanel ctrlWrapper1;
	private JPanel ctrlWrapper2;
	private JPanel ctrlWrapper3;
	private JPanel ctrlWrapper4;

	private UserDomain userDomain;
	private FarmDomain farmDomain;

	private Map<String, AccountDomain> accountList = new HashMap<String, AccountDomain>();

	private List<EmOperations> operationList;
	private List<String> checkedFieldIdList;
	private List<Integer> checkedStoreCropList;
	private List<EmFriendOperations> friendOperationList;

	private boolean auto = false;
	private int accountRowId;
	private String friendId;

	public HelperFrame() {
		this.refreshBtn = new JButton("刷新");
		this.executeBtn = new JButton("执行护理");
		this.executeBtn.setToolTipText("会根据土地状态，判断执行复选框内选中的操作");
		this.autoCareBtn = new JButton("开启自动护理");
		this.autoCareBtn.setEnabled(false);
		this.autoCareBtn.setBackground(Color.WHITE);
		this.autoCareBtn.setToolTipText("智能自动护理将除三害，并自动收/铲/种");
		this.sellSelectedBtn = new JButton("一键卖出选中");
		this.sellSelectedBtn.setToolTipText("卖出仓库内选中的果实");
		this.refreshFriends = new JButton("刷新好友列表");
		this.refreshFriends.setPreferredSize(new Dimension(50, 20));
		this.fieldTitle = new TitledBorder("土地信息");

		this.refreshFriendField = new JButton("刷新好友土地");
		this.helpFriend = new JButton("护理好友土地");

		this.loggerArea = new JTextArea();
		this.loggerArea.setLineWrap(true);
		this.operationList = new ArrayList<EmOperations>();
		this.checkedFieldIdList = new ArrayList<String>();
		this.checkedStoreCropList = new ArrayList<Integer>();
		this.friendOperationList = new ArrayList<EmFriendOperations>();

		this.setTitle("Farmer Helper - Version 0.1.4 :: designed by Chloe's studio");
		this.setSize(1010, 700);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		try {
			this.setIconImage(ImageIO.read(ResourceLoader.load("icon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.setResizable(true);
		this.loginEvent = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loginDialog.showIt();
				fillExecutionsWithConfig();
			}
		};
		this.logoutEvent = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (null != logoutPopMenu) {
					logoutPopMenu.setVisible(false);
					logoutPopMenu = null;
				}
				stopSchedule();
				HelperLoggerAppender.writeLog("登出成功");
				accountList.remove(FarmDomain.getInstance().getUserId());
				accountTableModel.removeRow(accountRowId);
				if (accountRowId > 0) {
					accountRowId--;
				}
				if (accountTableModel.getRowCount() > 0) {
					accountTable.setRowSelectionInterval(accountTableModel.getRowCount() - 1, accountTableModel.getRowCount() - 1);
					refreshSelectedAccount((String) accountTableModel.getValueAt(accountTableModel.getRowCount() - 1, 0));
				} else {
					farmTableModel.setRowCount(0);
					friendTableModel.setRowCount(0);
					storageTableModel.setRowCount(0);
					packageTableModel.setRowCount(0);
					((JScrollPane) infoPane.getComponentAt(0)).setViewportView(new JPanel());
					auto = false;
					autoCareBtn.setEnabled(false);
					autoCareBtn.setBackground(Color.WHITE);
				}
			}
		};
		HelperLoggerAppender.setInstance(this);

		this.setJMenuBar(constructMenuBar(this));
		this.getContentPane().add(constructAccountsPane(), BorderLayout.WEST);
		this.getContentPane().add(constructMainPanel(), BorderLayout.CENTER);
	}

	public void enableAutoCare() {
		this.auto = false;
		this.autoCareBtn.setEnabled(true);
		this.autoCareBtn.setText("开启自动护理");
		this.autoCareBtn.setBackground(new Color(186, 209, 145));
	}

	private JPanel constructAccountsPane() {
		JPanel westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(300, 650));
		BoxLayout lo = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
		westPanel.setLayout(lo);
		JScrollPane accoutsScrollPane = new JScrollPane(constructAccountsTable());
		accoutsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		accoutsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		accoutsScrollPane.setPreferredSize(new Dimension(295, 300));
		accoutsScrollPane.setBorder(new TitledBorder("已登录帐号"));
		westPanel.add(accoutsScrollPane);

		westPanel.add(refreshFriends);

		JScrollPane friendsScrollPane = new JScrollPane(constructFriendsTable());
		friendsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		friendsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		friendsScrollPane.setPreferredSize(new Dimension(295, 300));
		friendsScrollPane.setBorder(new TitledBorder("好友帐号"));
		westPanel.add(friendsScrollPane);
		return westPanel;
	}

	private JTable constructFriendsTable() {
		friendTableModel = new CheckTableModel(new Object[] { "Id", "用户名", "经验", "金币" }, 0);

		friendTable = new JTable();
		friendTable.setModel(friendTableModel);
		friendTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		friendTable.setSize(new Dimension(400, 350));

		TableColumnModel tcm = friendTable.getColumnModel();
		tcm.getColumn(0).setWidth(150);// id
		tcm.getColumn(1).setWidth(150);// name
		tcm.getColumn(1).setWidth(150);// exp
		tcm.getColumn(2).setWidth(150);// money

		friendTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					// 左键事件
					ctrlWrapper1.setVisible(false);
					ctrlWrapper2.setVisible(false);
					ctrlWrapper3.setVisible(true);
					ctrlWrapper4.setVisible(true);

					String userId = (String) accountTableModel.getValueAt(accountRowId, 0);
					userDomain = accountList.get(userId).getUserDomain();
					farmDomain = accountList.get(userId).getFarmDomain();
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);

					JTable table = (JTable) e.getComponent();
					friendId = (String) friendTableModel.getValueAt(table.rowAtPoint(e.getPoint()), 0);
					refreshFriendField();
				}
			}
		});
		return this.friendTable;
	}

	private void refreshFriendField() {
		RefreshFriendFarmService refreshFriendFarmService = ServiceFactory.getService(RefreshFriendFarmService.class);
		try {
			refreshFriendFarmService.refreshFriendFarm(friendId);
			refreshFarmTable(FarmDomain.getInstance().getFriendById(friendId).getFieldList());
		} catch (org.apache.http.ParseException | IOException | ParseException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
		}
		StringBuilder sb = new StringBuilder("[").append(FarmDomain.getInstance().getUserName()).append("]").append("的好友").append("[")
				.append(FarmDomain.getInstance().getFriendById(friendId).getUserName()).append("]").append("土地信息");
		fieldTitle.setTitle(sb.toString());
		scrollTablePanel.repaint();
		HelperLoggerAppender.writeLog("刷新好友[" + FarmDomain.getInstance().getFriendById(friendId).getUserName() + "]的土地信息成功");
	}

	private JTable constructAccountsTable() {
		accountTableModel = new CheckTableModel(new Object[] { "Id", "用户名", "域", "状态" }, 0);

		accountTable = new JTable();
		accountTable.setModel(accountTableModel);
		accountTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		accountTable.setSize(new Dimension(400, 350));

		TableColumnModel tcm = accountTable.getColumnModel();
		tcm.getColumn(0).setWidth(150);// id
		tcm.getColumn(1).setWidth(150);// name
		tcm.getColumn(1).setWidth(150);// domain
		tcm.getColumn(2).setWidth(150);// status

		accountTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ctrlWrapper1.setVisible(true);
				ctrlWrapper2.setVisible(true);
				ctrlWrapper3.setVisible(false);
				ctrlWrapper4.setVisible(false);
				if (SwingUtilities.isRightMouseButton(e)) {
					// 右键事件
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
						table.setRowSelectionInterval(accountRowId, accountRowId);
					}
					// 生成右键菜单
					String userId = (String) accountTableModel.getValueAt(accountRowId, 0);
					userDomain = accountList.get(userId).getUserDomain();
					farmDomain = accountList.get(userId).getFarmDomain();
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					logoutPopMenu = makePopup();
					logoutPopMenu.show(e.getComponent(), e.getX(), e.getY());
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					// 左键事件
					JTable table = (JTable) e.getComponent();
					accountRowId = table.rowAtPoint(e.getPoint());
					refreshSelectedAccount((String) accountTableModel.getValueAt(accountRowId, 0));
				}
			}
		});
		return this.accountTable;
	}

	private void refreshSelectedAccount(String userId) {
		userDomain = accountList.get(userId).getUserDomain();
		farmDomain = accountList.get(userId).getFarmDomain();
		UserDomain.setInstance(userDomain);
		FarmDomain.setInstance(farmDomain);
		auto = accountList.get(userId).isAutoCareEnable();
		if (auto) {
			autoCareBtn.setText("停止自动护理");
			autoCareBtn.setBackground(new Color(240, 117, 82));
		} else {
			autoCareBtn.setText("开启自动护理");
			autoCareBtn.setBackground(new Color(186, 209, 145));
		}
		refreshAccount();
		refreshFriends();
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
		mainBar.setPreferredSize(new Dimension(700, 660));

//		mainBar.setBorder(fieldTitle);
		mainBar.add(constructManuallyPanel());
		return mainBar;

	}

	private JPanel constructManuallyPanel() {
		manuallyWrapper = new JPanel();
		BoxLayout lo = new BoxLayout(manuallyWrapper, BoxLayout.Y_AXIS);
		manuallyWrapper.setPreferredSize(new Dimension(690, 650));
		manuallyWrapper.setLayout(lo);

		// 表格区域
		scrollTablePanel = new JScrollPane(constructFarmFieldTable());
		scrollTablePanel.setPreferredSize(new Dimension(690, 325));

		// 控制面板
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(690, 90));

		ctrlWrapper1 = new JPanel();// control 面板第一行，checkbox和dropdown
		ctrlWrapper1.setPreferredSize(new Dimension(690, 35));
		constructControlCheckbox();
		ctrlWrapper1.add(new JLabel("作物"));
		ctrlWrapper1.add(constructSeedCombo());
		controlPanel.add(ctrlWrapper1);

		ctrlWrapper2 = new JPanel();// control 面板第二行，按钮
		ctrlWrapper2.setPreferredSize(new Dimension(690, 35));
		ctrlWrapper2.add(executeBtn);
		ctrlWrapper2.add(refreshBtn);
		ctrlWrapper2.add(autoCareBtn);
		ctrlWrapper2.add(sellSelectedBtn);
		controlPanel.add(ctrlWrapper2);

		ctrlWrapper3 = new JPanel();// 好友操作面板，隐藏
		ctrlWrapper3.setPreferredSize(new Dimension(690, 35));
		ctrlWrapper3.setVisible(false);
		constructFriendControlCheckbox();
		controlPanel.add(ctrlWrapper3);

		ctrlWrapper4 = new JPanel();// 好友操作面板，隐藏
		ctrlWrapper4.setPreferredSize(new Dimension(690, 35));
		ctrlWrapper4.setVisible(false);
		ctrlWrapper4.add(refreshFriendField);
		ctrlWrapper4.add(helpFriend);
		controlPanel.add(ctrlWrapper4);

		bindRefreshEvent();
		bindExecuteEvent();
		bindAutoEvent();
		bindSellEvent();
		bindRefreshFriendEvent();
		bindRefreshFriendFieldEvent();
		bindHelpFriendEvent();

		// 日志
		footerWrapper = new JPanel();
		consoleTab = new JTabbedPane();
		consoleTab.setPreferredSize(new Dimension(480, 190));
		JScrollPane loggerScroll = new JScrollPane(loggerArea);
		loggerScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		consoleTab.addTab("操作日志", loggerScroll);

		// 仓库
		JScrollPane storageScroll = new JScrollPane(constructStorageTable());
		storageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		consoleTab.addTab("仓库", storageScroll);

		// 背包
		JScrollPane packageScroll = new JScrollPane(constructPackageTable());
		packageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		consoleTab.addTab("背包", packageScroll);

		// 个人信息
		infoPane = new JTabbedPane();
		infoPane.setPreferredSize(new Dimension(200, 190));
		infoPane.addTab("个人信息", new JScrollPane());
		footerWrapper.add(consoleTab);
		footerWrapper.add(infoPane);

		scrollTablePanel.setBorder(fieldTitle);
		manuallyWrapper.add(scrollTablePanel);
		manuallyWrapper.add(controlPanel);
		manuallyWrapper.add(footerWrapper);
		return manuallyWrapper;
	}

	private JTable constructPackageTable() {
		packageTableModel = new CheckTableModel(new Object[] { "名称", "数量" }, 0);

		packageTable = new JTable();
		packageTable.setModel(packageTableModel);
		packageTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int row = packageTable.rowAtPoint(e.getPoint());
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					@SuppressWarnings("unchecked")
					PackageUnitDomain unit = (PackageUnitDomain) ((Vector<PackageUnitDomain>) packageTableModel.getDataVector().elementAt(row))
							.elementAt(0);
					if (unit.getType() == EmPackageType.SEED) {
						for (int i = 0; i < seedCombo.getItemCount(); i++) {
							if (seedCombo.getItemAt(i).getcId().equals(unit.getcId())) {
								seedCombo.setSelectedIndex(i);
								break;
							}
						}
					}
				}
				this.mouseReleased(e);
			}
		});

		TableColumnModel tcm = packageTable.getColumnModel();
		PackageTableRenderer renderer = new PackageTableRenderer();
		tcm.getColumn(0).setCellRenderer(renderer);
		tcm.getColumn(0).setMaxWidth(100);// NAME
		tcm.getColumn(1).setCellRenderer(renderer);
		tcm.getColumn(1).setMaxWidth(100);

		return packageTable;
	}

	private JTable constructStorageTable() {
		storageTableModel = new CheckTableModel(new Object[] { "", "名称", "数量", "单价", "总价" }, 0);

		storageTable = new JTable();
		storageTable.setModel(storageTableModel);
		storageTable.getTableHeader().setDefaultRenderer(new TableHeaderCheckboxRender(storageTable, false));

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
		return seedCombo;
	}

	private void bindSellEvent() {
		this.sellSelectedBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -5343809425474458881L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						collectStorage();
						if (checkedStoreCropList.size() == storageTableModel.getRowCount()) {
							SellAllService sellAllService = ServiceFactory.getService(SellAllService.class);
							sellAllService.sellAll();
						} else {
							SellOneService sellOneService = ServiceFactory.getService(SellOneService.class);
							for (int index : checkedStoreCropList) {
								StoreUnitDomain storeUnit = FarmDomain.getInstance().getStoreUnitDomainByIndex(index);
								sellOneService.sellOne(storeUnit.getAmount(), storeUnit.getcId());
							}
						}
						refreshBtn.doClick();
					} catch (Throwable e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				}
			}
		});
	}

	private void bindAutoEvent() {
		this.autoCareBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (autoCareBtn.isEnabled()) {
					auto = !auto;
					if (auto) {
						autoCareBtn.setText("停止自动护理");
						autoCareBtn.setBackground(new Color(240, 117, 82));
						accountTableModel.setValueAt("已开启自动护理", accountRowId, 3);
						startSchedule();
					} else {
						autoCareBtn.setText("开启自动护理");
						autoCareBtn.setBackground(new Color(186, 209, 145));
						accountTableModel.setValueAt("未开启自动护理", accountRowId, 3);
						stopSchedule();
					}
				}
				this.mouseReleased(e);
			}
		});
	}

	private void bindHelpFriendEvent() {
		this.helpFriend.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 2411859637994675758L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						collectFriendOperations();
						collectFields();
						HelpFriendService helpFriendService = ServiceFactory.getService(HelpFriendService.class);
						helpFriendService.help(friendOperationList, checkedFieldIdList, friendId);
						saveFriendConfig();
						refreshFriendField.doClick();
						RefreshStorageService storeService = ServiceFactory.getService(RefreshStorageService.class);
						storeService.refreshStorage();
						refreshStorage();
					} catch (Throwable e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				}
			}
		});
	}

	private void bindExecuteEvent() {
		this.executeBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1619866963615403453L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						collectOperations();
						collectFields();
						ExecuteService executeService = ServiceFactory.getService(ExecuteService.class);
						executeService.execute(operationList, checkedFieldIdList, ((CropDomain) seedCombo.getSelectedItem()).getcId());
						saveExecutionsToConfig();
						refreshBtn.doClick();
					} catch (Throwable e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				}
			}
		});
	}

	private void saveExecutionsToConfig() {
		UserPreferenceUnit userConfig = UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId());
		userConfig.setWater(this.water.isSelected());
		userConfig.setWorm(this.worm.isSelected());
		userConfig.setWeed(this.weed.isSelected());
		userConfig.setPlow(this.plow.isSelected());
		userConfig.setHarvest(this.harvest.isSelected());
		userConfig.setBuy(this.buy.isSelected());
		userConfig.setPlant(this.plant.isSelected());
		userConfig.setSeedComboIndex(this.seedCombo.getSelectedIndex());
	}

	private void saveFriendConfig() {
		UserPreferenceUnit userConfig = UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId());
		userConfig.setWaterFriend(this.waterFriend.isSelected());
		userConfig.setWormFriend(this.wormFriend.isSelected());
		userConfig.setWeedFriend(this.weedFriend.isSelected());
		userConfig.setStealFriend(this.stealFriend.isSelected());
	}

	private void fillExecutionsWithConfig() {
		UserPreferenceUnit userConfig = UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId());
		if (null != userConfig) {
			this.water.setSelected(userConfig.isWater());
			this.worm.setSelected(userConfig.isWorm());
			this.weed.setSelected(userConfig.isWeed());
			this.plow.setSelected(userConfig.isPlow());
			this.harvest.setSelected(userConfig.isHarvest());
			this.buy.setSelected(userConfig.isBuy());
			this.plant.setSelected(userConfig.isPlant());
			this.seedCombo.setSelectedIndex(userConfig.getSeedComboIndex());
			// this.seedCombo.repaint();
			this.waterFriend.setSelected(userConfig.isWaterFriend());
			this.weedFriend.setSelected(userConfig.isWeedFriend());
			this.wormFriend.setSelected(userConfig.isWormFriend());
			this.stealFriend.setSelected(userConfig.isStealFriend());
		}
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
				checkedFieldIdList.add(String.valueOf(farmTableModel.getValueAt(i, 1)));
			}
		}
	}

	private void collectFriendOperations() {
		friendOperationList.clear();
		if (waterFriend.isSelected()) {
			friendOperationList.add(EmFriendOperations.WATER);
		}
		if (weedFriend.isSelected()) {
			friendOperationList.add(EmFriendOperations.WEED);
		}
		if (wormFriend.isSelected()) {
			friendOperationList.add(EmFriendOperations.WORM);
		}
		if (stealFriend.isSelected()) {
			friendOperationList.add(EmFriendOperations.STEAL);
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

	private void constructFriendControlCheckbox() {
		waterFriend = new JCheckBox("浇水", null, true);
		wormFriend = new JCheckBox("杀虫", null, true);
		weedFriend = new JCheckBox("除草", null, true);
		stealFriend = new JCheckBox("偷花", null, false);
		ctrlWrapper3.add(waterFriend);
		ctrlWrapper3.add(wormFriend);
		ctrlWrapper3.add(weedFriend);
		ctrlWrapper3.add(stealFriend);
	}

	private void constructControlCheckbox() {
		water = new JCheckBox("浇水", null, true);
		worm = new JCheckBox("杀虫", null, true);
		weed = new JCheckBox("除草", null, true);
		harvest = new JCheckBox("收获", null, true);
		plow = new JCheckBox("翻地", null, true);
		buy = new JCheckBox("自动买种", null, false);
		plant = new JCheckBox("播种", null, true);
		ctrlWrapper1.add(water);
		ctrlWrapper1.add(worm);
		ctrlWrapper1.add(weed);
		ctrlWrapper1.add(harvest);
		ctrlWrapper1.add(plow);
		ctrlWrapper1.add(buy);
		ctrlWrapper1.add(plant);
	}

	private void bindRefreshFriendFieldEvent() {
		this.refreshFriendField.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -8093968179983914047L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						refreshFriendField();
					} catch (Throwable e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				}
			}
		});
	}

	private void bindRefreshFriendEvent() {
		this.refreshFriends.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 3121303421690335101L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					RefreshFriendService friendService = ServiceFactory.getService(RefreshFriendService.class);
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						friendService.refreshFriend();
						refreshFriends(UserDomain.getInstance(), FarmDomain.getInstance());
					} catch (org.apache.http.ParseException | IOException | ParseException e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
				}
			}
		});
	}

	private void bindRefreshEvent() {
		this.refreshBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 3231109692374056419L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userDomain != null && farmDomain != null) {
					RefreshFarmStep4Service farmService = ServiceFactory.getService(RefreshFarmStep4Service.class);
					RefreshStorageService storeService = ServiceFactory.getService(RefreshStorageService.class);
					PackageService packageService = ServiceFactory.getService(PackageService.class);
					UserDomain.setInstance(userDomain);
					FarmDomain.setInstance(farmDomain);
					try {
						farmService.refreshFarm();
						storeService.refreshStorage();
						packageService.refreshPackageInfo();
					} catch (IOException | ParseException e1) {
						e1.printStackTrace();
						HelperLoggerAppender.writeLog(e1.getMessage());
					}
					refreshAccount(UserDomain.getInstance(), FarmDomain.getInstance());
				}
			}
		});

	}

	private void startSchedule() {
		UserDomain.setInstance(userDomain);
		FarmDomain.setInstance(farmDomain);
		accountList.get(farmDomain.getUserId()).setAutoCareEnable(true);
		HelperLoggerAppender.writeLog("自动护理开启，随机刷新间隔" + "5分钟，将种植 "
				+ ((CropDomain) this.seedCombo.getItemAt(UserPreferenceDomain.getSeedIndexById(farmDomain.getUserId()))).getcName());
	}

	private void stopSchedule() {
		UserDomain.setInstance(userDomain);
		FarmDomain.setInstance(farmDomain);
		accountList.get(farmDomain.getUserId()).setAutoCareEnable(false);
		HelperLoggerAppender.writeLog("自动护理关闭");
	}

	public void createAutoTask(final String userId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				while (true) {
					try {
						AccountDomain autoOnAccount = accountList.get(userId);
						if (autoOnAccount == null) {
							// 帐号已被登出
							break;
						}
						int oldHash = autoOnAccount.hashCode();
						long nearestHarvest = countSleepTime(autoOnAccount);
						if (nearestHarvest > 300000L && count < 300) {
							// 距离下一次执行时间超过5分钟，沉默1秒
							Thread.sleep(1000L);
							count++;
						} else if (count >= 300) {
							count = 0;
							autoOnAccount = accountList.get(userId);
							if (autoOnAccount == null || oldHash != autoOnAccount.hashCode()) {
								// 原帐号已被登出
								break;
							}
							if (autoOnAccount.isAutoCareEnable()) {
								UserDomain.setInstance(autoOnAccount.getUserDomain());
								FarmDomain.setInstance(autoOnAccount.getFarmDomain());
								RefreshFarmStep4Service farmService = ServiceFactory.getService(RefreshFarmStep4Service.class);
								try {
									farmService.refreshFarm();
									HelperLoggerAppender.writeLog("自动护理: 刷新状态成功");
								} catch (IOException | ParseException e) {
									e.printStackTrace();
									HelperLoggerAppender.writeLog(e.getMessage());
								}
							}
						} else {
							Thread.sleep(nearestHarvest);
							autoOnAccount = accountList.get(userId);
							if (autoOnAccount == null || oldHash != autoOnAccount.hashCode()) {
								// 原帐号已被登出
								break;
							}
							if (autoOnAccount.isAutoCareEnable()) {
								UserDomain.setInstance(autoOnAccount.getUserDomain());
								FarmDomain.setInstance(autoOnAccount.getFarmDomain());
								RefreshFarmStep4Service farmService = ServiceFactory.getService(RefreshFarmStep4Service.class);
								try {
									farmService.refreshFarm();
									HelperLoggerAppender.writeLog("自动护理: 刷新状态成功");
								} catch (IOException | ParseException e) {
									e.printStackTrace();
									HelperLoggerAppender.writeLog(e.getMessage());
								}
								ExecuteService executeService = ServiceFactory.getService(ExecuteService.class);
								executeService.executeAll(UserPreferenceDomain.getSeedIndexById(farmDomain.getUserId()));
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						HelperLoggerAppender.writeLog(e.getMessage());
					}
				}
			}
		}).start();
	}

	private static long countSleepTime(AccountDomain autoOnAccount) {
		List<FieldUnitDomain> fieldList = autoOnAccount.getFarmDomain().getFieldList();
		long nearestHarvest = 300000L;
		for (FieldUnitDomain unit : fieldList) {
			long cycle = Long.parseLong(ShopDomain.getGrowthCycle(unit.getA()));
			long harvest = (Long.parseLong(unit.getQ()) + cycle) * 1000L - System.currentTimeMillis();
			if (harvest < nearestHarvest) {
				nearestHarvest = harvest;
			}
			if (Integer.parseInt(unit.getF()) > 0) {
				nearestHarvest = 1000L;// "杂草"
				break;
			}
			if (Integer.parseInt(unit.getG()) > 0) {
				nearestHarvest = 1000L;// "虫害"
				break;
			}
			if (Integer.parseInt(unit.getH()) == 0) {
				nearestHarvest = 1000L;// "干旱"
				break;
			}
		}
		if (nearestHarvest >= 300000L) {
			return 300000L + (long) (1000L * (new Random()).nextFloat());
		} else if (nearestHarvest <= 1000L) {
			return 1000L + (long) (1000L * (new Random()).nextFloat());
		} else {
			return nearestHarvest;
		}
	}

	private JTable constructFarmFieldTable() {
		farmTableModel = new CheckTableModel(new Object[] { "", "土地", "名称", "阶段  当前季/总季", "(花期)第一季/每季", "剩余/产量", "杂草", "虫害", "干旱", "收获时间" }, 0);

		farmTable = new JTable();
		farmTable.setModel(farmTableModel);
		farmTable.getTableHeader().setDefaultRenderer(new TableHeaderCheckboxRender(farmTable, true));

		TableColumnModel tcm = farmTable.getColumnModel();
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new CheckboxInTableRenderer());
		tcm.getColumn(0).setMaxWidth(20);

		tcm.getColumn(1).setMaxWidth(40);

		tcm.getColumn(2).setMaxWidth(100);// NAME
		tcm.getColumn(3).setWidth(150);// status

		tcm.getColumn(4).setWidth(100);
		tcm.getColumn(5).setMaxWidth(60);
		tcm.getColumn(6).setMaxWidth(50);
		tcm.getColumn(7).setMaxWidth(50);
		tcm.getColumn(8).setMaxWidth(50);
		tcm.getColumn(9).setWidth(55);

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

		JMenu menuExit = new JMenu("退出E");
		menuExit.setMnemonic(KeyEvent.VK_E);
		menuExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					UserPreferenceDomain.saveToFile();
					System.exit(0);
				}
			}
		});

		menuBar.add(menuLogin);
		menuBar.add(menuShop);
		menuBar.add(menuExit);
		return menuBar;
	}

	private void refreshInfoPane() {
		JScrollPane userInfoPane = (JScrollPane) infoPane.getComponentAt(0);
		JPanel tempPanel = new JPanel();
		BoxLayout lo = new BoxLayout(tempPanel, BoxLayout.Y_AXIS);
		tempPanel.setLayout(lo);
		tempPanel.add(new JLabel("用户名： " + FarmDomain.getInstance().getUserName(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("ID： " + FarmDomain.getInstance().getUserId(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("金币： " + FarmDomain.getInstance().getMoney(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("经验： " + FarmDomain.getInstance().getExp(), SwingConstants.LEFT));
		tempPanel.add(new JLabel("魅力值： " + FarmDomain.getInstance().getCharm(), SwingConstants.LEFT));
		userInfoPane.setViewportView(tempPanel);
	}

	private void refreshFarmTable(List<FieldUnitDomain> fieldList) {
		farmTableModel.setRowCount(0);
		int i = 0;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (FieldUnitDomain unit : fieldList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(true));
			entry.add(i++);
			entry.add(ShopDomain.getCropName(unit.getA()).replace("种子", ""));
			StringBuilder status = new StringBuilder(EmCropStatus.getStatusName(unit.getB()));
			status.append(" ").append(unit.getJ()).append("/").append(ShopDomain.getSeasonNuber(unit.getA()));
			entry.add(status);// 阶段 当前季/总季
			int season = Integer.parseInt(ShopDomain.getSeasonNuber(unit.getA()));
			long cycle = Long.parseLong(ShopDomain.getGrowthCycle(unit.getA()));
			StringBuilder cycleTime = new StringBuilder();
			if (season == 0) {
				cycleTime.append("-");
			} else if (season > 1) {
				long reMaturingCycle = Long.parseLong(ShopDomain.getReMaturingTime(unit.getA()));
				cycleTime.append(formatCycle(cycle)).append(" / ").append(formatCycle(reMaturingCycle));
			} else {
				cycleTime.append(formatCycle(cycle)).append(" / ").append("-");
			}
			entry.add(cycleTime);// 花期(第一季/每季)
			entry.add(unit.getM() + "/" + unit.getK());
			entry.add(Integer.parseInt(unit.getF()) > 0 ? Integer.parseInt(unit.getF()) : "-");// "杂草"
			entry.add(Integer.parseInt(unit.getG()) > 0 ? Integer.parseInt(unit.getG()) : "-");// "虫害"
			entry.add(Integer.parseInt(unit.getH()) == 0 ? "旱" : "-");// "干旱"
			long harvest = Long.parseLong(unit.getQ());
			if (harvest > 0L) {
				Date harvestDate = new Date((harvest + cycle) * 1000L);
				entry.add(sdf.format(harvestDate));
			} else {
				entry.add("-");
			}
			farmTableModel.addRow(entry);
		}
		farmTable.setModel(farmTableModel);
	}

	private void refreshPackage() {
		packageTableModel.setRowCount(0);
		List<PackageUnitDomain> packageList = FarmDomain.getInstance().getPackageList();
		for (PackageUnitDomain unit : packageList) {
			packageTableModel.addRow(new Object[] { unit, unit });
		}
		packageTable.setModel(packageTableModel);
	}

	private void refreshStorage() {
		storageTableModel.setRowCount(0);
		List<StoreUnitDomain> storeList = FarmDomain.getInstance().getStoreList();
		for (StoreUnitDomain unit : storeList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(new Boolean(false));
			entry.add(unit.getcName());
			entry.add(unit.getAmount());
			entry.add(unit.getPrice());
			int total = Integer.parseInt(unit.getAmount()) * Integer.parseInt(unit.getPrice());
			entry.add(total);
			storageTableModel.addRow(entry);
		}
		storageTable.setModel(storageTableModel);
	}

	private void refreshAccount() {
		fillExecutionsWithConfig();
		refreshInfoPane();
		refreshFarmTable(FarmDomain.getInstance().getFieldList());
		refreshStorage();
		refreshPackage();
		fieldTitle.setTitle("[" + FarmDomain.getInstance().getUserName() + "]" + "土地信息");
		scrollTablePanel.repaint();
		HelperLoggerAppender.writeLog("刷新状态成功");
	}

	private void refreshFriends() {
		friendTableModel.setRowCount(0);
		List<BaseFarmDomain> friendList = FarmDomain.getInstance().getFriendList();
		for (BaseFarmDomain unit : friendList) {
			Vector<Object> entry = new Vector<Object>();
			entry.add(unit.getUserId());
			entry.add(unit.getUserName());
			entry.add(unit.getExp());
			entry.add(unit.getMoney());
			friendTableModel.addRow(entry);
		}
		friendTable.setModel(friendTableModel);
		HelperLoggerAppender.writeLog("刷新好友列表成功");
	}

	public void refreshFriends(UserDomain userDomain, FarmDomain farmDomain) {
		this.userDomain = userDomain;
		this.farmDomain = farmDomain;
		refreshFriends();
	}

	public void refreshAccount(UserDomain userDomain, FarmDomain farmDomain) {
		this.userDomain = userDomain;
		this.farmDomain = farmDomain;
		refreshAccount();
	}

	public void addAccountToAccountList(UserDomain userDomain, FarmDomain farmDomain) {
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
		} else if (UserDomain.getInstance().isZhinei()) {
			accountRow.add("通过职内网关登录");
		} else if (UserDomain.getInstance().isLianpen()) {
			accountRow.add("通过脸盆网关登录");
		}
		accountRow.add("未开启自动护理");
		this.accountTableModel.addRow(accountRow);
		this.accountTable.setRowSelectionInterval(accountTableModel.getRowCount() - 1, accountTableModel.getRowCount() - 1);
		this.accountRowId = accountTable.getSelectedRow();
	}

	private String formatCycle(long cycle) {
		StringBuilder sb = new StringBuilder();
		long min = cycle / 60;
		long hour = 0L;
		if (min > 60) {
			hour = min / 60;
			min = min % 60;
			return sb.append(hour).append("小时").append(min).append("分").toString();
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
