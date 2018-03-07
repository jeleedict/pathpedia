/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

import java.io.Serializable;

public class PathInfoSNUMICROField implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String fieldName;
	private String date;
	private String type;
	
	public PathInfoSNUMICROField( String fieldName ) {
		super();
		
		this.fieldName = fieldName;
	}	
	
	public PathInfoSNUMICROField( String... vars ) {
		super();
				
		this.id = vars[0];
		this.fieldName = vars[1];
		this.date = vars[2];		
		this.type = vars[3];
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
