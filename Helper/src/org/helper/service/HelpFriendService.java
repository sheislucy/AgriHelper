package org.helper.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.helper.domain.BaseFarmDomain;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.domain.ShopDomain;
import org.helper.enums.EmFriendOperations;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.JSONObject;

public class HelpFriendService {
	public void help(List<EmFriendOperations> operationList, List<String> checkedFieldIdList, String friendId) {
		for (String fieldId : checkedFieldIdList) {
			for (EmFriendOperations o : operationList) {
				switch (o) {
				case WATER:
					// doWatering(fieldId);
					break;
				case WORM:
					// doWorm(fieldId);
					break;
				case WEED:
					// doWeed(fieldId);
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

	private void doSteal(String fieldId, String friendId) {
		BaseFarmDomain friend = FarmDomain.getInstance().getFriendById(friendId);
		int fieldIndex = Integer.parseInt(fieldId);
		if (friend.getFieldList().size() > fieldIndex + 1) {
			FieldUnitDomain field = friend.getFieldList().get(fieldIndex);
			if (field.getN().equals("2")) {
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
