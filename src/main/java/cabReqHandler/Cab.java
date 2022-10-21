package cabReqHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Cab
 */
@WebServlet("/api/v0/cab-booking")
public class Cab extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("json");
		
		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		JSONObject resObject = new JSONObject();
		
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject travelDetails = null;
		try {
			travelDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(travelDetails.has("from") && travelDetails.has("to"))
		{
			CabHandler cabHandler = new CabHandler();
			try {
				resObject = cabHandler.ShowAvailableCab(travelDetails.getString("from"), travelDetails.getString("to"));
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(resObject.length() != 0)
		{
			try {
				resObject.put("status", "success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			try {
				resObject.put("status", "error");
				resObject.put("reason", "No cabs Available near");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pOut.println(resObject);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		int res = 0;
		JSONObject resObject = new JSONObject();
		
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject travelDetails = null;
		try {
			travelDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(travelDetails.has("id") && travelDetails.has("from") && travelDetails.has("to") && travelDetails.has("custId") && travelDetails.has("date"))
		{
			
			CabHandler cabHandler = new CabHandler();
			try {
				res = cabHandler.BookCab(travelDetails.getInt("custId"), travelDetails.getInt("id"), travelDetails.getString("from"), travelDetails.getString("to"), travelDetails.getString("date"));
				
				if(res != 0)
				{
					resObject.put("status", "success");
					resObject.put("travelNo", res);
				}
				
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	
		if(res == 0) {
			try {
				resObject.put("status", "error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pOut.println(resObject);
		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		int res = 0;
		JSONObject resObject = new JSONObject();
		
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject cabDetails = null;
		try {
			cabDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cabDetails.has("travelNo"))
		{
			CabHandler cabHandler = new CabHandler();
			try {
				res = cabHandler.CancelCab(cabDetails.getInt("travelNo"));
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(res != 0)
		{
			try {
				resObject.put("status", "success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				resObject.put("status", "error");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pOut.println(resObject);
	}
	
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
