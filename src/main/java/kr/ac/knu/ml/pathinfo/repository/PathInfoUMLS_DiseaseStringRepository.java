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
import kr.ac.knu.ml.pathinfo.unit.PathInfoUMLS_DiseaseString;
import kr.ac.knu.ml.similarity.LevenshteinDistance;
import kr.ac.knu.ml.similarity.SoftTFIDFDistance;
import kr.ac.knu.ml.unit.surgeon.Disease;

public class PathInfoUMLS_DiseaseStringRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PathInfoUMLS_DiseaseString> disorderStrings;

	static final Comparator<PathInfoUMLS_DiseaseString> PathInfoDisorderStringComparable = new Comparator<PathInfoUMLS_DiseaseString>() {
		public int compare(PathInfoUMLS_DiseaseString arg0,
				PathInfoUMLS_DiseaseString arg1) {
			return arg1.getUMLSdisorderName().compareToIgnoreCase(
					arg0.getUMLSdisorderName());
		}
	};

	public <E> void constructRepository(ArrayList<E> disorderStrings) {
		this.disorderStrings = new ArrayList<PathInfoUMLS_DiseaseString>();

		for (E e : disorderStrings) {
			if (e instanceof String[]) {
				PathInfoUMLS_DiseaseString pids = new PathInfoUMLS_DiseaseString(
						(String[]) e);
				if (pids.getActive().equals("O"))
					this.disorderStrings.add(pids);
			} else if (e instanceof PathInfoUMLS_DiseaseString) {
				this.disorderStrings.add((PathInfoUMLS_DiseaseString) e);
			}
		}
		// sort disorderString by disorderName
		Collections
				.sort(this.disorderStrings, PathInfoDisorderStringComparable);
	}

	public String getDisorderSui(String disorderName) {
		int idx = Collections.binarySearch(disorderStrings,
				new PathInfoUMLS_DiseaseString(disorderName),
				PathInfoDisorderStringComparable);
		if (idx >= 0)
			return disorderStrings.get(idx).getUMLSdisorderSui();
		return null;
	}

	public String getDisorderSuiFromDB(String disorderName) {
		try {
			String q = "select * from pathinfo.UMLS_DiseaseString where UMLSdisorderName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, disorderName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("UMLSdisorderSui");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDisorderSui(int idx) {
		return disorderStrings.get(idx).getUMLSdisorderSui();
	}

	public String getDisorderSuiFromDB(int idx) {
		try {
			String q = "select * from pathinfo.UMLS_DiseaseString where PK = ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setInt(1, idx);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("UMLSdisorderSui");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDisorderCui(String disorderName) {
		int idx = Collections.binarySearch(disorderStrings,
				new PathInfoUMLS_DiseaseString(disorderName),
				PathInfoDisorderStringComparable);
		if (idx >= 0)
			return disorderStrings.get(idx).getUMLSdisorderSui();
		return null;
	}

	public String getDisorderCuiFromDB(String disorderName) {
		try {
			String q = "select * from pathinfo.UMLS_DiseaseString where UMLSdisorderName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, disorderName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("UMLSdisorderCui");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Pair<Boolean, Integer> contains(String disorderName) {
		int idx = Collections.binarySearch(disorderStrings,
				new PathInfoUMLS_DiseaseString(disorderName),
				PathInfoDisorderStringComparable);

		boolean ret = false;
		if (idx >= 0)
			ret = true;

		Pair<Boolean, Integer> tuple = new Pair<Boolean, Integer>(ret, idx);
		return tuple;
	}

	public Pair<Boolean, Integer> containsFromDB(String disorderName) {
		disorderStrings = new ArrayList<PathInfoUMLS_DiseaseString>();
		try {

			PreparedStatement ps = DBConnection
					.getDBConnection()
					.prepareStatement(
							"select * from pathinfo.UMLS_DiseaseString where UMLSdisorderName like ?");
			ps.setString(1, disorderName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return new Pair<Boolean, Integer>(false, -1);
			rs.first();

			Pair<Boolean, Integer> tuple = new Pair<Boolean, Integer>(true,
					rs.getInt("PK"));
			return tuple;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Pair<Boolean, Integer>(false, -1);
	}

	public Disease getSimilarDiseaseNameUsingLevenshtein(
			String diseaseNameCandidate) {
		disorderStrings = new ArrayList<PathInfoUMLS_DiseaseString>();
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement(
							"select * from pathinfo.UMLS_DiseaseString");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {

				this.disorderStrings.add(new PathInfoUMLS_DiseaseString(rs
						.getString("UMLSdisorderSui"), rs
						.getString("UMLSdisorderCui"), rs
						.getString("UMLSdisorderName"), rs
						.getString("PATH_DID"), rs.getString("Date"), rs
						.getString("Active"), rs.getString("InactiveDate")));
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
		for (PathInfoUMLS_DiseaseString pids : disorderStrings) {
			String name = pids.getUMLSdisorderName();
			double levenshteinSimilarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							diseaseNameCandidate.toLowerCase(),
							name.toLowerCase());

			if (levenshteinSimilarity > levenshteinPair.getB()) {
				levenshteinPair = Pair.makePair(name, levenshteinSimilarity);
			}
		}
		Disease disease = new Disease();
		disease.setCandidateDiseaseName(diseaseNameCandidate);
		disease.setDiseaseName(levenshteinPair.getA());
		disease.setUMLSdiseaseSui(getDisorderSui(levenshteinPair.getA()));
		disease.setDiseaseSimilarity(levenshteinPair.getB());
		return disease;
	}

	public Disease getSimilarDiseaseNameUsingSoftTFIDF(
			String diseaseNameCandidate) {
		disorderStrings = new ArrayList<PathInfoUMLS_DiseaseString>();
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement(
							"select * from pathinfo.UMLS_DiseaseString");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {

				this.disorderStrings.add(new PathInfoUMLS_DiseaseString(rs
						.getString("UMLSdisorderSui"), rs
						.getString("UMLSdisorderCui"), rs
						.getString("UMLSdisorderName"), rs
						.getString("PATH_DID"), rs.getString("Date"), rs
						.getString("Active"), rs.getString("InactiveDate")));
				rs.next();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pair<String, Double> candidate = Pair.makePair(null, 0.0);
		for (PathInfoUMLS_DiseaseString pids : disorderStrings) {
			String name = pids.getUMLSdisorderName();
			double similarity = SoftTFIDFDistance.similarity(
					diseaseNameCandidate.toLowerCase(), name.toLowerCase());

			if (similarity > candidate.getB()) {
				candidate = Pair.makePair(name, similarity);
			}
		}

		Disease disease = new Disease();
		disease.setCandidateDiseaseName(diseaseNameCandidate);
		if (candidate.getA() != null) {
			disease.setDiseaseName(candidate.getA());
			disease.setUMLSdiseaseSui(getDisorderSui(candidate.getA()));
			disease.setDiseaseSimilarity(candidate.getB());
		}
		return disease;
	}

	public boolean startWithDiseaseName(String content) {
		content = content.toLowerCase();
		for (PathInfoUMLS_DiseaseString pids : disorderStrings) {
			String name = pids.getUMLSdisorderName().toLowerCase();
			if (content.startsWith(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean startWithDiseaseNameFromDB(String content) {
		content = content.toLowerCase();
		try {
			String q = "select * from pathinfo.UMLS_DiseaseString where UMLSdisorderName like ?";
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
