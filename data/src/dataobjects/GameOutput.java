package dataobjects;

import game.DailyOutput;

import java.util.List;

public class GameOutput {

	private List<DailyOutput> dailyOutputs;
	private int totalFunds;
	
	public GameOutput(List<DailyOutput> dailyOutputs, int totalFunds) {
		this.dailyOutputs = dailyOutputs;
		this.totalFunds = totalFunds;
	}
	
	/** 
	 * The details of each day's trading over the year.
	 * @return The details of each day's trading over the year.
	 */
	public List<DailyOutput> getDailyOutputs() {
		return dailyOutputs;
	}
	
	/**
	 * The final funds total after a year of trading.
	 * @return The final funds total after a year of trading.
	 */
	public int getTotalFunds() {
		return totalFunds;
	}
	
	
}
