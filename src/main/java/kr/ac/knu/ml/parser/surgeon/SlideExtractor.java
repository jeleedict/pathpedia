/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.common.unit.Pair;

public class SlideExtractor {
	public static Pair<String, String> extractSlideFromBioMarker( String bioMarker ) {
		Pattern pattern = Pattern.compile( "\\(.+\\)");				
		Matcher matcher = pattern.matcher( bioMarker );
		String REPLACE = "";
		StringBuffer matched = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		while ( matcher.find() ) {
			matched.append( matcher.group() );
			matcher.appendReplacement(sb,REPLACE);
		}		
		matcher.appendTail(sb);
//		System.out.println( sb.toString() );
//		System.out.println( matched.toString() );
		Pair<String, String> tuple = new Pair<String, String>(sb.toString(), matched.toString());
		return tuple;
	}
}
