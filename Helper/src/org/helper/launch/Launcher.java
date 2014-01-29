package org.helper.launch;

import org.helper.ui.HelperTray;
import org.helper.util.HelperLoggerAppender;

public class Launcher {

	public static void main(String[] args) {
		new HelperTray();
		HelperLoggerAppender.writeLog("启动成功");
	}
}
