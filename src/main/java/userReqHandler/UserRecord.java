package userReqHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserRecord
 */
@WebServlet("/api/v0/user-records")
public class UserRecord extends HttpServlet {
	
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
			UserHandler userHandler = new UserHandler();
			try {
				userHistoryDetails = userHandler.GetUserHistory(userDetails.getInt("id"));
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
