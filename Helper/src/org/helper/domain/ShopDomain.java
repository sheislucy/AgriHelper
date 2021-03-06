package org.helper.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShopDomain implements Serializable {
	private static final long serialVersionUID = -4482579886995409763L;
	private static List<CropDomain> cropList = new ArrayList<CropDomain>();

	static {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					new FileInputStream(new File("shop.json")));
			JSONObject shopJson = (JSONObject) new JSONParser()
					.parse(inputStreamReader);
			inputStreamReader.close();
			JSONArray cropsArray = (JSONArray) shopJson.get("1");

			SAXReader reader = new SAXReader();
			Document document = reader.read(new File("crop.xml"));
			Element cropsElm = document.getRootElement().element("crops");
			List crops = cropsElm.elements("crop");

			cropList.clear();
			for (Object jsonUnit : cropsArray) {
				CropDomain crop = new CropDomain();
				crop.setcId(String.valueOf(((JSONObject) jsonUnit).get("cId")));
				crop.setcName(String.valueOf(((JSONObject) jsonUnit)
						.get("cName")));
				crop.setcType(String.valueOf(((JSONObject) jsonUnit)
						.get("cType")));
				crop.setGrowthCycle(String.valueOf(((JSONObject) jsonUnit)
						.get("growthCycle")));
				crop.setMaturingTime(String.valueOf(((JSONObject) jsonUnit)
						.get("maturingTime")));
				crop.setExpect(String.valueOf(((JSONObject) jsonUnit)
						.get("expect")));
				crop.setOutput(String.valueOf(((JSONObject) jsonUnit)
						.get("output")));
				crop.setSale(String.valueOf(((JSONObject) jsonUnit).get("sale")));
				crop.setPrice(String.valueOf(((JSONObject) jsonUnit)
						.get("price")));
				crop.setFBPrice(String.valueOf(((JSONObject) jsonUnit)
						.get("FBPrice")));
				crop.setcLevel(String.valueOf(((JSONObject) jsonUnit)
						.get("cLevel")));
				crop.setCropExp(String.valueOf(((JSONObject) jsonUnit)
						.get("cropExp")));
				crop.setcCharm(String.valueOf(((JSONObject) jsonUnit)
						.get("cCharm")));
				crop.setCropChr(String.valueOf(((JSONObject) jsonUnit)
						.get("cropChr")));

				int season = Integer.parseInt(crop.getMaturingTime());
				if ( season > 1) {
					for (Iterator it = crops.iterator(); it.hasNext();) {
						Element cropEl = (Element) it.next();
						int cropELId = Integer.parseInt(cropEl
								.attributeValue("id"));
						String timeArrayStr = cropEl.element("cropGrow")
								.attributeValue("value");
						String[] timeArray = timeArrayStr.split(",");
						if (cropELId == Integer.parseInt(crop.getcId())
								&& null != timeArray && timeArray.length > 2) {
							long halfTime = Long.parseLong(timeArray[2]);
							long totalMaturingTime = Long.parseLong(crop
									.getGrowthCycle());
							crop.setReMaturingTime(String
									.valueOf(totalMaturingTime - halfTime));
						}
					}
				}
				cropList.add(crop);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static Vector<CropDomain> getCropList() {
		return new Vector<CropDomain>(cropList);
	}

	public static String getGrowthCycle(String cId) {
		int cropId = Integer.parseInt(cId);
		for (CropDomain cd : cropList) {
			if (Integer.parseInt(cd.getcId()) == cropId) {
				return cd.getGrowthCycle();
			}
		}
		return "0";
	}

	public static String getSeasonNuber(String cId) {
		int cropId = Integer.parseInt(cId);
		for (CropDomain cd : cropList) {
			if (Integer.parseInt(cd.getcId()) == cropId) {
				return cd.getMaturingTime();
			}
		}
		return "";
	}

	public static String getReMaturingTime(String cId) {
		int cropId = Integer.parseInt(cId);
		for (CropDomain cd : cropList) {
			if (Integer.parseInt(cd.getcId()) == cropId) {
				return cd.getReMaturingTime();
			}
		}
		return "";
	}

	public static String getCropName(String cId) {
		int cropId = Integer.parseInt(cId);
		for (CropDomain cd : cropList) {
			if (Integer.parseInt(cd.getcId()) == cropId) {
				return cd.getcName();
			}
		}
		return "";
	}

	public static String getCropType(String cId) {
		int cropId = Integer.parseInt(cId);
		for (CropDomain cd : cropList) {
			if (Integer.parseInt(cd.getcId()) == cropId) {
				return cd.getcType();
			}
		}
		return "";
	}

	public static Vector<String> getCropNameList() {
		Vector<String> nameList = new Vector<String>();
		for (CropDomain cd : cropList) {
			nameList.add(cd.getcName().replace("种子", ""));
		}
		return nameList;
	}
}
