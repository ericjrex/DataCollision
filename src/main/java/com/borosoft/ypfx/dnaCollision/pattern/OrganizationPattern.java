package com.borosoft.ypfx.dnaCollision.pattern;

import org.springframework.stereotype.Service;

import com.borosoft.commons.utils.UserInfo;
import com.borosoft.middleware.dirEntry.CustomPatternRule;
import com.borosoft.ypfx.dnaCollision.utils.SubofficeOrgUtils;

@Service("organizationPattern")
public class OrganizationPattern implements CustomPatternRule{

	@Override
	public String warpPrefixValue(String expression, UserInfo userInfo) {
		String rganizationCode = userInfo.getOrganizationCode();
		if (SubofficeOrgUtils.isSubofficeOrg(rganizationCode)) {
		 // 分局及其下属单位
			if (rganizationCode.length() == 12) {
				rganizationCode = rganizationCode.substring(0, rganizationCode.length() - 6);
			}
		} else {	
			// 市级单位
			rganizationCode = "4419";
		}
		return rganizationCode;
	}

}
