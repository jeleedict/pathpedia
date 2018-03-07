/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoOrganRelation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String diseaseName;
	private String diseaseID;
	private String mapID;
	private String organID;
	private String snomed1;
	private String snomed2;
	private String snomed3;	
	private String snomedVersion;
	private String date;
	private String active;
	private String inactiveDate;
	private String comment;
	
	public PathInfoOrganRelation(String mapID, String diseaseID, String diseaseName, 
			String organID, String snomed1, String snomed2, String snomed3, String snomedVersion, 
			String date, String active, String inactiveDate, String comment) {
		super();
		this.mapID = mapID;
		this.diseaseName = diseaseName;
		this.diseaseID = diseaseID;		
		this.organID = organID;
		this.snomed1 = snomed1;
		this.snomed2 = snomed2;
		this.snomed3 = snomed3;
		this.snomedVersion = snomedVersion;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
		this.comment = comment;
	}
	
	public PathInfoOrganRelation(String... vars) {
		super();
		// for previous Pathinfo_DB_130916 
		
		this.mapID = vars[0];
		this.diseaseID = vars[1];		
		this.diseaseName = vars[2];
		this.organID = vars[3];
		this.snomed1 = vars[4];
		this.snomed2 = vars[5];
		this.snomed3 = vars[6];
		this.snomedVersion = vars[7];
		this.date = vars[8];
		this.active = vars[9];
		this.inactiveDate = vars[10];
		this.comment = vars[11];
	}
	
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public String getDiseaseID() {
		return diseaseID;
	}
	public void setDiseaseID(String diseaseID) {
		this.diseaseID = diseaseID;
	}
	public String getMapID() {
		return mapID;
	}
	public void setMapID(String mapID) {
		this.mapID = mapID;
	}
	public String getOrganID() {
		return organID;
	}
	public void setOrganID(String organID) {
		this.organID = organID;
	}	
	public String getSnomed1() {
		return snomed1;
	}
	public void setSnomed1(String snomed1) {
		this.snomed1 = snomed1;
	}
	public String getSnomed2() {
		return snomed2;
	}
	public void setSnomed2(String snomed2) {
		this.snomed2 = snomed2;
	}
	public String getSnomed3() {
		return snomed3;
	}
	public void setSnomed3(String snomed3) {
		this.snomed3 = snomed3;
	}
	public String getSnomedVersion() {
		return snomedVersion;
	}
	public void setSnomedVersion(String snomedVersion) {
		this.snomedVersion = snomedVersion;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getInactiveDactive() {
		return inactiveDate;
	}
	public void setInactiveDactive(String inactiveDactive) {
		this.inactiveDate = inactiveDactive;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}	
}
