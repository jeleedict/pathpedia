/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.exec.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.ac.knu.ml.parser.BioMedicalDocumentParser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SlideKeyClassifier {
	public Sheet load( String inputFileName ) {
		long start = System.nanoTime();
		try {
			System.out.println ( "Loading from '" + inputFileName + "'");
			InputStream inp = new FileInputStream( inputFileName );
			Workbook wb = WorkbookFactory.create(inp);
			System.out.println ( "'" + inputFileName + "' loaded.");
			long end = System.nanoTime();
			System.out.println ( "Loading time : " + (end - start) / Math.pow(10,9) + " second");
			return wb.getSheetAt( 0 );
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void info( Sheet sheet ) {
		System.out.println ( "Name of Sheet : " + sheet.getSheetName() );
		System.out.println ( "# of Rows : " + sheet.getLastRowNum() );
	}

	private void extract( String inputFileName ) throws FileNotFoundException, InvalidFormatException, IOException {
		extract ( new File[]{ new File(inputFileName) } );
	}
	
	private void extract ( File inputFiles[] ) throws FileNotFoundException, InvalidFormatException, IOException {		
		ArrayList<Sheet> sheets = new ArrayList<Sheet>();
		for ( File f : inputFiles ) {
			Sheet sheet = load ( f.toString() );
			if ( sheet == null ) {
				System.err.println( f.toString() );
				continue;
			}
			
			info ( sheet );
			sheets.add( sheet );
		}
	    
	    BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
	    ArrayList<SurgeonDocumentForSKC> sdocs = bmdp.getResultText ( sheets );
	    
	    // To it yourself using SurgeonDocument
	    for ( SurgeonDocumentForSKC sdoc : sdocs ) {
	    	System.out.println( sdoc.getPathologyID() );
	    }
	}	
	
	public static void main(String[] args) throws FileNotFoundException, InvalidFormatException, IOException {		
		SimpleDateFormat formatter = new SimpleDateFormat ( "YYMMdd" );		
		String inputFileName = "input/11/S11_withoutRedun.xls";				
		SlideKeyClassifier skc = new SlideKeyClassifier();		
		
		try {
			File inputFile = new File ( inputFileName );
			if ( inputFile.isDirectory() ) {
				File[] files = inputFile.listFiles();
				skc.extract ( files );
			}
			else {
				skc.extract( inputFileName );
			}
		}
		catch ( FileNotFoundException fnfe ) {
			fnfe.printStackTrace();
		}
		catch ( InvalidFormatException ife ) {
			ife.printStackTrace();
		}
		catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
	public static void printLog() {
		System.err.println ("Usage (SReportExtractor): ");
		System.err.println ("\t\tjava -Xmx16G -jar SReportExtractor.jar inputFileName|inputFileFolder outputFileName thesaurusFile" );
		System.err.println ("Example : data.xlsx output.xlsx Pathinfo_DB_ver1.3.xlsx" );
		System.err.println ("Example : 07-11?�료  markerList.xlsx Pathinfo_DB_ver1.3.xlsx" );		
		System.exit( 1 );
	}
}
