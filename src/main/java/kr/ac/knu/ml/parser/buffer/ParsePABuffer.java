/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.buffer;

import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.ml.unit.immunity.BioMarker;

public class ParsePABuffer extends ParseBuffer{
	private String diagnosisName;		
	private String lastDiagnosisName;
	private String diagnosisSummary;
	private String diagnosisNote;
	private boolean addendum;
	private String addendumDate;
	private ArrayList<BioMarker> markers;	
	private String diagnosisMethod;
	private String diagnosisLocation; 

	public ParsePABuffer() {
		super();
		
		diagnosisName = null;
		lastDiagnosisName = null;
		diagnosisSummary = "";
		diagnosisNote = "";
		addendum = false;
		addendumDate = null;
		markers = new ArrayList<BioMarker>();
		diagnosisMethod = "";
		diagnosisLocation = "";
	}
	
	public void init() {		
		lastDiagnosisName = diagnosisName;
		diagnosisName = null;
		diagnosisSummary = "";
		diagnosisNote = "";		
		markers = new ArrayList<BioMarker>();
		diagnosisMethod = "";
		diagnosisLocation = "";
	}
	
	public String getDiagnosisName() {
		return diagnosisName;
	}
	
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	
	
	public String getLastDiagnosisName() {
		return lastDiagnosisName;
	}
	public void setLastDiagnosisName(String lastDiagnosisName) {
		this.lastDiagnosisName = lastDiagnosisName;
	}
	
	
	public String getDiagnosisSummary() {
		return diagnosisSummary;
	}
	public void setDiagnosisSummary(String diagnosisSummary) {
		this.diagnosisSummary = diagnosisSummary;
	}	
	
	
	public String getDiagnosisNote() {
		return diagnosisNote;
	}

	public void setDiagnosisNote(String diagnosisNote) {
		this.diagnosisNote = diagnosisNote;
	}

	
	public boolean isAddendum() {
		return addendum;
	}
	
	public void setAddendum(boolean addendum) {
		this.addendum = addendum;
	}
	
	public String getAddendumDate() {
		return addendumDate;
	}
	
	public void setAddendumDate(String addendumDate) {
		this.addendumDate = addendumDate;
	}

	
	public ArrayList<BioMarker> getMarkers() {
		return markers;
	}
	
	public void setMarkers(ArrayList<BioMarker> markers) {
		this.markers = markers;
	}
	
	public void addMarkers(List<BioMarker> markers) {
		this.markers.addAll( markers );
	}
	
	public void addMarker(BioMarker marker) {
		this.markers.add( marker );
	}
	
	public String getDiagnosisMethod() {
		return diagnosisMethod;
	}

	public void setDiagnosisMethod(String diagnosisMethod) {
		this.diagnosisMethod = diagnosisMethod;
	}
	
	public String getDiagnosisLocation() {
		return diagnosisLocation;
	}

	public void setDiagnosisLocation(String diagnosisLocation) {
		this.diagnosisLocation = diagnosisLocation;
	}

	public void updateValueAtLastBioMarker(String value) {
		int size = this.markers.size();
		if ( size == 0 )
			return;
		
		BioMarker bm = this.markers.get( size - 1 );
		bm.setValue( value );
	}
	
	public void appendValueAtLastBioMarker(String value) {
		int size = this.markers.size();
		if ( size == 0 )
			return;
		
		BioMarker bm = this.markers.get( size - 1 );
		String previousValue = bm.getValue().trim();
		value = value.toUpperCase();
		if ( previousValue.length() == 0 )
			bm.setValue( value );
		else if ( previousValue.endsWith(",") ) 
			bm.setValue( previousValue + " " + value );
		else 
			bm.setValue( previousValue + ", " + value );	
	}
	
	public BioMarker getLastMarker() {
		int size = this.markers.size();
		if ( size == 0 )
			return null;
		return this.markers.get( size - 1 );
	}

	@Override
	public String toString() {
		return "ParsePABuffer [diagnosisName=" + diagnosisName
				+ ", lastDiagnosisName=" + lastDiagnosisName
				+ ", diagnosisSummary=" + diagnosisSummary + ", diagnosisNote="
				+ diagnosisNote + ", addendum=" + addendum + ", addendumDate="
				+ addendumDate + ", markers=" + markers + "]";
	}
}
