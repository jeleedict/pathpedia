/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoBioMarker implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String PNID;
	private String preferredName;
	private String dx;
	private String px;
	private String tx;
	private String date;
	private String active;
	private String inactiveDate;
	private String comment;
	
	public PathInfoBioMarker(String pNID, String preferredName, String dx,
			String px, String tx, String date, String active,
			String inactiveDate, String comment) {
		super();
		PNID = pNID;
		this.preferredName = preferredName;
		this.dx = dx;
		this.px = px;
		this.tx = tx;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
		this.comment = comment;
	}
	
	
	public PathInfoBioMarker(String... vars) {
		super();
		this.PNID = vars[0];
		this.preferredName = vars[1];
		this.dx = vars[2];
		this.px = vars[3];
		this.tx = vars[4];
		this.date = vars[5];
		this.active = vars[6];
		this.inactiveDate = vars[7];
		this.comment = vars[8];
	}
	
	public String getPNID() {
		return PNID;
	}
	public void setPNID(String pNID) {
		PNID = pNID;
	}
	public String getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	public String getDx() {
		return dx;
	}
	public void setDx(String dx) {
		this.dx = dx;
	}
	public String getPx() {
		return px;
	}
	public void setPx(String px) {
		this.px = px;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
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
		return "PathInfoBioMarker [PNID=" + PNID + ", preferredName="
				+ preferredName + ", dx=" + dx + ", px=" + px + ", tx=" + tx
				+ ", date=" + date + ", active=" + active + ", inactiveDate="
				+ inactiveDate + ", comment=" + comment + "]";
	}
}

