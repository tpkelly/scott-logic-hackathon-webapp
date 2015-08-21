package tradingstrategy;

import game.DailyOutput;
import game.TradingManager;
import dataobjects.DailyTrades;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public abstract class BaseTradingStrategy {

	protected TradingManager tradingManager;

	protected BaseTradingStrategy () {
		tradingManager = new TradingManager();
	}

	public abstract void makeDailyTrade(DailyTrades dailyTrades) throws InsufficientFundsException, InsufficientSharesException;
	
	public final DailyOutput finaliseDailyTrade(DailyTrades dailyTrades)
	{
		return tradingManager.finalizeTrade(dailyTrades.getTrades().get(0));
	}
}