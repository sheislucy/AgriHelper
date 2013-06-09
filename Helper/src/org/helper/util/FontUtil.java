package org.helper.util;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;

public class FontUtil {
	public static Font generateUnderline() {
		HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>();
		hm.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); // 定义是否有下划线
		hm.put(TextAttribute.SIZE, 12); // 定义字号
		hm.put(TextAttribute.FAMILY, "Simsun"); // 定义字体名
		Font font = new Font(hm); // 生成字号为12，字体为宋体，字形带有下划线的字体
		return font;
	}
}
