/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
// Copyright 2010 - 2014, Kyungpook National University and Contributors

package kr.ac.knu.ml.document;

import java.util.ArrayList;

/**
 * Universal Class for SNUH pathology report in excel <br/>
 * 
 * @author Hyun-Je
 * 
 */
public abstract class BioMedicalDocument {
	private String pathologyID;
	private String originalText;
	private String patientNumber;
	private String gender;
	private int age;
	private String ageUnit;

	private String snomedLocation;
	private String snomedTestName;
	private String snomedDiagnosisName;

	private ArrayList<String> errorStrs;

	public BioMedicalDocument() {
		super();
		this.pathologyID = null;
		this.originalText = null;
		this.patientNumber = null;
		this.gender = null;
		this.age = 0;
		this.ageUnit = null;
		this.snomedLocation = null;
		this.snomedTestName = null;
		this.snomedDiagnosisName = null;

		this.errorStrs = null;
	}

	public BioMedicalDocument(String pathologyID) {
		super();
		this.pathologyID = pathologyID;
		this.originalText = null;
		this.patientNumber = null;
		this.gender = null;
		this.age = 0;
		this.ageUnit = null;
		this.snomedLocation = null;
		this.snomedTestName = null;
		this.snomedDiagnosisName = null;

		this.errorStrs = null;
	}

	public BioMedicalDocument(String pathologyID, String originalText,
			String patientNumber, String gender, int age, String ageUnit,
			String snomedLocation, String snomedTestName,
			String snomedDiagnosisName) {
		super();
		this.pathologyID = pathologyID;
		this.originalText = originalText;
		this.patientNumber = patientNumber;
		this.gender = gender;
		this.age = age;
		this.ageUnit = ageUnit;
		this.snomedLocation = snomedLocation;
		this.snomedTestName = snomedTestName;
		this.snomedDiagnosisName = snomedDiagnosisName;

		this.errorStrs = null;
	}

	public String getPathologyID() {
		return pathologyID;
	}

	public void setPathologyID(String pathologyID) {
		this.pathologyID = pathologyID;
	}

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getPatientNumber() {
		return patientNumber;
	}

	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}

	public String getSnomedLocation() {
		return snomedLocation;
	}

	public void setSnomedLocation(String snomedLocation) {
		this.snomedLocation = snomedLocation;
	}

	public String getSnomedTestName() {
		return snomedTestName;
	}

	public void setSnomedTestName(String snomedTestName) {
		this.snomedTestName = snomedTestName;
	}

	public String getSnomedDiagnosisName() {
		return snomedDiagnosisName;
	}

	public void setSnomedDiagnosisName(String snomedDiagnosisName) {
		this.snomedDiagnosisName = snomedDiagnosisName;
	}

	public ArrayList<String> getErrorStrs() {
		return errorStrs;
	}

	public void setErrorStr(ArrayList<String> errorStrs) {
		this.errorStrs = errorStrs;
	}

	public void addErrorStr(String errorStr) {
		if (this.errorStrs == null)
			this.errorStrs = new ArrayList<String>();

		this.errorStrs.add(errorStr);
	}

	@Override
	public String toString() {
		return "BioMedicalDocument [pathologyID=" + pathologyID
				+ ", patientNumber=" + patientNumber + ", gender=" + gender
				+ ", age=" + age + ", ageUnit=" + ageUnit + ", snomedLocation="
				+ snomedLocation + ", snomedTestName=" + snomedTestName
				+ ", snomedDiagnosisName=" + snomedDiagnosisName + "]";
	}
}
