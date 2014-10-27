package dataobjects;

import java.util.List;

public class GameData {
	private String name;
	private List<DailyInput> inputs;
	
	public GameData(String companyName, List<DailyInput> inputs) {
		this.name = companyName;
		this.inputs = inputs;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<DailyInput> getInputs() {
		return this.inputs;
	}
	
}
