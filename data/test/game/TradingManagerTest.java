package game;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import dataobjects.DailyInput;
import dataobjects.TradeActivity;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientSharesException;

public class TradingManagerTest {

	private TradingManager manager;
	private DailyInput input;
	
	@Before
	public void setup() {
		manager = new TradingManager();
		//close price of 2.0
		input = new DailyInput("Test company", 1, 1.0, 2.0, 3.0, 0.0);
	}
	
	@Test
	public void test_buy_shares_of_value_reduces_available_funds_and_increases_shares_held() throws InsufficientFundsException {
		manager.buySharesOfValue(input , 5000);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(2500, 0), 5000, 5000, 1);
		Assert.assertEquals(expected.getAvailableFunds(), out.getAvailableFunds());
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
	}
	
	@Test (expected = InsufficientFundsException.class)
	public void test_buy_shares_of_value_with_insufficient_funds_throws_exception() throws InsufficientFundsException {
		manager.buySharesOfValue(input , 10002);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_buy_shares_of_value_with_negative_number_throws_exception() throws InsufficientFundsException {
		manager.buySharesOfValue(input, -10);
	}
	
	@Test
	public void test_buy_number_of_shares_reduces_available_funds_and_increases_shares_held() throws InsufficientFundsException {
		manager.buyNumberOfShares(input, 2500);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(2500, 0), 5000, 5000, 1);
		Assert.assertEquals(expected.getAvailableFunds(), out.getAvailableFunds());
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
	}
	
	@Test (expected = InsufficientFundsException.class)
	public void test_buy_number_of_shares_with_insufficient_funds_throws_exception() throws InsufficientFundsException {
		manager.buyNumberOfShares(input, 5001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_buy_number_of_shares_with_negative_number_throws_exception() throws InsufficientFundsException {
		manager.buyNumberOfShares(input, -10);
	}
	
	@Test
	public void test_buy_max_shares_reduces_available_funds_to_zero() {
		manager.buyMaxNumberOfShares(input);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(5000, 0), 0, 10000, 1);
		Assert.assertEquals(expected.getAvailableFunds(), out.getAvailableFunds());
	}
	
	@Test
	public void test_sell_shares_of_value_increases_available_funds_and_decreases_shares_held() throws InsufficientSharesException  {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellSharesOfValue(input , 5000);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(0, 2500), 5000, 15000, 1);
		Assert.assertEquals(expected.getAvailableFunds(), out.getAvailableFunds());
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
    }
	
	@Test (expected = InsufficientSharesException.class)
	public void test_sell_shares_of_value_with_insufficient_shares_throws_exception() throws InsufficientSharesException  {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellSharesOfValue(input , 20002);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test_sell_shares_of_value_with_negative_number_throws_exception() throws InsufficientSharesException  {
		manager.sellSharesOfValue(input , -1);
	}
	
	@Test
	public void test_sell_number_of_shares_increases_available_funds_and_decreases_shares_held() throws InsufficientSharesException  {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellNumberOfShares(input, 2500);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(0, 2500), 5000, 15000, 1);
		Assert.assertEquals(expected.getAvailableFunds(), out.getAvailableFunds());
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
	}
	
	@Test (expected = InsufficientSharesException.class)
	public void test_sell_number_of_shares_with_insufficient_shares_throws_exception() throws InsufficientSharesException  {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellNumberOfShares(input, 10001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_sell_number_of_shares_with_negative_number_throws_exception() throws InsufficientSharesException {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellNumberOfShares(input, -10);
	}
	
	@Test
	public void test_sell_all_shares_reduces_shares_to_zero() {
		TradingManager manager = new TradingManager(0, 10000); //start with 10000 shares (worth £20,000)
		manager.sellAllShares(input);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(0, 10000), 20000, 0, 1);
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
	}
	
	@Test
	public void test_finalize_does_nothing() {
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(0, 0), 10000, 0, 1);
		Assert.assertEquals(expected, out);
	}
	
	@Test
	public void test_investments_held_doubled_with_close_price_doubling() {
		manager.buyMaxNumberOfShares(input);
		DailyOutput out = manager.finalizeTrade(input);
		DailyOutput expected = new DailyOutput(new TradeActivity(5000, 0), 0, 10000, 1);
		Assert.assertEquals(expected.getInvestmentAmount(), out.getInvestmentAmount());
		
		DailyInput inputDay2 = new DailyInput("Test Company", 2, 2.0, 4.0, 5.0, 1.0);
		DailyOutput outDay2 = manager.finalizeTrade(inputDay2);
		DailyOutput expectedDay2 = new DailyOutput(new TradeActivity(0, 0), 0, 20000, 2);
		Assert.assertEquals(expectedDay2.getInvestmentAmount(), outDay2.getInvestmentAmount());
	}
}
