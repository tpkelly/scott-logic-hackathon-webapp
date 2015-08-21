package dataobjects;

import java.util.List;

public class DailyTrades {

	private List<DailyInput> dailyTrades;
	private int day;
	private int remainingDays;
	
	public DailyTrades(List<DailyInput> inputs, int day, int remainingDays)
	{
		this.day = day;
		this.remainingDays = remainingDays;
		this.dailyTrades = inputs;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getRemainingDays() {
		return remainingDays;
	}
	
	public List<DailyInput> getTrades() {
		return dailyTrades;
	}
}
