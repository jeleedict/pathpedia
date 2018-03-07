/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

import java.util.ArrayList;

/**
 * 장기명을 처리하는 클래스 <br/>
 * 
 * @author Hyun-Je
 *
 */
public class OrganProcessor {
	private OrganProcessor() {		
	}
	
	/**
	 * 
	 * 
	 * @param organName
	 * @return 
	 */
	private static String removeParens( String organName ) {
		int lastpn = 0;
		for ( int i = 0; i < organName.length(); i++ ) {
			char ch = organName.charAt(i);
			if ( ch == ')' ) {
				lastpn = i;
			}
			else if ( ch == '(' )
				break;
		}
		if ( lastpn != 0 ) {
			organName = organName.substring( lastpn + 1 ).trim();
		}
		
		return organName;
	}
	
	private static String removeSquareBracket( String organName ) {
		int lastpn = 0;
		for ( int i = 0; i < organName.length(); i++ ) {
			char ch = organName.charAt(i);
			if ( ch == ']' ) {
				lastpn = i;
			}
		}
		if ( lastpn != 0 ) {
			organName = organName.substring( lastpn + 1 ).trim();
		}
		
		return organName;
	}
	
	/**
	 * 콜론(Colon,:)을 제거하는 함수
	 * 
	 * @param organName
	 * @return
	 */
	private static String removeColon( String organName ) {
		int lastpn = 0;
		for ( int i = 0; i < organName.length(); i++ ) {
			char ch = organName.charAt(i);
			if ( ch == ':' ) {
				lastpn = i;
			}
		}
		if ( lastpn != 0 ) {
			organName = organName.substring( lastpn + 1 ).trim();
		}		
		return organName;
	}
	
	private static String removeStartingSpecialCharacters( String organName ) {
		String modifiedOrganName = organName;
		if ( organName.equals("#1.") || organName.startsWith("1.") 
				|| organName.startsWith("2.") || organName.startsWith("A.") 
				|| organName.startsWith("#A") || organName.startsWith("#1") 
				|| organName.startsWith("A)") || organName.startsWith("B)") 
				|| organName.startsWith("A-D)") || organName.startsWith("C-G)")  
				|| organName.startsWith("A and C)") || organName.startsWith("A)E)") ) {
			
			int idx = organName.indexOf(" ");
			if ( idx != -1 )
				modifiedOrganName = organName.substring( organName.indexOf(" ") + 1 ).trim();
			else
				modifiedOrganName = "";
		}
		return modifiedOrganName;
	}
	
	private static String removeStartingFro( String organName ) {
		String modifiedOrganName = organName;
		if ( organName.startsWith("Fro") || organName.startsWith("(Fro") ) {
			int idx = organName.indexOf(" ");
			if ( idx != -1 )
				modifiedOrganName = organName.substring( organName.indexOf(" ") + 1 ).trim();
			else
				modifiedOrganName = "";
		}
		return modifiedOrganName;
	}
	
	private static boolean removedMatchedPredefinedString( String organName ) {
		if ( organName.equals("MICRO (1 HE)") || organName.equals("#1-5") 
				|| organName.equals("(A and Fro#1") || organName.equals("A-D & Fro#1") )	
			return true;
		return false;
	}
	
	private static boolean removeOneCharacter( String organName ) {
		if ( organName.length() == 1 )
			return true;
		return false;
	}
	
	private static String removeQuotation( String organName ) {
		organName = organName.trim();
		if ( organName.length() == 0 )
			return "";
		
		String modifiedOrganName = organName;
		if ( organName.charAt(0) == '"' && organName.charAt( organName.length() - 1 ) == '"' ) 
			modifiedOrganName = organName.substring( 1, organName.length() - 1 );			
		else if ( organName.charAt(0) == '"' )
			modifiedOrganName = organName.substring( 1, organName.length() );
		
		return modifiedOrganName;
	}
	
	private static String removeLastColon( String organName ) {
		String modifiedOrganName = organName;
		if ( modifiedOrganName.length() == 0 )
			return organName;
		if ( organName.charAt( organName.length() - 1 ) == ':' )
			modifiedOrganName.substring( 0, organName.length() - 1 );
		return modifiedOrganName;
	}
	
	private static String convertToCapitalLetter( String organName ) {
		String modifiedOrganName = organName;
		if ( modifiedOrganName.length() == 0 )
			return organName;
		
		modifiedOrganName = modifiedOrganName.substring(0,1).toUpperCase() + modifiedOrganName.substring( 1 );
		return modifiedOrganName;
	}
	
	private static String removeLabeled ( String organName ) {
		// Labeled, Labelled
		if ( organName.length() == 0 )
			return "";
		
		String modifiedOrganName = "";
		if ( organName.startsWith("Labeled") || organName.startsWith("Labelled")) {
			String tokens[] = organName.split("\\s");
			if ( tokens.length > 1 ) {
				for ( int i = 1; i < tokens.length - 1; i++ ) {
					modifiedOrganName += tokens[i] + " ";
				}
				modifiedOrganName += tokens[tokens.length - 1];
			} 
			else
				return "";
		}	
		else
			modifiedOrganName = organName;
					
		return modifiedOrganName;
	}
	
	private static String removeSpecimen ( String organName ) {
		// Specimen labeled, Specimen labelled
		if ( organName.length() == 0 )
			return "";
		
		String modifiedOrganName = "";
		if ( organName.startsWith("Specimen") ) {
			String tokens[] = organName.split("\\s");
			if ( tokens.length > 2 ) {
				for ( int i = 2; i < tokens.length - 1; i++ ) {
					modifiedOrganName += tokens[i] + " ";
				}
				modifiedOrganName += tokens[tokens.length - 1];
			}
			else
				return "";
		}		
		else
			modifiedOrganName = organName;
			
		return modifiedOrganName;
	}
	
	/**
	 * Tissue from/form/around/labeled 등으로 시작한는 장기명을 처리하는 함수 <br/>
	 * 
	 * 
	 * @param organName
	 * @return
	 */
	private static String removeTissuefrom ( String organName ) {
		// Tissue form, Tissue fro, Tissue from, Tissue around, Tissue abeled, Tissre labeled 
		// Tissue labeled, Tisseu labeled, Tissue labelled, Tissue unlabeled
		// Tissue labled
		if ( organName.length() == 0 )
			return "";
		
		String modifiedOrganName = "";		
		if ( organName.startsWith("Tissue") || organName.startsWith("Tisseu") || organName.startsWith("Tissre") ) {
			String tokens[] = organName.split("\\s");
			if ( tokens.length > 2 ) {
				for ( int i = 2; i < tokens.length - 1; i++ ) {
					modifiedOrganName += tokens[i] + " ";
				}
				modifiedOrganName += tokens[tokens.length - 1];
			}
			else
				return "";	
		}
		else
			modifiedOrganName = organName;
					
		return modifiedOrganName;
	}
	
	private static String removeEndingSpecialCharacters( String organName ) {
		if ( organName.length() == 0 )
			return "";
		
		String modifiedOrganName = organName;
		if ( organName.endsWith("(see note)") ) {
			modifiedOrganName = organName.substring( organName.indexOf("(see note)") );
		}
		else if ( organName.endsWith("(Fro#1)") ) {
			modifiedOrganName = organName.substring( organName.indexOf("(Fro#1)") );
		}
		else if ( organName.endsWith("(#1") ) {
			modifiedOrganName = organName.substring( organName.indexOf("(#1") );
		}
		else if ( organName.endsWith("(#1)") ) {
			modifiedOrganName = organName.substring( organName.indexOf("(#1)") );
		}
		
		return modifiedOrganName;
	}
	
	public static String postProcessing( String organName ) {
		organName = organName.trim();
		
		organName = removeStartingFro ( organName );	
		organName = removeStartingSpecialCharacters ( organName );
		organName = removeEndingSpecialCharacters ( organName );
		
		organName = removeLabeled ( organName );
		organName = removeSpecimen ( organName );
		organName = removeTissuefrom ( organName );
		
		organName = removeQuotation ( organName );			// remove quotation
		organName = removeLastColon ( organName );			// remove colon
		organName = removeParens ( organName ) ;			// remove parenthesis
		organName = removeSquareBracket ( organName ) ;
		organName = removeColon ( organName );
		
		organName = convertToCapitalLetter ( organName );		
		
		if ( removeOneCharacter(organName) || removedMatchedPredefinedString ( organName ) )
			organName = "";
		
		return organName;
	}
	
	/**
	 * 주어진 장기명이 and로 구분되어 있는 경우, and를 기준으로 여러 장기명으로 나누는 함수 <br/> 
	 *  
	 * <pre>
	 * String organs[] = organName.split("\\sand\\s");
	 * </pre>
	 * @param organName
	 * @return
	 */
	public static ArrayList<String> splitConjunction( String organName ) {
		ArrayList<String> organList = new ArrayList<String>();
		String organs[] = organName.split("\\sand\\s");
		for ( String token : organs ) {
			if ( token.trim().length() != 0 ) {		
				token = token.substring(0,1).toUpperCase() + token.substring(1);
				token = removeQuotation ( token.trim() );
				organList.add( token );
			}
		}		
		return organList;
	}
}
