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
import kr.ac.knu.ml.pathinfo.unit.PathInfoBioMarker;
import kr.ac.knu.ml.similarity.LevenshteinDistance;

public class PathInfoBioMarkerRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoBioMarker> bioMarkers;

	public <E> void constructRepository(ArrayList<E> bioMarkers) {
		this.bioMarkers = new ArrayList<PathInfoBioMarker>();

		for (E e : bioMarkers) {
			if (e instanceof String[]) {
				PathInfoBioMarker pibm = new PathInfoBioMarker((String[]) e);
				this.bioMarkers.add(pibm);
			} else if (e instanceof PathInfoBioMarker) {
				this.bioMarkers.add((PathInfoBioMarker) e);
			}
		}
	}

	public String getPNID(String preferredName) {
		for (PathInfoBioMarker pibm : bioMarkers) {
			String pn = pibm.getPreferredName();
			if (pn.equalsIgnoreCase(preferredName))
				return pibm.getPNID();
		}
		return null;
	}

	public String getPNIDFromDB(String preferredName) {
		try {
			String q = "SELECT PNID from pathinfo.biomarker where PreferredName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, preferredName);
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

	public String getPreferredName(String PNID) {
		try {
			String q = "select PreferredName from pathinfo.biomarker where PNID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, PNID);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("PreferredName");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Assumed that the element of BioMarker is sequentially stored!
	public String getPreferredNameFromDB(String PNID) {
		try {
			String q = "select PreferredName from pathinfo.biomarker where PNID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, PNID);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("PreferredName");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<PathInfoBioMarker> getBioMarkers() {
		return bioMarkers;
	}

	static final Comparator<Pair<String, Double>> PairComparable = new Comparator<Pair<String, Double>>() {
		public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
			return Double.compare(p2.getB(), p1.getB());
		}
	};

	public String getMostSimilarPreferredName(String preferredName) {
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement("select * from pathinfo.biomarker");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {
				this.bioMarkers.add(new PathInfoBioMarker(rs.getString("pNID"),
						rs.getString("PreferredName"), rs
								.getString("Diagnosis"), rs
								.getString("Prognosis"), rs
								.getString("Treadment"), rs.getString("Date"),
						rs.getString("Active"), rs.getString("InactiveDate"),
						rs.getString("Comment")));
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
		for (PathInfoBioMarker pibm : bioMarkers) {
			if (pibm.getActive().equalsIgnoreCase("N"))
				continue;

			String bioMarker = pibm.getPreferredName();
			double similarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							preferredName.toLowerCase(),
							bioMarker.toLowerCase());
			Pair<String, Double> p = new Pair<String, Double>(bioMarker,
					similarity);
			pairs.add(p);
		}
		Collections.sort(pairs, PairComparable);
		if (pairs.size() == 0)
			return null;

		return pairs.get(0).getA();
	}

	public boolean isDX(String PNID) {
		return true;
	}
}
