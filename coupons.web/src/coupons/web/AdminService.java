package coupons.web;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupons.core.CouponException;
import coupons.core.CouponsSystem;
import coupons.core.facade.AdminFacade;
import coupons.core.facade.ClientType;
import coupons.ejb.IncomeDaoLocal;
import coupons.ejb.data.Income;
import coupons.web.beans.Message;


@Path("/coupons/admin")
public class AdminService {

	private IncomeDaoLocal daoStub;

	public AdminService() {
		try {
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
		AdminFacade admin = (AdminFacade) CouponsSystem.getInstance().login(username, password, ClientType.ADMIN);
		HttpSession session = request.getSession(true);
		session.setAttribute("admin", admin);

		return new Message(session.getId());
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("admin") != null) {
			session.invalidate();
			return new Message("Successful logout");
		} else {
			throw new RuntimeException("Could not logout");
		}
	}
	
	@GET
	@Path("/income")
	@Produces(MediaType.APPLICATION_JSON)
	public Income[] viewAllIncome(
			@QueryParam("byCompId") Long compId, 
			@QueryParam("byCustId")Long custId ,
			@Context HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("admin") != null) {
			if(compId!=null) {
				return this.daoStub.viewIncomeByCompany(compId).toArray(new Income[0]);
			}else if(custId!=null) {
				return this.daoStub.viewIncomeByCustomer(custId).toArray(new Income[0]);
			}else {
				return this.daoStub.viewAllIncome().toArray(new Income[0]);
			}
		}else{
			throw new RuntimeException("Admin must be logged in");
		}
	}
	
//	@GET
//	@Path("/income/byCompany/{compId}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Income[] viewIncomeByCompany(@PathParam("compId")long id){
//		return this.daoStub.viewIncomeByCompany(id, OperationType.CUSTOMER_PURCHASE).toArray(new Income[0]);
//	}
//	
//	@GET
//	@Path("/income/byCustomer/{custId}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Income[] viewIncomeByCustomer(@PathParam("custId")long id){
//		return this.daoStub.viewIncomeByCustomer(id, OperationType.CUSTOMER_PURCHASE).toArray(new Income[0]);
//	}
}
