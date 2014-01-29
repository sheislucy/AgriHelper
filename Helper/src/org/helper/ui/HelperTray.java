/**
 * 
 */
package org.helper.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.helper.domain.UserPreferenceDomain;
import org.helper.util.HelperLoggerAppender;
import org.helper.util.ResourceLoader;

/**
 * @author lucy
 * 
 */
public class HelperTray {
	private final HelperFrame frame;

	public HelperTray() {
		SystemTray tray = SystemTray.getSystemTray();
		PopupMenu menu = new PopupMenu();
		frame = new HelperFrame();
		try {
			Image image = ImageIO.read(ResourceLoader.load("icon.png"));
			MenuItem item = new MenuItem("Exit");
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UserPreferenceDomain.saveToFile();
					System.exit(0);
				}
			});
			menu.add(item);
			TrayIcon icon = new TrayIcon(image, "农场助手");
			icon.setPopupMenu(menu);
			icon.setImageAutoSize(true);
			icon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						frame.setVisible(true);
					}
				}
			});
			tray.add(icon);
		} catch (AWTException | IOException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
		}
		frame.setVisible(true);
	}
}
