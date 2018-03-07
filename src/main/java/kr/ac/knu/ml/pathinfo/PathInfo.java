/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kr.ac.knu.ml.db.DBConnection;
import kr.ac.knu.ml.pathinfo.unit.PathInfoBioMarker;
import kr.ac.knu.ml.pathinfo.unit.PathInfoBioMarkerString;
import kr.ac.knu.ml.pathinfo.unit.PathInfoDisease;
import kr.ac.knu.ml.pathinfo.unit.PathInfoDiseaseNormalize;
import kr.ac.knu.ml.pathinfo.unit.PathInfoSCT_OrganString;
import kr.ac.knu.ml.pathinfo.unit.PathInfoUMLS_DiseaseString;

public class PathInfo {
	private Connection connection;
	private Statement statement;
	private boolean connectedToDatabase;

	public PathInfo() {
		try {
			connection = DBConnection.getDBConnection();
			statement = connection.createStatement();
			connectedToDatabase = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PathInfoBioMarker getBiomarker(String PNID)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT BMID, PNID, PreferredName, Diagnosis, Prognosis, Treatment "
				+ "Date, Active, InactiveDate "
				+ "FROM biomarker "
				+ "where PNID = '" + PNID + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoBioMarker bm = new PathInfoBioMarker();
		while (resultSet.next()) {
			bm.setPNID((String) resultSet.getObject("PNID"));
			bm.setPreferredName((String) resultSet.getObject("PreferredName"));
		}
		return bm;
	}

	public PathInfoBioMarkerString getBiomarkerString(String markerName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT BSID, BSUI, String, PNID, Date, Active FROM biostring "
				+ "where String = '" + markerName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoBioMarkerString bms = new PathInfoBioMarkerString();
		while (resultSet.next()) {
			bms.setSUI((String) resultSet.getObject("BSUI"));
			bms.setString((String) resultSet.getObject("String"));
			bms.setPNID((String) resultSet.getObject("PNID"));
		}
		return bms;
	}

	public PathInfoBioMarkerString getSimilarBiomarkerString(String markerName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT BSID, BSUI, String, PNID, Date, Active FROM biostring "
				+ "where String = '" + markerName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoBioMarkerString bms = new PathInfoBioMarkerString();
		while (resultSet.next()) {
			bms.setSUI((String) resultSet.getObject("BSUI"));
			bms.setString((String) resultSet.getObject("String"));
			bms.setPNID((String) resultSet.getObject("PNID"));
		}
		return bms;
	}

	public PathInfoSCT_OrganString getSCT_OrganString(String organName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT SNOMEDorgainsui, SNOMEDorgancui, SNOMEDorganName, "
				+ "Date, Active, InactiveDate "
				+ "FROM SCT_OrganString "
				+ "where SNOMEDorganName = '" + organName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoSCT_OrganString organString = new PathInfoSCT_OrganString();
		while (resultSet.next()) {
			organString.setSNOMEDorganSui((String) resultSet
					.getObject("SNOMEDorgainsui"));
			organString.setSNOMEDorganCui((String) resultSet
					.getObject("SNOMEDorgancui"));
			organString.setSNOMEDorganName((String) resultSet
					.getObject("SNOMEDorganName"));
		}
		return organString;
	}

	public PathInfoSCT_OrganString getSimilarSCT_OrganString(String organName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT SNOMEDorgainsui, SNOMEDorgancui, SNOMEDorganName, "
				+ "Date, Active, InactiveDate "
				+ "FROM SCT_OrganString "
				+ "where SNOMEDorganName = '" + organName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoSCT_OrganString organString = new PathInfoSCT_OrganString();
		while (resultSet.next()) {
			organString.setSNOMEDorganSui((String) resultSet
					.getObject("SNOMEDorgainsui"));
			organString.setSNOMEDorganCui((String) resultSet
					.getObject("SNOMEDorgancui"));
			organString.setSNOMEDorganName((String) resultSet
					.getObject("SNOMEDorganName"));
		}
		return organString;
	}

	public PathInfoDisease getDiseases(String diseaseName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT DTID, DiseaseID, DiseaseName, SNOMED1, SNOMED2, SNOMED3"
				+ "SNOMEDVersion FROM disease "
				+ "WHERE DiseaseName = '"
				+ diseaseName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoDisease pd = new PathInfoDisease();
		while (resultSet.next()) {
			pd.setDiseaseID((String) resultSet.getObject("DiseaseID"));
			pd.setDiseaseName((String) resultSet.getObject("DiseaseName"));
			pd.setSNOMED1((String) resultSet.getObject("SNOMED1"));
			pd.setSNOMED2((String) resultSet.getObject("SNOMED2"));
			pd.setSNOMED3((String) resultSet.getObject("SNOMED3"));
		}
		return pd;
	}

	public PathInfoDiseaseNormalize getNormalizedDiseases(String diseaseName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT DTID, DiseaseID, DiseaseName, SNOMED1, SNOMED2, SNOMED3"
				+ "SNOMEDVersion FROM disease "
				+ "WHERE DiseaseName = '"
				+ diseaseName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoDiseaseNormalize pdn = new PathInfoDiseaseNormalize();
		while (resultSet.next()) {

		}
		return pdn;
	}

	public PathInfoUMLS_DiseaseString getUMLS_DiseasesString(String dieaseName)
			throws IllegalStateException, SQLException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		String selectSQL = "SELECT UMLSdisorderSui, UMLSdisorderCui, UMLSdisorderName, "
				+ "Date, Active, InactiveDate FROM UMLS_DiseaseString "
				+ "WHERE UMLSdisorderName = '" + dieaseName + "'";
		ResultSet resultSet = statement.executeQuery(selectSQL);

		PathInfoUMLS_DiseaseString umls_ds = new PathInfoUMLS_DiseaseString();
		while (resultSet.next()) {
			umls_ds.setUMLSdisorderSui((String) resultSet
					.getObject("UMLSdisorderSui"));
			umls_ds.setUMLSdisorderCui((String) resultSet
					.getObject("UMLSdisorderCui"));
			umls_ds.setUMLSdisorderName((String) resultSet
					.getObject("UMLSdisorderName"));
		}
		return umls_ds;
	}
}
