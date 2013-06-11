package org.helper.util;

import java.util.HashMap;
import java.util.Map;

public enum EmCropStatus {
	SEED(1, "未成熟"), SPROUT(2, "发芽"), LEAF(3, "嫩叶"), TEMP(4, "嫩叶2"), FLOWER(5,
			"开花"), RIPE(6, "成熟"), WITHERED(7, "待铲地"), EMPTY(0, "空盆");
	private int id;
	private String status;

	public static Map<Integer, EmCropStatus> MAP = new HashMap<Integer, EmCropStatus>();

	static {
		for (EmCropStatus em : EmCropStatus.values()) {
			MAP.put(em.getId(), em);
		}
	}

	public static String getStatusName(String statusId) {
		int id = Integer.parseInt(statusId);
		return MAP.get(id).getStatus();
	}

	private EmCropStatus(int id, String status) {
		this.id = id;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
