/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;

public class Matcher {
	static final Comparator<SurgeonDocument> SurgeonDocumentComparable = new Comparator<SurgeonDocument>() {
		public int compare(SurgeonDocument sd1, SurgeonDocument sd2) {
			return sd1.getPathologyID().compareTo(sd2.getPathologyID());
		}
	};
	
	static final Comparator<SurgeonDocument> SurgeonDocumentAbbrComparable = new Comparator<SurgeonDocument>() {
		public int compare(SurgeonDocument sd1, SurgeonDocument sd2) {
			return sd1.getAbbrPathologyID().compareTo(sd2.getAbbrPathologyID());
		}
	};
	
	static final Comparator<ChildSurgeonDocument> ChildSurgeonDocumentComparable = new Comparator<ChildSurgeonDocument>() {
		public int compare(ChildSurgeonDocument csd1, ChildSurgeonDocument csd2) {
			return csd1.getPathologyID().compareTo(csd2.getPathologyID());
		}
	};
	
	static final Comparator<ChildSurgeonDocument> ChildSurgeonDocumentAbbrComparable = new Comparator<ChildSurgeonDocument>() {
		public int compare(ChildSurgeonDocument csd1, ChildSurgeonDocument csd2) {
			return csd1.getAbbrPathologyID().compareTo(csd2.getAbbrPathologyID());
		}
	};
	
	private static void searching( String pathologyNum, PADiagnosis pa, ArrayList<SurgeonDocument> sdocs ) {
		SurgeonDocument sd = new SurgeonDocument ();
		
		sd.setPathologyID( pathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( sdocs, sd, SurgeonDocumentComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( sdocs.get( left - 1 ).getPathologyID().equals( pathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < sdocs.size() ) {
				if ( sdocs.get( right + 1 ).getPathologyID().equals( pathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				pa.addCandidateSurgeonDocument( sdocs.get( i ) );
			}
		}
	}
	
	private static void abbrSearching( String revisedPathologyNum, PADiagnosis pa, ArrayList<SurgeonDocument> abbrSdocs ) {
		SurgeonDocument sd = new SurgeonDocument ();
		sd.setAbbrPathologyID( revisedPathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( abbrSdocs, sd, SurgeonDocumentAbbrComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( abbrSdocs.get( left - 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < abbrSdocs.size() ) {
				if ( abbrSdocs.get( right + 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				pa.addCandidateSurgeonDocument( abbrSdocs.get( i ) );
			}
		}
	}
	
	private static void searching( String pathologyNum, MorphometricAnalysis ma, ArrayList<SurgeonDocument> sdocs ) {
		SurgeonDocument sd = new SurgeonDocument ();
		sd.setPathologyID( pathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( sdocs, sd, SurgeonDocumentComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( sdocs.get( left - 1 ).getPathologyID().equals( pathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < sdocs.size() ) {
				if ( sdocs.get( right + 1 ).getPathologyID().equals( pathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				ma.addCandidateSurgeonDocument( sdocs.get( i ) );
			}
		}
	}
	
	private static void abbrSearching( String revisedPathologyNum, MorphometricAnalysis ma, ArrayList<SurgeonDocument> abbrSdocs ) {
		SurgeonDocument sd = new SurgeonDocument ();
		sd.setAbbrPathologyID( revisedPathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( abbrSdocs, sd, SurgeonDocumentAbbrComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( abbrSdocs.get( left - 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < abbrSdocs.size() ) {
				if ( abbrSdocs.get( right + 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				ma.addCandidateSurgeonDocument( abbrSdocs.get( i ) );
			}
		}
	}
	
	private static void searchingCS( String pathologyNum, MorphometricAnalysis ma, ArrayList<ChildSurgeonDocument> csdocs ) {
		ChildSurgeonDocument csd = new ChildSurgeonDocument ();
		csd.setPathologyID( pathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( csdocs, csd, ChildSurgeonDocumentComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( csdocs.get( left - 1 ).getPathologyID().equals( pathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < csdocs.size() ) {
				if ( csdocs.get( right + 1 ).getPathologyID().equals( pathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				ma.addCandidateChildSurgeonDocument( csdocs.get( i ) );
			}
		}
	}
	
	private static void abbrSearchingCS( String revisedPathologyNum, MorphometricAnalysis ma, ArrayList<ChildSurgeonDocument> abbrCSdocs ) {
		ChildSurgeonDocument csd = new ChildSurgeonDocument ();
		csd.setAbbrPathologyID( revisedPathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( abbrCSdocs, csd, ChildSurgeonDocumentAbbrComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( abbrCSdocs.get( left - 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < abbrCSdocs.size() ) {
				if ( abbrCSdocs.get( right + 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				ma.addCandidateChildSurgeonDocument( abbrCSdocs.get( i ) );
			}
		}
	}
	
	private static void searchingCS( String pathologyNum, PADiagnosis pa, ArrayList<ChildSurgeonDocument> csdocs ) {
		ChildSurgeonDocument csd = new ChildSurgeonDocument ();
		
		csd.setPathologyID( pathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( csdocs, csd, ChildSurgeonDocumentComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( csdocs.get( left - 1 ).getPathologyID().equals( pathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < csdocs.size() ) {
				if ( csdocs.get( right + 1 ).getPathologyID().equals( pathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				pa.addCandidateChildSurgeonDocument( csdocs.get( i ) );
			}
		}
	}
	
	private static void abbrSearchingCS( String revisedPathologyNum, PADiagnosis pa, ArrayList<ChildSurgeonDocument> abbrCSdocs ) {
		ChildSurgeonDocument csd = new ChildSurgeonDocument ();
		csd.setAbbrPathologyID( revisedPathologyNum );
		int left, right;
		int idx = left = right = Collections.binarySearch( abbrCSdocs, csd, ChildSurgeonDocumentAbbrComparable );
		
		if ( idx > 0 ) {
			while ( left - 1 >= 0 ) {
				if ( abbrCSdocs.get( left - 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					left--;	
				else
					break;
			}
			while ( right + 1 < abbrCSdocs.size() ) {
				if ( abbrCSdocs.get( right + 1 ).getAbbrPathologyID().equals( revisedPathologyNum ) )
					right++;
				else
					break;
			}
			
			for ( int i = left; i <= right; i++ ) {
				pa.addCandidateChildSurgeonDocument( abbrCSdocs.get( i ) );
			}
		}
	}
	
	
	
	public static void matcherImmunityDocumentToSurgeonDocument( ArrayList<ImmunityDocument> idocs, 
			ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs ) {
		System.out.println ( "Finding the SurgeonDocument & ChildSurgeonDocument.");
		long start = System.nanoTime();
							
		Collections.sort( sdocs, SurgeonDocumentComparable );
		Collections.sort( csdocs, ChildSurgeonDocumentComparable );
		
		ArrayList<SurgeonDocument> abbrSdocs = new ArrayList<SurgeonDocument>(sdocs.size());
		try {			
			for( SurgeonDocument sd : sdocs) {
				abbrSdocs.add( (SurgeonDocument) sd.clone());
			}
		}
		catch ( CloneNotSupportedException e ) {
			e.printStackTrace();
		}
		Collections.sort( abbrSdocs, SurgeonDocumentAbbrComparable );
		
		ArrayList<ChildSurgeonDocument> abbrCSdocs = new ArrayList<ChildSurgeonDocument>(csdocs.size());
		try {			
			for( ChildSurgeonDocument csd : csdocs) {
				abbrCSdocs.add( (ChildSurgeonDocument) csd.clone());
			}
		}
		catch ( CloneNotSupportedException e ) {
			e.printStackTrace();
		}
		Collections.sort( abbrCSdocs, ChildSurgeonDocumentAbbrComparable );
		
		int count = 1;
		for ( ImmunityDocument idoc : idocs ) {
			if ( count++ % 1000 == 0 )
				System.out.print(".");
			
			for ( PADiagnosis pa : idoc.getDiagnosises() ) {	
				String pathologyNum = pa.getPathologyNum();				
				String revisedPathologyNum = pa.getRevisedPathologyNum();
				
				if ( pathologyNum == null || pathologyNum.length() == 0 ) {
					continue;
				}
				
				String type = StringUtils.getType( pathologyNum );
				if ( type.equals("S") ) {
					searching ( pathologyNum, pa, sdocs );
					abbrSearching ( revisedPathologyNum, pa, abbrSdocs );
				} else if ( type.equals("CS") ){ 					// We should add some codes depending on the type
					searchingCS ( pathologyNum, pa, csdocs );
					abbrSearchingCS ( revisedPathologyNum, pa, abbrCSdocs );
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				String pathologyNum = ma.getPathologyNum();
				String revisedPathologyNum = ma.getRevisedPathologyNum();
								
				if ( pathologyNum == null || pathologyNum.length() == 0 ) {
					continue;
				}		    
				
				String type = StringUtils.getType( pathologyNum );			
				if ( type.equals("S") ) {
					searching ( pathologyNum, ma, sdocs );
					abbrSearching ( revisedPathologyNum, ma, abbrSdocs );
				} else if ( type.equals("CS") ){ 					// We should add some codes depending on the type
					searchingCS ( pathologyNum, ma, csdocs );
					abbrSearchingCS ( revisedPathologyNum, ma, abbrCSdocs );
				}
			}
		}		
		
		// Step - 3
		// Check the patient id whether they are same or not
		for ( ImmunityDocument idoc : idocs ) {
			if ( count++ % 1000 == 0 )
				System.out.print(".");
			
			for ( PADiagnosis pa : idoc.getDiagnosises() ) {	
				ArrayList<SurgeonDocument> candidateSdocs = pa.getCandidateSurgeonDocuments();
				for ( SurgeonDocument sd : candidateSdocs ) {
					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					pa.addSurgeonDocuments( sd );
				}
				
				ArrayList<ChildSurgeonDocument> candidateCSdocs = pa.getCandidateChildSurgeonDocuments();
				for ( ChildSurgeonDocument csd : candidateCSdocs ) {
					if ( !csd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					pa.addChildSurgeonDocuments( csd );
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				ArrayList<SurgeonDocument> candidateSdocs = ma.getCandidateSurgeonDocuments();
				for ( SurgeonDocument sd : candidateSdocs ) {
					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					ma.addSurgeonDocuments( sd );
				}
				
				ArrayList<ChildSurgeonDocument> candidateCSdocs = ma.getCandidateChildSurgeonDocuments();
				for ( ChildSurgeonDocument csd : candidateCSdocs ) {
					if ( !csd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					ma.addChildSurgeonDocuments( csd );
				}
			}
		}
	
		long end = System.nanoTime();
		System.out.println();
		System.out.println ( "Finding complete.");
		System.out.println ( "Finding time : " + (end - start) / Math.pow(10,9) + " second");
	}
	
	
	public static void matcherImmunityDocumentToSurgeonDocument( ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs ) {
		System.out.println ( "Finding the SurgeonDocument.");
		long start = System.nanoTime();
							
		Collections.sort( sdocs, SurgeonDocumentComparable );
		
		ArrayList<SurgeonDocument> abbrSdocs = new ArrayList<SurgeonDocument>(sdocs.size());
		try {			
			for( SurgeonDocument sd : sdocs) {
				abbrSdocs.add( (SurgeonDocument) sd.clone());
			}
		}
		catch ( CloneNotSupportedException e ) {
			e.printStackTrace();
		}
		Collections.sort( abbrSdocs, SurgeonDocumentAbbrComparable );
		
		int count = 1;
		for ( ImmunityDocument idoc : idocs ) {
			if ( count++ % 1000 == 0 )
				System.out.print(".");
			
			for ( PADiagnosis pa : idoc.getDiagnosises() ) {	
				String pathologyNum = pa.getPathologyNum();				
				String revisedPathologyNum = pa.getRevisedPathologyNum();
				
				if ( pathologyNum == null || pathologyNum.length() == 0 ) {
					continue;
				}
				
				String type = StringUtils.getType( pathologyNum );
				if ( type.equals("S") ) {
					searching ( pathologyNum, pa, sdocs );
					abbrSearching ( revisedPathologyNum, pa, abbrSdocs );
				} else {
					// We should add some codes depending on the type
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				String pathologyNum = ma.getPathologyNum();
				String revisedPathologyNum = ma.getRevisedPathologyNum();
								
				if ( pathologyNum == null || pathologyNum.length() == 0 ) {
					continue;
				}		    
				
				String type = StringUtils.getType( pathologyNum );			
				if ( type.equals("S") ) {
					searching ( pathologyNum, ma, sdocs );
					abbrSearching ( revisedPathologyNum, ma, abbrSdocs );
				} else {
					// We should add some codes depending on the type
				}
			}
		}		
		
		// Step - 3
		// Check the patient id whether they are same or not
		for ( ImmunityDocument idoc : idocs ) {
			if ( count++ % 1000 == 0 )
				System.out.print(".");
			
			for ( PADiagnosis pa : idoc.getDiagnosises() ) {	
				ArrayList<SurgeonDocument> csdocs = pa.getCandidateSurgeonDocuments();
				for ( SurgeonDocument sd : csdocs ) {
					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					pa.addSurgeonDocuments( sd );
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				ArrayList<SurgeonDocument> csdocs = ma.getCandidateSurgeonDocuments();
				for ( SurgeonDocument sd : csdocs ) {
					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					ma.addSurgeonDocuments( sd );
				}
			}
		}
	
		long end = System.nanoTime();
		System.out.println();
		System.out.println ( "Finding complete.");
		System.out.println ( "Finding time : " + (end - start) / Math.pow(10,9) + " second");
	}
	
	static final Comparator<PADiagnosis> PADiagnosisComparable = new Comparator<PADiagnosis>() {
		public int compare(PADiagnosis pa1, PADiagnosis pa2) {
			return pa1.getPathologyNum().compareTo(pa2.getPathologyNum());
		}
	};
	
	static final Comparator<MorphometricAnalysis> MorphometricAnalysisComparable = new Comparator<MorphometricAnalysis>() {
		public int compare(MorphometricAnalysis ma1, MorphometricAnalysis ma2) {
			return ma1.getPathologyNum().compareTo(ma2.getPathologyNum());
		}
	};
	
	static final Comparator<PADiagnosis> PADiagnosisRevisedComparable = new Comparator<PADiagnosis>() {
		public int compare(PADiagnosis pa1, PADiagnosis pa2) {
			return pa1.getRevisedPathologyNum().compareTo(pa2.getRevisedPathologyNum());
		}
	};
	
	static final Comparator<MorphometricAnalysis> MorphometricAnalysisRevisedComparable = new Comparator<MorphometricAnalysis>() {
		public int compare(MorphometricAnalysis ma1, MorphometricAnalysis ma2) {
			return ma1.getRevisedPathologyNum().compareTo(ma2.getRevisedPathologyNum());
		}
	};
	
	
	public static void matcherSurgeonDocumentToImmunityDocument( ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs ) {
		System.out.println ( "Matching ImmunityDocuments to SurgeonDocuments.");
		long start = System.nanoTime();
				
		int count = 1;
		// preprocessing		
		ArrayList<PADiagnosis> pas = new ArrayList<PADiagnosis>();
		ArrayList<MorphometricAnalysis> mas = new ArrayList<MorphometricAnalysis>();
		for ( ImmunityDocument idoc : idocs ) {
			for ( PADiagnosis pa : idoc.getDiagnosises() ) {	
				String pathologyNum = pa.getPathologyNum();		
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;
				pas.add( pa );
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				String pathologyNum = ma.getPathologyNum();		
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;
				mas.add( ma );
			}
		}
		
		Collections.sort( pas, PADiagnosisComparable );
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count++ % 10000 == 0 )
				System.out.print( ".");
			String patologyID = sdoc.getPathologyID();
			PADiagnosis pa = new PADiagnosis();
			pa.setPathologyNum( patologyID ); 
			int idx = Collections.binarySearch( pas, pa, PADiagnosisComparable );
			if ( idx > 0 ) {
				sdoc.setMappedPADiagnosis( pas.get(idx) );
			}
		}
		
		Collections.sort( pas, PADiagnosisRevisedComparable );
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count++ % 10000 == 0 )
				System.out.print( ".");
			String abbrPatologyID = sdoc.getAbbrPathologyID();
			PADiagnosis pa = new PADiagnosis();
			pa.setRevisedPathologyNum( abbrPatologyID ); 
			int idx = Collections.binarySearch( pas, pa, PADiagnosisRevisedComparable );
			if ( idx > 0 ) {
				sdoc.setMappedPADiagnosis( pas.get(idx) );
			}
		}
		
		Collections.sort( mas, MorphometricAnalysisComparable );
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count++ % 10000 == 0 )
				System.out.print( ".");
			String patologyID = sdoc.getPathologyID();
			MorphometricAnalysis ma = new MorphometricAnalysis();
			ma.setPathologyNum( patologyID ); 
			int idx = Collections.binarySearch( mas, ma, MorphometricAnalysisComparable );
			if ( idx > 0 ) {
				sdoc.setMappedMorphometricAnalysis( mas.get(idx) );
			}
		}
		
		Collections.sort( mas, MorphometricAnalysisRevisedComparable );
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count++ % 10000 == 0 )
				System.out.print( ".");
			String abbrPatologyID = sdoc.getAbbrPathologyID();
			MorphometricAnalysis ma = new MorphometricAnalysis();
			ma.setRevisedPathologyNum( abbrPatologyID ); 
			int idx = Collections.binarySearch( mas, ma, MorphometricAnalysisRevisedComparable );
			if ( idx > 0 ) {
				sdoc.setMappedMorphometricAnalysis( mas.get(idx) );
			}
		}
	
		long end = System.nanoTime();
		System.out.println ();
		System.out.println ( "Matching complete.");
		System.out.println ( "Matching time : " + (end - start) / Math.pow(10,9) + " second");
	}
}
