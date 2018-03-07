/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

/**
 * 바이오마커가 값을 다루는 클래스
 * 
 * @author Hyun-Je
 *
 */
public class BioMarkerValue {
	private String degree;
	private String polarity;
	private String location;
	
	public BioMarkerValue() {		
	}
	
	public BioMarkerValue(String degree, String polarity, String location) {
		this.degree = degree;
		this.polarity = polarity;
		this.location = location;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getPolarity() {
		return polarity;
	}

	public void setPolarity(String polarity) {
		this.polarity = polarity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append ( "Degree : " + degree + "\n");
		sb.append ( "Polarity : " + polarity + "\n");
		sb.append ( "위치 : " + location + "\n");
		return sb.toString();
	}
}