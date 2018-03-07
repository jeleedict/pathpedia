/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoOrgan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String OrganID;
	private String OrganName;
	private String SNOMED1;
	private String SNOMED2;
	private String SNOMED3;	
	private String SystemID;
	private String Date;
	private String Active;
	private String InactiveDate;	
	private String Comment;
	
	public PathInfoOrgan(String organID, String organName, String sNOMED1,
			String sNOMED2, String sNOMED3, String systemID, String date,
			String active, String inactiveDate, String comment) {
		super();
		OrganID = organID;
		OrganName = organName;
		SNOMED1 = sNOMED1;
		SNOMED2 = sNOMED2;
		SNOMED3 = sNOMED3;
		SystemID = systemID;
		Date = date;
		Active = active;
		InactiveDate = inactiveDate;
		Comment = comment;
	}
	
	public PathInfoOrgan(String... vars) {
		super();
		this.OrganID = vars[0];
		this.OrganName = vars[1];
		this.SNOMED1 = vars[2];
		this.SNOMED2 = vars[3];
		this.SNOMED3 = vars[4];
		this.SystemID = vars[5];
		this.Date = vars[6];
		this.Active = vars[7];
		this.InactiveDate = vars[8];
		this.Comment = vars[9];
	}
	
	public String getOrganID() {
		return OrganID;
	}
	public void setOrganID(String OrganID) {
		this.OrganID = OrganID;
	}
	public String getOrganName() {
		return OrganName;
	}
	public void setOrganName(String OrganName) {
		this.OrganName = OrganName;
	}
	public String getSNOMED1() {
		return SNOMED1;
	}
	public void setSNOMED1(String SNOMED1) {
		this.SNOMED1 = SNOMED1;
	}
	public String getSNOMED2() {
		return SNOMED2;
	}
	public void setSNOMED2(String SNOMED2) {
		this.SNOMED2 = SNOMED2;
	}
	public String getSNOMED3() {
		return SNOMED3;
	}
	public void setSNOMED3(String SNOMED3) {
		this.SNOMED3 = SNOMED3;
	}
	public String getSystemID() {
		return SystemID;
	}
	public void setSystemID(String SystemID) {
		this.SystemID = SystemID;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String Date) {
		this.Date = Date;
	}
	public String getActive() {
		return Active;
	}
	public void setActive(String Active) {
		this.Active = Active;
	}
	public String getInactiveDate() {
		return InactiveDate;
	}
	public void setInactiveDate(String InactiveDate) {
		this.InactiveDate = InactiveDate;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String Comment) {
		this.Comment = Comment;
	}
	
	@Override
	public String toString() {
		return "Organ [OrganID=" + OrganID + ", OrganName=" + OrganName
				+ ", SNOMED1=" + SNOMED1 + ", SNOMED2=" + SNOMED2
				+ ", SNOMED3=" + SNOMED3 + ", SystemID=" + SystemID + ", Date="
				+ Date + ", Active=" + Active + ", InactiveDate="
				+ InactiveDate + ", Comment=" + Comment + "]";
	}	
}
