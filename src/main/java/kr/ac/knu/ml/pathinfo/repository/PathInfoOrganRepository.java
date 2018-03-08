/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.ac.knu.ml.db.DBConnection;
import kr.ac.knu.ml.pathinfo.unit.PathInfoOrgan;
import kr.ac.knu.ml.pathinfo.unit.PathInfoOrganNormalize;

public class PathInfoOrganRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoOrgan> organs;

	public <E> void constructRepository(ArrayList<E> organs) {
		this.organs = new ArrayList<PathInfoOrgan>();

		for (E e : organs) {
			if (e instanceof String[]) {
				PathInfoOrgan pid = new PathInfoOrgan((String[]) e);
				this.organs.add(pid);
			} else if (e instanceof PathInfoOrgan) {
				this.organs.add((PathInfoOrgan) e);
			}
		}
	}

	public String getOrganID(String organName) {
		organName = organName.trim();
		for (PathInfoOrgan pio : organs) {
			String on = pio.getOrganName();
			if (on.equalsIgnoreCase(organName))
				return pio.getOrganID();
		}
		return null;
	}

	public String getOrganIDFromDB(String organName) {

		try {
			String q = "select OrganID from pathinfo.organ where OrganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organName);
			ResultSet rs = ps.executeQuery();
			rs.first();
			return rs.getString("OrganID");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public String getOrganName(String organID) {
		organID = organID.trim();
		for (PathInfoOrgan pio : organs) {
			String oid = pio.getOrganID();
			if (oid.equalsIgnoreCase(organID))
				return pio.getOrganName();
		}
		return null;
	}

	public String getOrganNameFromDB(String organID) {

		try {
			String q = "select OrganName from pathinfo.organ where OrganID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organID);
			ResultSet rs = ps.executeQuery();
			rs.first();
			return rs.getString("OrganName");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean startWith(String token) {
		for (PathInfoOrgan pio : organs) {
			String on = pio.getOrganName();
			if (token.length() != 0 && on.startsWith(token))
				return true;
		}
		return false;
	}

	public boolean startWithFromDB(String token) {

		try {
			String q = "select OrganName from pathinfo.organ where OrganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, token + "%");
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return false;
			rs = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public String getOrganIDwrtstartWith(String token) {
		for (PathInfoOrgan pio : organs) {
			String on = pio.getOrganName();
			if (token.length() != 0 && on.startsWith(token))
				return pio.getOrganID();
		}
		return null;
	}

	public String getOrganIDwrtstartWithFromDB(String token) {
		try {
			String q = "select OrganName from pathinfo.organ where OrganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, token + "%");
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("OrganID");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean contains(String organName) {
		for (PathInfoOrgan pio : organs) {
			String on = pio.getOrganName();
			if (on.equalsIgnoreCase(organName))
				return true;
			else if (on.contains(organName))
				return true;
		}
		return false;
	}

	public boolean containsFromDB(String organName) {
		try {
			String q = "select OrganName from pathinfo.organ where OrganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, "%" + organName + "%");
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return false;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public String getSystemID(String organID) {
		organID = organID.trim();
		for (PathInfoOrgan pio : organs) {
			String oid = pio.getOrganID();
			if (oid.equalsIgnoreCase(organID))
				return pio.getSystemID();
		}
		return null;
	}

	public String getSystemIDFromDB(String organID) {
		try {
			String q = "select SystemID from pathinfo.organ where OrganID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organID);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("SystemID");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
