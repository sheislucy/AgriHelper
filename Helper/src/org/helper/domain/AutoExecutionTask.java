package org.helper.domain;

import java.io.IOException;
import java.util.TimerTask;

import org.helper.domain.login.UserDomain;
import org.helper.service.ExecuteService;
import org.helper.service.RefreshFarmStep4Service;
import org.helper.service.ServiceFactory;
import org.helper.util.HelperLoggerAppender;
import org.json.simple.parser.ParseException;

public class AutoExecutionTask extends TimerTask {

	private AccountDomain autoOnAccount;

	public AutoExecutionTask(UserDomain user, FarmDomain farm) {
		autoOnAccount = new AccountDomain();
		autoOnAccount.setUserDomain(user);
		autoOnAccount.setFarmDomain(farm);
		autoOnAccount.setAutoCareEnable(true);
	}

	@Override
	public void run() {
		UserDomain.setInstance(autoOnAccount.getUserDomain());
		FarmDomain.setInstance(autoOnAccount.getFarmDomain());
		RefreshFarmStep4Service farmService = ServiceFactory
				.getService(RefreshFarmStep4Service.class);
		try {
			farmService.refreshFarm();
			HelperLoggerAppender.writeLog("刷新状态成功");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			HelperLoggerAppender.writeLog(e.getMessage());
		}
		ExecuteService executeService = ServiceFactory
				.getService(ExecuteService.class);
		executeService.executeAll(UserPreferenceDomain
				.getSeedIndexById(autoOnAccount.getFarmDomain().getUserId()));
	}

}
