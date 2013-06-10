package org.helper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.http.HttpException;
import org.helper.domain.VeryCDResponse;
import org.helper.service.LoginService;
import org.helper.service.RefreshFarmService;
import org.helper.util.HelperAppContext;
import org.helper.util.HttpResponseStatus;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class LoginDialog extends JDialog implements ActionListener,
		PropertyChangeListener {
	private static final long serialVersionUID = -9129214165101380074L;

	private final static String OKButtonString = "确定";
	private final static String CANCELButtonString = "取消";
	private final JTextField userNameTf;
	private final JTextField passwordTf;
	private JOptionPane optionPane;
	private HelperFrame parentFrame;

	public LoginDialog(HelperFrame parentFrame) {
		super(parentFrame, "登录帐户", true);
		this.parentFrame = parentFrame;
		String nameLabel = "用户名";
		String pswLabel = "密码";
		userNameTf = new JTextField(6);
		passwordTf = new JTextField(6);
		Object array[] = { nameLabel, userNameTf, pswLabel, passwordTf };
		Object options[] = { OKButtonString, CANCELButtonString };
		optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		setContentPane(optionPane);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(220, 180);
		setResizable(false);
		optionPane.addPropertyChangeListener(this);
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				userNameTf.requestFocusInWindow();
			}
		});
	}

	public void showIt() {
		setLocation(
				parentFrame.getLocationOnScreen().x + parentFrame.getWidth()
						/ 2,
				parentFrame.getLocationOnScreen().y + parentFrame.getHeight()
						/ 4);
		setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if ((e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}

			final String user = userNameTf.getText();
			final String psw = passwordTf.getText();

			if (OKButtonString.equals(value) && !StringUtils.isEmpty(user)
					&& !StringUtils.isEmpty(psw)) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						LoginService loginService = HelperAppContext.CTX
								.getBean(LoginService.class);
						try {
							// synchronized (loginService) {
							checkSuccess(loginService.login(user, psw));
							// }
						} catch (HttpException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}

					}
				}).start();

			}
			setVisible(false);
		}
	}

	private void checkSuccess(VeryCDResponse response) {
		if (HttpResponseStatus.ERROR.getValue().equalsIgnoreCase(
				response.getStatus())) {
			JOptionPane.showMessageDialog(this.parentFrame, response.getInfo());
		} else {
			parentFrame.changeLoginMenuName();
			RefreshFarmService farmService = HelperAppContext.CTX
					.getBean(RefreshFarmService.class);
			farmService.refresh();
		}
		parentFrame.refreshAccount();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(OKButtonString);
	}

}
