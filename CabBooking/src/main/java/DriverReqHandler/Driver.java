package DriverReqHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cabReqHandler.CabHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userReqHandler.UserHandler;

/**
 * Servlet implementation class Driver
 */
@WebServlet("/api/v0/drivers")
public class Driver extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		int result = 0;
		JSONObject resObject = new JSONObject();
		JSONArray userHistoryDetails = new JSONArray();
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject userDetails = null;
		try {
			userDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(userDetails.has("id"))
		{
			DriverHandler driverHandler = new DriverHandler();
			try {
				userHistoryDetails = driverHandler.GetDriverHistory(userDetails.getInt("id"));
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(userHistoryDetails.length() != 0)
			{
				try {
					resObject.put("status", "success");
					resObject.put("userHistory", userHistoryDetails);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		if(userHistoryDetails.length() == 0)
		{
			try {
				resObject.put("status", "error");
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
		int result = 0;
		JSONObject resObject = new JSONObject();
		
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject driverDetails = null;
		try {
			driverDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(driverDetails.has("name") && driverDetails.has("phnNo") && driverDetails.has("carModel") && driverDetails.has("carNo") && driverDetails.has("carLocation") && driverDetails.has("pricePerKm"))
		{
			DriverHandler driverHandler = new DriverHandler();
			try {
				result = driverHandler.InsertDriver(driverDetails.getString("name"), driverDetails.getLong("phnNo"), driverDetails.getString("carModel"), driverDetails.getString("carNo"), driverDetails.getString("carLocation"), driverDetails.getInt("pricePerKm"));
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(result != 0)
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
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		int result = 0;
		JSONObject resObject = new JSONObject();
		
		BufferedReader reader = request.getReader();
		while ((bodyMsgline = reader.readLine()) != null) {
			bodyMsg +=bodyMsgline;
		}
		JSONObject driverDetails = null;
		try {
			driverDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(driverDetails.has("id"))
		{
			CabHandler cabHandler = new CabHandler();
			try {
				result = cabHandler.UpdateStatus(driverDetails.getInt("id"));
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(result != 0)
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
