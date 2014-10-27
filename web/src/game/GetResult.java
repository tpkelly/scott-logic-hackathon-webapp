package game;


import implementation.TradingStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import dataobjects.DailyInput;
import dataobjects.GameData;
import dataobjects.GameOutput;
import exceptions.GameFailureException;


public class GetResult extends HttpServlet {

	private static final long serialVersionUID = 1;
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		TradingStrategy strategy = new TradingStrategy();
		
		String company = req.getParameter("company");
		
		GameData gameData;
		try {
			gameData = GameDataResolver.getInstance().getGameData(company);			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Supplied company is invalid");
			return;
		}
		
		Game game = new Game(strategy, gameData);
		GameOutput result;
		try {
			result = game.getResult();
		} catch (GameFailureException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		
		Result toReturn = new Result();
		toReturn.output = result;
		toReturn.chartData = buildChartData(result, gameData.getInputs());
		toReturn.company = company;
		
		mapper.writeValue(resp.getOutputStream(), toReturn);
		resp.getOutputStream().close();
	}
	
	private List<ChartSeries> buildChartData(GameOutput result, List<DailyInput> inputs) {
		List<ChartSeries> chartData = new LinkedList<ChartSeries>();
		
		ChartSeries in = new ChartSeries();
		in.name = "Company Close Price";
		in.data = new LinkedList<Double>();
		in.yAxis = 0;
		for (DailyInput di : inputs) {
			in.data.add(di.getClose());
		}
		
		ChartSeries out = new ChartSeries();
		out.name = "Total";
		out.data = new LinkedList<Double>();
		out.yAxis = 1;
		for (DailyOutput dailyOut : result.getDailyOutputs()) {
			out.data.add((double) (dailyOut.getAvailableFunds() + dailyOut.getInvestmentAmount()));
		}
		
		chartData.add(in);
		chartData.add(out);
		
		return chartData;
	}

	private class Result {
		private GameOutput output;
		private List<ChartSeries> chartData;
		private String company;
		public GameOutput getOutput() {
			return output;
		}
		public void setOutput(GameOutput output) {
			this.output = output;
		}
		public List<ChartSeries> getChartData() {
			return chartData;
		}
		public void setChartData(List<ChartSeries> chartData) {
			this.chartData = chartData;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		
	}
	private class ChartSeries {
		private String name;
		private List<Double> data;
		private int yAxis;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Double> getData() {
			return data;
		}
		public void setData(List<Double> data) {
			this.data = data;
		}
		public int getyAxis() {
			return yAxis;
		}
		public void setyAxis(int yAxis) {
			this.yAxis = yAxis;
		}
	}
}
