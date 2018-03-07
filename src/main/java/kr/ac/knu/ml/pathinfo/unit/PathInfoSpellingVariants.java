/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.unit;

public class PathInfoSpellingVariants {
	private int num;
	private String id;
	private String normalTerm;
	private String variants;
	
	public PathInfoSpellingVariants(String... vars ) {
		super();
		this.num = Integer.parseInt( vars[0] );
		this.id = vars[1];
		this.normalTerm = vars[2];
		this.variants = vars[3];
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNormalTerm() {
		return normalTerm;
	}
	public void setNormalTerm(String normalTerm) {
		this.normalTerm = normalTerm;
	}
	public String getVariants() {
		return variants;
	}
	public void setVariants(String variants) {
		this.variants = variants;
	}
}
