package coupons.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import coupons.core.facade.CouponClientFacade;

public class BaseService {
	protected CouponClientFacade getFacadeFromSession(String facadeName, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (CouponClientFacade) session.getAttribute(facadeName);
	}
}
