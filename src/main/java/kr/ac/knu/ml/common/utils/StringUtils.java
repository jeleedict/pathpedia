/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.common.utils;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.unit.immunity.BioMarkerValue;

public class StringUtils {
	private StringUtils() {
	}
	public static int juiceNumFromString( String str ) {
		int num = 0;
		String tmp = "";
		for ( int i = 0; i < str.length(); i++ ) {
			char ch = str.charAt( i );
			if ( Character.isDigit( ch ) ) {
				tmp += ch;				
			}			
			else {
				if ( !tmp.equals("") ) {
					num += Integer.parseInt( tmp );
					tmp = "";
				}
			}				
		}
		return num;
	}
	
	public static String digitNormalize( String str ) {
		/*
		 * Normalize!!
		 * 
		 * 1) 
		 * 2)
		 * 3)
		 * 
		 * to
		 * 
		 * #)
		 * #)
		 * 
		 */	
		
		String tmp = "";
		for ( int i = 0; i < str.length(); i++ ) {
			char ch = str.charAt( i );
			if ( Character.isDigit( ch ) ) 
				tmp += "#";
			else
				tmp += ch;
		}
		return tmp;
	}
	
	public static String alphabetNormalize( String str ) {			
		String tmp = "";
		for ( int i = 0; i < str.length(); i++ ) {
			char ch = str.charAt( i );
			if ( Character.isLetter( ch ) ) 
				tmp += "*";
			else
				tmp += ch;
		}
		return tmp;
	}
	
	public static boolean isStartSymbols( String str ) {
		str = str.trim();
		if ( str.length() == 0 )
			return false;
		
		char ch = str.charAt( 0 );
		if ( Character.isAlphabetic( ch ) || ch == '(' ) {
			return false;
		}
		
		return true;
	}
	
	public static String cleansorStartSymbols( String str ) {
		int i = 0;
		for ( ; i < str.length(); i++ ) {
			char ch = str.charAt( i );
			if ( Character.isAlphabetic( ch ) || ch == '(' ) {
				break;
			}
		}		
		return str.substring( i );
	}
	public static int numOfIndentation( String str ) {
		for ( int i = 0; i < str.length(); i++ ) {
			if ( !Character.isWhitespace( str.charAt( i ) ) )
				return i;
		}
		return str.length();
	}
	public static String getStrings( String[] lines ) {
		StringBuilder sb = new StringBuilder();
		for ( String line : lines ) {
			sb.append( line + "\n" );
		}
		return sb.toString();
	}
	public static BioMarkerValue getBioMarkerValue( String value ) {
		BioMarkerValue markerValue = new BioMarkerValue();
	
		Pattern polarityPattern = Pattern.compile( "POSITIVE|NEGATIVE");
		Matcher polarityMatcher = polarityPattern.matcher( value );
		boolean matched = false;
		while ( polarityMatcher.find() ) {
			int startIdx = polarityMatcher.start();
			int endIdx = polarityMatcher.end();
			if ( startIdx != 0 )
				markerValue.setDegree( value.substring(0, startIdx - 1 ));
			markerValue.setPolarity( value.substring(startIdx, endIdx));
			if ( endIdx != value.length() )
				markerValue.setLocation( value.substring(endIdx + 1));
			
			matched = true;
		}
		if ( !matched )
			markerValue.setPolarity( value );
		return markerValue;
	}
	
	public static ArrayList<String> tokenizer( String line ) {
		return tokenizer( line, "\n");
	}
	
	public static int getYear( String str ) {
		String num = "";
		for ( int i = 0; i < str.length(); i++ ) {
			if ( Character.isDigit( str.charAt(i) ) ) {
				num += str.charAt(i);
			}
		}
		if ( num.equals("") )
			return -1;
		
		return Integer.parseInt( num );
	}
	
	public static String getType( String str ) {
		String type = "";
		for ( int i = 0; i < str.length(); i++ ) {
			if ( Character.isDigit( str.charAt(i) ) )
				break;
			else
				type += str.charAt(i);
		}
		return type;
	}
	
	public static int getNumPosition( String str ) {
		for ( int i = 0; i < str.length(); i++ ) {
			if ( !Character.isDigit( str.charAt(i) ) ) {
				return i;
			}
		}
		
		return str.length();
	}
	
	public static boolean isStartWithNum( String str ) {
		if ( str.length() > 0 )
			return Character.isDigit( str.charAt(0) );
		
		return false;
	}
	
	public static String removeSpace( String str ) {
		return str.replace(" ", "");
	}
	
	public static String removeSpecialCharacterFromLast( String str ) {
		for ( int i = str.length() - 1; i >= 0; i-- ) {
			char ch = str.charAt( i );
			if ( Character.isDigit( ch ) )
				return str.substring( 0, i + 1 );
		}
		return "";
	}
	
	public static String removeSpecialCharacter( String str ) {
		StringBuilder sb = new StringBuilder();
		
		String tokens[] = str.split("\\s");
		if ( tokens.length == 1 ) {
			for ( int i = 0; i < str.length(); i++ ) {
				char ch = str.charAt( i );
				if ( Character.isAlphabetic( ch ) || Character.isWhitespace( ch ) )
					sb.append( ch );
			}			
		}
		else {
			for ( int i = 0; i < str.length(); i++ ) {
				char ch = str.charAt( i );
				
				if ( Character.isAlphabetic( ch ) || Character.isWhitespace( ch ) 
						|| ch == ',' || ch == '"' || ch == '-' )
					sb.append( ch );
			}
		}
		return sb.toString().trim();
	}
	
	public static String getCapitalCharacters( String str ) {
		StringBuilder sb = new StringBuilder();
		
		for ( int i = 0; i < str.length(); i++ ) {
			char ch = str.charAt( i );
			if ( Character.isUpperCase( ch ) || Character.isWhitespace( ch ) )
				sb.append( ch );
			else
				break;
		}			
		return sb.toString().trim();
	}
	
	public static ArrayList<String> tokenizer( String line, String delimeter ) {
		ArrayList<String> lists = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer ( line, delimeter );
		while ( st.hasMoreTokens() ) {
			lists.add( st.nextToken() );
		}
		return lists;
	}
	
	public static boolean checkParenthesis( String line ) {
		int sum = 0;
		for ( int i = 0; i < line.length(); i++ ) {
			char ch = line.charAt(i);
			if ( ch == '(' ) 
				sum++;			
			else if ( ch == ')' )
				sum--;
		}
		
		if ( sum != 0 )
			return false;
			
		return true;
	}
	

	public static boolean isDigit( String line ) {
		if ( line.length() == 0 )
			return false;
		
		for ( int i = 0; i < line.length(); i++ ) {
			char ch = line.charAt( i );
			if ( Character.isDigit( ch ) == false )
				return false;
		}
		return true;
	}
}
