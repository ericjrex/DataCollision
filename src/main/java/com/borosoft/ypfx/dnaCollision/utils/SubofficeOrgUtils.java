package com.borosoft.ypfx.dnaCollision.utils;

import java.util.HashSet;
import java.util.Set;

import com.borosoft.framework.AppConfig;
import com.borosoft.framework.utils.StringUtils;

/**
 * 分局机构编码辅助类
 * 
 * @author Administrator
 *
 */
public class SubofficeOrgUtils {

	private Set<String> subofficeOrgCodeSet = new HashSet<>();

	private static SubofficeOrgUtils subofficeOrgUtils = null;

	private SubofficeOrgUtils() {
	}

	public static SubofficeOrgUtils getInstance() {
		if (subofficeOrgUtils == null) {
			synchronized (SubofficeOrgUtils.class) {
				if (subofficeOrgUtils == null) {
					subofficeOrgUtils = new SubofficeOrgUtils();
					subofficeOrgUtils.intSubofficeOrgCode();
				}
			}
		}
		return subofficeOrgUtils;
	}

	/**
	 * 获取分局机构编码前缀
	 * 
	 * @return
	 */
	public static Set<String> getOrgCodes() {
		return getInstance().subofficeOrgCodeSet;
	}

	/**
	 * 是否为分局机构
	 * 
	 * @param orgCode
	 * @return
	 */
	public static boolean isSubofficeOrg(String rganizationCode) {
		return getInstance().isSuboffice(rganizationCode);
	}

	private boolean isSuboffice(String rganizationCode) {
		if (StringUtils.isBlank(rganizationCode) || rganizationCode.length() <= 6) {
			return false;
		}
		rganizationCode = rganizationCode.substring(0, rganizationCode.length() - 6);
		return subofficeOrgCodeSet.contains(rganizationCode);
	}

	private void intSubofficeOrgCode() {
		String orgCodes = AppConfig.getConfig().getString("suboffice.orgCode");
		String[] orgCodeArr = orgCodes.split("\\|");
		for (String orgCode : orgCodeArr) {
			subofficeOrgCodeSet.add(orgCode);
		}
	}

}
