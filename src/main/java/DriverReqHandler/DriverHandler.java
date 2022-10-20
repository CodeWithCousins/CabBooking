package DriverReqHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jdbc.SqlConnector;

public class DriverHandler {
	
	public int InsertDriver(String name, long phnNo, String carModel, String carNo, String carLocation, int pricePerKm) throws SQLException
	{
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("insert into drivers(name, phnNo, carModel, carNo, carLocation, pricePerKm) values(?,?,?,?,?,?)");
		
		ps.setString(1, name);
		ps.setLong(2, phnNo);
		ps.setString(3, carModel);
		ps.setString(4, carNo);
		ps.setString(5, carLocation);
		ps.setInt(6, pricePerKm);
		
		int result = ps.executeUpdate();
		return result;
	}
	
	public JSONArray GetDriverHistory(int id) throws SQLException, JSONException
	{
		JSONArray driverHistoryDetails = new JSONArray();
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from TravelRecords where driverId = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			JSONObject driverHistoryObj = new JSONObject();
			driverHistoryObj.put("driverId", rs.getInt("driverId"));
			driverHistoryObj.put("fromLocation", rs.getString("fromLocation"));
			driverHistoryObj.put("toLocation", rs.getString("toLocation"));
			driverHistoryObj.put("date", rs.getDate("journeyDate"));
			driverHistoryObj.put("custId", rs.getInt("custId"));
			driverHistoryObj.put("amount", rs.getInt("amount"));
			driverHistoryDetails.put(driverHistoryObj);
		}
		
		return driverHistoryDetails;
	}

}