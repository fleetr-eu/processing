package eu.fleetr.components;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;

@Scope("prototype")
public class FleetrMessageTransformer {
	
	private MongoTemplate mongoTemplate;

	class DBObjectToStringConverter implements Converter<DBObject, String> {
		  public String convert(DBObject source) {
		    return source == null ? null : source.toString();
		  }
		}
	
	public FleetrMessageTransformer (MongoDbFactory mongoFactory) {
		 mongoTemplate = new MongoTemplate(mongoFactory);
	}	 
	
	@Transformer
    public Message<String> transform(Message<Map<String, Object>> message) {
		
		Number deviceId = (Number) message.getPayload().get("deviceId");

		String vehicleString = mongoTemplate.findOne(query(where("unitId").is(deviceId)), String.class, "vehicles");
		Map<String, Object> vehicle = null;
		try {
			vehicle = new ObjectMapper().readValue(vehicleString, HashMap.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (vehicle != null) {
			message.getPayload().put("vechile", vehicle);
		}	
		
		try {
			return new GenericMessage(new ObjectMapper().writeValueAsString(message.getPayload()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return new GenericMessage(null);
    }
}