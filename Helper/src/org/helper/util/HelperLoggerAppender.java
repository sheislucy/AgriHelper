package org.helper.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import org.helper.ui.HelperFrame;

public class HelperLoggerAppender {

	private HelperFrame mainframe;
	private static HelperLoggerAppender logger;
	private DateFormat sdf;

	private HelperLoggerAppender() {
	}

	public static void setInstance(HelperFrame mainframe) {
		logger = new HelperLoggerAppender();
		logger.mainframe = mainframe;
		logger.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static void writeLog(String logText) {
		JTextArea loggerArea = logger.mainframe.getLoggerArea();
		if (loggerArea.getRows() > 200) {
			loggerArea.setText("");
		}
		loggerArea.append(logger.sdf.format(new Date()) + " " + logText + "\n");
		loggerArea.paintImmediately(loggerArea.getBounds());
	}
}
