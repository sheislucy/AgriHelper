package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.util.EmCropStatus;
import org.helper.util.EmOperations;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONObject;

public class ExecuteService {
	public void execute(List<EmOperations> operationList,
			List<String> checkedFieldIdList, String seedId) {
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
				case PLOW:
					doPlow(fieldId);
					break;
				case HARVEST:
					doHarvest(fieldId);
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

	private void doWatering(String fieldId) {
	}

	private void doWorm(String fieldId) {
	}

	private void doWeed(String fieldId) {
	}

	private void doPlow(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList()
				.get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.WITHERED.getId()) {
			PlowService ps = ServiceFactory.getService(PlowService.class);
			try {
				JSONObject responseJson = ps.plow(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地铲地");
				// TODO decode response of plowing
				if (null != responseJson.get("harvest")) {
					int harvestCount = Integer.parseInt(String
							.valueOf(responseJson.get("harvest")));
					if (harvestCount > 0) {
						logText.append("成功，数量")
								.append(String.valueOf(responseJson
										.get("harvest")))
								.append("，获得经验")
								.append(String.valueOf(responseJson.get("exp")));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void doHarvest(String fieldId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList()
				.get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.RIPE.getId()) {
			HarvestService hs = ServiceFactory.getService(HarvestService.class);
			try {
				JSONObject responseJson = hs.harvest(fieldId);
				StringBuilder logText = new StringBuilder("第");
				logText.append(fieldId).append("块土地收获");
				if (null != responseJson.get("harvest")) {
					int harvestCount = Integer.parseInt(String
							.valueOf(responseJson.get("harvest")));
					if (harvestCount > 0) {
						logText.append("成功，数量")
								.append(String.valueOf(responseJson
										.get("harvest")))
								.append("，获得经验")
								.append(String.valueOf(responseJson.get("exp")));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void doBuySeed(String fieldId, String seedId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList()
				.get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.EMPTY.getId()) {
			BuyService bs = ServiceFactory.getService(BuyService.class);
			try {
				JSONObject responseJson = bs.buy("1", seedId);
				StringBuilder logText = new StringBuilder("购买");
				logText.append(ShopDomain.getCropName(seedId));
				// TODO decode buy response
				if (null != responseJson.get("harvest")) {
					int harvestCount = Integer.parseInt(String
							.valueOf(responseJson.get("harvest")));
					if (harvestCount > 0) {
						logText.append("成功，数量")
								.append(String.valueOf(responseJson
										.get("harvest")))
								.append("，获得经验")
								.append(String.valueOf(responseJson.get("exp")));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void doPlant(String fieldId, String seedId) {
		FieldUnitDomain field = FarmDomain.getInstance().getFieldList()
				.get(Integer.parseInt(fieldId));
		if (Integer.parseInt(field.getB()) == EmCropStatus.EMPTY.getId()) {
			PlantService ps = ServiceFactory.getService(PlantService.class);
			try {
				JSONObject responseJson = ps.plant(fieldId, seedId);
				StringBuilder logText = new StringBuilder("播种");
				logText.append(ShopDomain.getCropName(seedId));
				// TODO decode plant response
				if (null != responseJson.get("harvest")) {
					int harvestCount = Integer.parseInt(String
							.valueOf(responseJson.get("harvest")));
					if (harvestCount > 0) {
						logText.append("成功，数量")
								.append(String.valueOf(responseJson
										.get("harvest")))
								.append("，获得经验")
								.append(String.valueOf(responseJson.get("exp")));
					}
				} else {
					logText.append("失败");
				}
				HelperLoggerAppender.writeLog(logText.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
