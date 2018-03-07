/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoDiseaseNormalize implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String extracted_diseaseName;
	private String mapped_UMLSdiseaseID;
	private String pathInfo_DiseaseID;
	private String pathInfo_DiseaseName;
	private String date;	
	private String active;
	private String inactiveDate;
	
	public PathInfoDiseaseNormalize(String extracted_diseaseName,
			String mapped_UMLSdiseaseID, String pathInfo_DiseaseID,
			String pathInfo_DiseaseName, String date, String active,
			String inactiveDate) {
		super();
		this.extracted_diseaseName = extracted_diseaseName;
		this.mapped_UMLSdiseaseID = mapped_UMLSdiseaseID;
		this.pathInfo_DiseaseID = pathInfo_DiseaseID;
		this.pathInfo_DiseaseName = pathInfo_DiseaseName;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
	}

	public PathInfoDiseaseNormalize(String extracted_diseaseName) {
		super();
		
		this.extracted_diseaseName = extracted_diseaseName;		
		this.mapped_UMLSdiseaseID = null;
		this.pathInfo_DiseaseID = null;
		this.pathInfo_DiseaseName = null;
		this.date = null;
		this.active = null;
		this.inactiveDate = null;		
	}
	
	public PathInfoDiseaseNormalize(String... vars) {
		super();
		
		this.extracted_diseaseName = vars[0];
		this.mapped_UMLSdiseaseID = vars[1];
		this.pathInfo_DiseaseID = vars[2];
		this.pathInfo_DiseaseName = vars[3];
		this.date = vars[4];
		this.active = vars[5];
		this.inactiveDate = vars[6];		
	}

	public String getExtracted_diseaseName() {
		return extracted_diseaseName;
	}

	public void setExtracted_diseaseName(String extracted_diseaseName) {
		this.extracted_diseaseName = extracted_diseaseName;
	}

	public String getMapped_UMLSdiseaseID() {
		return mapped_UMLSdiseaseID;
	}

	public void setMapped_UMLSdiseaseID(String mapped_UMLSdiseaseID) {
		this.mapped_UMLSdiseaseID = mapped_UMLSdiseaseID;
	}

	public String getPathInfo_DiseaseID() {
		return pathInfo_DiseaseID;
	}

	public void setPathInfo_DiseaseID(String pathInfo_DiseaseID) {
		this.pathInfo_DiseaseID = pathInfo_DiseaseID;
	}

	public String getPathInfo_DiseaseName() {
		return pathInfo_DiseaseName;
	}

	public void setPathInfo_DiseaseName(String pathInfo_DiseaseName) {
		this.pathInfo_DiseaseName = pathInfo_DiseaseName;
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

	public String getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(String inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

}
