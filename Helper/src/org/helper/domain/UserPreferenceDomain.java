package org.helper.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.helper.util.HelperLoggerAppender;

public class UserPreferenceDomain implements Serializable {
	private static final long serialVersionUID = 8300668837232655639L;
	private static boolean isWater;
	private static boolean isWorm;
	private static boolean isWeed;
	private static boolean isHarvest;
	private static boolean isPlow;
	private static boolean isBuy;
	private static boolean isPlant;
	private static String seedComboIndex;

	static {
		File userPreference = new File("user.ini");
		if (!userPreference.exists()) {
			try {
				userPreference.createNewFile();
				UserPreferenceDomain.saveToFile(false, false, false, false,
						false, false, false, "0");
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						userPreference));
				String line;
				while ((line = br.readLine()) != null) {
					String[] config = line.split("=");
					if (null != config && config.length > 1) {
						if (config[0].equalsIgnoreCase("isWater")) {
							isWater = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isWorm")) {
							isWorm = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isWater")) {
							isWater = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isWeed")) {
							isWeed = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isHarvest")) {
							isHarvest = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isPlow")) {
							isPlow = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isBuy")) {
							isBuy = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("isPlant")) {
							isPlant = new Boolean(config[1]);
						} else if (config[0].equalsIgnoreCase("seedComboIndex")) {
							seedComboIndex = config[1];
						}
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}

		}
	}

	public static void saveToFile(boolean isWater, boolean isWorm,
			boolean isWeed, boolean isHarvest, boolean isPlow, boolean isBuy,
			boolean isPlant, String seedComboIndex) {
		UserPreferenceDomain.isWater = isWater;
		UserPreferenceDomain.isWorm = isWorm;
		UserPreferenceDomain.isWeed = isWeed;
		UserPreferenceDomain.isHarvest = isHarvest;
		UserPreferenceDomain.isPlow = isPlow;
		UserPreferenceDomain.isBuy = isBuy;
		UserPreferenceDomain.isPlant = isPlant;
		UserPreferenceDomain.seedComboIndex = seedComboIndex;

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"user.ini"), false));
			writer.write("isWater=" + UserPreferenceDomain.isWater);
			writer.newLine();
			writer.write("isWorm=" + UserPreferenceDomain.isWorm);
			writer.newLine();
			writer.write("isWeed=" + UserPreferenceDomain.isWeed);
			writer.newLine();
			writer.write("isHarvest=" + UserPreferenceDomain.isHarvest);
			writer.newLine();
			writer.write("isPlow=" + UserPreferenceDomain.isPlow);
			writer.newLine();
			writer.write("isBuy=" + UserPreferenceDomain.isBuy);
			writer.newLine();
			writer.write("isPlant=" + UserPreferenceDomain.isPlant);
			writer.newLine();
			writer.write("seedComboIndex="
					+ UserPreferenceDomain.seedComboIndex);
			writer.newLine();

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
		}
	}

	public static boolean isWater() {
		return isWater;
	}

	public static void setWater(boolean isWater) {
		UserPreferenceDomain.isWater = isWater;
	}

	public static boolean isWorm() {
		return isWorm;
	}

	public static void setWorm(boolean isWorm) {
		UserPreferenceDomain.isWorm = isWorm;
	}

	public static boolean isWeed() {
		return isWeed;
	}

	public static void setWeed(boolean isWeed) {
		UserPreferenceDomain.isWeed = isWeed;
	}

	public static boolean isHarvest() {
		return isHarvest;
	}

	public static void setHarvest(boolean isHarvest) {
		UserPreferenceDomain.isHarvest = isHarvest;
	}

	public static boolean isPlow() {
		return isPlow;
	}

	public static void setPlow(boolean isPlow) {
		UserPreferenceDomain.isPlow = isPlow;
	}

	public static boolean isBuy() {
		return isBuy;
	}

	public static void setBuy(boolean isBuy) {
		UserPreferenceDomain.isBuy = isBuy;
	}

	public static String getSeedComboIndex() {
		return seedComboIndex;
	}

	public static void setSeedComboIndex(String seedComboIndex) {
		UserPreferenceDomain.seedComboIndex = seedComboIndex;
	}

	public static boolean isPlant() {
		return isPlant;
	}

	public static void setPlant(boolean isPlant) {
		UserPreferenceDomain.isPlant = isPlant;
	}

}
