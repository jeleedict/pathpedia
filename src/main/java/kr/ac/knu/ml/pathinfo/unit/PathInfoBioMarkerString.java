/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoBioMarkerString implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String BSUI;
	private String string;
	private String PNID;
	private String pathName;
	private String date;
	private String active;
	private String inactiveDate;
	private String comment;
	
	public PathInfoBioMarkerString(String BSUI, String string, String pNID, String pathName,
			String date, String active, String inactiveDate, String comment) {
		super();
		this.BSUI = BSUI;
		this.string = string;
		this.PNID = pNID;
		this.pathName = pathName;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
		this.comment = comment;
	}
	
	public PathInfoBioMarkerString(String... vars) {
		super();
		this.BSUI = vars[0];
		this.string = vars[1];
		this.PNID = vars[2];
		this.pathName = vars[3];
		this.date = vars[4];
		this.active = vars[5];
		this.inactiveDate = vars[6];
		this.comment = vars[7];
	}
	
	public String getSUI() {
		return BSUI;
	}
	public void setSUI(String BSUI) {
		this.BSUI = BSUI;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public String getPNID() {
		return PNID;
	}
	public void setPNID(String pNID) {
		PNID = pNID;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "PathInfoBioMarkerString [BSUI=" + BSUI + ", string=" + string
				+ ", PNID=" + PNID + ", date=" + date + ", active=" + active
				+ ", inactiveDate=" + inactiveDate + ", comment=" + comment
				+ "]";
	}
}
