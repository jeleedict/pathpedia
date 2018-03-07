/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoOrganNormalize implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String extracted_OrganName;
	private String mapped_SCTorganID;
	private String pathinfo_OrganID;
	private String pathinfo_OrganName;
	private String date;
	private String active;
	private String inactiveDate;
	
	public PathInfoOrganNormalize(String extracted_OrganName,
			String mapped_SCTorganID, String pathinfo_OrganID,
			String pathinfo_OrganName, String date, String active,
			String inactiveDate) {
		super();
		
		this.extracted_OrganName = extracted_OrganName;
		this.mapped_SCTorganID = mapped_SCTorganID;
		this.pathinfo_OrganID = pathinfo_OrganID;
		this.pathinfo_OrganName = pathinfo_OrganName;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
	}
	
	public PathInfoOrganNormalize(String... vars) {
		super();
		
		this.extracted_OrganName = vars[0];
		this.mapped_SCTorganID = vars[1];
		this.pathinfo_OrganID = vars[2];
		this.pathinfo_OrganName = vars[3];
		this.date = vars[4];
		this.active = vars[5];
		this.inactiveDate = vars[6];
	}

	public String getExtracted_OrganName() {
		return extracted_OrganName;
	}


	public void setExtracted_OrganName(String extracted_OrganName) {
		this.extracted_OrganName = extracted_OrganName;
	}


	public String getMapped_SCTorganID() {
		return mapped_SCTorganID;
	}


	public void setMapped_SCTorganID(String mapped_SCTorganID) {
		this.mapped_SCTorganID = mapped_SCTorganID;
	}


	public String getPathinfo_OrganID() {
		return pathinfo_OrganID;
	}


	public void setPathinfo_OrganID(String pathinfo_OrganID) {
		this.pathinfo_OrganID = pathinfo_OrganID;
	}


	public String getPathinfo_OrganName() {
		return pathinfo_OrganName;
	}


	public void setPathinfo_OrganName(String pathinfo_OrganName) {
		this.pathinfo_OrganName = pathinfo_OrganName;
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
