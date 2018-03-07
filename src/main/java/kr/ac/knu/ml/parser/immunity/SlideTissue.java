/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.immunity;

import java.util.ArrayList;

public class SlideTissue {
	private String slideTissueName;	
	private String slideTissueInformation;
	private ArrayList<String> slideTissueKeyID;
	
	public SlideTissue(String slideTissueName, String slideTissueInformation) {
		this.slideTissueName = slideTissueName;
		this.slideTissueInformation = slideTissueInformation;
		this.slideTissueKeyID = new ArrayList<String>();
	}
	public String getSlideTissueName() {
		return slideTissueName;
	}
	public void setSlideTissueName(String slideTissueName) {
		this.slideTissueName = slideTissueName;
	}
	
	public ArrayList<String> getSlideTissueKeyID() {
		return slideTissueKeyID;
	}
	public void setSlideTissueKeyID(String slideTissueKeyID) {		
		this.slideTissueKeyID.add( slideTissueKeyID );
	}
	
	public String getSlideTissueInformation() {
		return slideTissueInformation;
	}
	public void setSlideTissueInformation(String slideTissueInformation) {
		this.slideTissueInformation = slideTissueInformation;
	}
	@Override
	public String toString() {
		return "SlideTissue [slideTissueName=" + slideTissueName
				+ ", slideTissueKeyID=" + slideTissueKeyID
				+ ", slideTissueInformation=" + slideTissueInformation + "]";
	}
}
