/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

public class DiagnosisProcessor {
	private static final String sizeIndicator[] = {"- Tumor size", "- Size of tumor:", 
		"- Size ", "- size", "size;", "- Size:", "- Size;", 		
		"1) invasive tumor size:", "2) invasive tumor size:", "3) invasive tumor size:",
		"1) Size of tumor:", "2) Size of tumor:", "3) Size of tumor:",
		"1) size:", "2) size:", "3) size:",
		"1) tumor size:", "2) tumor size:", "3) tumor size:",
		"1) tumor size;", "2) tumor size;", "3) tumor size;",
		"1. Tumor size:", "2. Tumor size:", "3. Tumor size:"								
	};		
	
	private DiagnosisProcessor() {		
	}
	
	private static String removeStartingSpecialCharacters( String diagnosisName ) {
		if ( diagnosisName.length() == 0 )
			return "";
		
		String modifiedDiagnosisName = diagnosisName;
		if ( diagnosisName.startsWith("1.")	|| diagnosisName.startsWith("2.") 
				|| diagnosisName.startsWith("1) ") || diagnosisName.startsWith("2) ")
				|| diagnosisName.startsWith("; ") ) {
			
			int idx = diagnosisName.indexOf(" ");
			if ( idx != -1 )
				modifiedDiagnosisName = diagnosisName.substring( diagnosisName.indexOf(" ") + 1 ).trim();
			else
				modifiedDiagnosisName = "";
		}
		return modifiedDiagnosisName;
	}
	
	private static String removeConsisntentWith( String diagnosisName ) {
		if ( diagnosisName.length() == 0 )
			return "";
		
		String modifiedDiagnosisName = "";		
		if ( diagnosisName.startsWith("consistent") || diagnosisName.startsWith("Consistent") || diagnosisName.startsWith("CONSISTENT") ) {
			String tokens[] = diagnosisName.split("\\s");
			if ( tokens.length > 2 ) {
				for ( int i = 2; i < tokens.length - 1; i++ ) {
					modifiedDiagnosisName += tokens[i] + " ";
				}
				modifiedDiagnosisName += tokens[tokens.length - 1];
			}
			else
				return "";	
		}
		else
			modifiedDiagnosisName = diagnosisName;
					
		return modifiedDiagnosisName;
	}
	
	public static String postProcessing( String diagnosisName ) {
		diagnosisName = diagnosisName.trim();		
		diagnosisName = removeStartingSpecialCharacters ( diagnosisName );
		diagnosisName = removeConsisntentWith ( diagnosisName );	
		return diagnosisName;
	}
	
	public static boolean hypenStarted( String diagnosisName ) {
		if ( diagnosisName.length() == 0 )
			return false;
		
		if ( diagnosisName.startsWith("-") ) {
			return true;
		}		
		return false;
	}
	
	public static String getType( String diagnosisName ) {
		if ( diagnosisName.length() == 0 )
			return "";
		
		int idx = diagnosisName.indexOf(":");
		if ( idx != -1 ) {
			return diagnosisName.substring( diagnosisName.indexOf("-") + 1, idx );
		} 
		idx = diagnosisName.indexOf(";");
		if ( idx != -1 ) {
			return diagnosisName.substring( diagnosisName.indexOf("-") + 1, idx );
		} 
		return diagnosisName;
	}
	
	public static boolean isSize( String diagnosisName ) {
		for ( String size : sizeIndicator ) {
			if ( diagnosisName.toLowerCase().startsWith( size.toLowerCase() )) {
				return true;
			}
		}
		return false;
	}
}
