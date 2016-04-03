package eu.fleetr.components;

import org.json.JSONObject;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

public class StringToJSONTransformer {
	
	@Transformer
	public Message<JSONObject> transform(Message<String> message) {
		JSONObject object = new JSONObject(message.getPayload().toString());
		return new GenericMessage<JSONObject>(object);
	}
}
