package org.helper.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.helper.util.HelperLoggerAppender;

/**
 * 
 * The format of each line in the file should be like userId.[action]=true/false
 * 
 * The first line of one user should be userId.userName=[userName]
 * 
 * userId or domain+userName can both guarantee the uniqueness
 * 
 * @author Administrator
 * 
 */
public class UserPreferenceDomain implements Serializable {
	private static final long serialVersionUID = 8300668837232655639L;
	// <userId in farmDomain, userInfo>
	public static Map<String, UserPreferenceUnit> USERS = new HashMap<String, UserPreferenceUnit>();

	static {
		File userPreference = new File("user.ini");
		if (!userPreference.exists()) {
			try {
				userPreference.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		} else {
			try (BufferedReader br = new BufferedReader(new FileReader(userPreference));) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] config = line.split("=");
					if (null != config && config.length > 1) {
						String[] idAndTitle = config[0].split("\\.");
						if (USERS.containsKey(idAndTitle[0])) {
							getUsersFromConfigFile(idAndTitle, config);
						} else {
							UserPreferenceUnit user = new UserPreferenceUnit();
							USERS.put(idAndTitle[0], user);
							getUsersFromConfigFile(idAndTitle, config);
						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private static void getUsersFromConfigFile(String[] idAndTitle, String[] config) {
		USERS.get(idAndTitle[0]).setUserId(idAndTitle[0]);
		if (idAndTitle[1].equalsIgnoreCase("userName")) {
			USERS.get(idAndTitle[0]).setUserName(config[1]);
		} else if (idAndTitle[1].equalsIgnoreCase("password")) {
			USERS.get(idAndTitle[0]).setPassword(config[1]);
		} else if (idAndTitle[1].equalsIgnoreCase("domainIndex")) {
			USERS.get(idAndTitle[0]).setDomainIndex(Integer.parseInt(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isWater")) {
			USERS.get(idAndTitle[0]).setWater(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isWorm")) {
			USERS.get(idAndTitle[0]).setWorm(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isWeed")) {
			USERS.get(idAndTitle[0]).setWeed(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isHarvest")) {
			USERS.get(idAndTitle[0]).setHarvest(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isPlow")) {
			USERS.get(idAndTitle[0]).setPlow(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isBuy")) {
			USERS.get(idAndTitle[0]).setBuy(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("isPlant")) {
			USERS.get(idAndTitle[0]).setPlant(new Boolean(config[1]));
		} else if (idAndTitle[1].equalsIgnoreCase("seedComboIndex")) {
			USERS.get(idAndTitle[0]).setSeedComboIndex(Integer.parseInt(config[1]));
		}
	}

	public static List<String> getUserNameList() {
		List<String> nameList = new ArrayList<String>();
		for (Map.Entry<String, UserPreferenceUnit> user : USERS.entrySet()) {
			nameList.add(user.getValue().getUserName());
		}
		return nameList;
	}

	public static String getPasswordByName(String userName) {
		for (Map.Entry<String, UserPreferenceUnit> user : USERS.entrySet()) {
			if (user.getValue().getUserName().equals(userName)) {
				return user.getValue().getPassword();
			}
		}
		return "";
	}

	public static int getDomainIndexByName(String userName) {
		for (Map.Entry<String, UserPreferenceUnit> user : USERS.entrySet()) {
			if (user.getValue().getUserName().equals(userName)) {
				return user.getValue().getDomainIndex();
			}
		}
		return 0;
	}

	public static int getSeedIndexById(String userId) {
		for (Map.Entry<String, UserPreferenceUnit> user : USERS.entrySet()) {
			if (user.getKey().equals(userId)) {
				return user.getValue().getSeedComboIndex();
			}
		}
		return 0;
	}

	public static void saveToFile() {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("user.ini"), false));
			for (Map.Entry<String, UserPreferenceUnit> userEntry : USERS.entrySet()) {
				UserPreferenceUnit userPreferenceUnit = userEntry.getValue();
				if (null != userPreferenceUnit.getUserId()) {
					writer.write(userPreferenceUnit.getUserId() + ".userName=" + userPreferenceUnit.getUserName());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".password=" + userPreferenceUnit.getPassword());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".domainIndex=" + userPreferenceUnit.getDomainIndex());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isWater=" + userPreferenceUnit.isWater());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isWorm=" + userPreferenceUnit.isWorm());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isWeed=" + userPreferenceUnit.isWeed());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isHarvest=" + userPreferenceUnit.isHarvest());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isPlow=" + userPreferenceUnit.isPlow());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isBuy=" + userPreferenceUnit.isBuy());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".isPlant=" + userPreferenceUnit.isPlant());
					writer.newLine();
					writer.write(userPreferenceUnit.getUserId() + ".seedComboIndex=" + userPreferenceUnit.getSeedComboIndex());
					writer.newLine();
				}
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
		}
	}
}
