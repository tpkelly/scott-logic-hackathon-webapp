package dataobjects;

import java.util.List;

/**
 * Details of the stock prices for all companies on a single day.
 * @author tkelly
 *
 */
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
	
	/**
	 * The number of days since trading began. The first day is Day 1.
	 * @return The number of days since trading began
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * The number of days of trading remaining in the year. The final day of trading has 0 remaining days.
	 * @return The number of days of trading remaining in the year.
	 */
	public int getRemainingDays() {
		return remainingDays;
	}
	
	/**
	 * The trading data for the day.
	 * @return All of the trading data for the day.
	 */
	public List<DailyInput> getTrades() {
		return dailyTrades;
	}
}
