package coupons.ejb;

import javax.ejb.Remote;

import coupons.ejb.data.Income;

@Remote
public interface IncomeService {

	public void storeIncome(Income income);
	
}
