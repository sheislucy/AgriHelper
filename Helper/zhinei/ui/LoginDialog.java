package org.helper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.helper.domain.FarmDomain;
import org.helper.domain.login.ZhineiResponseDomain;
import org.helper.domain.login.ZhineiUserDomain;
import org.helper.service.RefreshFarmService;
import org.helper.service.ServiceFactory;
import org.helper.service.login.LoginService;
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

	public LoginDialog(HelperFrame parentFrame) {
		super(parentFrame, "登录帐户", true);
		this.parentFrame = parentFrame;
		String nameLabel = "用户名";
		String pswLabel = "密    码";
		userNameTf = new JTextField(12);
		passwordTf = new JTextField(12);
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
		contentPanel.add(okButton);
		contentPanel.add(cancelButton);

		bindLoginEvent();

		setContentPane(contentPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(220, 120);
		setResizable(false);
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				userNameTf.requestFocusInWindow();
			}
		});
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

			new Thread(new Runnable() {
				@Override
				public void run() {
					LoginService loginService = ServiceFactory
							.getService(LoginService.class);
					try {
						checkSuccess(loginService.loginPhase1(user, psw));
					} catch (ParserException | HttpException | IOException
							| ParseException e) {
						e.printStackTrace();
						HelperLoggerAppender.writeLog(e.getMessage());
					}
				}
			}).start();
			userNameTf.setText("");
			passwordTf.setText("");
			dispose();
		}
	}

	private void checkSuccess(ZhineiResponseDomain response) {
		if (HttpResponseStatus.ERROR == response.getStatus()) {
			JOptionPane.showMessageDialog(this.parentFrame,
					response.getInfoText());
		} else {
			parentFrame.changeLoginToLogout();
			LoginService loginService = ServiceFactory
					.getService(LoginService.class);
			try {
				loginService.loginPhase2(response.getLogin3Url());
			} catch (ClientProtocolException | IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
			RefreshFarmService farmService = ServiceFactory
					.getService(RefreshFarmService.class);
			farmService.refresh();
			parentFrame.enableAutoCare();
		}
		parentFrame.refreshAccount(ZhineiUserDomain.getInstance(),
				FarmDomain.getInstance());
	}

}
