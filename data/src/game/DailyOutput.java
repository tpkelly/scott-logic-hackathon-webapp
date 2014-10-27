package game;

import dataobjects.TradeActivity;

public final class DailyOutput {

	private TradeActivity tradeActivity;
	private int availableFunds;
	private int investmentAmount;
	private int day;
	
	DailyOutput(TradeActivity tradeActivity, int availableFunds, int investmentAmount, int day) {
		this.tradeActivity = tradeActivity;
		this.availableFunds = availableFunds;
		this.investmentAmount = investmentAmount;
		this.day = day;
	}
	
	public TradeActivity getTradeActivity() {
		return tradeActivity;
	}
	public int getAvailableFunds() {
		return availableFunds;
	}
	public int getInvestmentAmount() {
		return investmentAmount;
	}
	public int getDay() {
		return day;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyOutput other = (DailyOutput) obj;
		if (availableFunds != other.availableFunds)
			return false;
		if (day != other.day)
			return false;
		if (investmentAmount != other.investmentAmount)
			return false;
		if (tradeActivity == null) {
			if (other.tradeActivity != null)
				return false;
		} else if (!tradeActivity.equals(other.tradeActivity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DailyOutput [tradeActivity=" + tradeActivity
				+ ", availableFunds=" + availableFunds + ", investmentAmount="
				+ investmentAmount + ", day=" + day + "]";
	}
	
	
}
