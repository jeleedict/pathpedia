package kr.ac.knu.ml.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTester {
	private Connection connection;
	private Statement statement;
	private boolean connectedToDatabase;

	public DBTester() {
		try {
			connection = DBConnection.getDBConnection();
			System.out.println(connection.isClosed());
			statement = connection.createStatement();
			connectedToDatabase = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getTest() throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			System.exit(1);

		// MySQL
		String selectSQL = "SELECT @@VERSION";
		ResultSet resultSet = statement.executeQuery(selectSQL);
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1));
		}
	}

	public static void main(String[] args) {
		DBTester test = new DBTester();
		try {
			test.getTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
