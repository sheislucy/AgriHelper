package org.helper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import org.helper.domain.login.UserDomain;
import org.helper.domain.login.VeryCDResponse;
import org.helper.domain.login.ZhineiResponseDomain;
import org.helper.service.RefreshFarmService;
import org.helper.service.ServiceFactory;
import org.helper.service.login.VeryCDLoginService;
import org.helper.service.login.ZhineiLoginService;
import org.helper.util.EmUrlDomain;
import org.helper.util.HelperLoggerAppender;
import org.helper.util.HttpResponseStatus;
import org.helper.util.StringUtils;
import org.htmlparser.util.ParserException;
import org.json.simple.parser.ParseException;

public class LoginDialog extends JDialog {
	private static final long serialVersionUID = -9129214165101380074L;

	private JTextField userNameTf;
	private JTextField passwordTf;
	private JPanel contentPanel;
	private HelperFrame parentFrame;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<EmUrlDomain> urlComboBox;

	public LoginDialog(HelperFrame parentFrame) {
		super(parentFrame, "登录帐户", true);
		this.parentFrame = parentFrame;
		String nameLabel = "用户名";
		String pswLabel = "密    码";
		userNameTf = new JTextField(20);
		passwordTf = new JTextField(20);
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
		urlComboBox.setSelectedIndex(Integer.parseInt(UserPreferenceDomain
				.getSeedComboIndex()));
		return urlComboBox;
	}

	public void showIt() {
		setLocation(
				parentFrame.getLocationOnScreen().x + parentFrame.getWidth()
						/ 3,
				parentFrame.getLocationOnScreen().y + parentFrame.getHeight()
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
		final String user = userNameTf.getText();
		final String psw = passwordTf.getText();

		if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(psw)) {
			for (Map.Entry<String, AccountDomain> account : parentFrame
					.getAccountList().entrySet()) {
				if (account.getValue().getFarmDomain().getUserName()
						.equals(user)
						&& account.getValue().getUserDomain().getUrlDomain() == (EmUrlDomain) urlComboBox
								.getSelectedItem()) {
					JOptionPane.showMessageDialog(this.parentFrame, "请不要重复登录");
					return;
				}
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					UserDomain.getInstance().setUrlDomain(
							(EmUrlDomain) urlComboBox.getSelectedItem());
					if (UserDomain.getInstance().isVeryCD()) {
						VeryCDLoginService loginService = ServiceFactory
								.getService(VeryCDLoginService.class);
						try {
							checkSuccessForVC(loginService.login(user, psw));
						} catch (HttpException | IOException | ParseException e) {
							e.printStackTrace();
							HelperLoggerAppender.writeLog(e.getMessage());
						}
					} else {
						ZhineiLoginService loginService = ServiceFactory
								.getService(ZhineiLoginService.class);
						try {
							checkSuccessForZN(loginService.loginPhase1(user,
									psw));
						} catch (ParserException | HttpException | IOException
								| ParseException e) {
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

	private void checkSuccessForVC(VeryCDResponse response) {
		if (HttpResponseStatus.ERROR.getValue().equalsIgnoreCase(
				response.getStatus())) {
			JOptionPane.showMessageDialog(this.parentFrame, response.getInfo());
		} else {
			RefreshFarmService farmService = ServiceFactory
					.getService(RefreshFarmService.class);
			farmService.refresh();
			parentFrame.enableAutoCare();
			parentFrame.refreshAccount(UserDomain.getInstance(),
					FarmDomain.getInstance());
			parentFrame.addAccountToAccountList(UserDomain.getInstance(),
					FarmDomain.getInstance());
		}
	}

	private void checkSuccessForZN(ZhineiResponseDomain response) {
		if (HttpResponseStatus.ERROR == response.getStatus()) {
			JOptionPane.showMessageDialog(this.parentFrame,
					response.getInfoText());
		} else {
			// parentFrame.changeLoginToLogout();
			ZhineiLoginService loginService = ServiceFactory
					.getService(ZhineiLoginService.class);
			try {
				loginService.loginPhase2(response.getLogin3Url());
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
			RefreshFarmService farmService = ServiceFactory
					.getService(RefreshFarmService.class);
			farmService.refresh();
			parentFrame.enableAutoCare();
			parentFrame.refreshAccount(UserDomain.getInstance(),
					FarmDomain.getInstance());
			parentFrame.addAccountToAccountList(UserDomain.getInstance(),
					FarmDomain.getInstance());
		}
	}

}
