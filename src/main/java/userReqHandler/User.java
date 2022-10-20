package userReqHandler;

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

/**
 * Servlet implementation class User
 */
@WebServlet("/api/v0/users/*")
public class User extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter pOut = response.getWriter();
		String bodyMsgline = null;
		String bodyMsg = "";
		JSONObject result = new JSONObject();
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

		String uri = request.getRequestURI();
		uri = uri.replace("/api/v0/users","");
		System.out.println(uri);
		if(userDetails.has("id") )
		{
			UserHandler userHandler = new UserHandler();
			if(uri.equals("")) {
				try {
					result = userHandler.GetUserDetails(userDetails.getInt("id"));
				} catch (SQLException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(result.length() != 0)
				{
					try {
						resObject.put("status", "success");
						resObject.put("userDetails", result);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(uri.equals("/records"))
			{
				System.out.println(uri);
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
		}

		if(result.length() != 0 && userHistoryDetails.length() == 0)
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
		JSONObject userDetails = null;
		try {
			userDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(userDetails.has("name") && userDetails.has("phnNo") && userDetails.has("email") && userDetails.has("address"))
		{
			UserHandler userHandler = new UserHandler();

			try {
				result = userHandler.InsertUser(userDetails.getString("name"), userDetails.getLong("phnNo"), userDetails.getString("email"), userDetails.getString("address"));
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
		JSONObject userDetails = null;
		try {
			userDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name ="", email = "", address="";
		long phnNo = 0;
		if(userDetails.has("id") )
		{
			UserHandler userHandler = new UserHandler();


			if(userDetails.has("name"))
			{
				try {
					name = userDetails.getString("name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(userDetails.has("phnNo"))
			{
				try {
					phnNo = userDetails.getLong("phnNo");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(userDetails.has("email"))
			{
				try {
					email = userDetails.getString("email");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(userDetails.has("address"))
			{
				try {
					address = userDetails.getString("address");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				result = userHandler.UpdateUser(userDetails.getInt("id"), name, phnNo, email, address);
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
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
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
		JSONObject userDetails = null;
		try {
			userDetails = new JSONObject(bodyMsg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(userDetails.has("id") )
		{
			UserHandler userHandler = new UserHandler();

			try {
				result = userHandler.DeleteUser(userDetails.getInt("id"));
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
