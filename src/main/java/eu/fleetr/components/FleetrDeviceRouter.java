package eu.fleetr.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.endpoint.PollingConsumer;
import org.springframework.integration.handler.BridgeHandler;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

public class FleetrDeviceRouter extends AbstractMessageRouter {

	private final LinkedHashMap<String, QueueChannel> channels = new LinkedHashMap<String, QueueChannel>();

	private String processorChannelName;

	public FleetrDeviceRouter (String processorChannelName) {
		this.processorChannelName = processorChannelName;
		
	}
	protected QueueChannel getChannel(Message<?> message) {
		String deviceId = getDeviceId(message);
		QueueChannel channel = this.channels.get(deviceId);
		if (channel == null) {
			channel = this.channels.get(deviceId);
			if (channel == null) {
				
				logger.info("Created new channel for deviceId: "+deviceId);
				BlockingQueue<Message<?>> q = new LinkedBlockingQueue<Message<?>>();
			    channel = new QueueChannel(q);
			    channel.setBeanFactory(getBeanFactory());
				BridgeHandler bridge = new BridgeHandler();
				bridge.setBeanFactory(getApplicationContext());
				bridge.setOutputChannelName(processorChannelName);
				
				PollingConsumer consumer = new PollingConsumer(channel, bridge);
				consumer.setMaxMessagesPerPoll(1);
				consumer.setTrigger(new PeriodicTrigger(10, TimeUnit.MILLISECONDS));
			    consumer.setBeanFactory(getApplicationContext());
//			    pc.setTaskScheduler(taskSched);
				consumer.afterPropertiesSet();
				consumer.setAutoStartup(true);
				consumer.start();
				
			    this.channels.put(deviceId, channel);
			}
			return channel;
		}
		logger.info("Found existing channel for deviceId: "+deviceId);
		return channel;
	}

	protected String getDeviceId(Message<?> message) {
		return message.getPayload().toString();
	}

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		ArrayList<MessageChannel> list = new ArrayList<MessageChannel>();
		list.add(getChannel(message));
		return Collections.unmodifiableList(list);     
	}
}