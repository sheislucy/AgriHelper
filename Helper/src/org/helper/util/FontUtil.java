package org.helper.util;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;

public class FontUtil {
	public static Font generateUnderline() {
		HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>();
		hm.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); // �����Ƿ����»���
		hm.put(TextAttribute.SIZE, 12); // �����ֺ�
		hm.put(TextAttribute.FAMILY, "Simsun"); // ����������
		Font font = new Font(hm); // �����ֺ�Ϊ12������Ϊ���壬���δ����»��ߵ�����
		return font;
	}
}
