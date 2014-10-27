package dataobjects;

public class TradeActivity {
	
	private int buy;
	private int sell;
	
	public TradeActivity(int buy, int sell) {
		this.buy = buy;
		this.sell = sell;
	}

	public int getBuy() {
		return buy;
	}

	public int getSell() {
		return sell;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeActivity other = (TradeActivity) obj;
		if (buy != other.buy)
			return false;
		if (sell != other.sell)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TradeActivity [buy=" + buy + ", sell=" + sell + "]";
	}
}
