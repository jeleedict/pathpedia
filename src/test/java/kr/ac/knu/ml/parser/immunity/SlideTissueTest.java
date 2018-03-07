/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.immunity;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class SlideTissueTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public static void main(String[] args) {		
////	String line = "     Item      | Main tumor component  l Retiform   l Heterologous";
////	String line = "               l                       l  element   l  component";
////	String line = "                       | (Infiltrating ductal carcinoma portion)";
////	String line = "Item     |        (1)        l (2,Fro#1,#2,#3,#5) l  (Fro#4)     ";
////	String line = "Cytokeratin | Positive in       l     Positive       l  Negative";
////	String line = "            l  mesothelial cell l                    l";
////	String line = "   Dystrophin I    |  Focal loss in degenerating myofibers";
////	String line = "   Dystrophin II   |  Focal loss in degenerating myofibers";
////	String line = "   Dystrophin III  |  Focal loss or weak expression";
////	String line = "                        |  in #11 and #12";
//    String line = "   acid protein            ";
////	String line = "                      |  epithelial part   | ";
////	String line = "               l  mesothelial cell l                    l";
//	
//	
////	   Item     |        (1)        l (2,Fro#1,#2,#3,#5) l  (Fro#4) 
////	   ==================================================================
////	   Cytokeratin | Positive in       l     Positive       l  Negative  
////	               l  mesothelial cell l                    l
////	   ------------------------------------------------------------------
//	
////	String line = "            l  mesothelial cell l                    l";
//	
//	// `Dystrophin I' in PA070000852, ... , 향후 BioMarker중 xxx I로 끝나는 것이 있으면 추가해야함!!		
////	String tokens[] = line.split("\\s*\\|\\s*|\\s+l\\s+|(?<!Dystrophin)\\s+I\\s+");
////	String tokens[] = line.split("\\s*\\|\\s*|(?<!\\w)\\s*l(?!\\w)\\s*|(?<!Dystrophin)\\s+I\\s+", -1);
////	String tokens[] = line.split("\\||(?<!\\w)\\s*l(?!\\w)\\s*|(?<!Dystrophin)\\s+I\\s+", -1);
//	String tokens[] = line.split("\\||(?<!\\w)l(?!\\w)|(?<!Dystrophin |\\w)I(?!\\w)", -1);
//	for ( String token : tokens ) {
//		System.out.println( token + "\t" + token.trim().length() + "\t" + token.length() );
//	}
			
	String values[] = { "(1)(2)(3)", "(5,6)", "(3),(4),(5)", "(11,12)", "(2,Fro#1,#2,#3,#5)", "(Fro#1)", "(A1-5)", "(A11-15)", "(11, 12) adenocarcinoma", "(11,12) stromal tumor", 
			"(4)", "(11)", "(16-20)", "(A)", "PAX5 (repeat)", "CD30 (Ki-1)", "(some positive neurons in CA4)", "(No lymphoepithelial lesion)", "(cytoplasmic membranus pattern)" }; 
	
	Pattern pattern = Pattern.compile( "\\(\\w*[ ,-]*\\w+[\\#\\d+]*\\)");
	for ( String value : values ) {
		System.out.print( value + "\t" );
		Matcher matcher = pattern.matcher( value );
		while ( matcher.find() ) {
			System.out.print( matcher.group() + "\t");
		}
		System.out.println();
	}
	}
}
