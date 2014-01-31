package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.enums.EmCropStatus;
import org.helper.enums.EmOperations;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONObject;

public class ExecuteService {
	public void execute(List<EmOperations> operationList, List<String> checkedFieldIdList, String seedId) {
		for (String fieldId : checkedFieldIdList) {
			for (EmOperations o : operationList) {
				switch (o) {
				case WATER:
					doWatering(fieldId);
					break;
				case WORM:
					doWorm(fieldId);
					break;
				case WEED:
					doWeed(fieldId);
					break;
				case HARVEST:
					doHarvest(fieldId);
					break;
				case PLOW:
					doPlow(fieldId);
					break;
				case BUY:
					doBuySeed(fieldId, seedId);
					break;
				case PLANT:
					doPlant(fieldId, seedId);
					break;
				default:
					break;
				}
			}
		}
	}

	public void executeAll(int seedId) {
		for (int i = 0; i < FarmDomain.getInstance().getFieldList().size(); i++) {
			doWatering(String.valueOf(i));
			doWorm(String.valueOf(i));
			doWeed(String.valueOf(i));
			doHarvest(String.valueOf(i));
			doPlow(String.valueOf(i));
			doBuySeed(String.valueOf(i), String.valueOf(seedId));
			doPlant(String.valueOf(i), String.valueOf(seedId));
		}
	}

	private void doWatering(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getH()) == 0) {
			WaterService ws = ServiceFactory.getService(WaterService.class);
			try {
				JSONObject responseJson = ws.water(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地浇水");
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功");
						field.setF(String.valueOf(responseJson.get("weed")));
						field.setG(String.valueOf(responseJson.get("pest")));
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doWorm(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		int wormNumber = Integer.parseInt(field.getG());
		WormService ws = ServiceFactory.getService(WormService.class);
		for (int i = 0; i < wormNumber; i++) {
			try {
				JSONObject responseJson = ws.killWorm(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地杀虫");
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功");
						field.setF(String.valueOf(responseJson.get("weed")));
						field.setG(String.valueOf(responseJson.get("pest")));
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doWeed(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		int weedNumber = Integer.parseInt(field.getF());
		WeedService ws = ServiceFactory.getService(WeedService.class);
		for (int i = 0; i < weedNumber; i++) {
			try {
				JSONObject responseJson = ws.clearWeed(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地除草");
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功");
						field.setF(String.valueOf(responseJson.get("weed")));
						field.setG(String.valueOf(responseJson.get("pest")));
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doPlow(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.WITHERED.getId()) {
			PlowService ps = ServiceFactory.getService(PlowService.class);
			try {
				JSONObject responseJson = ps.plow(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地铲地");
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功，获得经验").append(String.valueOf(responseJson.get("exp")));
						field.setB("0");
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doHarvest(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.RIPE.getId()) {
			HarvestService hs = ServiceFactory.getService(HarvestService.class);
			try {
				JSONObject responseJson = hs.harvest(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地收获");
				if (null != responseJson.get("harvest")) {
					int harvestCount = Integer.parseInt(String.valueOf(responseJson.get("harvest")));
					if (harvestCount > 0) {
						logText.append("成功，数量").append(String.valueOf(responseJson.get("harvest"))).append("，获得经验")
								.append(String.valueOf(responseJson.get("exp")));
						JSONObject cropResponse = (JSONObject) responseJson.get("status");
						field.setB(String.valueOf(cropResponse.get("cropStatus")));
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doBuySeed(String fieldId, String seedId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.EMPTY.getId() && !FarmDomain.getInstance().hasSeed(seedId)) {
			BuyService bs = ServiceFactory.getService(BuyService.class);
			try {
				JSONObject responseJson = bs.buy("1", seedId);
				StringBuilder logText = new StringBuilder("购买");
				logText.append(ShopDomain.getCropName(seedId));
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功，数量").append(String.valueOf(responseJson.get("num"))).append("，金钱")
								.append(String.valueOf(responseJson.get("money")));
					} else {
						logText.append("失败，原因" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}

	private void doPlant(String fieldId, String seedId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList().get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.EMPTY.getId()) {
			PlantService ps = ServiceFactory.getService(PlantService.class);
			try {
				JSONObject responseJson = ps.plant(fieldId, seedId);
				StringBuilder logText = new StringBuilder("在");
				logText.append(fieldId).append("号地播种").append(ShopDomain.getCropName(seedId));
				if (null != responseJson.get("code")) {
					int code = Integer.parseInt(String.valueOf(responseJson.get("code")));
					if (code == 1) {
						logText.append("成功，获得经验").append(String.valueOf(responseJson.get("exp")));
						field.setB("1");
						FarmDomain.getInstance().removeOneSeed(seedId);
					} else {
						logText.append("失败" + responseJson.get("direction"));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (IOException | org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				HelperLoggerAppender.writeLog(e.getMessage());
			}
		}
	}
}
