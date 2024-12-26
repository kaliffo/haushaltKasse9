package haushaltKasse8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Mysql
{
	private static final String host = "localhost";
	private static final String port = "3306";
	private static final String database = "hashaltKasse";
	private static final String username = "root";
	private static final String password = "";
	
	public static ResultSet resultSet;
	
	public static ResultSetMetaData metaData ;
	
	
	public static Connection con;
	
	public static boolean isConnected() {
		return (con == null ? false:true);
	}
	
	public static void connect() throws ClassNotFoundException {
		if(!isConnected()) {
			
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, username, password);
				System.out.println("[MySQL] Verbunden!");
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void disconnect() {
		try
		{
			con.close();
			System.out.println("[MySQL] Verbindung Geschlossen!");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void update(String querry) throws ClassNotFoundException {
		
		try
		{
			connect();
			PreparedStatement ps = con.prepareStatement(querry);
			ps.execute();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
	}
	public static ResultSetMetaData display(String querry) throws ClassNotFoundException, SQLException {
		connect();
		Statement statement = con.createStatement();
		resultSet = statement.executeQuery(querry);
		metaData = resultSet.getMetaData();
		
		
		return metaData;
		
	}
	
}
