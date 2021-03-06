package json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GsonTest {
	
	public static void main(String[] args) {
					
		// for simple strings
		//Gson gson = new Gson();
		//Type type = new TypeToken<Map<String, Object>>(){}.getType();
		//Map<String, Object> map =  gson.fromJson("{'key1':'123','key2':'456'}", type);
		//printMap(map);
		
		// for json with arrays
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Object.class, new NaturalDeserializer());
		Gson gson = gsonBuilder.create();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> natural = (Map<String, Object>)gson.fromJson("{'Client':'Popescu','Project':['Anytime CRM' , 'Anytime Sales']}", Object.class);

		for (String key : natural.keySet()) {
			System.out.print("key = " + key);
			System.out.print("  value = ");
			Object val = natural.get(key);
			if (val instanceof Object[]) {
				System.out.println(Arrays.asList((Object[])val));
			} else {
				System.out.println(val);
			}
		}						
	}
	
	private static void printMap(Map<String, Object> map) {
		Iterator iterator = map.keySet().iterator();		 
		while (iterator.hasNext()) {
		   String key = iterator.next().toString();
		   String value = map.get(key).toString();		 
		   System.out.println(key + " : " + value);
		}
	}
	
	private static class NaturalDeserializer implements JsonDeserializer<Object> {
		public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
			if (json.isJsonNull())
				return null;
			else if (json.isJsonPrimitive())
				return handlePrimitive(json.getAsJsonPrimitive());
			else if (json.isJsonArray())
				return handleArray(json.getAsJsonArray(), context);
			else
				return handleObject(json.getAsJsonObject(), context);
		}

		private Object handlePrimitive(JsonPrimitive json) {
			if (json.isBoolean())
				return json.getAsBoolean();
			else if (json.isString())
				return json.getAsString();
			else {
				BigDecimal bigDec = json.getAsBigDecimal();
				// Find out if it is an int type
				try {
					bigDec.toBigIntegerExact();
					try {
						return bigDec.intValueExact();
					} catch (ArithmeticException e) {
					}
					return bigDec.longValue();
				} catch (ArithmeticException e) {
				}
				// Just return it as a double
				return bigDec.doubleValue();
			}
		}

		private Object handleArray(JsonArray json, JsonDeserializationContext context) {
			Object[] array = new Object[json.size()];
			for (int i = 0; i < array.length; i++)
				array[i] = context.deserialize(json.get(i), Object.class);
			return array;
		}

		private Object handleObject(JsonObject json, JsonDeserializationContext context) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<String, JsonElement> entry : json.entrySet())
				map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
			return map;
		}
	}


}
