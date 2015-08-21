package game;

import dataobjects.DailyInput;
import dataobjects.TradeActivity;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public final class TradingManager {
	
	private static final int INITIAL_AMOUNT = 10000;
	private int availableFunds;
	private int sharesOwned;
	
	public TradingManager () {
		availableFunds = INITIAL_AMOUNT;
		sharesOwned = 0; 
	}
	
	//for testing
	TradingManager(int availableFunds, int sharesOwned) {
		this.availableFunds = availableFunds;
		this.sharesOwned = sharesOwned;
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
	public int getSharesOwned() {
		return sharesOwned;
	}
	
	/**
	 * Get the cash value of the shares currently invested against the close price of the input
	 */
	public int getInvestmentAmount(DailyInput input) {
		return (int) (sharesOwned * input.getClose());
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
		TradeActivity activity = new TradeActivity(0, sharesOwned); 
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
	
	private DailyOutput makeTrade(DailyInput input, TradeActivity tradeActivity)
			throws InsufficientFundsException, InsufficientSharesException {
		sharesOwned += tradeActivity.getBuy();
		sharesOwned -= tradeActivity.getSell();
		
		if (sharesOwned < 0) {
			throw new InsufficientSharesException("You have insufficent shares to sell");
		}
		
		availableFunds += tradeActivity.getSell() * input.getClose();
		availableFunds -= tradeActivity.getBuy() * input.getClose();
		
		if (availableFunds < 0) {
			throw new InsufficientFundsException("You have insufficient funds to make this trade");
		}
		
		return new DailyOutput(tradeActivity, availableFunds, (int)(sharesOwned * input.getClose()), input.getDay());
		
	}
}
