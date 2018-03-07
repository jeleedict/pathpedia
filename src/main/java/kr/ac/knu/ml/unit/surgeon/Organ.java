/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.surgeon;

/**
 * 장기명 클래스
 * 
 * @author Hyun-Je
 *
 */
public class Organ {
	private String organName;
	private String SNOMEDorganSui;
	private String candidateOrganName;
	private double organSimilarity;
	
	public Organ() {
		this.organName = null;
		this.SNOMEDorganSui = null;
		this.candidateOrganName = null;
		this.organSimilarity = 0.0;
	}
	
	public Organ( String candidateOrganName ) { 
		this.organName = null;
		this.SNOMEDorganSui = null;
		this.candidateOrganName = candidateOrganName;
		this.organSimilarity = 0.0;
	}
	
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getSNOMEDorganSui() {
		return SNOMEDorganSui;
	}
	public void setSNOMEDorganSui(String sNOMEDorganSui) {
		SNOMEDorganSui = sNOMEDorganSui;
	}
	public String getCandidateOrganName() {
		return candidateOrganName;
	}
	public void setCandidateOrganName(String candidateOrganName) {
		this.candidateOrganName = candidateOrganName;
	}
	public double getOrganSimilarity() {
		return organSimilarity;
	}
	public void setOrganSimilarity(double organSimilarity) {
		this.organSimilarity = organSimilarity;
	}

	@Override
	public String toString() {
		return "Organ [organName=" + organName + ", SNOMEDorganSui="
				+ SNOMEDorganSui + ", candidateOrganName=" + candidateOrganName
				+ ", organSimilarity=" + organSimilarity + "]";
	}
}
