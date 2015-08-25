package dataobjects;

public class DailyInput {
	
	private String companyName;
	private int day;
	private double open;
	private double close;
	private double high;
	private double low;
	
	public DailyInput(String companyName, int day, double open, double close,
			double high, double low) {
		this.companyName = companyName;
		this.day = day;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
	}
	
	/**
	 * The company this daily trading corresponds to.
	 * @return The company this daily trading corresponds to. 
	 */
	public String getCompany() {
		return companyName;
	}

	/**
	 * The number of days since trading began. The first day is Day 1.
	 * @return The number of days since trading began
	 */
	public int getDay() {
		return day;
	}
	
	/** 
	 * The price of the stock at the opening of the markets.
	 * @return The price of the shares at the opening of the markets.
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * The price of the stock at the close of the markets. This is the price which shares are bought and sold at.
	 * @return The price of the stock at the close of the markets.
	 */
	public double getClose() {
		return close;
	}

	/**
	 * The highest price reached by the stock during the day.
	 * @return The highest price reached by the stock during the day.
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * The lowest price reached by the stock during the day.
	 * @return The lowest price reached by the stock during the day.
	 */
	public double getLow() {
		return low;
	}
	
	
	
}
