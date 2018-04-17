package coupons.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import coupons.ejb.data.Income;


@MessageDriven(activationConfig = { 
		@ActivationConfigProperty(propertyName="destination", propertyValue="java:/jms/queue/test"), 
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
		})
public class IncomeConsumerMDB implements MessageListener {
	
	@EJB
	private IncomeDaoLocal daoStub;

    public IncomeConsumerMDB() {
    }

    public void onMessage(Message message) {
    	 try {
 			if (message instanceof ObjectMessage) {
 				Object obj = ((ObjectMessage) message).getObject();
 				if (obj instanceof Income) {
 					Income income = (Income)obj;
 					this.daoStub.storeIncome(income);
 				}else {
 					throw new Exception("Illegal message content");
 				}
 			}else {
 				throw new Exception("Illegal message type");
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
    }
}
    

