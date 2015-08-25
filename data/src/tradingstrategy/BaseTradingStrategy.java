package tradingstrategy;

import game.DailyOutput;
import game.TradingManager;
import dataobjects.DailyTrades;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

/**
 * The trading algorithm used to execute trading. Each "day", the {@link #makeDailyTrade(DailyTrades) makeDailyTrade} will be fed data about the stock prices on that day,
 * and will use the tradingManager to buy or sell shares. 
 * @author tkelly
 *
 */
public abstract class BaseTradingStrategy {

	protected TradingManager tradingManager;

	protected BaseTradingStrategy () {
		tradingManager = new TradingManager();
	}

	/**
	 * Take the OHLC (Open, High, Low, Close) price data for the day from all companies, and execute trades on the data. Buying and selling is done at the "Close" price.
	 * @param dailyTrades The daily prices for all companies.
	 * @throws InsufficientFundsException Thrown when trying to buy more shares than you have money.
	 * @throws InsufficientSharesException Thrown when trying to sell more shares than you own for that company.
	 */
	public abstract void makeDailyTrade(DailyTrades dailyTrades) throws InsufficientFundsException, InsufficientSharesException;
	
	public final DailyOutput finaliseDailyTrade(DailyTrades dailyTrades)
	{
		return tradingManager.finalizeTrade(dailyTrades);
	}
}