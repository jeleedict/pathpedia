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
import kr.ac.knu.ml.pathinfo.unit.PathInfoSCT_OrganString;
import kr.ac.knu.ml.similarity.LevenshteinDistance;
import kr.ac.knu.ml.unit.surgeon.Organ;

public class PathInfoSCT_OrganStringRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PathInfoSCT_OrganString> organStrings;

	static final Comparator<PathInfoSCT_OrganString> PathInfoOrganStringComparable = new Comparator<PathInfoSCT_OrganString>() {
		public int compare(PathInfoSCT_OrganString arg0,
				PathInfoSCT_OrganString arg1) {
			return arg1.getSNOMEDorganName().compareToIgnoreCase(
					arg0.getSNOMEDorganName());
		}
	};

	public <E> void constructRepository(ArrayList<E> oStrings) {
		this.organStrings = new ArrayList<PathInfoSCT_OrganString>();

		for (E e : oStrings) {
			if (e instanceof String[]) {
				PathInfoSCT_OrganString pios = new PathInfoSCT_OrganString(
						(String[]) e);
				this.organStrings.add(pios);
			} else if (e instanceof PathInfoSCT_OrganString) {
				this.organStrings.add((PathInfoSCT_OrganString) e);
			}
		}

		Collections.sort(this.organStrings, PathInfoOrganStringComparable);
	}

	public String getSNOMEDorganSui(String organName) {
		int idx = Collections.binarySearch(organStrings,
				new PathInfoSCT_OrganString(organName),
				PathInfoOrganStringComparable);
		if (idx >= 0)
			return organStrings.get(idx).getSNOMEDorganSui();

		return null;
	}

	public String getSNOMEDorganSuiFromDB(String organName) {
		try {
			String q = "select * from pathinfo.SCT_OrganString where SNOMEDorganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("SNOMEDorgansui");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getSNOMEDorganCui(String organName) {
		int idx = Collections.binarySearch(organStrings,
				new PathInfoSCT_OrganString(organName),
				PathInfoOrganStringComparable);
		if (idx >= 0)
			return organStrings.get(idx).getSNOMEDorganCui();

		return null;
	}

	public String getSNOMEDorganCuiFromDB(String organName) {
		try {
			String q = "select * from pathinfo.SCT_OrganString where SNOMEDorganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("SNOMEDorgancui");

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
		int idx = Collections.binarySearch(organStrings,
				new PathInfoSCT_OrganString(organName),
				PathInfoOrganStringComparable);
		if (idx < 0)
			return false;

		return true;
	}

	public boolean containsFromDB(String organName) {
		try {
			String q = "select * from pathinfo.SCT_OrganString where SNOMEDorganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organName);
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

	public Organ getSimilarOrgans(String organNameCandidate) {
		organStrings = new ArrayList<PathInfoSCT_OrganString>();
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement("select * from pathinfo.SCT_OrganString");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {

				this.organStrings.add(new PathInfoSCT_OrganString(rs
						.getString("SNOMEDorgansui"), rs
						.getString("SNOMEDorgancui"), rs
						.getString("SNOMEDorganName"), rs.getString("Date"), rs
						.getString("Active"), rs.getString("InactiveDate"), rs
						.getString("type"), rs.getString("language"), rs
						.getString("version")));
				rs.next();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pair<String, Double> levenshteinPair = Pair.makePair(null, 0.0);

		for (PathInfoSCT_OrganString pios : organStrings) {
			String name = pios.getSNOMEDorganName();
			double levenshteinSimilarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							organNameCandidate.toLowerCase(),
							name.toLowerCase());

			if (levenshteinSimilarity > levenshteinPair.getB()) {
				levenshteinPair = Pair.makePair(name, levenshteinSimilarity);
			}
		}

		Organ organ = new Organ();
		organ.setOrganName(levenshteinPair.getA());
		organ.setCandidateOrganName(organNameCandidate);
		organ.setSNOMEDorganSui(getSNOMEDorganSui(levenshteinPair.getA()));
		organ.setOrganSimilarity(levenshteinPair.getB());

		return organ;
	}

	public Organ getSimilarOrgansFromDB(String organNameCandidate) {
		organStrings = new ArrayList<PathInfoSCT_OrganString>();
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement("select * from pathinfo.SCT_OrganString");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {

				this.organStrings.add(new PathInfoSCT_OrganString(rs
						.getString("SNOMEDorgansui"), rs
						.getString("SNOMEDorgancui"), rs
						.getString("SNOMEDorganName"), rs.getString("Date"), rs
						.getString("Active"), rs.getString("InactiveDate"), rs
						.getString("type"), rs.getString("language"), rs
						.getString("version")));
				rs.next();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pair<String, Double> levenshteinPair = Pair.makePair(null, 0.0);

		for (PathInfoSCT_OrganString pios : organStrings) {
			String name = pios.getSNOMEDorganName();
			double levenshteinSimilarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							organNameCandidate.toLowerCase(),
							name.toLowerCase());

			if (levenshteinSimilarity > levenshteinPair.getB()) {
				levenshteinPair = Pair.makePair(name, levenshteinSimilarity);
			}
		}

		Organ organ = new Organ();
		organ.setOrganName(levenshteinPair.getA());
		organ.setCandidateOrganName(organNameCandidate);
		organ.setSNOMEDorganSui(getSNOMEDorganSui(levenshteinPair.getA()));
		organ.setOrganSimilarity(levenshteinPair.getB());

		return organ;
	}

	public boolean startWithOrganNameFromDB(String content) {
		try {
			String q = "select * from pathinfo.SCT_OrganString where SNOMEDorganName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, content + "%");
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
}
