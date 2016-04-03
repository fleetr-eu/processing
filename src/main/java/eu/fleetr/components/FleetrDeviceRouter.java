package eu.fleetr.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.json.JSONObject;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.BridgeHandler;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public class FleetrDeviceRouter extends AbstractMessageRouter {

	private final LinkedHashMap<Long, SubscribableChannel> channels = new LinkedHashMap<Long, SubscribableChannel>();

	ExpressionParser parser = new SpelExpressionParser();

	private String consumerName;

	public FleetrDeviceRouter(String consumerName) {
		this.consumerName = consumerName;
	}
	
	protected SubscribableChannel getChannel(Message<?> message) {
		JSONObject payload = (JSONObject) message.getPayload();
		Long deviceId = payload.getLong("deviceId"); 
		
		SubscribableChannel channel = this.channels.get(deviceId);

		if (channel == null) {

			channel = new DirectChannel();

			((DirectChannel) channel).setBeanName("channel-" + deviceId);
			logger.info("Created new channel:" + ((DirectChannel) channel).getComponentName());
			
			Object t = getApplicationContext().getBean(consumerName);

			DirectChannel consumer = (DirectChannel)t;
			
			BridgeHandler bridge = new BridgeHandler();
			bridge.setOutputChannel(consumer);
			
			EventDrivenConsumer bridgeConsumer = new EventDrivenConsumer(channel, bridge);
			bridgeConsumer.start();
			
			//channel.subscribe(bridge);
			this.channels.put(deviceId, channel);
		}
		logger.info("Found existing channel:" + ((DirectChannel) channel).getComponentName());
		return channel;
	}

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		ArrayList<MessageChannel> list = new ArrayList<MessageChannel>();
		list.add(getChannel(message));
		return Collections.unmodifiableList(list);
	}
}