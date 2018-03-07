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
import java.util.HashMap;

import kr.ac.knu.ml.db.DBConnection;
import kr.ac.knu.ml.pathinfo.unit.PathInfoOrganRelation;

public class PathInfoOrganRelationRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoOrganRelation> ODrelations;
	private HashMap<String, ArrayList<String>> diseaseNames;

	public <E> void constructRepository(ArrayList<E> ODrelations) {
		this.ODrelations = new ArrayList<PathInfoOrganRelation>();

		for (E e : ODrelations) {
			if (e instanceof String[]) {
				PathInfoOrganRelation pid = new PathInfoOrganRelation(
						(String[]) e);
				this.ODrelations.add(pid);
			} else if (e instanceof PathInfoOrganRelation) {
				this.ODrelations.add((PathInfoOrganRelation) e);
			}
		}
		constructDiseaseNames();
	}

	// 보류
	public void constructDiseaseNames() {
		diseaseNames = new HashMap<String, ArrayList<String>>();

		for (PathInfoOrganRelation pior : ODrelations) {
			String organID = pior.getOrganID();
			ArrayList<String> tmp;
			if (diseaseNames.containsKey(organID))
				tmp = diseaseNames.get(organID);
			else
				tmp = new ArrayList<String>();

			tmp.add(pior.getDiseaseName());
			diseaseNames.put(organID, tmp);
		}
	}

	public ArrayList<String> getDiseaseName(String organID) {
		if (diseaseNames.containsKey(organID))
			return diseaseNames.get(organID);

		return null;
	}

	public ArrayList<String> getDiseaseNameFromDB(String organID) {
		try {
			ArrayList<String> result = new ArrayList<String>();
			String q = "select DiseaseName from pathinfo.odmap where OrganID like ?";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
			ps.setString(1, organID);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
			rs.first();
			while (!rs.isAfterLast()) {
				result.add(rs.getString("DiseaseName"));
				rs.next();
			}
			return result;

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
