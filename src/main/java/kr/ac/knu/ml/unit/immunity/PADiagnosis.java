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

public class PADiagnosis extends ImmunityDiagnosis{	
	private List<SurgeonDocument> candidateSurgeonDocuments;
	private List<SurgeonDocument> mappedSurgeonDocuments;
	private boolean isAllSurgeonDocumentsSame;
	
	private List<ChildSurgeonDocument> candidateChildSurgeonDocuments;
	private List<ChildSurgeonDocument> mappedChildSurgeonDocuments;
	private boolean isAllChildSurgeonDocumentsSame;
	
	public PADiagnosis() {
		super();
		candidateSurgeonDocuments = new ArrayList<SurgeonDocument>();
		mappedSurgeonDocuments = new ArrayList<SurgeonDocument>();
		isAllSurgeonDocumentsSame = true;
		
		candidateChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		mappedChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		isAllChildSurgeonDocumentsSame = true;
	}
	
	public PADiagnosis( String diagnosisName, List<BioMarker> markers, String diagnosisSummary ) {
		super ( diagnosisName, markers, diagnosisSummary, null );
		candidateSurgeonDocuments = new ArrayList<SurgeonDocument>();
		mappedSurgeonDocuments = new ArrayList<SurgeonDocument>();
		isAllSurgeonDocumentsSame = true;
		
		candidateChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		mappedChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		isAllChildSurgeonDocumentsSame = true;
	}
		
	public PADiagnosis( String diagnosisName, List<BioMarker> markers, String diagnosisSummary, String diagnosisNote ) {
		super ( diagnosisName, markers, diagnosisSummary, diagnosisNote );
		candidateSurgeonDocuments = new ArrayList<SurgeonDocument>();
		mappedSurgeonDocuments = new ArrayList<SurgeonDocument>();
		isAllSurgeonDocumentsSame = true;
		
		candidateChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		mappedChildSurgeonDocuments = new ArrayList<ChildSurgeonDocument>();
		isAllChildSurgeonDocumentsSame = true;
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
		if ( mappedSurgeonDocuments == null )
			return null;
		
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
		sb.append( super.toString() + "\n" );		
		return sb.toString();
	}
	
}