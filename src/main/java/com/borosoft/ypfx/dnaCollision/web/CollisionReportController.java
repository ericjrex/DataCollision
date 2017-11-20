package com.borosoft.ypfx.dnaCollision.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.borosoft.framework.commons.query.Criteria;
import com.borosoft.framework.commons.query.Order;
import com.borosoft.framework.commons.query.Query;
import com.borosoft.framework.utils.PinyinUtils;
import com.borosoft.platform.org.domain.Org;
import com.borosoft.platform.org.service.OrgService;
import com.borosoft.ypfx.dnaCollision.utils.SubofficeOrgUtils;

@RequestMapping("/collision/report/")
@Controller
public class CollisionReportController {
	
	@Resource(name = "orgService")
	private OrgService orgService;
	
	private String pageIndex = "/dna/report/";
	
	@RequestMapping("index.do")
	public String index(HttpServletRequest request){
		Set<String> orgSuffs = SubofficeOrgUtils.getOrgCodes();
		Set<String> organizationCodes = new HashSet<>();
		for (String organizationCode : orgSuffs) {
			organizationCodes.add(organizationCode + "000000");
		}
		Query query = Query.create(Org.class);
		query.add(Criteria.in("organizationCode", organizationCodes));
		query.addOrder(Order.asc("name"));
		List<Org> orgList = orgService.getOrg(query).getResultList();
		if (orgList != null && orgList.size() > 0) {
			String pinyin = null;
			for (Org org : orgList) {
				pinyin = PinyinUtils.converterToFirstSpell(org.getName()) + ","
						+ PinyinUtils.converterToSpell(org.getName());
				org.setExtend1(pinyin);
			}
			request.setAttribute("orgList", orgList);
		}
		// }

		request.setAttribute("reportName", request.getParameter("reportName"));
		//request.setAttribute("queryLabel", "案件立案日期区间");
		
		return pageIndex + "/default_Report.jsp";
	}

}
