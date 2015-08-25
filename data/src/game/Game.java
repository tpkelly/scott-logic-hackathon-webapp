package game;

import tradingstrategy.BaseTradingStrategy;

import java.util.LinkedList;

import dataobjects.DailyTrades;
import dataobjects.GameData;
import dataobjects.GameOutput;
import exceptions.GameFailureException;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public class Game {
	private BaseTradingStrategy strategy;
	private GameData gameData;
	
	/**
	 * Simulate a year of trading with a given trading strategy.
	 * @param strategy The strategy to use for buying and selling.
	 */
	public Game(BaseTradingStrategy strategy) {
		this.strategy = strategy;
		this.gameData = GameDataResolver.getInstance().getGameData();
	}
	
	/** 
	 * The final result of trading for the year.
	 * @return The fund totals and trading data for the year.
	 * @throws GameFailureException An invalid trade was attempted and failed.
	 */
	public GameOutput getResult() throws GameFailureException {
		LinkedList<DailyOutput> dailyOutputs = new LinkedList<DailyOutput>();
		
		for(DailyTrades input : gameData.getInputs()) {
			int day = input.getDay();
			
			try {
				strategy.makeDailyTrade(input);
				dailyOutputs.add(strategy.finaliseDailyTrade(input));
			} catch (InsufficientFundsException e) {
				throw new GameFailureException("You tried to make a trade but had insufficient funds. This occurred on day " + day, e);
			} catch (InsufficientSharesException e) {
				throw new GameFailureException("You tried to make a trade but had insufficient shares. This occurred on day " + day, e);
			}
		}
		DailyOutput last = dailyOutputs.getLast();
		int totalFunds = (int)last.getInvestmentAmount() + last.getAvailableFunds();
		
		return new GameOutput(dailyOutputs, totalFunds);
	}
}
