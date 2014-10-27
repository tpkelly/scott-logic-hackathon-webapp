package game;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import dataobjects.DailyInput;
import dataobjects.GameData;

public class GetDailyInput extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String company = req.getParameter("company");
		
		GameData gameData;
		try {
			gameData = GameDataResolver.getInstance().getGameData(company);			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Supplied company is invalid");
			return;
		}
		
		
		List<DailyInput> inputs = gameData.getInputs();
		
		mapper.writeValue(resp.getOutputStream(), inputs);
		resp.getOutputStream().close();
	}

}
