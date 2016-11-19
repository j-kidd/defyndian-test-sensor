package defyndian.sensor.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.rabbitmq.client.Connection;
import defyndian.config.DefyndianConfig;
import defyndian.core.DefyndianSensor;
import defyndian.datastore.exception.DatastoreCreationException;
import defyndian.exception.ConfigInitialisationException;
import defyndian.exception.DefyndianDatabaseException;
import defyndian.exception.DefyndianMQException;
import defyndian.messaging.*;
import defyndian.messaging.messages.BasicDefyndianMessage;
import defyndian.messaging.messages.DefyndianMessage;
import defyndian.messaging.routing.DefyndianRoutingKey;
import defyndian.messaging.routing.RoutingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSensor extends DefyndianSensor<String>{

	private static final Logger logger = LoggerFactory.getLogger(TestSensor.class);

	public TestSensor(String name, int delay, Connection connection, DefyndianConfig config) throws DefyndianMQException, DefyndianDatabaseException, ConfigInitialisationException, DatastoreCreationException, IOException {
		super(name, delay, connection, config);
	}

	@Override
	protected void createMessages(Collection<String> sensorInfo) {
		logger.info("Putting message in outbox");

        final RoutingInfo routingInfo = RoutingInfo.getRoute(
                config.getRabbitMQDetails().getExchange(),
                DefyndianRoutingKey.getDefaultKey(getName()));
        for( String s : sensorInfo ){
            DefyndianMessage message = new BasicDefyndianMessage(s);
            final DefyndianEnvelope<DefyndianMessage> envelope =
                    new DefyndianEnvelope<>(routingInfo, message);
            try {
                publisher.publish(envelope);
                logger.info("Published [{}]", envelope);
            } catch (InterruptedException e) {
                logger.error("Couldn't publish: [{}]", envelope);
            }
        }
	}

	@Override
	protected Collection<String> sensorFired() {
		return Collections.singleton("Test Message [" + new Date() + "]");
	}
	
	public static void main(String...args) throws Exception {
        final String name = "TestSensor";
		final DefyndianSensor<String> sensor;
		final DefyndianConfig config = DefyndianConfig.getConfig(name);
        final Connection connection = config.getRabbitMQDetails().getConnectionFactory().newConnection();
        sensor = new TestSensor(name, 10, connection, config);
        sensor.start();
	}
	
}
