package org.helper.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import org.helper.ui.HelperFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperLoggerAppender {

	private HelperFrame mainframe;
	private static HelperLoggerAppender logger;
	private DateFormat sdf;
	private static Logger fileLogger = LoggerFactory
			.getLogger(HelperLoggerAppender.class);

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
		loggerArea.setCaretPosition(loggerArea.getDocument().getLength());
		fileLogger.info(logText);
	}

	public static void clear() {
		logger.mainframe.getLoggerArea().setText("");
	}
}
