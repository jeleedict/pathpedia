/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.parser.buffer.ParseSBuffer;
import kr.ac.knu.ml.unit.surgeon.MicroValue;

public class SurgeonDocumentParser {
	private final String diagnosisNameIndicators[] = { "DIAGNOSIS:", "DIAGNOSIS :", "PRELIMINARY DIAGNOSIS:" };
	private final String variationalDiagnosisNameIndicators[] = { "DIAGNOSIS", "PRELIMINARY DIAGNOSIS", "FINAL DIAGNOSIS" };
	private final String indicators[] = { "DIAGNOSIS:", "임상진단", "Table", "면 역 화 학", "면역", "분 자 병 리", 
										"분자", "받은", "효소", "Revised", 
										"(Addendum", "((Addendum", "<Addendum", "Addendum", "<<addedendum",  
										"((Revised", "CORRECTED", "<Revised",
										"(NOTE", "Note", "(Noe)", "(NTOE)", "(Noet)",
										"Reference", "----", "Fro",  "Consultation", "Consultant", "병리", 
										"*****", "==================", "GFAP" };
	private final String slideKeyIndicators[] = {"(slide key:", "slide key:"};  
	private final String revisedIndicators[] = {"<Revised", "((Addendum", "(Addendum" };
	
	// What is the meaning?
	// detect A~~~), A,B,~~) ... 
	private final Pattern startCharacterSymbolPattern = Pattern.compile( "^(?!\\()[A-Z]+[^(;:/\"\\.]*\\)");
	
	// detect 
	// 숫자로 시작해서~~ 하는 부분을 체크하는거지.
	private final Pattern startDigitSymbolPattern = Pattern.compile( "^(?!\\()\\d+[-\\d\\s]*[\\)\\.]");
	
//	private final String clinicalDiagnosisNameIndicators[] = {"임상진단"};		
	private int idx;
	private int relativeIdx;
	private String lines[];
	private String pathologyID;
	
	public void ignoreBlankLine( ParseSBuffer si ) {
		String line = lines[ si.getIdx() ].trim();
		while ( line.length() == 0 && si.getIdx() + 1 < lines.length ) {
			line = lines[ si.getAddedIdx() ].trim();
		}
	}
	
	public boolean checkNextLine( ParseSBuffer si ) {
		if ( si.getIdx() + 1 < lines.length ) {			
			return true;
		}
		return false;
	}
	
	public void setSDiagnosis( ParseSBuffer buffer, SurgeonDocument doc ) {		
		doc.setMicro( buffer.getMicros() );
		doc.setMvs( buffer.getMvs() );
		
		ArrayList<String> slideKeys = buffer.getSlideKeys();
		doc.setSlideKeyStrs( slideKeys );
	}	
	
	private void parseMICRO( ParseSBuffer si ) {
		int depth = 0;			
		for ( ; si.getIdx() < lines.length; si.addIdx() ) {
			String line = lines[ si.getIdx() ];
//			System.out.println( line );
			
			// ignore blank line
			if ( line.trim().length() == 0 || line.trim().equals("\"") ) {		
				continue;
			}			
			
			// terminate conditions
			boolean matched = false;
			
			// the current line is not related with the MICRO value
			for ( String in : indicators ) {				
				if ( line.trim().toLowerCase().startsWith( in.toLowerCase() ) ) {									
					matched = true;
					break;
				} else if ( line.trim().equals(".") ) {
					matched = true;
					break;
				}
			}
			// because we consume one idx
			if ( matched == true ) {
				si.subIdx();
				break;
			}
						
			// check the character symbol... 	
			// detect A~~~), A,B,~~) ... 
			boolean startWithCharacterSymbol = false;
			String characterSymbol = null;
			Matcher matcher = startCharacterSymbolPattern.matcher( line.trim() );		
			while ( matcher.find() ) {
				characterSymbol = matcher.group();
				startWithCharacterSymbol = true;
			}
			
			// 문자 symbol로 시작하는 경우
			// 문자를 #으로 바꾼 후, 나머지 string은 nodeStr이 됨.
			int i = 0;
			String nodeStr = "";
			if ( startWithCharacterSymbol ) {
				i = line.indexOf(characterSymbol) + characterSymbol.length();
				nodeStr = line.substring( i ).trim();
				characterSymbol = StringUtils.alphabetNormalize( characterSymbol.trim() );
			}
												
			// check the digit symbol... 	
			// detect 1), 1,2,~~) ... 
			boolean startWithDigitSymbol = false;
			String digitSymbol = null;		// "";
			matcher = startDigitSymbolPattern.matcher( line.trim() );			
			while ( matcher.find() ){ 					
				digitSymbol = matcher.group();
				startWithDigitSymbol = true;
			}
			
			// 숫자 symbol로 시작하는 경우
			// 숫자를 #으로 바꾼 후, 나머지 string은 nodeStr이 됨.
			if ( startWithDigitSymbol ) {
				i = line.indexOf(digitSymbol) + digitSymbol.length();
				nodeStr = line.substring( i ).trim();
				digitSymbol = StringUtils.digitNormalize( digitSymbol.trim() );	// 1) 2) --> #) #)
			}			
			
			// -, ',', ;로 시작하는 symbol을 체크하는건데..
			// 문자나 숫자로 시작하지 않는 symbol들을 체크...
			if ( startWithDigitSymbol == false && startWithCharacterSymbol == false ) {
				if ( line.trim().startsWith( "-" ) ) {
					characterSymbol = "-";
					i = line.indexOf(characterSymbol) + characterSymbol.length();
					nodeStr = line.substring( i ).trim();
					startWithCharacterSymbol = true;
				}
				// Modified 5/12/14
				else if ( line.trim().startsWith( "," ) ) {
					characterSymbol = ",";
					i = line.indexOf(characterSymbol) + characterSymbol.length();
					nodeStr = line.substring( i ).trim();
					startWithCharacterSymbol = true;
				}
				else if ( line.trim().startsWith( ";" ) ) {
					characterSymbol = ";";
					i = line.indexOf(characterSymbol) + characterSymbol.length();
					nodeStr = line.substring( i ).trim();
					startWithCharacterSymbol = true;
				}
			}
			
			// 그래도 아무것도 없으면..
			// 문자열 symbol 또는 숫자 symbol로 시작하지 않으면, nodeStr은 그 전체가 된다. 
			if ( startWithCharacterSymbol == false && startWithDigitSymbol == false ) {
				nodeStr = line.substring( i ).trim();
			}
														
//			boolean isStartSymbol2= StringUtils.isStartSymbols( line );
//			String str = line.trim();
//			
//			String startSymbol = "";
//			int i = 0;
//			if ( isStartSymbol == true ) {								
//				for ( ; i < str.length(); i++ ) {
//					char ch = str.charAt( i );
//					if ( Character.isAlphabetic( ch ) || ch == '(' ) {
//						break;
//					}
//				}
//				startSymbol = str.substring(0, i);
//			}	
//			startSymbol = StringUtils.normalize( startSymbol.trim() );						
//			String nodeStr = str.substring( i ).trim();
			
			/*
			 * Process like this!
			 * 
			 *  9)
			 * 10)
			 *  
			 * 숫자 symbol이면, 
			 */
			char ch = '\0';
			i = 1;
			if ( digitSymbol != null ) {
				ch = digitSymbol.charAt( 0 );								
				for ( ; i < digitSymbol.length(); i++ ) {
					char jthchar = digitSymbol.charAt( i );
					if ( ch != jthchar ) {
						break;
					}
				}
				digitSymbol = digitSymbol.substring( i - 1 );
			}
									
			// (i-1) compensate from the startSymbol normalization
			int currentIndentation = StringUtils.numOfIndentation( line ) + (i - 1);
			
			// backtrack to the MICROValue
			// Find the same indentation line
			ArrayList<MicroValue> mvs = si.getMvs();
			int a = mvs.size() - 1;
			int iter = 0;
			boolean check = false;
			for ( ; a >= 0; a-- ) {
				int previousIndentation = mvs.get( a ).getIndentation();
				if ( previousIndentation < currentIndentation ) {
					depth = mvs.get( a ).getDepth() + 1;
					check = true;
					break;
				} else if ( previousIndentation == currentIndentation ) {
					depth = mvs.get( a ).getDepth();
					break;
				}
				iter++;
			}
				
			// init relativeIdx
			if ( startWithDigitSymbol == false && startWithCharacterSymbol == false 
					&& currentIndentation == 0 && iter >= 1 ) {
				if ( !nodeStr.trim().endsWith(":") && !nodeStr.trim().endsWith(";") ) {
					if ( nodeStr.startsWith("(") || Character.isLowerCase( nodeStr.charAt(0) ) ) {
						if ( idx - 1 >= 0 )
							depth = mvs.get(idx - 1).getDepth() + 1;
					}
					else {
						relativeIdx = 0;
						if ( check == true ) {
							for ( int di = a + 1; di < idx; di++ ) {
								int tmpDepth =  mvs.get( di ).getDepth();
								mvs.get( di ).setDepth( tmpDepth + 1 );
							}
						}	
						
						if ( si.getIdx() + 1 < lines.length ) {
							String nextLine = lines[ si.getIdx() + 1 ].trim();
							if ( nextLine.endsWith(":")) {
								nodeStr += " " + nextLine;
								si.addIdx();
							}
						}						
					}
				}
				else {
					relativeIdx = 0;
					if ( check == true ) {
						for ( int di = a + 1; di < idx; di++ ) {
							int tmpDepth =  mvs.get( di ).getDepth();
							mvs.get( di ).setDepth( tmpDepth + 1 );
						}
					}
				}
			}
			
			MicroValue mv = new MicroValue ( idx++, relativeIdx++, nodeStr, depth, currentIndentation, 
					startWithDigitSymbol, startWithCharacterSymbol, digitSymbol, characterSymbol );
			
			si.addMv( mv );
		}	
	}
	private void processMicro( ParseSBuffer si ) {
		si.addIdx();
		relativeIdx = 0;
		parseMICRO ( si );
	}
	
	private void parseTreeBased( ParseSBuffer si ) {
		for ( ; si.getIdx() < lines.length; si.addIdx() ) {			
			String line = lines[ si.getIdx() ].trim();
			
			if ( line.startsWith("MICRO ") || line.startsWith("MICRO(")  ) {
				si.addMicro( line );
				continue;
			}
			
			boolean flag = false;
			for ( String diagnosisNameIndicator : diagnosisNameIndicators ) {				
				if ( line.toUpperCase().equals( diagnosisNameIndicator ) ) {
					processMicro ( si );
					flag = true;
					break;
				}
			}
			
			if ( flag == false ) {
				// Process variational diagnosis!!  
				for ( String diagnosisNameIndicator : variationalDiagnosisNameIndicators ) {
					if ( line.startsWith( diagnosisNameIndicator ) ) {
						// Find the continous colon or semicolon, or ...
//						System.out.println( line );
						int i = diagnosisNameIndicator.length();
						if ( line.length() == i ) {
							processMicro ( si );
							break;
						}
						else {
							while ( i < line.length() ) {
								if ( line.charAt(i) == ':' || line.charAt(i) == ';' || line.charAt(i) == '>' ) 
									break;
								
								i++;
							}
							// after removing ":", ";", ">", ...
							if ( line.length() == i ) {
								processMicro ( si );
								break;
							}
							else {
								String remainStr = line.substring( i + 1 ).trim();
								if ( remainStr.contains("A & Fro)") || remainStr.contains("A and Fro)") || remainStr.equals(":")) {
									processMicro ( si );
									break;
								}
								else {
									String tmp[] = new String[lines.length + 1];
									int idx = si.getIdx() + 1;
									System.arraycopy(lines, 0, tmp, 0, idx);
									tmp[ idx ] = remainStr;
									System.arraycopy(lines, idx, tmp, idx + 1, lines.length - idx );								
									lines = tmp;
									
									processMicro ( si );
									break;
								}
							}
						}
					}
				}				
			}
			
			// slide key
			for ( String slideKeyIndicator : slideKeyIndicators ) {
				if ( line.startsWith( slideKeyIndicator ) ) { 
					si.addSlideKye( line.substring( slideKeyIndicator.length() ) );
					break;
				}
			}
			
			for ( String revisedIndicator : revisedIndicators ) {
				if ( line.startsWith( revisedIndicator ) ) { 
					break;
				}
			}			
		}
	}

	
	public SurgeonDocument parse( String lines[], String pathologyID ) {
		this.lines = lines;
		this.pathologyID = pathologyID;
		idx = 0;
		SurgeonDocument doc = new SurgeonDocument();
		
		ParseSBuffer si = new ParseSBuffer();
		parseTreeBased ( si );
				
		setSDiagnosis ( si, doc );		
		return doc;
	}	
}
