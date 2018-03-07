/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.immunity;

public class SlideTissueKeyGenerator {
	// SP_report_ID(11digits)+TypeOfSpecimen(FR:동결절편/LN:림프노드/OS:외부블럭/NO:그외슬라이드/, 2digits)+SlideKeyNumbering(3digits)+Additional(1digit)
	public static String keyGenerator( String pathologyNum, String slideName, String slideTissueInformation ) {
//		String slideName = slideTissue.getSlideTissueName();
//		String slideTissueInformation = slideTissue.getSlideTissueInformation();
		return pathologyNum + getType ( slideName ) + slideKeyProcessor( slideName ) + additionalInfo ( slideTissueInformation );
	}
	
	public static String keyGenerator( String pathologyNum, String slideTissue ) {
		return pathologyNum + getType ( slideTissue ) + slideKeyProcessor( slideTissue ) + additionalInfo ( slideTissue );
	}
	
	private static String getType( String slideTissue ) {
		if ( slideTissue.contains("Fro") || slideTissue.contains("fro") )
			return "FR";
		else if ( slideTissue.contains("LN") )
			return "LN";
		else if ( slideTissue.contains("외부블럭") || slideTissue.contains("outside block") || slideTissue.contains("Outside block") 
				|| slideTissue.contains("외부 block") || slideTissue.contains("out block") || slideTissue.contains("out side block") )
			return "OS";
		else
			return "NO";
					
	}
	
	private static final String additionalContents[] 
			= {"block ", "block", "Fro ", "Fro", "Lt. ", "Lt.", "Frozen ", "MG", "LN ", "LN", "unstain", "unstain ", "kidney", "BM", "smear slide" };
	
	private static String slideKeyProcessor( String slideTissue ) {
		String revisedTissue = slideTissue;
		for ( String c : additionalContents ) {
			if ( revisedTissue.startsWith( c ) )
				revisedTissue = revisedTissue.replace(c, "").trim();
		}
		
		String keyNum = revisedTissue.substring(0, revisedTissue.length() < 3 ? revisedTissue.length() : 3 ).trim();
		if ( keyNum.equals("단") || keyNum.equals("un") )
			keyNum = "000";
		
		if ( keyNum.length() != 0 && keyNum.charAt( 0 ) == '#' )
			keyNum = keyNum.substring( 1 );
		
		int keyNumLength = keyNum.length();
		if ( keyNumLength != 3 ) { 
			for ( int i = 0; i < 3 - keyNumLength; i++ ) 
				keyNum = "0" + keyNum;
		}
		
		return keyNum;
	}
	
	private static String additionalInfo( String slideTissueInformation ) {
		if ( slideTissueInformation != null ) 
			return "1";
		else
			return "0";
	}
}
