package eu.fleetr.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringToJSONTransformer {
	
	@Transformer
	public Message<Map<String, Object>> transform(Message<String> message) {
		HashMap<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(message.getPayload(), HashMap.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new GenericMessage<Map<String, Object>> (result);
	}
}
