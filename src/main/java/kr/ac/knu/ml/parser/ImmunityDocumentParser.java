/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.common.unit.Pair;
import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.parser.buffer.ParsePABuffer;
import kr.ac.knu.ml.parser.surgeon.SlideExtractor;
import kr.ac.knu.ml.unit.immunity.BioMarker;
import kr.ac.knu.ml.unit.immunity.Confidence;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;
import kr.ac.knu.ml.unit.immunity.PATableEntity;
import kr.ac.knu.ml.unit.immunity.PATable;

/**
 * For saving an intermediate results when PA documents parsing 
 * 
 * @author Hyun-Je Song
 *
 */
public class ImmunityDocumentParser {
	private String pathologyID;
	private final Pattern pattern = Pattern.compile( "\\(\\w*[ ,-]*\\w+[\\#\\d+]*\\)");
	
	// I don't like this code.....
	private final String diagnosisNameIndicators[] = { 
			"Histopathologic Diagnosis", "병리학적 진단", "병리학적진단", ":병리학적 진단", "병리학적 진단", "외과병리번호 및 진단:",
			"1. 병리학적 진단:", "2. 병리학적 진단:", "3. 병리학적 진단:", "4. 병리학적 진단:" };
	private final String madiagnosisNameIndicators[] = { "외과병리번호:" };
	private final String reportIndicator[] = {
			"면역화학", "면역 화학", "면 역 화 학 검 사 보 고 서", "형태계측", "형태 계측", "형 태 계 측", "분자병리", "분자 병리", "분 자 병 리 검 사 보 고 서" };
	private final String summaryIndicators[] = { 
			"Summary :", "summary :", "SUMMARY :", "Summary:", "summary:", "SUMMARY:", "Summary", "summary", "SUMMARY",
			"요 약:", "(요약)", "요약:", "요약;", "요약)", "요약", "(요약", "약:",
			": Consistent", "- Consistent", "Consistent", "consistent", "Consistent with", "Consistant with", 
			"Supportive", "Conclusion" };
	private final String noteIndicators[] = { "(Note)", "(Note", "(NOTE", "Note)", "Note:" };
	private final String seeIndicators[] = { "(see", "(See" };
	private final String resultIndicators[] = { "검사결과", "결과", "검사 결과" };	
	private final String addendumIndicators[] = { 
			"(Addendum", "(Addeudum", "(Addendim", "((Addendum", "<Addendum", 
			"(( Addendum", "< Addendum", "( Addendum", "Addendum",  "[ Addendum", "[Addendum" };	
	private final String reviseIndicators[] = { 
			"(( Revised", "<Revised", "<<Revised", "(Revised", 
			"((Revised", "<< Revised", "Revised", "((Corrected" };		
	private final String repeatedIndicators[] = { "((Repeated" };
	private final String methodIndicators[] = { "검사 및 판독방법" };
	private final String locationIndicators[] = { "검사 및 판독실" };
	private final String checkListIndicators[] = { "검사항목" };
		
	private String tableEscapeIndicators[];
	
	private final String summaryEscapeIndicators[] = {
			"Histopathologic Diagnosis", "병리학적 진단", "병리번호 및 진단", ":병리학적 진단", "<", "(Addendum",
			"((", "면 역 화 학 검 사 보 고 서", "형태계측검사보고서", "분 자 병 리 검 사 보 고 서", 
			"---", "===", "(Note", "(NOTE", "Note:", "(( Addendum", "면역화학", "면역 화학"};	
	
	private final String lineBylineEscapeIndicators[] = {
			"요약", "요 약", "약:", "Summary", "summary", "SUMMARY", "(요약",	// summaryIndicators
			"(( Revised", "((Revised", "(Revised", "((Corrected", 			// reviseIndicators
			"(Addendum", "(Addeudum", "(Addendim", "((Addendum", "(( Addendum",	"( Addendum", "<Addendum",	// addendumIndicators 
			"(Note", "(NOTE", "Note", "NOTE", 	// noteIndicators
			"(see", "(See", // seeIndicators
			"((Repeated",	// repeatedIndicators
			"면역화학", "면역 화학", "면 역 화 학 검 사 보 고 서", "형태계측", "형태 계측", "형 태 계 측", "분자병리", "분자 병리", "분 자 병 리 검 사 보 고 서", // reportIndicator
			"검사 및 판독방법:", "검사 및 판독실", // methodIndicators + checkListIndicators
			"검사결과", // resultIndicators
			"IHC Result", // resultIndicators
			"------------", "=========", // 
			"검사항목",	// checkListIndicators
			"Histopathologic Diagnosis",
			"외과병리번호", "병리학적 진단", "병리번호 및 진단", "진단", "참조", "검사자", "전공의", "* ", "결과", 
			": Consistent", "- Consistent", "Conclusion", "consistent", "Consistent with", "Consistant with", "Supportive" };	
	private final String noteEscapeIndicators[] = { 
			"면 역", "((Revised", "병리학적", "요약:", "-" };
			
	public ImmunityDocumentParser() {
		// 나중에 바꿀것. class를 써서...
		tableEscapeIndicators = new String[summaryIndicators.length + resultIndicators.length + diagnosisNameIndicators.length 
		                                   + addendumIndicators.length + reportIndicator.length + methodIndicators.length + noteIndicators.length + 1];
		int length = 0;
		System.arraycopy(summaryIndicators, 0, tableEscapeIndicators, length, summaryIndicators.length);
		length += summaryIndicators.length;
		
		System.arraycopy(resultIndicators, 0, tableEscapeIndicators, length, resultIndicators.length);
		length += resultIndicators.length;
		
		System.arraycopy(diagnosisNameIndicators, 0, tableEscapeIndicators, length, diagnosisNameIndicators.length);
		length += diagnosisNameIndicators.length;
		
		System.arraycopy(addendumIndicators, 0, tableEscapeIndicators, length, addendumIndicators.length);
		length += addendumIndicators.length;
		
		System.arraycopy(reportIndicator, 0, tableEscapeIndicators, length, reportIndicator.length);
		length += reportIndicator.length;
		
		System.arraycopy(methodIndicators, 0, tableEscapeIndicators, length, methodIndicators.length);
		length += methodIndicators.length;
		
		System.arraycopy(noteIndicators, 0, tableEscapeIndicators, length, noteIndicators.length);
		length += noteIndicators.length;
		
		tableEscapeIndicators[length] = "Error";		
	}
	
	public List<BioMarker> extractMarkers ( String line ) {
		return extractMarkers( line, null, null );
	}
	
	public List<BioMarker> extractMarkers ( String line, String slideKeyInfo ) {
		return extractMarkers( line, slideKeyInfo, null );
	}
	
	public List<BioMarker> extractMarkers ( String line, String slideKeyInfo, String slideAdditionalInfo ) {
		String bioMarker = null;
		String result = null;
		List<BioMarker> markerList = new ArrayList<BioMarker>();

		// Exceptions - Do not extract as bioMarker
		// PA120010559 (- 형태계측검사)
		// 
		if ( line.startsWith("- 형태계측검사") )
			return markerList;
		
		int startIdx = 1;
		for ( ; startIdx < line.length(); startIdx++ ) {
			char ch = line.charAt( startIdx );
			if ( !Character.isWhitespace( ch ) )
				break;
		}
		
		int idx = line.indexOf(":");
		if ( idx == -1 ) {
			// Try error recovery
			idx = line.indexOf(";");			
			if ( idx != -1 ) {
				startIdx = idx < startIdx ? idx : startIdx;
				bioMarker = line.substring( startIdx, idx ).trim();										
				result = line.substring( idx + 1 ).toUpperCase().trim();				
				
				Pair<String, String> extractedMarker = SlideExtractor.extractSlideFromBioMarker( bioMarker );
				bioMarker = extractedMarker.getA();
				String tmp = extractedMarker.getB();
				if ( tmp.trim().length() != 0 ) {
					slideKeyInfo += " " + tmp.trim();
					slideKeyInfo = slideKeyInfo.trim();
				}
				markerList.add ( new BioMarker(bioMarker, result, false, Confidence.FAIR, slideKeyInfo, slideAdditionalInfo ) );
			}			
			else if ( line.startsWith("- EGFR-Positive") || line.startsWith("-EGFR-Positive") || line.startsWith("- EGFR - Focal positive") ) {
				bioMarker = "EGFR";
				result = "Positive";
				markerList.add ( new BioMarker(bioMarker, result, false, Confidence.POOR, slideKeyInfo, slideAdditionalInfo ) );
			}
			else if ( line.startsWith("- EGFR-Negative") || line.startsWith("-EGFR-Negative") || line.startsWith("- EGFR - Focal negative")) {
				bioMarker = "EGFR";
				result = "Negative";
				markerList.add ( new BioMarker(bioMarker, result, false, Confidence.POOR, slideKeyInfo, slideAdditionalInfo ) );
			}
			else {				
				String tokens[] = line.split("\\s");
				if ( tokens.length <= 2 )
					return markerList;
				
				String recoveryLine = "";
				boolean flag = false;
				for ( int i = 0; i < tokens.length; i++ ) {
					if ( i < 2 )
						recoveryLine += tokens[i] + " ";
					else {
						if ( flag == false && Character.isUpperCase( tokens[i].charAt(0) ) ) {							
							recoveryLine += ": " + tokens[i] + " ";
							flag = true;
						} else
							recoveryLine += tokens[i] + " ";
					}
				}	
				
				idx = recoveryLine.indexOf(":");				
				if ( idx == -1 ) {
					bioMarker = recoveryLine.substring( startIdx ).trim();
					result = "";
				}
				else {
					startIdx = idx < startIdx ? idx : startIdx;
					bioMarker = recoveryLine.substring( startIdx, idx ).trim();				
					result = recoveryLine.substring( idx + 1 ).toUpperCase().trim();
				}				
				
				if ( bioMarker.endsWith("-") ) 
					bioMarker = bioMarker.substring( 0, bioMarker.length() - 1 );
						
				Pair<String, String> extractedMarker = SlideExtractor.extractSlideFromBioMarker( bioMarker );
				bioMarker = extractedMarker.getA();
				String tmp = extractedMarker.getB();
				if ( tmp.trim().length() != 0 ) {
					slideKeyInfo += " " + tmp.trim();
					slideKeyInfo = slideKeyInfo.trim();
				}
				
				markerList.add ( new BioMarker(bioMarker, result, false, Confidence.FAIR, slideKeyInfo, slideAdditionalInfo ) );
			}
		}
		else {
			startIdx = idx < startIdx ? idx : startIdx;
			bioMarker = line.substring( startIdx, idx ).trim();
			result = line.substring( idx + 1 ).toUpperCase().trim();
//			if ( bioMarker.contains( "(") && bioMarker.contains(")") )
//				System.out.println( bioMarker );
			
			// PA090005159
			if ( bioMarker.startsWith("- ") )	
				bioMarker = bioMarker.substring( "- ".length() );
			
			Pair<String, String> extractedMarker = SlideExtractor.extractSlideFromBioMarker( bioMarker );
			bioMarker = extractedMarker.getA();
			String tmp = extractedMarker.getB();
			if ( tmp.trim().length() != 0 ) {
				slideKeyInfo += " " + tmp.trim();
				slideKeyInfo = slideKeyInfo.trim();
			}
			
			markerList.add ( new BioMarker(bioMarker, result, false, Confidence.FAIR, slideKeyInfo, slideAdditionalInfo ) );
		}
		return markerList;
	}
	
	public List<BioMarker> extractMarkersFromTable ( String bioMarker, String result, String slideKeyInfo, String slideAdditionalInfo ) {
		List<BioMarker> markerList = new ArrayList<BioMarker>();
//		if ( bioMarker.contains( "(") && bioMarker.contains(")") )
//			System.out.println( bioMarker );
		
		Pair<String, String> extractedMarker = SlideExtractor.extractSlideFromBioMarker( bioMarker );
		bioMarker = extractedMarker.getA();
		String tmp = extractedMarker.getB();
		if ( tmp.trim().length() != 0 ) {
			slideKeyInfo += " " + tmp.trim();
			slideKeyInfo = slideKeyInfo.trim();
		}
		
		markerList.add ( new BioMarker( bioMarker, result.toUpperCase(), true, Confidence.FAIR, slideKeyInfo, slideAdditionalInfo ) );
		return markerList;
	}
	
	// Extract contents followed by ':'
	public String extractContents( String line ) {
		return line.substring( line.indexOf(":") + 1 ).trim(); 
	}
	
	private void parseSummary( String lines[], ParsePABuffer pi, String summaryIndicator ) {
		String summary = "";
		boolean startFlag = true;
		
		for ( ; pi.getIdx() < lines.length; pi.addIdx() ) {
			String line = lines[ pi.getIdx() ].trim();
			if ( line.length() == 0 )
				break;
		
			boolean exceptionFlag = false;
			if ( startFlag == true ) 
				line = line.substring( summaryIndicator.length() ).trim();
						
			for ( String escapeStr : summaryEscapeIndicators ) {	
				if ( line.startsWith( escapeStr ) ) {					
					if ( startFlag == true ) 
						lines[ pi.getIdx() ] = line;
					pi.subIdx();
					exceptionFlag = true;					
					break;
				}
			}									
			
			if ( exceptionFlag == true )
				break;
						
			summary = summary + " " + line;			
			startFlag = false;
		}		
		pi.setDiagnosisSummary( summary.trim() );
	}
	
	/**
	 * Ignore blank line.
	 * 
	 * @param linespat
	 * @param pi
	 */
	private void ignoreBlankLine( String lines[], ParsePABuffer pi ) {
		String line = lines[ pi.getIdx() ].trim();
		while ( line.length() == 0 && pi.getIdx() + 1 < lines.length ) {
			line = lines[ pi.getAddedIdx() ].trim();
		}
	}
	
	private void analysisLineByLine( String lines[], ParsePABuffer pi ) {
		String slideKeyInfo = null;
		String slideAdditionalInfo = null;
		
		for ( ; pi.getIdx() < lines.length; pi.addIdx() ) {			
			String line = lines[ pi.getIdx() ];
			String trimedLine = line.trim();			
			
			// ignore blank line
			while ( trimedLine.length() == 0 && pi.getIdx() + 1 < lines.length ) {
				line = lines[ pi.getAddedIdx() ];
				trimedLine = line.trim();
			}
			
//			sb.append( line + "\n" );
			boolean escape = false;				
			for ( String escapeStr : lineBylineEscapeIndicators ) {
				if ( trimedLine.startsWith( escapeStr ) ) {					
					escape = true;						
					break;
				}
			}				
			if ( escape == true ) {
				// look-up & check EGFR-Positive
				if ( pi.getIdx() + 1 < lines.length ) {
					String tmp = lines[ pi.getIdx() + 1 ].trim();
					if ( tmp.startsWith( "EGFR-Positive") || tmp.startsWith("EGFR-Negative") )
						continue;
				}
				pi.subIdx();
				break;
			}
			
			if ( trimedLine.startsWith("-") ) {
				pi.addMarkers( extractMarkers ( trimedLine, slideKeyInfo, slideAdditionalInfo ) );		
			}
			else if ( trimedLine.startsWith( "EGFR-Positive" ) || trimedLine.startsWith("EGFR-Negative") ) {
				pi.addMarkers( extractMarkers ( "- " + trimedLine, slideKeyInfo, slideAdditionalInfo ) );
			}
			else {
				if ( pi.getLastMarker() != null ) { // Process EGFR-Positive || EGFR-Negative
					String lastMarker = pi.getLastMarker().getMarkerName();		
					String confidence = pi.getLastMarker().getConfidence();
					if ( lastMarker.equals("EGFR") && confidence.equals( "POOR" ) ) {					
						continue;
					}
				}
				
				if ( trimedLine.equals("Clear cell type :") || trimedLine.equals("Papillary type: ") )
					slideKeyInfo = line.replace(" :", "");
				else if ( trimedLine.startsWith("S12-41791 (7), (8)") )
					slideKeyInfo = "7,8";
				else if ( trimedLine.startsWith("S12-41791 (9), (10)") )
					slideKeyInfo = "9,10";
				else if ( trimedLine.startsWith(":") ) {
					pi.appendValueAtLastBioMarker( trimedLine.substring( 1 ).trim() );
				}
				else if ( trimedLine.startsWith("(") ) {
					Matcher matcher = pattern.matcher( trimedLine );					
					StringBuilder tmp = new StringBuilder();
					while ( matcher.find() ) {
						String matched = matcher.group();
						tmp.append( matched.substring( 1, matched.length() - 1 ) + "," );
					}
					
					String tmpStr = tmp.toString().trim();
					if ( tmpStr.length() != 0 ) {
						slideKeyInfo = tmpStr.substring(0, tmpStr.length() - 1);
						
						if ( line.lastIndexOf(")") + 1 < line.length() ) {     
							String additionalInfo = line.substring( line.lastIndexOf(")") + 1 ).trim();							
							if ( additionalInfo.length() != 0 ) 
								slideAdditionalInfo = additionalInfo;
						}
					}
					else {
						pi.appendValueAtLastBioMarker( line );
					}
				}
				else {
					pi.appendValueAtLastBioMarker( line );
				}	
			}
		}
//		if ( check ) {
//			System.out.println();
//			System.out.println( sb.toString() );
//			System.out.println();
//		}
	}	
	
	private void analysisTable( String lines[], ParsePABuffer pi ) throws ArrayIndexOutOfBoundsException{
		int nCol = -1;
		boolean isPreviouslineTitle = false;
				
		StringBuilder sb = new StringBuilder();		
		boolean notMatch = false;
				
		ArrayList<PATable> tables = new ArrayList<PATable>();
		PATable table = null;
		boolean withoutItem = false; 
		for ( pi.addIdx() ; pi.getIdx() < lines.length; pi.addIdx() ) {						
			String line = lines[ pi.getIdx() ];
			String trimedLine = line.trim();
			
			sb.append( line + "\n" );
			if ( trimedLine.startsWith("---") || trimedLine.length() == 0 ) {				 
				try {
					if ( table != null ) {
						if ( withoutItem == true ) 
							tables.set( tables.size() - 1, (PATable) table.clone() );
						else 
							tables.add( (PATable) table.clone() );
					}
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				} finally {
					table = null;
					withoutItem = false;
				}									
				isPreviouslineTitle = false;
				continue;
			}
							
			// Table escaper
			boolean isEndofTable = false;
			for ( String escapeStr : tableEscapeIndicators ) {
				if ( trimedLine.startsWith( escapeStr ) ) {											
					isEndofTable = true;
				}
			}						
			if ( isEndofTable == true ) {				
				try {
					if ( table != null ) {
						if ( withoutItem == true ) 
							tables.set( tables.size() - 1, (PATable) table.clone() );
						else 
							tables.add( (PATable) table.clone() );
					}
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				} finally {
					table = null;
					withoutItem = false;
				}
				pi.subIdx();	
				break;
			}
			
			// Check if `====' is `table decorator' or not 
			if ( trimedLine.startsWith("======") ) {
				// this indicates `====' is not table discriminiator
				if ( isPreviouslineTitle == false ) {
					break;
				}
				else {
					isPreviouslineTitle = false;
					continue;
				}
			}			
			
			// Table analysis			
			String tokens[] = line.split("\\||(?<!\\w)l(?!\\w)|(?<!Dystrophin |\\w)I(?!\\w)", -1);
			if ( tokens.length > 0 ) {
				String firstColumn = tokens[0].trim();
				// The current row is title row
				if ( firstColumn.equals("Item") ) {
					isPreviouslineTitle = true;
					
					nCol = tokens.length;	// the number of columns in title										
					PATableEntity title[] = new PATableEntity[nCol];
					for ( int i = 0; i < tokens.length; i++ ) {
						String token = tokens[i];
						int indentation = StringUtils.numOfIndentation( token );
						int length = token.length();
						
						PATableEntity entity = new PATableEntity( token.trim(), indentation, length );
						title[i] = entity;
					}					
					table = new PATable();
					table.setTitle( title );
				}
//				// The current row is specialized version of title row e.g. PA120016197
				else if ( firstColumn.equals("(1)") || firstColumn.equals("(2)") ) {
					isPreviouslineTitle = true;
					nCol = tokens.length;
					
					PATableEntity title[] = new PATableEntity[] { 
							new PATableEntity( "Item", 0, "Item".length() ), 
							new PATableEntity ( firstColumn, 0, firstColumn.length() ) };
					
					table = new PATable();					
					table.setTitle( title );
					break;
				}
				// Special case - EGFR-Positive
				else if ( firstColumn.startsWith("- EGFR-Positive") ) {
					PATableEntity title[] = new PATableEntity[] { 
								new PATableEntity( "Item", 0, "Item".length() ), 
								new PATableEntity ("Result", 0, "Result".length() ) };
					PATableEntity row[] = new PATableEntity[] { 
								new PATableEntity( "EGFR", 0, "EGFR".length() ), 
								new PATableEntity ("Positive", 0, "Positive".length() ) };
					
					table = new PATable();
					table.setTitle( title );
					table.addRow( row );
					break;
				}
				else if ( isPreviouslineTitle == true ) {
					if ( tokens.length > nCol ) {
						// 추후, title bar가 두줄 이상 쓰여진 경우, 첫번째 줄보다 다음 줄에 더 긴 경우, 처리해야할 부분! 
						// Refer to PA080001763
						// Nothing						
					}
					else if ( tokens.length < nCol ) {					
						// Nothing						
					}
					else {					
						// tokens.length is same as nCol
						PATableEntity title[] = table.getTitle();
						for ( int i = 0; i < tokens.length; i++ ) {
							String token = tokens[i];
							title[i].addStr( token.trim() );
						}
					}
				}		
				else { 
					// Not title bar
					// Look up previous row
					if ( table == null ) {
						table = tables.get( tables.size() - 1 );
						withoutItem = true;
					}
					PATableEntity[] lastRow = table.getLastRow();					

					// title의 column이 현재 table row보다 큰 경우
					if ( tokens.length < nCol ) { 
						if ( lastRow == null ) {
							// Refer to PA080005663
							PATableEntity title[] = table.getTitle();							
							PATableEntity row[] = new PATableEntity[nCol];
							
							int i = 0;
							int c = 0;
							int cthMaxLength = 0;
							while ( i < tokens.length ) {
								String token = tokens[i];
								int indentation = StringUtils.numOfIndentation( token );
								int length = token.length();				
								
								cthMaxLength += title[c].getLength() + 1;
								if ( indentation > cthMaxLength ) {
									PATableEntity entity = new PATableEntity( "", 0, cthMaxLength );	// add null entity
									row[c++] = entity;
									continue;
								}
								
								PATableEntity entity = new PATableEntity( token.trim(), indentation, length );
								row[c] = entity;
								if ( length > cthMaxLength ) {	
									if ( c + 1 < nCol ) {	 // 1시 방향에 존재하는지 살펴볼 것!
										String diagonalStr = title[c + 1].getStr();
										if ( diagonalStr.length() != 0 ) {	// 값이 존재한다면 !
											row[c + 1] = entity;											
										}
										c++;
									}
								}
								i++;	// token increase
								c++;	// column increase
							}							
							while ( c < nCol ) {
								PATableEntity entity = new PATableEntity( "", 0, cthMaxLength );
								row[c++] = entity;
								continue;
							}
							table.addRow( row );
						}
						else {
							// refer to PA070000612
							int i = 0;
							int c = 0;
							int cthMaxLength = 0;
							while ( i < tokens.length ) {
								String token = tokens[i];
								int indentation = StringUtils.numOfIndentation( token );
								int length = token.length();				
								
								cthMaxLength += lastRow[c].getLength() + 1;								
								if ( indentation > cthMaxLength ) {
									c++;
									continue;
								}
								
								lastRow[c].addStr( token.trim() );
								if ( length > cthMaxLength ) {	// 여러 열에 걸쳐 작성된 경우!!!
									// 예외 병리보고서 - PA070000612 (손으로 수정함 ...)
									if ( c + 1 < nCol ) {	 // 1시 방향에 존재하는지 살펴볼 것!
										String diagonalStr = lastRow[c + 1].getStr();
										if ( diagonalStr.length() != 0 ) {	// 값이 존재한다면 ! 
											lastRow[c + 1].addStr( token.trim() );											
										}
										c++;
									}
								}
								i++;	// token increase
								c++;	// column increase
							}							
						}
					}
					else if ( tokens.length > nCol ) {										
						// title의 column이 현재 table row보다 적은 경우
						// Refer to PA080000589 
						// Nothing
					}
					else {	// title의 column이 현재 table row이랑 같다...	
						if ( lastRow == null ) {
							PATableEntity row[] = new PATableEntity[nCol];
							for ( int i = 0; i < tokens.length; i++ ) {
								String token = tokens[i];
								int indentation = StringUtils.numOfIndentation( token );
								int length = token.length();
								
								PATableEntity entity = new PATableEntity( token.trim(), indentation, length );
								row[i] = entity;
							}
							table.addRow( row );
						}
						else {
							boolean emptyColumn = false;
							boolean append = false;
							for ( int a = 0; a < tokens.length; a++ ) {
								String token = tokens[a];
								int indentation = StringUtils.numOfIndentation( token );
								int previousIndentation = lastRow[a].getIndentation();
								
								if ( a == 0 && token.trim().length() != 0 ) {
									if ( Character.isLowerCase( token.trim().charAt(0) ) && indentation > previousIndentation )
										append = true;
									break;
								}
								
								if ( token.trim().length() == 0 ) {
									emptyColumn = true;
								}
							}
							
							if ( emptyColumn || append ) {
								for ( int i = 0; i < tokens.length; i++ ) {
									String token = tokens[i];
									lastRow[i].addStr( token.trim() );
								}
							}
							else {
								PATableEntity row[] = new PATableEntity[nCol];
								for ( int i = 0; i < tokens.length; i++ ) {
									String token = tokens[i];
									int indentation = StringUtils.numOfIndentation( token );
									int length = token.length();
									
									PATableEntity entity = new PATableEntity( token.trim(), indentation, length );
									row[i] = entity;
								}
								
								table.addRow( row );
							}
						}		
					}	
				}
			}		
		}		
		
		analysisCleansedTable( tables, pi );		
		
//		if ( tables.size() >= 2 )
//			System.out.println( pathologyID + "\t" + tables.size() );
		
//		if ( notMatch == true ) {
//			System.out.println( pathologyID  );
//			System.out.println();
//			System.out.println( sb.toString() );
//			System.out.println();
//		}
	}

	private void analysisCleansedTable( ArrayList<PATable> tables, ParsePABuffer pi ) {
		for ( int i = 0; i < tables.size(); i++ ) {
			PATable tab = tables.get( i );
			int nCol = tab.getNCol();
			
			// Title parsing && extract slideKeyInfo and slideAdditionalInfo
			String slideKeyInfos[] = new String[nCol];
			String slideAdditionalInfos[] = new String[nCol];
			
			PATableEntity title[] = tab.getTitle();
			for ( int a = 1; a < nCol; a++ ) {
				String info = title[a].getStr();
				info = info.replace("Result", "").trim();
				
				if ( info.length() != 0 ) {
					if ( info.startsWith("(") ) {
						Matcher matcher = pattern.matcher( info );					
						StringBuilder tmp = new StringBuilder();
						while ( matcher.find() ) {
							String matched = matcher.group();
							tmp.append( matched.substring( 1, matched.length() - 1 ) + "," );
						}
						
						String tmpStr = tmp.toString().trim();
						if ( tmpStr.length() != 0 ) {
							slideKeyInfos[a] = tmpStr.substring(0, tmpStr.length() - 1);
							
							if ( info.lastIndexOf(")") + 1 < info.length() ) {     
								String additionalInfo = info.substring( info.lastIndexOf(")") + 1 ).trim();							
								if ( additionalInfo.length() != 0 ) 
									slideAdditionalInfos[a] = additionalInfo;
							}
						}
						else
							slideAdditionalInfos[a] = info;
					}
					else
						slideAdditionalInfos[a] = info;
				}					
			}
			
			ArrayList<PATableEntity[]> extractedTable = tab.getTable();
			for ( PATableEntity row[] : extractedTable ) {
				int a = 0;
				String bioMarker = null;
				String result = null;
				for ( PATableEntity entity : row ) {
					if ( a == 0 ) {
						// this is not to be null!!!
						bioMarker = entity.getStr();	
						a++;
					} else {
						result = entity.getStr();
						if ( result.trim().length() != 0 ) {							
							pi.addMarkers( extractMarkersFromTable ( bioMarker, result, slideKeyInfos[a], slideAdditionalInfos[a] ) );							
						}
						a++;
					}					
				}	
			}
		}
	}
	
	/**
	 * @param lines
	 * @param pi
	 */
	private void parseResult( String lines[], ParsePABuffer pi ) {
		if ( pi.getIdx() + 1 < lines.length ) {
			String line = lines[ pi.getAddedIdx() ].trim();
			
			// Blank remover!
			while ( line.length() == 0 && pi.getIdx() + 1 < lines.length ) {
				line = lines[ pi.getAddedIdx() ].trim();
			}
			
			// Ignore duplicated "검사결과:"  
			if ( line.startsWith("검사결과:") )
				return;
						
			try {
				// TODO: look ahead를 한 다음에 -인지 --인지를 살펴봐야 한다!!
				// (1) (2) (6) 이런거 다음에 테이블이 나올 수 있다.
				if ( line.startsWith("---") )
					analysisTable ( lines, pi );
				else 
					analysisLineByLine ( lines, pi );
			}
			catch ( ArrayIndexOutOfBoundsException e ) {
				e.printStackTrace();
			}
		}
	}

	// 손 봐야할 곳!!
	private void analysisAddendumResult( String lines[], ParsePABuffer pi ) {
		String line = lines[ pi.getIdx() ].trim();
		
		try {
			// itemize indicator			
			if ( line.startsWith("- ")) 
				analysisLineByLine ( lines, pi );
			else { // table			
				analysisTable( lines, pi );
			}
		}
		catch ( ArrayIndexOutOfBoundsException e ) {
			e.printStackTrace();
		}
	}	
	
	private void parseNote( String lines[], ParsePABuffer pi, String noteIndicator ) {
		String line = lines[ pi.getIdx() ].trim();		
		String diagnosisNote = line.substring( noteIndicator.length() ).trim();		

		if ( pi.getIdx() + 1 < lines.length ) {
			line = lines[ pi.getAddedIdx() ].trim();
			boolean exceptionFlag = false;
			for ( String escape : noteEscapeIndicators ) {
				if ( line.startsWith( escape ) ) {
					exceptionFlag = true;
				}
			}
			if ( exceptionFlag != true ) {
				diagnosisNote += line.trim();
			}
		}
		pi.setDiagnosisNote( diagnosisNote );
	}
	
	private void parseAddendum( String lines[], ParsePABuffer pi ) {
		String line = lines[ pi.getIdx() ].trim();
		String addendumDate = line.substring( line.indexOf(",") + 1 );
		
		// cleansing date!
		String refinedAddendumDate = "";
		for ( int i = 0; i < addendumDate.length(); i++ ) {
			char ch = addendumDate.charAt( i );
			if ( Character.isDigit(ch) || ch == '.' )
				refinedAddendumDate += ch;
		}
		pi.setAddendum( true );
		pi.setAddendumDate( refinedAddendumDate );
	}
	
	private void parseRevised( String lines[], ParsePABuffer pi ) {
		// nothing
	}

	private void addPADiagnosisToImmunityDocuments( ParsePABuffer pi, ImmunityDocument doc ) {
		boolean previousFlag = false;
		
		String diagnosisName = pi.getDiagnosisName();		
		ArrayList<BioMarker> markers = pi.getMarkers();
		String diagnosisSummary = pi.getDiagnosisSummary();
		String diagnosisNote = pi.getDiagnosisNote();
		boolean addendum = pi.isAddendum();
		String addendumDate = pi.getAddendumDate();
		
		if ( addendum == true ) {
			for ( BioMarker marker : markers ) {
				marker.setAddendum( true );
				marker.setAddendumDate( addendumDate );
			}
		}
			
		if ( diagnosisName == null || diagnosisName.length() == 0 ) {
			if ( doc.getLastDiagnosis() == null )
				return;
			
			PADiagnosis d = doc.getLastDiagnosis();
			d.addMarkers( pi.getMarkers() );
			d.addDiagnosisSummary( pi.getDiagnosisSummary() );
			d.setDiagnosisNote( pi.getDiagnosisNote() );				
		}
		else {
			for ( PADiagnosis d : doc.getDiagnosises() ) {
				if ( diagnosisName.equals( d.getDiagnosisName() ) ) {
					d.addMarkers( markers );
					d.addDiagnosisSummary( diagnosisSummary );
					d.setDiagnosisNote( diagnosisNote );
					previousFlag = true;
					break;
				}			
			}
			
			if ( previousFlag != true ) {
				PADiagnosis diagnosis = new PADiagnosis();
				
				diagnosis.setDiagnosisName( diagnosisName );
				diagnosis.setMarkers( markers );
				diagnosis.setDiagnosisSummary( diagnosisSummary );
				diagnosis.setDiagnosisNote( diagnosisNote );
				
				doc.addDiagnosis( diagnosis );
			}
		}
		
		// clear
		pi.init();
	}

	public void parsePADiagnosis( String lines[], ParsePABuffer pi, ImmunityDocument doc ) {
		for ( ; pi.getIdx() < lines.length; pi.addIdx() ) {
			String line = lines[ pi.getIdx() ].trim();
			// parseMorphometricAnalysis
			if ( line.startsWith("형태계측검사보고서") || line.startsWith("검사항목: 형태계측") ) {
				pi.subIdx();
				break;
			}
			
			// Detect diagnosis name,
			for ( String diagnosisNameIndicator : diagnosisNameIndicators ) {				
				if ( line.startsWith( diagnosisNameIndicator ) ) {
					if ( pi.getDiagnosisName() != null ) 
						addPADiagnosisToImmunityDocuments ( pi, doc );
					
					String diagnosisName = extractContents ( line );
					pi.setDiagnosisName( diagnosisName );
					break;
				}
			}
			
			// Parse BioMarker
			// This indicates that the next lines represent the biomarker & result 
			for ( String resultIndicator : resultIndicators ) {	
				if ( line.startsWith( resultIndicator ) ) {
					ignoreBlankLine ( lines, pi );
					parseResult ( lines, pi );
					break;
				}
			}
			
			// Parse Summary
			for ( String summaryIndicator : summaryIndicators ) {	
				if ( line.startsWith( summaryIndicator ) ) {
					parseSummary ( lines, pi, summaryIndicator );
					break;
				}
			}
				
			// Parse Note
			for ( String noteIndicator : noteIndicators ) {	
				if ( line.startsWith( noteIndicator ) ) {
					parseNote ( lines, pi, noteIndicator );
					break;
				}
			}			
			
			// Parse Addendum
			for ( String addendumIndicator : addendumIndicators ) {	
				if ( line.startsWith( addendumIndicator ) ) {					
					addPADiagnosisToImmunityDocuments ( pi, doc );					
					parseAddendum ( lines, pi  );
					break;
				}
			}
			
			// Parse Revised
			for ( String reviseIndicator : reviseIndicators ) {	
				if ( line.startsWith( reviseIndicator ) ) {
					parseRevised ( lines, pi );
					break;
				}
			}
			
			if ( line.startsWith("- ") ) {							
				if ( line.charAt( 0 ) == '-' && line.charAt( 1 ) == ' ' ) {
					if ( pi.isAddendum() == true )						
						analysisAddendumResult ( lines, pi );
					else
						analysisLineByLine ( lines, pi );
				}
			}
		}
	}	
	
	public void parseMorphometricAnalysis( String lines[], ParsePABuffer pi, ImmunityDocument doc ) {		
		boolean parseLocation = false;
		for ( ; pi.getIdx() < lines.length; pi.addIdx() ) {
			String line = lines[ pi.getIdx() ].trim();			
			
			// Detect diagnosis name,
			for ( String diagnosisNameIndicator : diagnosisNameIndicators ) {			
				if ( line.startsWith( diagnosisNameIndicator ) ) {					
					String localDiagnosisName = extractContents ( line );
					pi.setDiagnosisName( localDiagnosisName );
					break;
				}
			}
			
			for ( String diagnosisNameIndicator : madiagnosisNameIndicators ) {			
				if ( line.startsWith( diagnosisNameIndicator ) ) {
					if ( pi.getDiagnosisName() != null ) 
						addMorphometricAnalysisToImmunityDocuments ( pi, doc );
					
					String localDiagnosisName = extractContents ( line );
					pi.setDiagnosisName( localDiagnosisName );
					break;
				}
			}
			
			for ( String methodIndicator : methodIndicators ) {				
				if ( line.startsWith( methodIndicator ) ) {										
					parseDiagnosisMethod ( lines, pi );
					break;
				}
			}
			
			for ( String summaryIndicator : summaryIndicators ) {	
				if ( line.startsWith( summaryIndicator ) ) {
					parseSummary ( lines, pi, summaryIndicator );
					break;
				}
			}
			
			// Parse BioMarker
			// This indicates that the next lines represent the biomarker & result 
			for ( String resultIndicator : resultIndicators ) {	
				if ( line.startsWith( resultIndicator ) ) {
					ignoreBlankLine ( lines, pi );
					parseResult ( lines, pi );
					break;
				}
			}

			// Parse Note
			for ( String noteIndicator : noteIndicators ) {	
				if ( line.startsWith( noteIndicator ) ) {
					parseNote ( lines, pi, noteIndicator  );
					break;
				}
			}
			
			// Parse location 
			for ( String locationIndicator : locationIndicators ) {	
				if ( line.startsWith( locationIndicator ) ) {					
					parseLocation ( lines, pi );
					parseLocation = true;
					break;
				}
			}
			
			if ( parseLocation == true ) {
				addMorphometricAnalysisToImmunityDocuments ( pi, doc );
				break;
			}
		}
	}
	
	private void parseDiagnosisMethod( String lines[], ParsePABuffer pi ) {
		String line = lines[ pi.getIdx() ].trim();
		String diagnosisMethod = extractContents ( line );
		pi.setDiagnosisMethod( diagnosisMethod );
	}
	
	private void parseLocation( String lines[], ParsePABuffer pi ) {
		String line = lines[ pi.getIdx() ].trim();
		String diagnosisLocation = extractContents ( line );
		pi.setDiagnosisLocation( diagnosisLocation );
	}
	
	private void addMorphometricAnalysisToImmunityDocuments( ParsePABuffer pi, ImmunityDocument doc ) {		
		String diagnosisName = pi.getDiagnosisName();		
		ArrayList<BioMarker> markers = pi.getMarkers();
		String diagnosisSummary = pi.getDiagnosisSummary();
		String diagnosisNote = pi.getDiagnosisNote();
		String diagnosisMethod = pi.getDiagnosisMethod();
		String diagnosisLocation = pi.getDiagnosisLocation();
		boolean addendum = pi.isAddendum();
		String addendumDate = pi.getAddendumDate();
		
		if ( addendum == true ) {
			for ( BioMarker marker : markers ) {
				marker.setAddendum( true );
				marker.setAddendumDate( addendumDate );
			}
		}
						
		MorphometricAnalysis ma = new MorphometricAnalysis();		
		if ( diagnosisName == null || diagnosisName.length() == 0 ) {
			if ( pi.getLastDiagnosisName() != null ) {
				diagnosisName = pi.getLastDiagnosisName();
			}
		}
		
		ma.setDiagnosisName( diagnosisName );
		ma.setMarkers( markers );
		ma.setDiagnosisSummary( diagnosisSummary );
		ma.setDiagnosisNote( diagnosisNote );
		ma.setDiagnosisMethod( diagnosisMethod );
		ma.setDiagnosisLocation( diagnosisLocation );
		
		doc.addMorphometricAnalysis( ma );
		
		// clear
		pi.init();
	}	
	
	public ImmunityDocument parse( String lines[], String pathologyID ) {
//		System.out.println( pathologyID );
		this.pathologyID = pathologyID;
		ImmunityDocument doc = new ImmunityDocument();		
		ParsePABuffer pi = new ParsePABuffer();
		
		// Process line by line like as the SAX Parser
		for ( ; pi.getIdx() < lines.length; pi.addIdx() ) {
			// ignore the blank line
			ignoreBlankLine ( lines, pi );
			
			String line = lines [ pi.getIdx() ].trim();
			// MorphometricAnalysis (형태계측검사보고서)
			if ( line.startsWith("형태계측검사보고서") || line.startsWith("검사항목: 형태계측") ) {	
				// check the previous analysis		
				if ( pi.getDiagnosisName() != null )
					addPADiagnosisToImmunityDocuments ( pi, doc );
				// parse MorphometricAnalysis
				parseMorphometricAnalysis( lines, pi, doc );				
			}
			else 
				parsePADiagnosis ( lines, pi, doc );
		}
		// check the previous analysis 
		if ( pi.getDiagnosisName() != null || pi.getMarkers().size() > 0 ) {
			addPADiagnosisToImmunityDocuments ( pi, doc );
		}
				
		return doc;
	}
}