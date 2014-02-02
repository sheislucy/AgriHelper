package org.helper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.HttpException;
import org.helper.domain.AccountDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.UserPreferenceDomain;
import org.helper.domain.UserPreferenceUnit;
import org.helper.domain.login.LianpuResponseDomain;
import org.helper.domain.login.UserDomain;
import org.helper.domain.login.VeryCDResponseDomain;
import org.helper.domain.login.ZhineiResponseDomain;
import org.helper.enums.EmUrlDomain;
import org.helper.enums.HttpResponseStatus;
import org.helper.service.RefreshFarmService;
import org.helper.service.RefreshFriendService;
import org.helper.service.ServiceFactory;
import org.helper.service.login.LianpuLoginService;
import org.helper.service.login.VeryCDLoginService;
import org.helper.service.login.ZhineiLoginService;
import org.helper.util.HelperLoggerAppender;
import org.helper.util.StringUtils;
import org.htmlparser.util.ParserException;
import org.json.simple.parser.ParseException;

public class LoginDialog extends JDialog {
	private static final long serialVersionUID = -9129214165101380074L;

	private Java2sAutoTextField userNameTf;
	private JTextField passwordTf;
	private JPanel contentPanel;
	private HelperFrame parentFrame;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<EmUrlDomain> urlComboBox;

	private String passWord;
	private String userName;

	public LoginDialog(HelperFrame parentFrame) {
		super(parentFrame, "登录帐户", true);
		this.parentFrame = parentFrame;
		String nameLabel = "用户名";
		String pswLabel = "密    码";
		userNameTf = new Java2sAutoTextField(UserPreferenceDomain.getUserNameList());
		userNameTf.setStrict(false);
		userNameTf.setColumns(20);
		passwordTf = new JTextField(20);
		userNameTf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				passwordTf.setText(UserPreferenceDomain.getPasswordByName(userNameTf.getText()));
				urlComboBox.setSelectedIndex(UserPreferenceDomain.getDomainIndexByName(userNameTf.getText()));
			}
		});
		passwordTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
					keyReleased(e);
				}
			}
		});
		okButton = new JButton("确定");
		cancelButton = new JButton("取消");

		contentPanel = new JPanel();
		contentPanel.add(new JLabel(nameLabel));
		contentPanel.add(userNameTf);
		contentPanel.add(new JLabel(pswLabel));
		contentPanel.add(passwordTf);
		contentPanel.add(constructUrlCombo());
		contentPanel.add(okButton);
		contentPanel.add(cancelButton);

		bindLoginEvent();

		setContentPane(contentPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(320, 150);
		setResizable(false);
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				userNameTf.requestFocusInWindow();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private JComboBox<EmUrlDomain> constructUrlCombo() {
		urlComboBox = new JComboBox<EmUrlDomain>(EmUrlDomain.getDomains());
		urlComboBox.setRenderer(new UrlComboBoxRenderer());
		return urlComboBox;
	}

	public void showIt() {
		setLocation(parentFrame.getLocationOnScreen().x + parentFrame.getWidth() / 3, parentFrame.getLocationOnScreen().y + parentFrame.getHeight()
				/ 4);
		setVisible(true);
	}

	private void bindLoginEvent() {
		this.okButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -3738743161637541965L;

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		this.cancelButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -2019371555658827959L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void login() {
		userName = userNameTf.getText();
		passWord = passwordTf.getText();

		if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passWord)) {
			for (Map.Entry<String, AccountDomain> account : parentFrame.getAccountList().entrySet()) {
				if (account.getValue().getFarmDomain().getUserName().equals(userName)
						&& account.getValue().getUserDomain().getUrlDomain() == (EmUrlDomain) urlComboBox.getSelectedItem()) {
					JOptionPane.showMessageDialog(this.parentFrame, "请不要重复登录");
					return;
				}
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					UserDomain.getInstance().setUrlDomain((EmUrlDomain) urlComboBox.getSelectedItem());
					if (UserDomain.getInstance().isVeryCD()) {
						VeryCDLoginService loginService = ServiceFactory.getService(VeryCDLoginService.class);
						try {
							checkSuccessForVC(loginService.login(userName, passWord));
						} catch (HttpException | IOException | ParseException e) {
							e.printStackTrace();
							HelperLoggerAppender.writeLog(e.getMessage());
						}
					} else if (UserDomain.getInstance().isZhinei()) {
						ZhineiLoginService loginService = ServiceFactory.getService(ZhineiLoginService.class);
						try {
							checkSuccessForZN(loginService.loginPhase1(userName, passWord));
						} catch (ParserException | HttpException | IOException | ParseException e) {
							e.printStackTrace();
							HelperLoggerAppender.writeLog(e.getMessage());
						}
					} else if (UserDomain.getInstance().isLianpen()) {
						LianpuLoginService loginService = ServiceFactory.getService(LianpuLoginService.class);
						try {
							checkSuccessForLP(loginService.login(userName, passWord));
						} catch (org.apache.http.ParseException | ParserException | IOException e) {
							e.printStackTrace();
							HelperLoggerAppender.writeLog(e.getMessage());
						}
					}
				}
			}).start();
			userNameTf.setText("");
			passwordTf.setText("");
			dispose();
		}
	}

	private void checkSuccessForVC(VeryCDResponseDomain response) {
		if (HttpResponseStatus.ERROR == response.getStatus()) {
			JOptionPane.showMessageDialog(this.parentFrame, response.getInfo());
		} else {
			commonSuccessfulLogin();
		}
	}

	private void checkSuccessForLP(LianpuResponseDomain response) {
		if (HttpResponseStatus.ERROR == response.getStatus()) {
			JOptionPane.showMessageDialog(this.parentFrame, response.getInfoText());
		} else {
			commonSuccessfulLogin();
		}
	}

	private void checkSuccessForZN(ZhineiResponseDomain response) {
		if (HttpResponseStatus.ERROR == response.getStatus()) {
			JOptionPane.showMessageDialog(this.parentFrame, response.getInfoText());
		} else {
			ZhineiLoginService loginService = ServiceFactory.getService(ZhineiLoginService.class);
			try {
				loginService.loginPhase2(response.getLogin3Url());
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
			commonSuccessfulLogin();
		}
	}

	private void commonSuccessfulLogin() {
		FarmDomain.getInstance().setUserName(userName);// override
		RefreshFarmService farmService = ServiceFactory.getService(RefreshFarmService.class);
		farmService.refresh();
		parentFrame.enableAutoCare();
		parentFrame.refreshAccount(UserDomain.getInstance(), FarmDomain.getInstance());
		parentFrame.addAccountToAccountList(UserDomain.getInstance(), FarmDomain.getInstance());
		if (UserPreferenceDomain.USERS.containsKey(FarmDomain.getInstance().getUserId())) {
			UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId()).setPassword(passWord);
			UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId()).setUserName(userName);
			UserPreferenceDomain.USERS.get(FarmDomain.getInstance().getUserId()).setDomainIndex(urlComboBox.getSelectedIndex());
		} else {
			UserPreferenceUnit user = new UserPreferenceUnit();
			user.setUserName(userName);
			user.setPassword(passWord);
			user.setUserId(FarmDomain.getInstance().getUserId());
			user.setDomainIndex(urlComboBox.getSelectedIndex());
			UserPreferenceDomain.USERS.put(FarmDomain.getInstance().getUserId(), user);
		}
		parentFrame.createAutoTask(FarmDomain.getInstance().getUserId());
		final FarmDomain farm = FarmDomain.getInstance();
		final UserDomain user = UserDomain.getInstance();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				FarmDomain.setInstance(farm);
				UserDomain.setInstance(user);
				RefreshFriendService friendService = ServiceFactory.getService(RefreshFriendService.class);
				try {
					friendService.refreshFriend();
				} catch (org.apache.http.ParseException | IOException | ParseException e) {
					e.printStackTrace();
					HelperLoggerAppender.writeLog(e.getMessage());
				}
				parentFrame.refreshFriends(UserDomain.getInstance(), FarmDomain.getInstance());

			}
		}).start();
	}
}
