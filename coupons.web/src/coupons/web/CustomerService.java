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
import coupons.core.facade.CustomerFacade;
import coupons.ejb.IncomeDaoLocal;
import coupons.ejb.IncomeService;
import coupons.ejb.data.Income;
import coupons.ejb.data.OperationType;
import coupons.web.beans.Message;

@Path("/coupons/customer")
public class CustomerService extends BaseService {

	private IncomeService serviceStub;
	private IncomeDaoLocal daoStub;

	public CustomerService() {
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
		CustomerFacade customer = (CustomerFacade) CouponsSystem.getInstance().login(username, password,
				ClientType.CUSTOMER);
		HttpSession session = request.getSession(true);
		session.setAttribute("customer", customer);

		return new Message(session.getId());
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("customer") != null) {
			session.invalidate();
			return new Message("Successful logout");
		} else {
			throw new RuntimeException("Could not logout");
		}
	}

	@GET
	@Path("/coupon/purchase/{id}/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message purchaseCoupon(@PathParam("id") Long id, @PathParam("price") double price,
			@Context HttpServletRequest request) {
		CustomerFacade customer = (CustomerFacade) getFacadeFromSession("customer", request);
		Long custId = customer.getId();
		customer.purchaseCoupon(new Coupon(id, price));
		serviceStub.storeIncome(new Income(custId, price, OperationType.CUSTOMER_PURCHASE, new Date()));
		return new Message("Customer #" + custId + " successfully purchased coupon #" + id + " for $" + price);
	}

	@GET
	@Path("/income")
	@Produces(MediaType.APPLICATION_JSON)
	public Income[] viewIncome(@Context HttpServletRequest request){
		CustomerFacade customer = (CustomerFacade) getFacadeFromSession("customer", request);
		if(customer!=null) {
			Long custId = customer.getId();
			return this.daoStub.viewIncomeByCustomer(custId).toArray(new Income[0]);
		}else{
			throw new RuntimeException("Customer must be logged in");
		}
	}
}
