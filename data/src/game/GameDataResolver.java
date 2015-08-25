package game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import dataobjects.DailyInput;
import dataobjects.DailyTrades;
import dataobjects.GameData;


class GameDataResolver {

	private static GameDataResolver instance;
	private static Object LOCK = new Object();
	public static final List<String> COMPANIES = Collections.unmodifiableList(
			Arrays.asList("Amazon", "Yahoo", "Tesco"));
	
	private GameData data;


	private GameDataResolver() {
		try {
			data = createGameData(COMPANIES);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GameData createGameData(List<String> companies) throws JsonParseException, IOException {
		
		List<List<DailyInput>> inputs = new LinkedList<List<DailyInput>>();
		
		for (String company : companies)
		{
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data/" + company + ".json");
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(resourceAsStream);
			
			JsonNode results = jsonNode.get("query").get("results").get("quote");
			
			List<DailyInput> companyInputs = new LinkedList<DailyInput>();
			for (int i = 0; i < results.size(); i++) {
				
				JsonNode result = results.get(i);
				
				double open = Double.parseDouble(result.get("Open").getTextValue());
				double close = Double.parseDouble(result.get("Close").getTextValue());
				double high = Double.parseDouble(result.get("High").getTextValue());
				double low = Double.parseDouble(result.get("Low").getTextValue());
				
				companyInputs.add(new DailyInput(company, i+1, open, close, high, low));
			}
			
			inputs.add(companyInputs);
		}
		
		// Map to daily trades
		List<DailyTrades> dailyTrades = new LinkedList<DailyTrades>();
		int totalDays = inputs.get(0).size();
		
		for (int i = 0; i < totalDays; i++)
		{
			List<DailyInput> dailyInputs = new LinkedList<DailyInput>();
			for (List<DailyInput> companyInput : inputs) {
				dailyInputs.add(companyInput.get(i));
			}
			
			dailyTrades.add(new DailyTrades(dailyInputs, i+1, totalDays - i));
		}
		
		return new GameData(companies, dailyTrades);
	}

	GameData getGameData() {
		return data;
	}
	
	static GameDataResolver getInstance() {
		synchronized(LOCK) {
			if (instance == null) {
				instance = new GameDataResolver();
			}
		}
		return instance;
		
	}
	
}
