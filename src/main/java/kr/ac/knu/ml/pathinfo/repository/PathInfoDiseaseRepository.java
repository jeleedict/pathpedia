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
import kr.ac.knu.ml.pathinfo.unit.PathInfoDisease;
import kr.ac.knu.ml.similarity.JaroWinkler;
import kr.ac.knu.ml.similarity.LevenshteinDistance;
import kr.ac.knu.ml.similarity.StringKernel;

public class PathInfoDiseaseRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoDisease> diseases;
	private ArrayList<String> diseaseNames;

	static final Comparator<PathInfoDisease> DiseaseNameComparalbe = new Comparator<PathInfoDisease>() {
		public int compare(PathInfoDisease pid1, PathInfoDisease pid2) {
			return pid1.getDiseaseName().toLowerCase()
					.compareTo(pid2.getDiseaseName().toLowerCase());
		}
	};

	public <E> void constructRepository(ArrayList<E> diseases) {
		this.diseases = new ArrayList<PathInfoDisease>();

		for (E e : diseases) {
			if (e instanceof String[]) {
				PathInfoDisease pid = new PathInfoDisease((String[]) e);
				this.diseases.add(pid);
			} else if (e instanceof PathInfoDisease) {
				this.diseases.add((PathInfoDisease) e);
			}
		}

		Collections.sort(this.diseases, DiseaseNameComparalbe);
		constructDiseaseNames();
	}

	public void constructDiseaseNames() {
		diseaseNames = new ArrayList<String>();
		for (PathInfoDisease pid : diseases) {
			diseaseNames.add(pid.getDiseaseName());
		}
	}

	public String getDiseaseName(String diseaseID) {
		for (PathInfoDisease pid : diseases) {
			String did = pid.getDiseaseID();
			if (did.equalsIgnoreCase(diseaseID)) {
				return pid.getDiseaseName();
			}
		}
		return null;
	}

	public String getDiseaseNameFromDB(String diseaseID) {
		try {
			String q = "select DiseaseName from pathinfo.disease where DiseaseID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, diseaseID);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("DiseaseName");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public String getDiseaseName(int idx) {
		return diseases.get(idx).getDiseaseName();
	}

	public String getDiseaseID(int idx) {
		return diseases.get(idx).getDiseaseID();
	}

	public String getDiseaseIDFromDB(int idx) {
		try {
			String q = "select DiseaseID from pathinfo.disease where PK = ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setInt(1, idx);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			return rs.getString("DiseaseID");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> getDiseaseNames() {
		if (diseaseNames != null)
			return diseaseNames;
		return null;
	}

	public Pair<Boolean, Integer> contains(String disaseName) {
		disaseName = disaseName.trim();
		PathInfoDisease pid = new PathInfoDisease(disaseName);

		int idx = Collections
				.binarySearch(diseases, pid, DiseaseNameComparalbe);

		boolean ret = false;
		if (idx >= 0)
			ret = true;

		Pair<Boolean, Integer> tuple = new Pair<Boolean, Integer>(ret, idx);
		return tuple;
	}

	public Pair<Boolean, Integer> containsFromDB(String diseaseName) {
		diseaseName = diseaseName.trim();

		try {
			String q = "select * from pathinfo.disease where DiseaseName like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, diseaseName);
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

	public ArrayList<String> getSimilarDiseaseNames(String diseaseName) {
		ArrayList<PathInfoDisease> diseases = new ArrayList<PathInfoDisease>();
		try {

			PreparedStatement ps = DBConnection.getDBConnection()
					.prepareStatement("select * from pathinfo.disease");
			ResultSet rs = ps.executeQuery();
			rs.first();
			while (!rs.isAfterLast()) {
				this.diseases.add(new PathInfoDisease(
						rs.getString("DiseaseID"), rs.getString("DiseaseName"),
						rs.getString("SNOMED1"), rs.getString("SNOMED2"), rs
								.getString("SNOMED3"), rs.getString("Date"), rs
								.getString("Active"), rs
								.getString("InactiveDate"), rs
								.getString("Comment")));
				rs.next();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Pair<String, Double> jaroWinklerPair = Pair.makePair(null, 0.0);
		Pair<String, Double> stringKernelPair = Pair.makePair(null, 0.0);
		Pair<String, Double> levenshteinPair = Pair.makePair(null, 0.0);

		for (PathInfoDisease pid : diseases) {
			if (pid.getActive().equalsIgnoreCase("N"))
				continue;

			String dn = pid.getDiseaseName();
			double jaroWinklerSimilarity = JaroWinkler.similarity(
					diseaseName.toLowerCase(), dn.toLowerCase());
			double stringKernelSimilarity = StringKernel.similarity(
					diseaseName.toLowerCase(), dn.toLowerCase());
			double levenshteinSimilarity = 1 - LevenshteinDistance
					.computeNormalizedLevenshteinDistance(
							diseaseName.toLowerCase(), dn.toLowerCase());

			if (jaroWinklerSimilarity > jaroWinklerPair.getB()) {
				jaroWinklerPair = Pair.makePair(dn, jaroWinklerSimilarity);
			}

			if (stringKernelSimilarity > stringKernelPair.getB()) {
				stringKernelPair = Pair.makePair(dn, stringKernelSimilarity);
			}

			if (levenshteinSimilarity > levenshteinPair.getB()) {
				levenshteinPair = Pair.makePair(dn, levenshteinSimilarity);
			}
		}
		ArrayList<String> candidateVariants = new ArrayList<String>();
		candidateVariants.add(jaroWinklerPair.getA());
		candidateVariants.add(stringKernelPair.getA());
		candidateVariants.add(levenshteinPair.getA());

		return candidateVariants;
	}
}
