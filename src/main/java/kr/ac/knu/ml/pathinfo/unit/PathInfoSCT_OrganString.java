/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoSCT_OrganString implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SNOMEDorganSui;
	private String SNOMEDorganCui;
	private String SNOMEDorganName;
	private String date;
	private String active;
	private String inactiveDate;
	private String type;	
	private String language;
	private String version;
	
	public PathInfoSCT_OrganString(String sNOMEDorganSui,
			String sNOMEDorganCui, String sNOMEDorganName, String date,
			String active, String inactiveDate, String type, String language,
			String version) {
		super();
		
		SNOMEDorganSui = sNOMEDorganSui;
		SNOMEDorganCui = sNOMEDorganCui;
		SNOMEDorganName = sNOMEDorganName;
		this.date = date;
		this.active = active;
		this.inactiveDate = inactiveDate;
		this.type = type;
		this.language = language;
		this.version = version;
	}

	public PathInfoSCT_OrganString( String sNOMEDorganName ) {
		super();
				
		SNOMEDorganName = sNOMEDorganName;
		
		SNOMEDorganSui = null;
		SNOMEDorganCui = null;		
		this.date = null;
		this.active = null;
		this.inactiveDate = null;
		this.type = null;
		this.language = null;
		this.version = null;
	}
	
	public PathInfoSCT_OrganString( String... vars ) {
		super();
		
		SNOMEDorganSui = vars[0];
		SNOMEDorganCui = vars[1];
		SNOMEDorganName = vars[2];
		this.date = vars[3];
		this.active = vars[4];
		this.inactiveDate = vars[5];
		this.type = vars[6];
		this.language = vars[7];
		this.version = vars[8];
	}

	public String getSNOMEDorganSui() {
		return SNOMEDorganSui;
	}

	public void setSNOMEDorganSui(String sNOMEDorganSui) {
		SNOMEDorganSui = sNOMEDorganSui;
	}

	public String getSNOMEDorganCui() {
		return SNOMEDorganCui;
	}

	public void setSNOMEDorganCui(String sNOMEDorganCui) {
		SNOMEDorganCui = sNOMEDorganCui;
	}

	public String getSNOMEDorganName() {
		return SNOMEDorganName;
	}

	public void setSNOMEDorganName(String sNOMEDorganName) {
		SNOMEDorganName = sNOMEDorganName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
