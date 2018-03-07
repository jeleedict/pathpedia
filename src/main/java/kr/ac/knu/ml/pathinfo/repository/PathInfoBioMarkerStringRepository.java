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
import java.util.Collections;
import java.util.Comparator;

import kr.ac.knu.ml.common.unit.Pair;
import kr.ac.knu.ml.db.DBConnection;
import kr.ac.knu.ml.pathinfo.unit.PathInfoBioMarkerString;
import kr.ac.knu.ml.similarity.LevenshteinDistance;

public class PathInfoBioMarkerStringRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoBioMarkerString> bioMarkerStrings;

	public <E> void constructRepository(ArrayList<E> bioMarkerStrings) {
		this.bioMarkerStrings = new ArrayList<PathInfoBioMarkerString>();

		for (E e : bioMarkerStrings) {
			if (e instanceof String[]) {
				PathInfoBioMarkerString pibms = new PathInfoBioMarkerString(
						(String[]) e);
				this.bioMarkerStrings.add(pibms);
			} else if (e instanceof PathInfoBioMarkerString) {
				this.bioMarkerStrings.add((PathInfoBioMarkerString) e);
			}
		}
	}

	public String getPNID(String SUI) {
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			String psui = pibms.getSUI();
			if (psui.equalsIgnoreCase(SUI)) {
				return pibms.getPNID();
			}
		}
		return null;
	}

	public String getPNIDFromDB(String SUI) {
		try {
			String q = "select * from pathinfo.biostring where BSUI like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, SUI);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("PNID");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean containBioMarkerString(String bioMarker) {
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			if (pibms.getActive().equalsIgnoreCase("N"))
				continue;

			String str = pibms.getString();
			if (str.equalsIgnoreCase(bioMarker)) {
				return true;
			}
		}
		return false;
	}

	public boolean containBioMarkerStringFromDB(String bioMarker) {
		try {
			String q = "select String from pathinfo.biostring where String like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, bioMarker);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return false;
			rs.first();
			return true;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// TODO: improve the search by using binary search
	public String getString(String SUI) {
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {

			String psui = pibms.getSUI();
			if (psui.equalsIgnoreCase(SUI)) {
				return pibms.getString();
			}
		}
		return null;
	}

	public String getStringFromDB(String SUI) {
		try {
			String q = "select * from pathinfo.biostring where BSUI like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, SUI);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("String");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// TODO: improve the search by using binary search
	public String getStringusingMarkerName(String bioMarkerString) {
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			if (pibms.getActive().equalsIgnoreCase("N"))
				continue;

			String str = pibms.getString();
			if (str.equalsIgnoreCase(bioMarkerString))
				return pibms.getString();
		}
		return null;
	}

	public String getStringusingMarkerNameFromDB(String bioMarkerString) {
		try {
			String q = "select * from pathinfo.biostring where String like ? and Active = 1";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, bioMarkerString);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("String");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// TODO: improve the search by using binary search
	public String getPNIDusingMarkerName(String bioMarkerString) {
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			if (pibms.getActive().equalsIgnoreCase("N"))
				continue;

			String str = pibms.getString();
			if (str.equalsIgnoreCase(bioMarkerString))
				return pibms.getPNID();
		}
		return null;
	}

	public String getPNIDusingMarkerNameFromDB(String bioMarkerString) {
		try {
			String q = "SELECT * from pathinfo.biostring where String like ? and Active = 1";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, bioMarkerString);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("PNID");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMostSimilarMarkerName(String preferredName) {
		ArrayList<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			if (pibms.getActive().equalsIgnoreCase("N"))
				continue;

			String bioMarker = pibms.getString();
			double similarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							preferredName.toLowerCase(),
							bioMarker.toLowerCase());

			Pair<String, Double> p = new Pair<String, Double>(pibms.getPNID(),
					similarity);
			pairs.add(p);
		}
		Collections.sort(pairs, PairComparable);
		if (pairs.size() == 0)
			return null;

		return pairs.get(0).getA();
	}

	public String getMostSimilarMarkerNameFromDB(String preferredName) {

		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement("select * from pathinfo.biostring");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {
				/*
				 * public PathInfoBioMarkerString(String BSUI, String string,
				 * String pNID, String pathName, String date, String active,
				 * String inactiveDate, String comment) {
				 */
				this.bioMarkerStrings.add(new PathInfoBioMarkerString(rs
						.getString("BSUI"), rs.getString("String"), rs
						.getString("PNID"), rs.getString("PathName"), rs
						.getString("Date"), rs.getString("Active"), rs
						.getString("InactiveDate"), rs.getString("Comment")));
				rs.next();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();
		for (PathInfoBioMarkerString pibms : bioMarkerStrings) {
			if (pibms.getActive().equalsIgnoreCase("N"))
				continue;

			String bioMarker = pibms.getString();
			double similarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							preferredName.toLowerCase(),
							bioMarker.toLowerCase());

			Pair<String, Double> p = new Pair<String, Double>(pibms.getPNID(),
					similarity);
			pairs.add(p);
		}
		Collections.sort(pairs, PairComparable);
		if (pairs.size() == 0)
			return null;

		return pairs.get(0).getA();
	}

	static final Comparator<Pair<String, Double>> PairComparable = new Comparator<Pair<String, Double>>() {
		public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
			return Double.compare(p2.getB(), p1.getB());
		}
	};
}
