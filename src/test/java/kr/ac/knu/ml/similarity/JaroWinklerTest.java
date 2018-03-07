/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.similarity;

public class JaroWinklerTest {
	public static void main(String[] args) {
		String str1 = "Endometriotic cyst";
		String str2 = "Endometrioid polyp";
		String str3 = "Endometrioid cystadenoma, benign";

		System.out.println( "LevenshteinDistance" );
		System.out.println( 1 - LevenshteinDistance.computeNormalizedLevenshteinDistance( str1, str2 ) );
		System.out.println( 1 - LevenshteinDistance.computeNormalizedLevenshteinDistance( str1, str3 ) );
		System.out.println( 1 - LevenshteinDistance.computeNormalizedLevenshteinDistance( str3, str3 ) );
		
		
		System.out.println( "JaroWinkler" );
		System.out.println( JaroWinkler.similarity( str1, str2 ) );
		System.out.println( JaroWinkler.similarity( str1, str3 ) );
		System.out.println( JaroWinkler.similarity( str3, str3 ) );
		
		
		System.out.println( "StringKernel" );
		System.out.println( StringKernel.similarity( str1, str2 ) );
		System.out.println( StringKernel.similarity( str1, str3 ) );
		System.out.println( StringKernel.similarity( str3, str3 ) );
		
		System.out.println( "SoftTFIDF" );
		System.out.println( SoftTFIDFDistance.similarity( str1, str2 ) );
		System.out.println( SoftTFIDFDistance.similarity( str1, str3 ) );
		System.out.println( SoftTFIDFDistance.similarity( str3, str3 ) );				
	}
}
