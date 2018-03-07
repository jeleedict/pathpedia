/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.surgeon;

import java.io.Serializable;

/**
 * 질병명 클래스
 * 
 * @author Hyun-Je
 *
 */
public class Disease implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String diseaseName;
	private String UMLSdiseaseSui;
	private String candidateDiseaseName;
	private double diseaseSimilarity;
	private boolean containNegation;
	private boolean containConsistentWith;
	private boolean containMetastatic;
	private boolean containInvolvement;
	private boolean containMostLikely;
	private boolean containSuspicious;
	private boolean containFavor;
	private String errorStr;

	public Disease() {
		this.diseaseName = null;
		this.UMLSdiseaseSui = null;
		this.candidateDiseaseName = null;
		this.diseaseSimilarity = 0.0;
		this.containNegation = false;
		this.containConsistentWith = false;
		this.containMetastatic = false;
		this.containInvolvement = false;
		this.containMostLikely = false;
		this.containSuspicious = false;
		this.containFavor = false;
		this.errorStr = null;
	}
	
	public Disease( String candidateDisorderName ) {
		this.diseaseName = null;
		this.UMLSdiseaseSui = null;
		this.candidateDiseaseName = candidateDisorderName;
		this.diseaseSimilarity = 0.0;
		this.containNegation = false;
		this.containConsistentWith = false;
		this.containMetastatic = false;
		this.containInvolvement = false;
		this.containMostLikely = false;
		this.containSuspicious = false;
		this.containFavor = false;
		this.errorStr = null;
	}
	
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String disorderName) {
		this.diseaseName = disorderName;
	}
	public String getUMLSdiseaseSui() {
		return UMLSdiseaseSui;
	}
	public void setUMLSdiseaseSui(String uMLSdisorderSui) {
		UMLSdiseaseSui = uMLSdisorderSui;
	}
	public String getCandidateDiseaseName() {
		return candidateDiseaseName;
	}
	public void setCandidateDiseaseName(String candidateDiseaseName) {
		this.candidateDiseaseName = candidateDiseaseName;
	}	
	public double getDiseaseSimilarity() {
		return diseaseSimilarity;
	}
	public void setDiseaseSimilarity(double diseaseSimilarity) {
		this.diseaseSimilarity = diseaseSimilarity;
	}
	public boolean isContainNegation() {
		return containNegation;
	}
	public void setContainNegation(boolean containNegation) {
		this.containNegation = containNegation;
	}
	public boolean isContainConsistentWith() {
		return containConsistentWith;
	}
	public void setContainConsistentWith(boolean containConsistentWith) {
		this.containConsistentWith = containConsistentWith;
	}	
	public boolean isContainMetastatic() {
		return containMetastatic;
	}
	public void setContainMetastatic(boolean containMetastatic) {
		this.containMetastatic = containMetastatic;
	}
	public boolean isContainMostLikely() {
		return containMostLikely;
	}
	public void setContainMostLikely(boolean containMostLikely) {
		this.containMostLikely = containMostLikely;
	}
	public boolean isContainInvolvement() {
		return containInvolvement;
	}
	public void setContainInvolvement(boolean containInvolvement) {
		this.containInvolvement = containInvolvement;
	}
	public boolean isContainSuspicious() {
		return containSuspicious;
	}
	public void setContainSuspicious(boolean containSuspicious) {
		this.containSuspicious = containSuspicious;
	}	
	public boolean isContainFavor() {
		return containFavor;
	}
	public void setContainFavor(boolean containFavor) {
		this.containFavor = containFavor;
	}
	public String getErrorStr() {
		return errorStr;
	}
	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Disease disease = (Disease)super.clone();
		return disease;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "candidateDiseaseName=" + candidateDiseaseName + "\n");
		sb.append( "diseaseName=" + diseaseName + "\n");
		sb.append( "UMLSdiseaseSui=" + UMLSdiseaseSui + "\n");
		sb.append( "diseaseSimilarity=" + diseaseSimilarity + "\n");
		sb.append("--------------\n");
		sb.append( "containConsistentWith=" + containConsistentWith + "\n");
		sb.append( "containMetastatic=" + containMetastatic + "\n");
		sb.append( "containInvolvement=" + containInvolvement + "\n");
		sb.append( "containMostLikely=" + containMostLikely + "\n");
		sb.append( "containSuspicious=" + containSuspicious + "\n");
		sb.append( "containFavor=" + containFavor + "\n");		
		return sb.toString();
	}
}
