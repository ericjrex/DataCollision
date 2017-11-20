package com.borosoft.ypfx.desk.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/desk/")
public class DeskController {

	/**
	 * 我的待办
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "daiban.do")
	public String viewDaiban(HttpServletRequest request) {
		request.setAttribute("groupCode", request.getParameter("groupCode"));
		return "/desk/desktop_daiban.jsp";
	}

	/**
	 * 我的提醒
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tixing.do")
	public String viewTixing(HttpServletRequest request) {
		request.setAttribute("groupCode", request.getParameter("groupCode"));
		return "/desk/desktop_tixing.jsp";
	}
	
}
