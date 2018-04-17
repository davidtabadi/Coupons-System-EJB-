package coupons.ejb;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import coupons.ejb.data.Income;
import coupons.ejb.data.OperationType;



@Stateless
public class IncomeDaoBean implements IncomeDaoLocal {
	
	@PersistenceContext(unitName="coupons")
	private EntityManager em;
	
    public IncomeDaoBean() {
    }

	@Override
	public Income storeIncome(Income income) {
		em.persist(income);
		return income;
	}

	@Override
	public Collection<Income> viewAllIncome() {
		String jpql = "SELECT i FROM Income AS i ORDER BY i.timestamp DESC";
		return this.em.createQuery(jpql, Income.class).getResultList();
	}

	@Override
	public Collection<Income> viewIncomeByCompany(long compId) {
		String jpql = "SELECT i FROM Income AS i WHERE i.invokerId = :invokerId AND i.type <> :type ORDER BY  i.timestamp DESC";
		return this.em.createQuery(jpql, Income.class)
			.setParameter("invokerId", compId)
			.setParameter("type", OperationType.CUSTOMER_PURCHASE)
			.getResultList();
	}

	@Override
	public Collection<Income> viewIncomeByCustomer(long custId) {
		String jpql = "SELECT i FROM Income AS i WHERE i.invokerId = :invokerId AND i.type = :type ORDER BY  i.timestamp DESC";
		return this.em.createQuery(jpql, Income.class)
			.setParameter("invokerId", custId)
			.setParameter("type", OperationType.CUSTOMER_PURCHASE)
			.getResultList();
	}

}
