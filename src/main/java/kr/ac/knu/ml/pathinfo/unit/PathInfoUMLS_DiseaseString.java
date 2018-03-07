/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoUMLS_DiseaseString implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UMLSdisorderSui;
	private String UMLSdisorderCui;
	private String UMLSdisorderName;
	private String Pathinfo_DID;
	private String date;
	private String active;
	private String inactiveDate;
	
	public PathInfoUMLS_DiseaseString(String uMLSdisorderSui,
			String uMLSdisorderCui, String uMLSdisorderName,
			String pathinfo_DID, String date, String active, String inactiveDate) {
		super();
		UMLSdisorderSui = uMLSdisorderSui;
		UMLSdisorderCui = uMLSdisorderCui;
		UMLSdisorderName = uMLSdisorderName;
		Pathinfo_DID = pathinfo_DID;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
	}

	public PathInfoUMLS_DiseaseString(String uMLSdisorderName) {
		super();
		
		UMLSdisorderName = uMLSdisorderName;
		
		UMLSdisorderSui = null;
		UMLSdisorderCui = null;
		Pathinfo_DID = null;
		this.date = null;
		this.active = null;
		this.inactiveDate = null;
	}
	
	
	public PathInfoUMLS_DiseaseString(String... vars) {
		super();
		UMLSdisorderSui = vars[0];
		UMLSdisorderCui = vars[1];
		UMLSdisorderName = vars[2];
		Pathinfo_DID = vars[3];
		this.date = vars[4];
		this.active = vars[5];
		this.inactiveDate = vars[6];
	}

	public String getUMLSdisorderSui() {
		return UMLSdisorderSui;
	}

	public void setUMLSdisorderSui(String uMLSdisorderSui) {
		UMLSdisorderSui = uMLSdisorderSui;
	}

	public String getUMLSdisorderCui() {
		return UMLSdisorderCui;
	}

	public void setUMLSdisorderCui(String uMLSdisorderCui) {
		UMLSdisorderCui = uMLSdisorderCui;
	}

	public String getUMLSdisorderName() {
		return UMLSdisorderName;
	}

	public void setUMLSdisorderName(String uMLSdisorderName) {
		UMLSdisorderName = uMLSdisorderName;
	}

	public String getPathinfo_DID() {
		return Pathinfo_DID;
	}

	public void setPathinfo_DID(String pathinfo_DID) {
		Pathinfo_DID = pathinfo_DID;
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
