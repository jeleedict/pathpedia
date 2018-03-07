/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.immunity;

import java.util.ArrayList;

import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.document.SurgeonDocument;

// Parse the getDiagnosisName, extract the slideInfo 
public class SlideParser {
	private final String exceptions[] = { "S0", "unstain", "outside", "외부블럭" };
	
	public ArrayList<SlideTissue> parseSlides ( String diagnosisName, ArrayList<SurgeonDocument> sdocs ) {
		ArrayList<SlideTissue> slideTissues = new ArrayList<SlideTissue>();		
		ArrayList<SlideTissueCandidate> splited = slidesSplitor ( diagnosisName );
		
		slideTissues.addAll( parseSlides ( splited ) );		
		cleansing ( slideTissues );
						
		for ( SlideTissue slide : slideTissues ) {
			for ( SurgeonDocument sdoc : sdocs ) {				
				String slideTissueKeyID = SlideTissueKeyGenerator.keyGenerator( sdoc.getPathologyID(), slide.getSlideTissueName(), slide.getSlideTissueInformation() );
				slide.setSlideTissueKeyID( slideTissueKeyID );
			}
		}
		
		return slideTissues;
	}
		
	private ArrayList<SlideTissueCandidate> slidesSplitor ( String diagnosisName ) {
		ArrayList<SlideTissueCandidate> matched = new ArrayList<SlideTissueCandidate>();
		int startIdx = diagnosisName.indexOf("(");		
		int endIdx = diagnosisName.indexOf( ")", startIdx + 1 );

		if ( startIdx != -1 && endIdx != -1 ) {
			while ( startIdx < diagnosisName.length() && endIdx != -1 && startIdx != -1 ) {	
				String tmp = diagnosisName.substring(startIdx + 1, endIdx).trim();
				
				// Processing double '('
				// For example) (asdfasdf(asdf)).
				if ( tmp.contains("(") ) {
					int count = 0;
					for ( int i = startIdx; i < diagnosisName.length(); i++ ) {
						char ch = diagnosisName.charAt( i );
						if ( ch == '(' )
							count++;
						else if ( ch == ')') 
							count--;
						
						if ( count == 0 ) {
							tmp = diagnosisName.substring(startIdx + 1, i);
							endIdx = i;
							break;
						}
					}
				}
				
				if ( tmp.contains("&") ) 
					tmp = tmp.replace("&", ",");
				
				String andsplitor[] = tmp.split(" and ");
				for ( String tissue : andsplitor ) {
					SlideTissueCandidate tissueCandidates = new SlideTissueCandidate( tissue, false );
					matched.add( tissueCandidates );	
				}
				
				startIdx = diagnosisName.indexOf("(", endIdx + 1 );				
				if ( startIdx == -1 ) 
					endIdx = endIdx + 1; 
				else {					
					if ( startIdx != endIdx + 1 ) {
						String additionalInfo = diagnosisName.substring( endIdx + 1, startIdx ).replace("," ,"").trim();
						if ( additionalInfo.startsWith(":") ) {
							int idx = additionalInfo.indexOf("and");
							if ( idx == -1 ) 
								idx = additionalInfo.length();
								
							for ( SlideTissueCandidate stc : matched ) 
								stc.setAdditionalInfo( additionalInfo.substring( additionalInfo.indexOf(":") + 1, idx ).trim() );							
						}
						// What the heck!!! For S09-24133 (3)
						else if ( additionalInfo.startsWith("temporal lobe") ) {
							for ( SlideTissueCandidate stc : matched ) 
								stc.setAdditionalInfo( "temporal lobe" );
						}
						// What the heck!!!
						else if ( additionalInfo.length() != 0 && !additionalInfo.startsWith(", " ) 
								&& !additionalInfo.startsWith("Rt") && !additionalInfo.startsWith("Lt") 
								&& !additionalInfo.startsWith("-") && !additionalInfo.startsWith("and") 
								&& !additionalInfo.startsWith("CS") && !additionalInfo.startsWith("S") ) {							
							throw new NullPointerException();
						}
					}
					endIdx = diagnosisName.indexOf( ")", startIdx + 1 );
				}				
			}
			
			if ( endIdx < diagnosisName.length() ) {
				String additionalInfo = diagnosisName.substring( endIdx ).replace(": ", "").replace(", ", "").replace("; ", "").trim();
				for ( SlideTissueCandidate stc : matched ) {
					if ( stc.getAdditionalInfo() != null )
						stc.setAdditionalInfo( additionalInfo );
				}
			}
		}
		return matched;
	}	
	
	private ArrayList<SlideTissue> parseSlides ( ArrayList<SlideTissueCandidate> splited ) {
		ArrayList<SlideTissue> slides = new ArrayList<SlideTissue>();
		for ( SlideTissueCandidate matched : splited ) {
			if ( matched.isIsparsed() == true )
				continue;
			
			String slideTissue = matched.getOriginalSurfaceForm();
			String slideInformation = matched.getAdditionalInfo();
			
			if ( slideTissue.contains(",") ) {
				commaParser ( slideTissue, slideInformation, slides );
				matched.setIsparsed( true );
			}	
			else if ( slideTissue.contains("-") ) {
				hypenParser ( slideTissue, slideInformation, slides );
				matched.setIsparsed( true );
			}
			else {
				SlideTissue st = new SlideTissue (slideTissue, slideInformation);
				slides.add( st );
				matched.setIsparsed( true );
			}
		}
		return slides;
	}
	
	private void commaParser( String slideTissue, String slideInformation, ArrayList<SlideTissue> slides ) {
		String rawslides[] = slideTissue.split(",");		
		for ( String rawslide : rawslides ) {
			SlideTissue st = new SlideTissue( rawslide, slideInformation );
			slides.add( st );			
		}
	}
	
	private void hypenParser( String slideTissue, String slideInformation, ArrayList<SlideTissue> slides ) {		
		for ( String exception : exceptions ) {
			if ( slideTissue.startsWith( exception ) )
				return;
		}		
		
		if ( slideTissue.contains("(") )
			return;
		
		int idx = slideTissue.indexOf("-");
		String start = slideTissue.substring(0, idx );
		String end = slideTissue.substring( idx + 1 );
		
		String startAlphabet = "";		
		int startIdx = 0;
		for ( ; startIdx < start.length(); startIdx++ ) {
			char ch = start.charAt( startIdx );
			if ( Character.isDigit( ch ) ) 
				break;
				
			startAlphabet += ch;
		}

		String endAlphabet = "";		
		int endIdx = 0;
		for ( ; endIdx < end.length(); endIdx++ ) {
			char ch = end.charAt( endIdx );
			if ( Character.isDigit( ch ) ) 
				break;
				
			endAlphabet += ch;
		}
		
		if ( startAlphabet.length() != 0 && endAlphabet.length() != 0 && !startAlphabet.equals(endAlphabet) ) {
			return;
		}
		
		String startValueStr = slideTissue.substring( startIdx, idx ).trim();
		String endValueStr = slideTissue.substring( idx + 1 + endIdx ).trim();
		
		if ( !StringUtils.isDigit( startValueStr ) || !StringUtils.isDigit( endValueStr ) )
			return;
		
		int startValue = Integer.parseInt( slideTissue.substring( startIdx, idx ).trim() );
		int endValue = Integer.parseInt( slideTissue.substring( idx + 1 + endIdx ).trim() );
		
		if ( startValue > endValue) {
			int temp = startValue;
			startValue = endValue;
			endValue = temp;
		}				
		
		for ( int i = startValue; i <= endValue; i++ ) {
			SlideTissue st = new SlideTissue( startAlphabet + i, slideInformation );
			slides.add( st );
		}
	}
	
	private void cleansing ( ArrayList<SlideTissue> slides ) {
		for ( SlideTissue slide : slides ) {
			String slideTissueName = slide.getSlideTissueName().trim();
			if ( slideTissueName.startsWith("\"") && slideTissueName.endsWith("\"" ) ) {
				slideTissueName = slideTissueName.replace("\"", "");
			}
			slide.setSlideTissueName( slideTissueName );
		}
	}
}
