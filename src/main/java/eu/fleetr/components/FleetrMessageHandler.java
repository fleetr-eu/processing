package eu.fleetr.components;

import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Scope("prototype")
public class FleetrMessageHandler implements MessageHandler {
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		System.out.println("--->"+message.getPayload().toString());
	}
}