package defyndiantest;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import defyndian.config.DefyndianConfig;
import defyndian.core.DefyndianSensor;
import defyndian.exception.DefyndianDatabaseException;
import defyndian.exception.DefyndianMQException;
import defyndian.messaging.DefyndianEnvelope;
import defyndian.messaging.DefyndianMessage;
import defyndian.messaging.RoutingInfo;

public class TestSensor extends DefyndianSensor<String>{

	public TestSensor(String arg0, int arg1) throws DefyndianMQException, DefyndianDatabaseException {
		super(arg0, arg1);
	}

	@Override
	protected void createMessages(Collection<String> sensorInfo) {
		logger.info("Putting message in outbox");
		
		try {
			for( String s : sensorInfo ){
				DefyndianMessage message = DefyndianMessage.withBody(s);
				logger.debug(message.toJSONString());
				putMessageInOutbox(message);
			}
		} catch (InterruptedException e) {
			logger.error("Interrupted while queueing message, message is lost");
		}
	}

	@Override
	protected Collection<String> sensorFired() {
		return Collections.singleton("Test Message [" + new Date() + "]");
	}
	
	public static void main(String...args){
		DefyndianSensor<String> sensor;
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
