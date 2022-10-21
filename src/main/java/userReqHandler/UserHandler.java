package userReqHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jdbc.SqlConnector;

public class UserHandler {

	public int InsertUser(String name, long phnNo, String email, String address) throws SQLException
	{
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("insert into users(userName, phnNo, email, address) values(?,?,?,?)");
		ps.setString(1, name);
		ps.setLong(2, phnNo);
		ps.setString(3, email);
		ps.setString(4, address);
		
		int result = ps.executeUpdate();
		return result;
	}
	
	public JSONObject GetUserDetails(int id) throws SQLException, JSONException
	{
		JSONObject userDetailsObj = new JSONObject();
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from users where id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			userDetailsObj.put("userName",  rs.getString("userName"));
			userDetailsObj.put("phnNo",  rs.getLong("phnNo"));
			userDetailsObj.put("email",  rs.getString("email"));
			userDetailsObj.put("address",  rs.getString("address"));
		}
		
		return userDetailsObj;
	}
	
	public int UpdateUser(int id, String name, long phnNo, String email, String address) throws SQLException
	{
		int res = 0;
		Connection con = SqlConnector.ConnectDb();
		
		if(name != "")
		{
			PreparedStatement ps = con.prepareStatement("update users set userName = ? where id = ?");
			ps.setString(1, name);
			ps.setInt(2, id);
			
			res = ps.executeUpdate();
		}
		if(phnNo != 0)
		{
			PreparedStatement ps = con.prepareStatement("update users set phnNo = ? where id = ?");
			ps.setLong(1, phnNo);
			ps.setInt(2, id);
			
			res = ps.executeUpdate();
		}
		if(email != "")
		{
			PreparedStatement ps = con.prepareStatement("update users set email = ? where id = ?");
			ps.setString(1, email);
			ps.setInt(2, id);
			
			res = ps.executeUpdate();
		}
		if(address != "")
		{
			PreparedStatement ps = con.prepareStatement("update users set address = ? where id = ?");
			ps.setString(1, address);
			ps.setInt(2, id);
			
			res = ps.executeUpdate();
		}
		return res;
	}
	
	public int DeleteUser(int id) throws SQLException
	{
		int res = 0;
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("delete from users where id = ?");
		ps.setInt(1, id);
		
		res = ps.executeUpdate();
		return res;
		
	}
	
	public JSONArray GetUserHistory(int id) throws SQLException, JSONException
	{
		JSONArray userHistory = new JSONArray();
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from TravelRecords where custId = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			JSONObject userHistoryObj = new JSONObject();
			userHistoryObj.put("travelNo", rs.getInt("travelNo"));
			userHistoryObj.put("driverId", rs.getInt("driverId"));
			userHistoryObj.put("fromLocation", rs.getString("fromLocation"));
			userHistoryObj.put("toLocation", rs.getString("toLocation"));
			userHistoryObj.put("date", rs.getDate("journeyDate"));
			userHistoryObj.put("custId", rs.getInt("custId"));
			userHistoryObj.put("amount", rs.getInt("amount"));
			userHistoryObj.put("status", rs.getString("status"));
			userHistory.put(userHistoryObj);
		}
		
		return userHistory;
	}
	
}
