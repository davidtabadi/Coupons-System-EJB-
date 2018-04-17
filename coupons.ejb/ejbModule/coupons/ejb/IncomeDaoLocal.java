package coupons.ejb;

import java.util.Collection;

import javax.ejb.Local;

import coupons.ejb.data.Income;

@Local
public interface IncomeDaoLocal {
	
	public Income storeIncome(Income income);
	
	public Collection<Income> viewAllIncome();

	public Collection<Income> viewIncomeByCompany(long compId);
	
	public Collection<Income> viewIncomeByCustomer(long custId);

}
