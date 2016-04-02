package eu.fleetr.components;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

@Scope("prototype")
public class FleetrMessageTransformer {
	
	@Transformer
    public Message transform(Message message) {
		
		JSONObject object = new JSONObject(message.getPayload().toString());
		Long deviceId = object.getLong("deviceId");
		GenericMessage newMessage = new GenericMessage ("{\"deviceId\":"+deviceId+", \"message\":\"Hello there\"}");
		
        System.out.println("---> " + newMessage.getPayload().toString());
        return newMessage;
    }
	
	public void handleMessage(Message<?> message) throws MessagingException {
		System.out.println("--->"+message.getPayload().toString());
	}
}