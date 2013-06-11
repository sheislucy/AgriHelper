package org.helper.util;

import javax.swing.JTextArea;

public abstract class HelperLoggerAppender {

	public static void writeLog(JTextArea loggerArea, String logText) {
		if (loggerArea.getRows() > 200) {
			loggerArea.setText("");
		}
		loggerArea.append(logText + "\n");
		loggerArea.paintImmediately(loggerArea.getBounds());
	}
}
