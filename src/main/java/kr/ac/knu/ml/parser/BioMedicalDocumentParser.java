/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.exec.classifier.SurgeonDocumentForSKC;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * To deal with pathology reports in a form of excel or txt file
 * 
 * @author Hyun-Je
 *
 */
public class BioMedicalDocumentParser {
	private ImmunityDocumentParser paParser;
	private SurgeonDocumentParser sParser;
	private ChildSurgeonDocumentParser csParser; 
	private boolean saveText;
	private final String titleStr[] = {"보고서생성일", "ReportID", "PatientID", "Sex", "age", "SNOMED_CT_ORGAN", "SNOMED_CT_TEST", "SNOMED_CT_DIAGNOSIS", "RESULTS" };
	private final String SNUIndicators[] = {"ReportID:", "PatientID:", "Gender:", "Age:", "SCTcodeOrgan:", "SCTcodeSurgeory", "SCTcodeDiagnosis", "Result:" };
	
	public BioMedicalDocumentParser() {
		paParser = new ImmunityDocumentParser();
		sParser = new SurgeonDocumentParser();
		csParser = new ChildSurgeonDocumentParser();
		this.saveText = true;
	}
	
	public BioMedicalDocumentParser( boolean saveText ) {
		paParser = new ImmunityDocumentParser();
		sParser = new SurgeonDocumentParser();
		csParser = new ChildSurgeonDocumentParser();
		this.saveText = saveText;
	}
			
	private boolean titleParser( Row row, HashMap<String, Integer> titles ) {
		int i = 0;
		for ( Cell cell : row ) {
			String title = cell.getStringCellValue();
			if ( title.equals("보고서생성일") ) {
				int idx = 0; 
				for ( String t : titleStr ) 
					titles.put( t, idx++ );
				break;
			}
			titles.put( title, i++ );			
		}
		
		if ( titles.keySet().size() == 0 ) {			
			return false;
		}
		return true;
	}
	
	public void excelparse( ArrayList<Sheet> sheets, 
			ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs, boolean saveOriginal ) {
		for ( Sheet sheet : sheets ) {
			excelparse ( sheet, idocs, sdocs, csdocs, saveOriginal );
		}
	}
	
	public void excelparse( ArrayList<Sheet> sheets, 
			ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs, String type, String testDoc, boolean saveOriginal ) {
		for ( Sheet sheet : sheets ) {
			excelparse ( sheet, idocs, sdocs, csdocs, type, testDoc, saveOriginal );
		}
	}
	
	private ImmunityDocument fillImmunityDocument( String typeOfDoc, String[] sentences, 
			String pathologyID, String resultText, String patientNumber, String gender, int age, String ageUnit,
			String snomedLocation, String snomedTestName, String snomedDiagnosisName )
	{		
		ImmunityDocument doc = null;						
		doc = paParser.parse( sentences, pathologyID );			
		doc.setPathologyID( pathologyID );		
		if ( saveText == true )
			doc.setOriginalText( resultText );
		doc.setPatientNumber( patientNumber );
		doc.setGender( gender );
    	doc.setAge( age );
    	doc.setAgeUnit( ageUnit );
    	doc.setSnomedLocation( snomedLocation );
    	doc.setSnomedTestName( snomedTestName );
    	doc.setSnomedDiagnosisName( snomedDiagnosisName );
    	
		return doc;
	}
	
	private SurgeonDocument fillSurgeonDocument( String typeOfDoc, String[] sentences, 
			String pathologyID, String resultText, String patientNumber, String gender, int age, String ageUnit,
			String snomedLocation, String snomedTestName, String snomedDiagnosisName )
	{		
		SurgeonDocument doc = null;
		
		doc = sParser.parse( sentences, pathologyID );		
		pathologyID = pathologyID.substring(0,1) + pathologyID.substring(2);		
		doc.setPathologyID( pathologyID );
		
		doc.setAbbrPathologyID( pathologyID.substring(0, pathologyID.length() - 1) );
		if ( saveText == true )
			doc.setOriginalText( resultText );
		doc.setPatientNumber( patientNumber );
		doc.setGender( gender );
    	doc.setAge( age );
    	doc.setAgeUnit( ageUnit );
    	doc.setSnomedLocation( snomedLocation );
    	doc.setSnomedTestName( snomedTestName );
    	doc.setSnomedDiagnosisName( snomedDiagnosisName );
		
		return doc;
	}
	
	private ChildSurgeonDocument fillChildSurgeonDocument( String typeOfDoc, String[] sentences, 
			String pathologyID, String resultText, String patientNumber, String gender, int age, String ageUnit,
			String snomedLocation, String snomedTestName, String snomedDiagnosisName )
	{		
		ChildSurgeonDocument doc = null;		
		
		doc = csParser.parse( sentences );		
		doc.setPathologyID( pathologyID );
		doc.setAbbrPathologyID( pathologyID.substring(0, pathologyID.length() - 1) );
		if ( saveText == true )
			doc.setOriginalText( resultText );			
		doc.setPatientNumber( patientNumber );
		doc.setGender( gender );
    	doc.setAge( age );
    	doc.setAgeUnit( ageUnit );
    	doc.setSnomedLocation( snomedLocation );
    	doc.setSnomedTestName( snomedTestName );
    	doc.setSnomedDiagnosisName( snomedDiagnosisName );

		return doc;
	}
	
	public ArrayList<SurgeonDocumentForSKC> getResultText( ArrayList<Sheet> sheets ) {
		ArrayList<SurgeonDocumentForSKC> sdocs = new ArrayList<SurgeonDocumentForSKC>();
		for ( Sheet sheet : sheets ) {
			sdocs.addAll( getResultText ( sheet ) );
		}
		return sdocs;
	}
	
	public ArrayList<SurgeonDocumentForSKC> getResultText( Sheet sheet ) {
		ArrayList<SurgeonDocumentForSKC> sdocs = new ArrayList<SurgeonDocumentForSKC>();
		
	    int idx = 0;
	    HashMap<String, Integer> titles = new HashMap<String, Integer>();	    
	    Row title = sheet.getRow( idx );
	    	   
	    if ( titleParser ( title, titles ) == true )
	    	idx = 1;	    
	    
	    for ( ; idx < sheet.getLastRowNum(); idx++ ) {
	    	Row row = sheet.getRow( idx );	    	
	    	
	    	try {
	    		if ( row.getCell( titles.get("ReportID") ) == null )
	    			continue;
	    		
	    		String pathologyID = row.getCell( titles.get("ReportID") ).getStringCellValue();
	    		String typeOfDoc = pathologyID.substring(0,2).trim();	    		
	    		Cell textCell = row.getCell( titles.get("RESULTS") );
	    		if ( textCell == null )	{
	    			continue;
	    		}
	    		
	    		String resultText = textCell.getRichStringCellValue().toString();	    		
	    		if ( typeOfDoc.equals("S") ) {	// 외과병리보고서	    	
	    			SurgeonDocumentForSKC skc = new SurgeonDocumentForSKC();	    			
	    			skc.setPathologyID( pathologyID ); 
	    			skc.setResultText( resultText );
	    			sdocs.add( skc );
	    		}	  
	    	}	
	    	catch ( StringIndexOutOfBoundsException e ) {	
	    		e.printStackTrace();
	    	}
	    }	    
	    return sdocs;
	}
	
	private void excelparse( Sheet sheet, ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs, boolean saveOriginal ) {
		long start = System.nanoTime();
		System.out.println ( "Store all texts : " + saveOriginal );
				
		StringBuilder sb = new StringBuilder();
	    	    
	    int idx = 0;
	    HashMap<String, Integer> titles = new HashMap<String, Integer>();	    
	    Row title = sheet.getRow( idx );
	       	    
	    if ( titleParser ( title, titles ) == true )
	    	idx = 1;
	    
	    String pathologyID = null;
	    for ( ; idx <= sheet.getLastRowNum(); idx++ ) {
	    	Row row = sheet.getRow( idx );	    	
	    	
	    	try {
	    		if ( row.getCell( titles.get("ReportID") ) == null )
	    			continue;
	    		
	    		pathologyID = row.getCell( titles.get("ReportID") ).getStringCellValue();
	    		Cell patientNumberCell = row.getCell( titles.get( "PatientID") );
	    			    		
	    		String patientNumber = null;
	    		if ( patientNumberCell != null )
	    			patientNumber = patientNumberCell.getStringCellValue();				// PatientID
	    		
	    		Cell genderCell = row.getCell( titles.get("Sex") );
		    	String gender = null;
		    	if ( genderCell != null ) 
		    		gender = genderCell.getStringCellValue();					// Sex
		    			    	
		    	Cell ageCell = row.getCell( titles.get("age") );
		    	int age = -1;
		    	String ageUnit = "세";		// default
		    	
		    	if ( ageCell != null ) {
			    	if ( ageCell.getCellType() == Cell.CELL_TYPE_STRING ) {
			    		String ageStr = ageCell.getStringCellValue();
			    		int i = 0;
			    		while ( i < ageStr.length() ) {
			    			if ( Character.isDigit( ageStr.charAt( i ) ) == false ) 
			    				break;
			    			i++;
			    		}
			    		age = Integer.parseInt( ageStr.substring( 0, i ) );
			    		ageUnit = ageStr.substring( i + 1 );
			    	}
			    	else
			    		age = (int) ageCell.getNumericCellValue();
		    	}
		    			    	
		    	Cell snomedLocationCell = row.getCell( titles.get("SNOMED_CT_ORGAN"));
		    	String snomedLocation = null;
		    	if ( snomedLocationCell != null ) {
			    	if ( snomedLocationCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedLocation = String.valueOf(snomedLocationCell.getNumericCellValue());
			    	else 
			    		snomedLocation = snomedLocationCell.getStringCellValue();
		    	}
		    	
		    	Cell snomedTestNameCell = row.getCell( titles.get("SNOMED_CT_TEST") );
		    	String snomedTestName = null;
		    	if ( snomedTestNameCell != null ) {
			    	if ( snomedTestNameCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedTestName = String.valueOf(snomedTestNameCell.getNumericCellValue());
			    	else
			    		snomedTestName = snomedTestNameCell.getStringCellValue();		    	
		    	}
		    	
		    	Cell snomedDiagnosisNameCell = row.getCell( titles.get("SNOMED_CT_DIAGNOSIS") );
		    	String snomedDiagnosisName = null;
		    	if ( snomedDiagnosisNameCell != null ) {
			    	if ( snomedDiagnosisNameCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedDiagnosisName = String.valueOf(snomedDiagnosisNameCell.getNumericCellValue());
			    	else 
			    		snomedDiagnosisName = snomedDiagnosisNameCell.getStringCellValue();			    	
		    	}	
		    			    			    			    	
	    		String typeOfDoc = pathologyID.substring(0,2).trim();	
	    		
	    		Cell textCell = row.getCell( titles.get("RESULTS") );
	    		if ( textCell == null )	{
	    			continue;
	    		}
	    		
	    		String resultText = textCell.getRichStringCellValue().toString(); 
	    		String sentences[] = resultText.split("\n");
	    		
	    		if ( typeOfDoc.equals("PA") ) { 	// 면역화학검사보고서
	    			ImmunityDocument doc = fillImmunityDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );
	    			
	    			if ( idocs != null )
	    				idocs.add( doc );
	    		}
	    		else if ( typeOfDoc.equals("S") ) {	// 외과병리보고서
	    			SurgeonDocument doc = fillSurgeonDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );
	    			
	    			if ( sdocs != null )
	    				sdocs.add( doc );
	    		}
	    		else if ( typeOfDoc.equals("CS") ) {	// 어린이외과병리보고서	    			
	    			ChildSurgeonDocument doc = fillChildSurgeonDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );
	    			
	    			if ( csdocs != null )
	    				csdocs.add( doc );
	    		}
	    	}
	    	catch ( NumberFormatException e ) {
	    		e.printStackTrace();
	    		sb.append( pathologyID + "에서 문자열을 숫자로 변환함에 있어 Error가 발생...\n");
	    	}
	    	catch ( StringIndexOutOfBoundsException e ) {
	    		sb.append( pathologyID + "에서 부분문자열을 추출함에 있어 Error가 발생...\n");
	    	}
	    	catch ( IllegalStateException e ) {
	    		sb.append( pathologyID + "를 포함한 row에서 문자열을 가져올 수 없음.\n");
	    	}
	    }
	    
	    long end = System.nanoTime();
	    System.out.println ( sheet.getSheetName() + "\t" + "Parsing complete.");	    
	    System.out.println ( "Parsing time : " + (end - start) / Math.pow(10,9) + " second");
	    if ( sb.toString().trim().length() != 0 )
			System.err.println( "주어진 액셀 파일 분석시 발생한 에러... \n" + sb.toString() );
	}
	
	private void excelparse( Sheet sheet, 
							ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs, 
							String type, String testDoc, boolean saveOriginal ) {
		long start = System.nanoTime();
		System.out.println ( "Store all texts : " + saveOriginal );
				
		StringBuilder sb = new StringBuilder();
	    	    
	    int idx = 0;
	    HashMap<String, Integer> titles = new HashMap<String, Integer>();	    
	    Row title = sheet.getRow( idx );
	       	    
	    if ( titleParser ( title, titles ) == true )
	    	idx = 1;	    
	    
	    String pathologyID = null;
	    for ( ; idx <= sheet.getLastRowNum(); idx++ ) {
	    	Row row = sheet.getRow( idx );	    	
	    	
	    	try {
	    		if ( row.getCell( titles.get("ReportID") ) == null )
	    			continue;
	    		
	    		pathologyID = row.getCell( titles.get("ReportID") ).getStringCellValue();
    			if ( !pathologyID.equals( testDoc ) )
    				continue;
	    		
	    		Cell patientNumberCell = row.getCell( titles.get( "PatientID") );
	    		String patientNumber = null;
	    		if ( patientNumberCell != null )
	    			patientNumber = patientNumberCell.getStringCellValue();				// PatientID
	    		
	    		Cell genderCell = row.getCell( titles.get("Sex") );
		    	String gender = null;
		    	if ( genderCell != null ) 
		    		gender = genderCell.getStringCellValue();					// Sex
		    			    	
		    	Cell ageCell = row.getCell( titles.get("age") );
		    	int age = -1;
		    	String ageUnit = "세";		// default
		    	
		    	if ( ageCell != null ) {
			    	if ( ageCell.getCellType() == Cell.CELL_TYPE_STRING ) {
			    		String ageStr = ageCell.getStringCellValue();
			    		int i = 0;
			    		while ( i < ageStr.length() ) {
			    			if ( Character.isDigit( ageStr.charAt( i ) ) == false ) 
			    				break;
			    			i++;
			    		}
			    		age = Integer.parseInt( ageStr.substring( 0, i ) );
			    		ageUnit = ageStr.substring( i + 1 );
			    	}
			    	else
			    		age = (int) ageCell.getNumericCellValue();
		    	}
		    			    	
		    	Cell snomedLocationCell = row.getCell( titles.get("SNOMED_CT_ORGAN"));
		    	String snomedLocation = null;
		    	if ( snomedLocationCell != null ) {
			    	if ( snomedLocationCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedLocation = String.valueOf(snomedLocationCell.getNumericCellValue());
			    	else 
			    		snomedLocation = snomedLocationCell.getStringCellValue();
		    	}
		    	
		    	Cell snomedTestNameCell = row.getCell( titles.get("SNOMED_CT_TEST") );
		    	String snomedTestName = null;
		    	if ( snomedTestNameCell != null ) {
			    	if ( snomedTestNameCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedTestName = String.valueOf(snomedTestNameCell.getNumericCellValue());
			    	else
			    		snomedTestName = snomedTestNameCell.getStringCellValue();		    	
		    	}
		    	
		    	Cell snomedDiagnosisNameCell = row.getCell( titles.get("SNOMED_CT_DIAGNOSIS") );
		    	String snomedDiagnosisName = null;
		    	if ( snomedDiagnosisNameCell != null ) {
			    	if ( snomedDiagnosisNameCell.getCellType() == Cell.CELL_TYPE_NUMERIC )
			    		snomedDiagnosisName = String.valueOf(snomedDiagnosisNameCell.getNumericCellValue());
			    	else 
			    		snomedDiagnosisName = snomedDiagnosisNameCell.getStringCellValue();			    	
		    	}	
		    			    			    			    	
	    		String typeOfDoc = pathologyID.substring(0,2).trim();	
	    		
	    		Cell textCell = row.getCell( titles.get("RESULTS") );
	    		if ( textCell == null )	{
	    			continue;
	    		}
	    		
	    		String resultText = textCell.getRichStringCellValue().toString();
	    		String sentences[] = resultText.split("\n");
	    		
	    		if ( type.equals("PA") && typeOfDoc.equals("PA") ) { 	// IHC reports
	    			ImmunityDocument doc = fillImmunityDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );
	    			idocs.add( doc );
	    		}	    		
	    		else if ( type.equals("S") && typeOfDoc.equals("S") ) {	// SP reports
	    			SurgeonDocument doc = fillSurgeonDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );	    				    				    		
	    			sdocs.add( doc );
	    		}
	    		else if ( type.equals("CS") && typeOfDoc.equals("CS") ) {	// Chile SP reports
	    			ChildSurgeonDocument doc = fillChildSurgeonDocument ( typeOfDoc, sentences, 
	    					pathologyID, resultText, patientNumber, gender, age, ageUnit,
	    					snomedLocation, snomedTestName, snomedDiagnosisName );
	    			csdocs.add( doc );
	    		}
	    	}
	    	catch ( NumberFormatException e ) {
	    		sb.append( pathologyID + ": errors from converting string to integer..\n");
	    	}
	    	catch ( StringIndexOutOfBoundsException e ) {
	    		sb.append( pathologyID + ": errors from extracting substring...\n");
	    	}
	    	catch ( IllegalStateException e ) {
	    		sb.append( pathologyID + ": errors from reading text\n");
	    	}
	    }
	    
	    long end = System.nanoTime();
	    System.out.println ( sheet.getSheetName() + "\t" + "Parsing complete.");	    
	    System.out.println ( "Parsing time : " + (end - start) / Math.pow(10,9) + " second");
	    if ( sb.toString().trim().length() != 0 )
			System.err.println( "Errors in excel file... \n" + sb.toString() );
	}
	
		
	private String endRemover( String contents ) {
		if ( contents.endsWith("<<end>>") ) 
			return contents.substring(0, contents.length() - "<<end>>".length() );
		return contents;
	}
	
	public void txtSNUparse( String content, ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs ) {
		txtSNUparse( content, idocs, sdocs, null );
	}
	
	public void txtSNUparse( String content, ArrayList<ImmunityDocument> idocs, ArrayList<SurgeonDocument> sdocs, ArrayList<ChildSurgeonDocument> csdocs ) {
		long start = System.nanoTime();
	    
	    String lines[] = content.split("\n");
	    StringBuilder sb = new StringBuilder();
	    
	    String typeOfDoc = null;	    
	    String pathologyID = null;
	    String patientNumber = null;
	    String gender = null;    	
    	String ageStr = null;
    	int age = 0;
    	String snomedLocation = null;
    	String snomedTestName = null;
    	String snomedDiagnosisName = null;
	    
    	String ageUnit = "세";				// default
    	boolean flag = false;
	    for ( String line : lines ) {
	    	if ( flag == true ) {
	    		sb.append( line + "\n" );
	    		continue;
	    	}
	    	
    		if ( line.startsWith( SNUIndicators[0] ) ) {
    			pathologyID = line.substring( SNUIndicators[0].length() );
    			pathologyID = endRemover ( pathologyID );
    		}
    		else if ( line.startsWith( SNUIndicators[1] ) ) {
    			patientNumber = line.substring( SNUIndicators[1].length() );
    			patientNumber = endRemover ( patientNumber );
    		}
    		else if ( line.startsWith( SNUIndicators[2] ) ) {
    			gender = line.substring( SNUIndicators[2].length() );
    			gender = endRemover ( gender );
    		}
    		else if ( line.startsWith( SNUIndicators[3] ) ) {
    			ageStr = line.substring( SNUIndicators[3].length() );
    			ageStr = endRemover ( ageStr );
    		}
    		else if ( line.startsWith( SNUIndicators[4] ) ) {
    			snomedLocation = line.substring( SNUIndicators[4].length() );
    			snomedLocation = endRemover ( snomedLocation );
    		}
    		else if ( line.startsWith( SNUIndicators[5] ) ) {
    			snomedTestName = line.substring( SNUIndicators[5].length() );
    			snomedTestName = endRemover ( snomedTestName );
    		}
    		else if ( line.startsWith( SNUIndicators[6] ) ) {
    			snomedDiagnosisName = line.substring( SNUIndicators[6].length() );
    			snomedDiagnosisName = endRemover ( snomedDiagnosisName );
    		}
    		else if ( line.startsWith( SNUIndicators[7] ) ) {
    			flag = true;
    		}	
	    }
	    
	    int i = 0;
		while ( i < ageStr.length() ) {
			if ( Character.isDigit( ageStr.charAt( i ) ) == false ) 
				break;
			i++;
		}
		age = Integer.parseInt( ageStr.substring( 0, i ) );
		typeOfDoc = pathologyID.substring(0,2).trim();
		String resultText = sb.toString();
		
	    String sentences[] = resultText.split("\n");
	    if ( typeOfDoc.equals("PA") ) { 	// IHC reports
			ImmunityDocument doc = fillImmunityDocument ( typeOfDoc, sentences, 
					pathologyID, resultText, patientNumber, gender, age, ageUnit,
					snomedLocation, snomedTestName, snomedDiagnosisName );
			idocs.add( doc );
		}
		else if ( typeOfDoc.equals("S") ) {	// SP reports
			SurgeonDocument doc = fillSurgeonDocument ( typeOfDoc, sentences, 
					pathologyID, resultText, patientNumber, gender, age, ageUnit,
					snomedLocation, snomedTestName, snomedDiagnosisName );
			sdocs.add( doc );
		}
		else if ( typeOfDoc.equals("CS") ) {	// Child SP reports
			ChildSurgeonDocument doc = fillChildSurgeonDocument ( typeOfDoc, sentences, 
					pathologyID, resultText, patientNumber, gender, age, ageUnit,
					snomedLocation, snomedTestName, snomedDiagnosisName );
			if ( csdocs != null )
				csdocs.add( doc );
		}
	    
	    long end = System.nanoTime();
	    System.out.println ( "Parsing complete.");	    
	    System.out.println ( "Parsing time : " + (end - start) / Math.pow(10,9) + " second");
	}	
}
