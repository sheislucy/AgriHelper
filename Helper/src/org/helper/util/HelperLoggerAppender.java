package org.helper.util;

import javax.swing.JTextArea;

import org.helper.ui.HelperFrame;

public class HelperLoggerAppender {

	private HelperFrame mainframe;
	private static HelperLoggerAppender logger;

	private HelperLoggerAppender() {
	}

	public static void setInstance(HelperFrame mainframe) {
		logger = new HelperLoggerAppender();
		logger.mainframe = mainframe;
	}

	public static void writeLog(String logText) {
		JTextArea loggerArea = logger.mainframe.getLoggerArea();
		if (loggerArea.getRows() > 200) {
			loggerArea.setText("");
		}
		loggerArea.append(logText + "\n");
		loggerArea.paintImmediately(loggerArea.getBounds());
	}
}
