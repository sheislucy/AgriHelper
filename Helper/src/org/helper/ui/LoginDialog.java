package org.helper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.http.HttpException;
import org.helper.service.LoginService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class LoginDialog extends JDialog implements ActionListener,
		PropertyChangeListener {
	private static final long serialVersionUID = -9129214165101380074L;

	private final static String OKButtonString = "ȷ��";
	private final static String CANCELButtonString = "ȡ��";
	private final JTextField userNameTf;
	private final JTextField passwordTf;
	private JOptionPane optionPane;

	protected final static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	public LoginDialog(JFrame parentFrame) {
		super(parentFrame, "��¼�ʻ�", true);
		String nameLabel = "�û���";
		String pswLabel = "����";
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
						LoginService s = ctx.getBean(LoginService.class);
						try {
							s.login(user, psw);
						} catch (HttpException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}).start();

			}
			setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(OKButtonString);
	}

}
