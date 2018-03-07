/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoDisease implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String DiseaseID;
	private String DiseaseName;
	private String SNOMED1;
	private String SNOMED2;
	private String SNOMED3;
	private String Date;
	private String Active;
	private String InactiveDate;
	private String Comment;
	
	public PathInfoDisease( String diseaseName ) {
		super();
		DiseaseID = null;
		
		DiseaseName = diseaseName;
		
		SNOMED1 = null;
		SNOMED2 = null;
		SNOMED3 = null;
		Date = null;
		Active = null;
		InactiveDate = null;
		Comment = null;		
	}
	
	public PathInfoDisease( String... vars ) {
		super();
		DiseaseID = vars[0];
		DiseaseName = vars[1];
		SNOMED1 = vars[2];
		SNOMED2 = vars[3];
		SNOMED3 = vars[4];
		Date = vars[5];
		Active = vars[6];
		InactiveDate = vars[7];
		Comment = vars[8];		
	}
	
	public String getDiseaseID() {
		return DiseaseID;
	}
	public void setDiseaseID(String DiseaseID) {
		this.DiseaseID = DiseaseID;
	}
	public String getDiseaseName() {
		return DiseaseName;
	}
	public void setDiseaseName(String DiseaseName) {
		this.DiseaseName = DiseaseName;
	}

	public String getSNOMED1() {
		return SNOMED1;
	}

	public void setSNOMED1(String sNOMED1) {
		SNOMED1 = sNOMED1;
	}

	public String getSNOMED2() {
		return SNOMED2;
	}

	public void setSNOMED2(String sNOMED2) {
		SNOMED2 = sNOMED2;
	}

	public String getSNOMED3() {
		return SNOMED3;
	}

	public void setSNOMED3(String sNOMED3) {
		SNOMED3 = sNOMED3;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getActive() {
		return Active;
	}

	public void setActive(String active) {
		Active = active;
	}

	public String getInactiveDate() {
		return InactiveDate;
	}

	public void setInactiveDate(String inactiveDate) {
		InactiveDate = inactiveDate;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	
}
