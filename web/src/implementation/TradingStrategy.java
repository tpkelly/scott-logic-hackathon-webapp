package implementation;

import game.DailyOutput;
import tradingstrategy.BaseTradingStrategy;
import dataobjects.DailyInput;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public class TradingStrategy extends BaseTradingStrategy {

	@Override
	public DailyOutput makeDailyTrade(DailyInput input) throws InsufficientFundsException, InsufficientSharesException {
		
		if (input.getDay() %2 == 1) {
			return tradingManager.buyMaxNumberOfShares(input);
		}
		return tradingManager.sellAllShares(input);
		
	}
}