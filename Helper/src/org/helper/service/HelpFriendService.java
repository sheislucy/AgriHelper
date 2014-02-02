package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.helper.domain.BaseFarmDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.enums.EmCropStatus;
import org.helper.enums.EmFriendOperations;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONObject;

public class HelpFriendService {
	public void help(List<EmFriendOperations> operationList, List<String> checkedFieldIdList, String friendId) {
		for (String fieldId : checkedFieldIdList) {
			for (EmFriendOperations o : operationList) {
				switch (o) {
				case WATER:
					doWater(fieldId, friendId);
					break;
				case WORM:
					doWorm(fieldId, friendId);
					break;
				case WEED:
					doWeed(fieldId, friendId);
					break;
				case STEAL:
					doSteal(fieldId, friendId);
					break;
				default:
					break;
				}
			}
		}
	}

	private void doWorm(String fieldId, String friendId) {
		BaseFarmDomain friend = FarmDomain.getInstance().getFriendById(friendId);
		int fieldIndex = Integer.parseInt(fieldId);
		if (friend.getFieldList().size() >= fieldIndex + 1) {
			FieldUnitDomain field = friend.getFieldList().get(fieldIndex);
			if (Integer.parseInt(field.getB()) < EmCropStatus.RIPE.getId()) {
				int wormNumber = Integer.parseInt(field.getG());
				WormFriendService wormFriendService = ServiceFactory.getService(WormFriendService.class);
				for (int i = 0; i < wormNumber; i++) {
					try {
						JSONObject friendFieldJson = wormFriendService.wormFriend(friendId, fieldId);
						if (String.valueOf(friendFieldJson.get("code")).equals("1")) {
							StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
									.append(friendFieldJson.get("farmlandIndex")).append("块土地除虫成功，获得金币").append(friendFieldJson.get("money"))
									.append("，经验").append(friendFieldJson.get("exp"));
							HelperLoggerAppender.writeLog(sb.toString());
						} else {
							StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
									.append(friendFieldJson.get("farmlandIndex")).append("块土地除虫失败");
							HelperLoggerAppender.writeLog(sb.toString());
						}
					} catch (ParseException | org.json.simple.parser.ParseException | IOException e) {
						e.printStackTrace();
						HelperLoggerAppender.writeLog(e.getMessage());
					}
				}
			}
		}
	}

	private void doWater(String fieldId, String friendId) {
		BaseFarmDomain friend = FarmDomain.getInstance().getFriendById(friendId);
		int fieldIndex = Integer.parseInt(fieldId);
		if (friend.getFieldList().size() >= fieldIndex + 1) {
			FieldUnitDomain field = friend.getFieldList().get(fieldIndex);
			if (Integer.parseInt(field.getH()) == 0 && Integer.parseInt(field.getB()) < EmCropStatus.RIPE.getId()) {
				WaterFriendService waterFriendService = ServiceFactory.getService(WaterFriendService.class);
				try {
					JSONObject friendFieldJson = waterFriendService.waterFriend(friendId, fieldId);
					if (String.valueOf(friendFieldJson.get("code")).equals("1")) {
						StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
								.append(friendFieldJson.get("farmlandIndex")).append("块土地浇水成功，获得金币").append(friendFieldJson.get("money"))
								.append("，经验").append(friendFieldJson.get("exp"));
						HelperLoggerAppender.writeLog(sb.toString());
					} else {
						StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
								.append(friendFieldJson.get("farmlandIndex")).append("块土地浇水失败");
						HelperLoggerAppender.writeLog(sb.toString());
					}
				} catch (ParseException | org.json.simple.parser.ParseException | IOException e) {
					e.printStackTrace();
					HelperLoggerAppender.writeLog(e.getMessage());
				}
			}

		}
	}

	private void doWeed(String fieldId, String friendId) {
		BaseFarmDomain friend = FarmDomain.getInstance().getFriendById(friendId);
		int fieldIndex = Integer.parseInt(fieldId);
		if (friend.getFieldList().size() >= fieldIndex + 1) {
			WeedFriendService weedFriendService = ServiceFactory.getService(WeedFriendService.class);
			if (Integer.parseInt(friend.getFieldList().get(fieldIndex).getB()) < EmCropStatus.RIPE.getId()) {
				int weedNumber = Integer.parseInt(friend.getFieldList().get(fieldIndex).getF());
				for (int i = 0; i < weedNumber; i++) {
					try {
						JSONObject friendFieldJson = weedFriendService.weedFriend(friendId, fieldId);
						if (String.valueOf(friendFieldJson.get("code")).equals("1")) {
							StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
									.append(friendFieldJson.get("farmlandIndex")).append("块土地除草成功，获得金币").append(friendFieldJson.get("money"))
									.append("，经验").append(friendFieldJson.get("exp"));
							HelperLoggerAppender.writeLog(sb.toString());
						} else {
							StringBuilder sb = new StringBuilder("为好友[").append(friend.getUserName()).append("]第")
									.append(friendFieldJson.get("farmlandIndex")).append("块土地除草失败");
							HelperLoggerAppender.writeLog(sb.toString());
						}
					} catch (ParseException | IOException | org.json.simple.parser.ParseException e) {
						e.printStackTrace();
						HelperLoggerAppender.writeLog(e.getMessage());
					}
				}
			}
		}
	}

	private void doSteal(String fieldId, String friendId) {
		BaseFarmDomain friend = FarmDomain.getInstance().getFriendById(friendId);
		int fieldIndex = Integer.parseInt(fieldId);
		if (friend.getFieldList().size() >= fieldIndex + 1) {
			FieldUnitDomain field = friend.getFieldList().get(fieldIndex);
			if (Integer.parseInt(field.getB()) == EmCropStatus.RIPE.getId()) {
				int leaving = Integer.parseInt(field.getM());
				int minimum = Integer.parseInt(field.getL());
				if (field.getN().equals("2") && leaving > minimum) {
					StealFriendService stealService = ServiceFactory.getService(StealFriendService.class);
					try {
						JSONObject friendFieldJson = stealService.stealFriend(friendId, fieldId);
						if (String.valueOf(friendFieldJson.get("code")).equals("1")) {
							field.setM(String.valueOf(friendFieldJson.get("leavings")));
							field.setN("1");
							JSONObject status = (JSONObject) friendFieldJson.get("status");
							StringBuilder sb = new StringBuilder("采摘好友[").append(friend.getUserName()).append("]第")
									.append(friendFieldJson.get("farmlandIndex")).append("块土地的[")
									.append(ShopDomain.getCropName(String.valueOf(status.get("cId"))).replace("种子", "").trim()).append("]成功，数量")
									.append(friendFieldJson.get("harvest"));
							HelperLoggerAppender.writeLog(sb.toString());
						} else {
							StringBuilder sb = new StringBuilder("采摘好友[").append(friend.getUserName()).append("]的第").append(fieldId).append("块土地失败");
							HelperLoggerAppender.writeLog(sb.toString());
						}
					} catch (ParseException | IOException | org.json.simple.parser.ParseException e) {
						e.printStackTrace();
						HelperLoggerAppender.writeLog(e.getMessage());
					}
				} else {
					StringBuilder sb = new StringBuilder("好友[").append(friend.getUserName()).append("]的第").append(fieldId).append("块土地已经采摘过");
					HelperLoggerAppender.writeLog(sb.toString());
				}
			}
		}
	}
}
