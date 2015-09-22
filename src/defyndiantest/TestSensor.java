package defyndiantest;

import java.util.Date;

import defyndian.config.DefyndianConfig;
import defyndian.core.DefyndianSensor;
import exception.DefyndianDatabaseException;
import exception.DefyndianMQException;
import messaging.DefyndianEnvelope;
import messaging.DefyndianMessage;
import messaging.RoutingInfo;

public class TestSensor extends DefyndianSensor{

	public TestSensor(String arg0, int arg1) throws DefyndianMQException, DefyndianDatabaseException {
		super(arg0, arg1);
	}

	@Override
	protected void createMessages() {
		logger.info("Putting message in outbox");
		try {
			DefyndianMessage message = DefyndianMessage.withBody(("Test Message [" + new Date() + "]"));
			logger.debug(message.toJSONString());
			putMessageInOutbox(message);
		} catch (InterruptedException e) {
			logger.error("Interrupted while queueing message, message is lost");
		}
	}

	@Override
	protected boolean sensorFired() {
		return true;
	}
	
	public static void main(String...args){
		DefyndianSensor sensor;
		try {
			sensor = new TestSensor("Test", 10);
			sensor.start();
		} catch (DefyndianMQException | DefyndianDatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
