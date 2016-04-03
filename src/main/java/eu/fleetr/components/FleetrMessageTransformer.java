package eu.fleetr.components;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

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
    public Message<String> transform(Message<JSONObject> message) {
		
		JSONObject object = (JSONObject) message.getPayload();
		Long deviceId = object.getLong("deviceId");
		
		JSONObject vehicle = new JSONObject(mongoTemplate.findOne(query(where("unitId").is(deviceId)), String.class, "vehicles"));
		if (vehicle != null) {
			object.append("vehicle", vehicle);
		}	
		
		GenericMessage<String> newMessage = new GenericMessage<String> (object.toString());

        return newMessage;
    }
}