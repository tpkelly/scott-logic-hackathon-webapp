package dataobjects;

import java.util.List;

public class GameData {
	private List<String> names;
	private List<DailyTrades> inputs;
	
	public GameData(List<String> companyNames, List<DailyTrades> inputs) {
		this.names = companyNames;
		this.inputs = inputs;
	}
	
	public List<String> getNames() {
		return this.names;
	}
	
	public List<DailyTrades> getInputs() {
		return this.inputs;
	}
	
}
