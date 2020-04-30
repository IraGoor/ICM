package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.controllers.*;
public class mysqlConnection {

	public static Connection con;
	public static void openConnection(DBDetails dbDetails)
	{
		try
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}

        try
        {
        	// Parameters class contains DB Details
        	//con = DriverManager.getConnection("jdbc:mysql://"+Parameters.DB_HOST+"/"+Parameters.DB_SCHEME,Parameters.DB_USERNAME,Parameters.DB_PASSWORD);
        	con = DriverManager.getConnection("jdbc:mysql://"+dbDetails.getDB_HOST()+"/"+dbDetails.getDB_SCHEME()+"",dbDetails.getDB_USERNAME(),dbDetails.getDB_PASSWORD());
//?serverTimezone=IST
        	//Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
            System.out.println("SQL connection succeed");
            //createTableCourses(conn);
            //printCourses(conn);
     	} catch (SQLException ex)
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}



	public ResultSet getAllTableData(Object message) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			//stmt.executeUpdate(sql);
			ResultSet rs = stmt.executeQuery("SELECT * FROM requirement ;");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	// ** Idan's Additions:


	public ResultSet getQuery (String query) {
//		System.out.println("getQuery");
//		System.out.println(con);
		ResultSet rs=null;
		try {
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public boolean insertOrUpdate (String query) {

		try {
			Statement stmt=con.createStatement();
			stmt.executeUpdate(query);
			System.out.println("my sql "+ query);
			return true;
		} catch (SQLException e) {
			System.out.println("insert or update exception !");
			return false;
		}
	}
}
