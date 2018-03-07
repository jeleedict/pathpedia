/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

public class PathInfoAbbreviation {
	private int idx;
	private String abbr;
	private String normalTerm;
	private String id;
	
	public PathInfoAbbreviation(int idx, String abbr, String normalTerm, String id) {
		super();
		this.idx = idx;
		this.abbr = abbr;
		this.normalTerm = normalTerm;
		this.id = id;	
	}
	
	public PathInfoAbbreviation(String... vars ) {
		super();
		this.idx = Integer.parseInt( vars[0] );
		this.abbr = vars[1];
		this.normalTerm = vars[2];
		this.id = vars[3];		
	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getNormalTerm() {
		return normalTerm;
	}
	public void setNormalTerm(String normalTerm) {
		this.normalTerm = normalTerm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
