package coupons.web;

import java.util.Date;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import coupons.core.CouponException;
import coupons.core.CouponsSystem;
import coupons.core.beans.Coupon;
import coupons.core.facade.ClientType;
import coupons.core.facade.CompanyFacade;
import coupons.ejb.IncomeDaoLocal;
import coupons.ejb.IncomeService;
import coupons.ejb.data.Income;
import coupons.ejb.data.OperationType;
import coupons.web.beans.Message;

@Path("/coupons/company")
public class CompanyService extends BaseService {

	private IncomeService serviceStub;
	private IncomeDaoLocal daoStub;

	public CompanyService() {
		try {
			this.serviceStub = (IncomeService) new InitialContext()
					.lookup("java:global/coupons.app/coupons.ejb/IncomeServiceBean!coupons.ejb.IncomeService");
			this.daoStub = (IncomeDaoLocal) new InitialContext()
					.lookup("java:global/coupons.app/coupons.ejb/IncomeDaoBean!coupons.ejb.IncomeDaoLocal");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GET
	@Path("/login/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message login(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException {
		CompanyFacade company = (CompanyFacade) CouponsSystem.getInstance().login(username, password,
				ClientType.COMPANY);
		HttpSession session = request.getSession(true);
		session.setAttribute("company", company);

		return new Message(session.getId());
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("company") != null) {
			session.invalidate();
			return new Message("Successful logout");
		} else {
			throw new RuntimeException("Could not logout");
		}
	}

	@GET
	@Path("/coupon/create/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message createCoupon(@PathParam("price") double price, @Context HttpServletRequest request) {
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		Long compId = company.getId();
		Coupon newCoupon = company.createCoupon(new Coupon(price));
		serviceStub.storeIncome(new Income(compId, 100, OperationType.COMPANY_CREATE, new Date()));
		return new Message("Company #" + compId + " successfully created coupon #" + newCoupon.getId());
	}

	@GET
	@Path("/coupon/update/{id}/{newPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message updateCoupon(@PathParam("id") Long id, @PathParam("newPrice") double newPrice,
			@Context HttpServletRequest request) {
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		Long compId = company.getId();
		company.updateCoupon(new Coupon(id, newPrice));
		serviceStub.storeIncome(new Income(compId, 10, OperationType.COMPANY_UPDATE, new Date()));
		return new Message("Company #" + compId + " successfully updated coupon #" + id);
	}
	
	@GET
	@Path("/income")
	@Produces(MediaType.APPLICATION_JSON)
	public Income[] viewIncome(@Context HttpServletRequest request){
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		if(company!=null) {
			Long compId = company.getId();
			return this.daoStub.viewIncomeByCompany(compId).toArray(new Income[0]);
		}else{
			throw new RuntimeException("Company must be logged in");
		}
	}
}
