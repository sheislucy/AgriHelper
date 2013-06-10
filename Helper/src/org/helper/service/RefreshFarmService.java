package org.helper.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.helper.domain.FarmDomain;
import org.helper.domain.FieldUnitDomain;
import org.helper.util.HelperAppContext;
import org.htmlparser.util.ParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class RefreshFarmService {

	public void refresh() {
		RefreshFarmStep1Service step1 = HelperAppContext.CTX
				.getBean(RefreshFarmStep1Service.class);
		RefreshFarmStep2Service step2 = HelperAppContext.CTX
				.getBean(RefreshFarmStep2Service.class);
		RefreshFarmStep3Service step3 = HelperAppContext.CTX
				.getBean(RefreshFarmStep3Service.class);
		RefreshFarmStep4Service step4 = HelperAppContext.CTX
				.getBean(RefreshFarmStep4Service.class);
		try {
			String url2 = step1.step1GetMethod();
			String url3 = step2.step2GetMethod(url2);
			step3.step3GetMethod(url3);
			JSONObject farmJson = step4.getFarmAndPlayerInfo();
			buildFarm(farmJson);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void buildFarm(JSONObject farmJson) {
		JSONObject user = (JSONObject) farmJson.get("user");
		FarmDomain.getInstance().setCharm(String.valueOf(user.get("charm")));
		FarmDomain.getInstance().setExp(String.valueOf(user.get("exp")));
		FarmDomain.getInstance().setUserName(
				String.valueOf(user.get("userName")));
		FarmDomain.getInstance().setUserId(String.valueOf(user.get("uId")));
		FarmDomain.getInstance().setMoney(String.valueOf(user.get("money")));
		JSONArray farmlandStatus = (JSONArray) farmJson.get("farmlandStatus");
		FarmDomain.getInstance().removeAllFields();
		for (Object jsonUnit : farmlandStatus) {
			FieldUnitDomain unit = new FieldUnitDomain();
			unit.setA(String.valueOf(((JSONObject) jsonUnit).get("a")));
			unit.setB(String.valueOf(((JSONObject) jsonUnit).get("b")));
			unit.setF(String.valueOf(((JSONObject) jsonUnit).get("f")));
			unit.setG(String.valueOf(((JSONObject) jsonUnit).get("g")));
			unit.setH(String.valueOf(((JSONObject) jsonUnit).get("h")));
			unit.setK(String.valueOf(((JSONObject) jsonUnit).get("k")));
			unit.setL(String.valueOf(((JSONObject) jsonUnit).get("l")));
			unit.setM(String.valueOf(((JSONObject) jsonUnit).get("m")));
			unit.setN(String.valueOf(((JSONObject) jsonUnit).get("n")));
			unit.setQ(String.valueOf(((JSONObject) jsonUnit).get("q")));
			unit.setR(String.valueOf(((JSONObject) jsonUnit).get("r")));
			unit.setS(String.valueOf(((JSONObject) jsonUnit).get("s")));
			unit.setT(String.valueOf(((JSONObject) jsonUnit).get("t")));
			unit.setU(String.valueOf(((JSONObject) jsonUnit).get("u")));
			FarmDomain.getInstance().getFieldList().add(unit);
		}
	}

}
