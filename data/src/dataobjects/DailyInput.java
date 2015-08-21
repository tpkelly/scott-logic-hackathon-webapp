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
	
	public String getCompany() {
		return companyName;
	}

	public int getDay() {
		return day;
	}
	
	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}
	
	
	
}
