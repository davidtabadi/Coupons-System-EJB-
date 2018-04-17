package coupons.ejb.helper;

import javax.ejb.Stateless;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

@Stateless
@JMSDestinationDefinitions({
	@JMSDestinationDefinition(
			name="java:/jms/queue/test",
			interfaceName="javax.jms.Queue",
			destinationName="testQueue",
			description="This is a test queue for deployment")
})
public class TestQueueDeployer {

}
