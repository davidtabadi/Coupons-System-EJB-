package coupons.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import coupons.ejb.data.Income;

@Stateless
public class IncomeServiceBean implements IncomeService {
	
	@Resource(lookup="java:jboss/DefaultJMSConnectionFactory")
	private QueueConnectionFactory factory;
	
	private QueueConnection connection;
	private QueueSession session;
	
	@Resource(lookup="java:/jms/queue/test")
	private Queue queue;
	
	private QueueSender sender;
	private ObjectMessage message;

    public IncomeServiceBean() {
    }

    @PostConstruct
    public void init() {
    	try {
    		connection = factory.createQueueConnection();
    		boolean txFlag = false;
    		session = connection.createQueueSession(txFlag, QueueSession.AUTO_ACKNOWLEDGE);
    		sender = session.createSender(queue);
    		message=session.createObjectMessage();
    		connection.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
	@Override
	public void storeIncome(Income income) {
		try {
			message.setObject(income);
			sender.send(message);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@PreDestroy
	public void destory() {
		try {
			connection.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sender.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}
