package game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import dataobjects.DailyInput;
import dataobjects.GameData;


public class GameDataResolver {

	private static GameDataResolver instance;
	private static Object LOCK = new Object();
	public static final List<String> COMPANIES = Collections.unmodifiableList(
			Arrays.asList("Amazon", "Yahoo", "Tesco"));
	
	private final Map<String, GameData> data = new HashMap<String, GameData>();


	private GameDataResolver() {
		for (String company : COMPANIES) {
			try {
				data.put(company, createGameData(company));
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private GameData createGameData(String company) throws JsonParseException, IOException {
		
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/" + company + ".json");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(resourceAsStream);
		
		JsonNode results = jsonNode.get("query").get("results").get("quote");
		
		List<DailyInput> inputs = new LinkedList<DailyInput>();
		for (int i = 0; i < results.size(); i++) {
			
			JsonNode result = results.get(i);
			
			double open = Double.parseDouble(result.get("Open").getTextValue());
			double close = Double.parseDouble(result.get("Close").getTextValue());
			double high = Double.parseDouble(result.get("High").getTextValue());
			double low = Double.parseDouble(result.get("Low").getTextValue());
			
			int day = i + 1;
			inputs.add(new DailyInput(day, results.size() - day, open, close, high, low));
		}
		
		return new GameData(company, inputs);
	}

	public GameData getGameData(String company) {
		GameData gameData = data.get(company);
		if (gameData == null) {
			throw new IllegalArgumentException("supplied company is invalid");
		}
		return gameData;
	}
	
	public static GameDataResolver getInstance() {
		synchronized(LOCK) {
			if (instance == null) {
				instance = new GameDataResolver();
			}
		}
		return instance;
		
	}
	
}
