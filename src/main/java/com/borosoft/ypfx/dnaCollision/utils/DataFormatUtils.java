package com.borosoft.ypfx.dnaCollision.utils;

import java.util.Date;

import com.borosoft.framework.utils.DateTimeUtils;
import com.borosoft.framework.utils.StringUtils;

public abstract class DataFormatUtils {

	public static String formatData(Object value) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		if (value instanceof Date) {
			Date d = (Date) value;
			return DateTimeUtils.format(d, DateTimeUtils.DF_DATETIME);
		}
		return value.toString();
	}
	
}
