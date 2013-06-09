package org.helper.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShopDomain implements Serializable {
	private static final long serialVersionUID = -4482579886995409763L;
	private static List<CropDomain> cropList = new ArrayList<CropDomain>();

	static {
		File shopFile = new File("resource/shop.json");
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(
					shopFile));
			JSONObject shopJson = (JSONObject) new JSONParser()
					.parse(inputStreamReader);
			JSONArray cropsArray = (JSONArray) shopJson.get("1");

			for (Object jsonUnit : cropsArray) {
				CropDomain crop = new CropDomain();
				crop.setcName(String.valueOf(((JSONObject) jsonUnit)
						.get("cName")));
				cropList.add(crop);
				//TODO fulfill all properties
			}

			inputStreamReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(cropList.size());
		System.out.println(cropList.get(0).getcName());
	}

}
