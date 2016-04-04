package eu.fleetr.helpers;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VehicleHelper {

	private MongoTemplate mongoTemplate;

	public VehicleHelper(MongoDbFactory mongoFactory) {
		mongoTemplate = new MongoTemplate(mongoFactory);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getVehicle(Number deviceId) {
		
		String vehicle = mongoTemplate.findOne(query(where("unitId").is(deviceId)), String.class, "vehicles");
		
		try {
			return new ObjectMapper().readValue(vehicle,HashMap.class);
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return null;
	}
}
