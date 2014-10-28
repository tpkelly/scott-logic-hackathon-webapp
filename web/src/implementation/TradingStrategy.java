package implementation;

import game.DailyOutput;
import game.TradingManager;
import tradingstrategy.BaseTradingStrategy;
import dataobjects.DailyInput;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public class TradingStrategy extends BaseTradingStrategy {

	public TradingStrategy(TradingManager tradingManager) {
		super.tradingManager = tradingManager;
	}

	@Override
	public DailyOutput makeDailyTrade(DailyInput input) throws InsufficientFundsException, InsufficientSharesException {

		if (input.getDay() % 2 == 1) {
			return tradingManager.buyMaxNumberOfShares(input);
		}
		return tradingManager.sellAllShares(input);

	}
}