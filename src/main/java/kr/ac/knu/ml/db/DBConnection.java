/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * DB class to access PathInfo database.
 * @author Hyun-Je
 * 
 */
public class DBConnection {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://147.47.224.103:3306/pathinfo?characterEncoding=UTF-8";
	private static final String DAtABASE_USER = "pathinfo_guest";
	private static final String DAtABASE_PASSWORD = "dkssudp@thinfo";

	private static Connection dbConnection = null;

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getDBConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(DBConnection.JDBC_DRIVER);
		if (dbConnection == null) {
			dbConnection = DriverManager.getConnection(
					DBConnection.DATABASE_URL, DBConnection.DAtABASE_USER,
					DBConnection.DAtABASE_PASSWORD);
		}
		return dbConnection;
	}

	public static void main(String[] args) {
		System.out.println("hi!");
		System.out.println("Connection Test!");
		System.out.println("DATABASE_URL:");
		System.out.println(DATABASE_URL);

		try {
			System.out.println(DBConnection.getDBConnection().isClosed());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
