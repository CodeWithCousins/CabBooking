package cabReqHandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jdbc.SqlConnector;

public class CabHandler {

	public JSONObject ShowAvailableCab(String from, String to) throws SQLException, JSONException
	{
		JSONObject cabDetails = new JSONObject();
		CabHandler cabHandler = new CabHandler();
		int distance = cabHandler.GetKmDistance(from, to);
		int locationCount = cabHandler.GetCountOfLocation();

		JSONObject locationDetails = cabHandler.GetLocationIdAndKm(from);
		int locationId = locationDetails.getInt("id");
		JSONArray cab = cabHandler.GetCab(locationId, distance, from, to);
		cabDetails.put("Cabs", new JSONArray());
		if(cab.length() != 0)
		{
			for(int i=0;i<cab.length();i++)
			{
				if(cabDetails.length() != 0) {
					cabDetails.append("Cabs", cab.get(i));
				}
				else
				{
					System.out.println("hiii");
					cabDetails.put("Cabs", cab.get(i));
				}
			}
			
		}

		int totKmLeft = locationDetails.getInt("km"), totKmRight = locationDetails.getInt("km"), left = locationId, right = locationId, totLeft = 0, totRight = 0;

		while(true)
		{
			left--;
			right++;

			if(left > 0)
			{
				totLeft = totKmLeft - cabHandler.GetLocationKm(left);
				if(totLeft <= 15)
				{

					JSONArray leftCabDetails = new JSONArray();
					leftCabDetails = cabHandler.GetCab(left, distance, from, to);
					for(int i=0;i<leftCabDetails.length();i++)
					{
						if(cabDetails.length() != 0) {
							cabDetails.append("Cabs",leftCabDetails.get(i));
						}
						else
						{
							cabDetails.put("Cabs",leftCabDetails.get(i));
						}
					}
				}
			}

			if(right <= locationCount)
			{
				totRight = cabHandler.GetLocationKm(right) - totKmRight;
				//				totKmRight = totRight;

				if(totRight <=15)
				{
					JSONArray rightCabDetails = new JSONArray();
					rightCabDetails = cabHandler.GetCab(right, distance, from, to);
					for(int i=0;i<rightCabDetails.length();i++)
					{
						if(cabDetails.length() != 0) {
							cabDetails.append("Cabs",rightCabDetails.get(i));
						}
						else
						{
							cabDetails.put("Cabs",rightCabDetails.get(i));
						}
					}
				}
			}

			if(totLeft >= 15 && totRight >=15)
			{
				break;
			}
			if(left <= 0 && right > locationCount)
			{
				break;
			}
		}
		return cabDetails;

	}

	public JSONObject GetLocationIdAndKm(String locationName) throws SQLException, JSONException
	{
		JSONObject locationDetails = new JSONObject();
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from location where locationName = ?");
		ps.setString(1, locationName);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			locationDetails.put("id", rs.getInt("id"));
			locationDetails.put("km", rs.getInt("kms"));
		}
		con.close();

		return locationDetails;
	}

	public int GetLocationKm(int id) throws SQLException, JSONException
	{
		int km = 0;
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from location where id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			km = rs.getInt("kms");
		}
		con.close();

		return km;
	}

	public JSONArray GetCab(int locationId, int distance, String from, String to) throws SQLException, JSONException // not finish
	{
		JSONArray AllCabDetails = new JSONArray();
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from location inner join drivers on location.locationName = drivers.carLocation where location.id = ? and drivers.status = \"available\"");
		ps.setInt(1, locationId);

		ResultSet rs = ps.executeQuery();
		CabHandler cabHandler = new CabHandler();

		while (rs.next()) {
			JSONObject cabDetails = new JSONObject();
			cabDetails.put("Driver Id", rs.getInt("drivers.id"));
			cabDetails.put("Driver Name", rs.getString("name"));
			cabDetails.put("Driver Phn no", rs.getLong("phnNo"));
			cabDetails.put("Car Model", rs.getString("carModel"));
			cabDetails.put("Car No", rs.getString("carNo"));
			cabDetails.put("Car Location", rs.getString("carLocation"));
			cabDetails.put("Amount Per Km", rs.getInt("pricePerKm"));
			cabDetails.put("Total Amount", rs.getInt("pricePerKm") * distance);
			cabDetails.put("totalKm", distance);
			Duration d = Duration.ofSeconds(distance*50);
			long days = d.toDaysPart();
			long hours = d.toHoursPart();
			long minutes = d.toMinutesPart();
			cabDetails.put("Duration of travel", String.format("%d Day(s) %d Hour(s) %d Minute(s)", days, hours, minutes));

			AllCabDetails.put(cabDetails);
		}

		return AllCabDetails;
	}

	public int GetKmDistance(String from, String to) throws SQLException
	{
		int distance = 0;
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select * from location where locationName = ? or locationName = ? ");
		ps.setString(1, from);
		ps.setString(2, to);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			if(rs.getInt("kms") > distance)
			{
				distance = rs.getInt("kms") - distance;
			}
			else if(rs.getInt("kms") < distance)
			{
				distance = distance - rs.getInt("kms");
			}

		}

		return distance;
	}

	public int GetCountOfLocation() throws SQLException
	{
		int locationCount =0;

		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("select count(id) as count from location");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			locationCount = rs.getInt("count");
		}

		return locationCount;
	}

	public int BookCab(int custId, int id, String from, String to, String dateStr) throws SQLException
	{
		int drivId =0, pricePerKm = 0,res = 0;
		Connection con = SqlConnector.ConnectDb();
		con.setAutoCommit(false);
		try {
			PreparedStatement ps = con.prepareStatement("select * from drivers where id =? and status =\"available\"");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				drivId = rs.getInt("id");
				pricePerKm = rs.getInt("pricePerKm");
			}

			if(drivId != 0)
			{
				CabHandler cabHandler = new CabHandler();
				int km = cabHandler.GetKmDistance(from, to);
				Date date=Date.valueOf(dateStr);
				PreparedStatement ps1 = con.prepareStatement("update drivers set status = \"booked\" where id=?");
				ps1.setInt(1, id);
				ps1.executeUpdate();

				PreparedStatement ps2 = con.prepareStatement("insert into TravelRecords(driverId,fromLocation,toLocation,journeyDate, custId, amount) values(?,?,?,?,?,?)");
				ps2.setInt(1, id);
				ps2.setString(2, from);
				ps2.setString(3, to);
				ps2.setDate(4, date);
				ps2.setInt(5, custId);
				ps2.setInt(6, pricePerKm*km);
				res = ps2.executeUpdate();
				con.commit();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			con.rollback();
		}
		return res;
	}
	
	public int UpdateStatus(int id) throws SQLException
	{
		int res = 0;
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("update drivers set status = \"available\" where id = ?");
		ps.setInt(1, id);
		
		res = ps.executeUpdate();
		
		return res;
	}
	
	public int CancelCab(int travelNo) throws SQLException
	{
		int res = 0;
		Connection con = SqlConnector.ConnectDb();
		PreparedStatement ps = con.prepareStatement("update TravelRecords set status = \"cancelled\" where travelNo = ?");
		
		ps.setInt(1, travelNo);
		
		res = ps.executeUpdate();
		
		return res;
		
	}

























}
