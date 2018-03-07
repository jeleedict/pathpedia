/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.SurgeonDocument;

public class MorphometricAnalysis extends ImmunityDiagnosis{
	private String diagnosisMethod;	
	private String diagnosisLocation;	
	private List<SurgeonDocument> candidateSurgeonDocuments;	
	private List<SurgeonDocument> mappedSurgeonDocuments;	
	private boolean isAllSurgeonDocumentsSame;
	
	private List<ChildSurgeonDocument> candidateChildSurgeonDocuments;	
	private List<ChildSurgeonDocument> mappedChildSurgeonDocuments;	
	private boolean isAllChildSurgeonDocumentsSame;
	
	public MorphometricAnalysis() {
		super();
		
		diagnosisMethod = null;
		diagnosisLocation = null;
		candidateSurgeonDocuments = new ArrayList<SurgeonDocument>();
		mappedSurgeonDocuments = new ArrayList<SurgeonDocument>();
		isAllSurgeonDocumentsSame = true;
		
		candidateChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		mappedChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		isAllChildSurgeonDocumentsSame = true;
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
	
	public void addCandidateSurgeonDocument( SurgeonDocument sd ) {
		candidateSurgeonDocuments.add( sd );
	}	
	public ArrayList<SurgeonDocument> getCandidateSurgeonDocuments() {
		return (ArrayList<SurgeonDocument>) candidateSurgeonDocuments;
	}
	
	public void addCandidateChildSurgeonDocument( ChildSurgeonDocument csd ) {
		candidateChildSurgeonDocuments.add( csd );
	}
	
	public ArrayList<ChildSurgeonDocument> getCandidateChildSurgeonDocuments() {
		return (ArrayList<ChildSurgeonDocument>) candidateChildSurgeonDocuments;
	}
	
	public boolean containsOf( SurgeonDocument sd ) {
		for ( SurgeonDocument s : mappedSurgeonDocuments ) {
			if ( s.getPathologyID().equals( sd.getPathologyID() ) )
				return true;
		}
		return false;
	}
	
	public boolean containsOf( ChildSurgeonDocument sd ) {
		for ( ChildSurgeonDocument cs : mappedChildSurgeonDocuments ) {
			if ( cs.getPathologyID().equals( sd.getPathologyID() ) )
				return true;
		}
		return false;
	}
	
	public void addSurgeonDocuments( SurgeonDocument sd ) {
		if ( mappedSurgeonDocuments.size() == 0 )
			mappedSurgeonDocuments.add( sd );
		else {
			if ( containsOf( sd ) == false ) 
				mappedSurgeonDocuments.add( sd );
		}
	}	
	
	public ArrayList<SurgeonDocument> getSurgeonDocuments() {		
		return (ArrayList<SurgeonDocument>) mappedSurgeonDocuments;
	}
	
	public void addChildSurgeonDocuments( ChildSurgeonDocument csd ) {
		if ( mappedChildSurgeonDocuments.size() == 0 )
			mappedChildSurgeonDocuments.add( csd );
		else {
			if ( containsOf( csd ) == false ) 
				mappedChildSurgeonDocuments.add( csd );
		}
	}
	
	public ArrayList<ChildSurgeonDocument> getChildSurgeonDocuments() {
		if ( mappedChildSurgeonDocuments == null )
			return null;
		
		return (ArrayList<ChildSurgeonDocument>) mappedChildSurgeonDocuments;
	}
	
	public void setIsAllSurgeonDocument( boolean isAllSurgeonDocumentsSame ) {
		this.isAllSurgeonDocumentsSame = isAllSurgeonDocumentsSame;  
	}
	
	public boolean getIsAllSurgeonDocument() {
		return isAllSurgeonDocumentsSame;  
	}

	public void setIsAllChildSurgeonDocument( boolean isAllChildSurgeonDocumentsSame ) {
		this.isAllChildSurgeonDocumentsSame = isAllChildSurgeonDocumentsSame;  
	}
	
	public boolean getIsAllChildSurgeonDocument() {
		return isAllChildSurgeonDocumentsSame;  
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append ( "diagnosisName : " + getDiagnosisName() + "\n" );
		
		for ( BioMarker marker : getMarkers() ){
			sb.append ( marker.toString() );
		}
		sb.append ( "diagnosisMethod : " + diagnosisMethod + "\n" );
		sb.append ( "diagnosisLocation : " + diagnosisLocation + "\n" );
		
		return sb.toString();
	}
}
