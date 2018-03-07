/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.parser.immunity.SlideTissue;

public class ImmunityDiagnosis {
	private String diagnosisName;
	private String pathologyNum;
	private String revisedPathologyNum;
	private ArrayList<SlideTissue> slides;
	private List<BioMarker> markers;
	private String diagnosisSummary;
	private String diagnosisNote;

	public ImmunityDiagnosis() {
		diagnosisName = null;
		pathologyNum = null;
		revisedPathologyNum = null;
		slides = new ArrayList<SlideTissue>();
		markers = new ArrayList<BioMarker>();
		diagnosisSummary = "";
		diagnosisNote = "";
	}
	
	public ImmunityDiagnosis( String diagnosisName, List<BioMarker> markers, String diagnosisSummary ) {
		this ( diagnosisName, markers, diagnosisSummary, null );	
	}
	
	public ImmunityDiagnosis( String diagnosisName, List<BioMarker> markers, String diagnosisSummary, String diagnosisNote ) {
		this.diagnosisName = diagnosisName;
		this.markers = new ArrayList<BioMarker>( markers );		
		this.diagnosisSummary = diagnosisSummary;
		this.diagnosisNote = diagnosisNote;		
		this.slides = new ArrayList<SlideTissue>();
	}
	
	public void setPathologyNum() throws NullPointerException, StringIndexOutOfBoundsException {			
		int hyphenIdx = diagnosisName.indexOf("-");			
		String typeYear = diagnosisName.substring(0, hyphenIdx).trim();
		typeYear = StringUtils.removeSpace( typeYear );	
		typeYear = StringUtils.removeSpecialCharacterFromLast( typeYear );
		
		String number = "";
		String rawNum = diagnosisName.substring(hyphenIdx + 1).trim();

		int blankIdx = StringUtils.getNumPosition( rawNum );
		
		if ( blankIdx != -1 ) 
			number = rawNum.substring(0, blankIdx);
		else 
			number = rawNum;
		
		if ( !number.equals("") )
			number = String.valueOf( Integer.parseInt( number ) );
		
		
		String sevenDigitNum = number;
		for ( int i = number.length(); i < 7; i++ ) {
			sevenDigitNum = "0" + sevenDigitNum; 
		}
		pathologyNum = typeYear + sevenDigitNum;
		
		String sixDigitNum = number;
		for ( int i = number.length(); i < 6; i++ ) {
			sixDigitNum = "0" + sixDigitNum; 
		}
		
		revisedPathologyNum = typeYear + sixDigitNum;
	}
	
	public void setPathologyNum( String pathologyNum ) {
		this.pathologyNum = pathologyNum;
	}
	
	public void setRevisedPathologyNum( String revisedPathologyNum ) {
		this.revisedPathologyNum = revisedPathologyNum;
	}
	
	public void cleansingDiagnosisName() {
		this.diagnosisName = this.diagnosisName.replace("- ", "-" ); 
	}
		
	public ArrayList<SlideTissue> getSlides() {
		return slides;
	}

	public void setSlides(ArrayList<SlideTissue> slides) {
		this.slides = slides;
	}
	
	public void addSlides(ArrayList<SlideTissue> slides) {
		this.slides.addAll( slides );
	}

	public String getPathologyNum() {
		return pathologyNum;
	}
	
	public String getRevisedPathologyNum() {
		return revisedPathologyNum;
	}
	
	public String getDiagnosisName() {
		return diagnosisName;
	}
	
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public void setMarkers( ArrayList<BioMarker> markers ) {
		this.markers.addAll( markers );
	}
	
	public void addMarker( BioMarker marker ) {
		this.markers.add( marker );
	}
	
	public void addMarkers( List<BioMarker> markers ) {
		this.markers.addAll( markers );
	}
	
	public List<BioMarker> getMarkers() {
		return markers;
	}
	
	public String getMarkerValue( String markerName ) {
		for ( BioMarker bio : markers ) {
			if ( bio.getMarkerName().equals( markerName ) )
				return bio.getValue();
		}
		return null;
	}

	public String getDiagnosisSummary() {
		return diagnosisSummary;
	}

	public void setDiagnosisSummary(String diagnosisSummary) {
		this.diagnosisSummary = diagnosisSummary;
	}
	
	public void addDiagnosisSummary(String diagnosisSummary) {
		if ( this.diagnosisSummary.length() == 0 )
			this.diagnosisSummary = diagnosisSummary;
		else if ( diagnosisSummary.length() != 0 )			
			this.diagnosisSummary = this.diagnosisSummary + ", " + diagnosisSummary;
	}

	public String getDiagnosisNote() {
		return diagnosisNote;
	}

	public void setDiagnosisNote(String diagnosisNote) {
		this.diagnosisNote = diagnosisNote;
	}

	@Override
	public String toString() {
		return "ImmunityDiagnosis [diagnosisName=" + diagnosisName
				+ ", pathologyNum=" + pathologyNum + ", revisedPathologyNum="
				+ revisedPathologyNum + ", slides=" + slides + ", markers="
				+ markers + ", diagnosisSummary=" + diagnosisSummary
				+ ", diagnosisNote=" + diagnosisNote + "]";
	}	
}
