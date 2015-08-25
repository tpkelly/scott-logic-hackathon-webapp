package game;

import java.util.HashMap;
import java.util.Map;

import dataobjects.DailyInput;
import dataobjects.DailyTrades;
import dataobjects.GameData;
import dataobjects.TradeActivity;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public final class TradingManager {
	
	private static final int INITIAL_AMOUNT = 10000;
	private int availableFunds;
	private Map<String, Integer> sharesOwned;
	
	public TradingManager () {
		this(INITIAL_AMOUNT); 
	}
	
	TradingManager (int initialFunds) {
		this(initialFunds, new HashMap<String, Integer>()); 
	}
	
	//for testing
	TradingManager(int availableFunds, Map<String, Integer> sharesOwned) {
		this.availableFunds = availableFunds;
		this.sharesOwned = sharesOwned;
	}

	private void verifyInput(DailyInput input) {
		GameData data = GameDataResolver.getInstance().getGameData();
		DailyTrades trades = data.getInputs().get(input.getDay() - 1);
		
		for (DailyInput trade : trades.getTrades()) {
			if (!trade.getCompany().equals(input.getCompany())) continue;
			
			// Check the close price has not been modified
			if (trade.getClose() != input.getClose())
				throw new IllegalArgumentException("Close price was modified from " + trade.getClose() + " -> " + input.getClose());
			
			return; // Found the valid input
		}
		
		// Could not find a company matching the name
  	    throw new IllegalArgumentException("Could not find the company: " + input.getCompany());
	}
	
	/**
	 * Gets the amount of cash available
	 */
	public int getAvailableFunds() {
		return availableFunds;
	}
	
	/**
	 * Gets the number of shares currently owned
	 */
	public int getSharesOwned(String companyName) {
		if (sharesOwned.containsKey(companyName)) {
			return sharesOwned.get(companyName);
		}
		
		return 0;
	}
	
	/**
	 * Get the cash value of the shares currently invested against the close price of the input
	 */
	public int getInvestmentAmount(DailyInput input) {
		return (int) (getSharesOwned(input.getCompany()) * input.getClose());
	}
	
	/**
	 * Buys a number of shares worth the amount given based on the close price of the input
	 * @throws InsufficientFundsException if there are less funds available than the given value
	 */
	public void buySharesOfValue(DailyInput input, int value) throws InsufficientFundsException {
		if (value < 0) {
			throw new IllegalArgumentException("Negative number given");
		}
		
		int sharesToBuy = (int)(value / input.getClose());
		TradeActivity activity = new TradeActivity(sharesToBuy, 0);
		try {
			makeTrade(input, activity);
		} catch (InsufficientSharesException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Buys the given number of shares at the close price of the input
	 * @throws InsufficientFundsException if there are insufficient funds to buy the number of shares requested
	 */
	public void buyNumberOfShares(DailyInput input, int totalShares) throws InsufficientFundsException {
		if (totalShares < 0) {
			throw new IllegalArgumentException("Negative number given");
		}
		
		TradeActivity activity = new TradeActivity(totalShares, 0);
		try {
			makeTrade(input, activity);
		} catch (InsufficientSharesException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Uses all of the available funds to buy as many shares as possible at the close price of the input.
	 * Note that after this trade, available balance may not be zero as the close price may not be exactly divisible by the available funds
	 */
	public void buyMaxNumberOfShares(DailyInput input) {
		//TODO rounding errors?
		int sharesToBuy = (int)(availableFunds / input.getClose());
		TradeActivity activity = new TradeActivity(sharesToBuy, 0);
		try {
			makeTrade(input, activity);
		} catch (InsufficientFundsException e) {
			//shouldn't happen
			throw new RuntimeException();
		} catch (InsufficientSharesException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Sells a number of shares worth the given value based on the close price of the input.
	 * Note that the given value is an approximate as the close price may not be exactly divisible
	 * @throws InsufficientSharesException if there are insufficient shares to the value of the given input
	 */
	public void sellSharesOfValue(DailyInput input, int value) throws InsufficientSharesException {
		if (value < 0) {
			throw new IllegalArgumentException("Negative number given");
		}
		
		int sharesToSell = (int)(value / input.getClose());
		TradeActivity activity = new TradeActivity(0, sharesToSell);
		try {
			makeTrade(input, activity);
		} catch (InsufficientFundsException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Sells the given number of shares at the close price of the input
	 * @throws InsufficientSharesException if there are insufficient shares currently held to sell the number of shares requested
	 */
	public void sellNumberOfShares(DailyInput input, int totalShares) throws InsufficientSharesException {
		if (totalShares < 0) {
			throw new IllegalArgumentException("Negative number given");
		}
		
		TradeActivity activity = new TradeActivity(0, totalShares);
		try {
			makeTrade(input, activity);
		} catch (InsufficientFundsException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Sells all currently held shares
	 */
	public void sellAllShares(DailyInput input) {
		TradeActivity activity = new TradeActivity(0, getSharesOwned(input.getCompany())); 
		try {
			makeTrade(input, activity);
		} catch (InsufficientFundsException e) {
			//shouldn't happen
			throw new RuntimeException();
		} catch (InsufficientSharesException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	/**
	 * Complete trade activity
	 */
	public DailyOutput finalizeTrade(DailyInput input) {
		TradeActivity activity = new TradeActivity(0, 0);
		try {
			return makeTrade(input, activity);
		} catch (InsufficientFundsException e) {
			//shouldn't happen
			throw new RuntimeException();
		} catch (InsufficientSharesException e) {
			//shouldn't happen
			throw new RuntimeException();
		}
	}
	
	public DailyOutput finalizeTrade(DailyTrades trade) {
		int investedFunds = 0;
		int availableFunds = 0;
		int sharesBought = 0;
		int sharesSold = 0;
		
		for (DailyInput input : trade.getTrades()) {
		    DailyOutput output = finalizeTrade(input);
		    investedFunds += output.getInvestmentAmount();
		    availableFunds = output.getAvailableFunds();
		    sharesBought += output.getTradeActivity().getBuy();
		    sharesSold += output.getTradeActivity().getSell();
		}
		
		return new DailyOutput(new TradeActivity(sharesBought, sharesSold), availableFunds, investedFunds, trade.getDay());
	}
	
	private DailyOutput makeTrade(DailyInput input, TradeActivity tradeActivity)
			throws InsufficientFundsException, InsufficientSharesException {
		// Check the input was not modified
		verifyInput(input);
		
		String company = input.getCompany();
		int companyShares = getSharesOwned(company) + tradeActivity.getBuy() - tradeActivity.getSell();
		
		if (companyShares < 0) {
			throw new InsufficientSharesException("You have insufficent shares to sell");
		}

		sharesOwned.put(company, companyShares);
		
		availableFunds += tradeActivity.getSell() * input.getClose();
		availableFunds -= tradeActivity.getBuy() * input.getClose();
		
		if (availableFunds < 0) {
			throw new InsufficientFundsException("You have insufficient funds to make this trade");
		}
		
		return new DailyOutput(tradeActivity, availableFunds, (int)(companyShares * input.getClose()), input.getDay());
		
	}
}
