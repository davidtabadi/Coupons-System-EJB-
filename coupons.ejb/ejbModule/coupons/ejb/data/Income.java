package coupons.ejb.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="INCOME_RECORDS")
public class Income implements Serializable {

	private static final long serialVersionUID = 644729556895816220L;
	
	private Long id;
	private long invokerId;
	private double amount;
	private OperationType type;
	private Date timestamp;
	
	public Income() {
		super();
	}

	public Income(long invokerId, double amount, OperationType type, Date timestamp) {
		super();
		this.invokerId = invokerId;
		this.amount = amount;
		this.type = type;
		this.timestamp = timestamp;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInvokerId() {
		return invokerId;
	}

	public void setInvokerId(long invokerId) {
		this.invokerId = invokerId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Enumerated(EnumType.STRING)
	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Income [id=" + id + ", invokerId=" + invokerId + ", amount=" + amount + ", type=" + type
				+ ", timestamp=" + timestamp + "]";
	}
	
}
