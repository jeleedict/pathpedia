/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.ml.document.SurgeonDocument;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SurgeonDocumentWriter {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
		
	private final String titles[] = {
			"병리번호", "환자번호", "성별", "검사당시나이", "스노메드 채취부위", "스노메드 검사명",  "스노메드 진단명", "검사 결과 TEXT"
	};	
	
	private HashMap<String, Integer> titleIdx;
	
	public SurgeonDocumentWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("SurgeonDocument");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
	}
	
	public void write( String fileName, ArrayList<SurgeonDocument> sdocs ) throws IOException {
		writeTitle();
		write( sdocs );
		FileOutputStream fileOut = new FileOutputStream( fileName );
		wb.write(fileOut);
		fileOut.close();
	}
	
	private void writeTitle() {
		Row row = sheet.getRow( rowIdx );
		if ( row == null )
			row = sheet.createRow( rowIdx );		
		rowIdx++;				
		
		for ( int i = 0; i < titles.length; i++ ) {			
			row.createCell(i).setCellValue( titles[i] );			
			titleIdx.put( titles[i], i );			
		}
	}
	
	private void write( ArrayList<SurgeonDocument> sdocs ) {
		int count = 1;
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count++ % 1000 == 0 )
				System.out.print(".");
			
			if ( sdoc.getMappedPADiagnosis() == null && sdoc.getMappedMorphometricAnalysis() == null ) {
				Row row = sheet.getRow( rowIdx );
				if ( row == null )
					row = sheet.createRow( rowIdx );
				
				row.createCell( titleIdx.get("병리번호") ).setCellValue( sdoc.getPathologyID() );
				
				row.createCell( titleIdx.get("환자번호") ).setCellValue( sdoc.getPatientNumber() );						
				row.createCell( titleIdx.get("성별") ).setCellValue( sdoc.getGender() );
				row.createCell( titleIdx.get("검사당시나이") ).setCellValue( sdoc.getAge() );
				row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( sdoc.getSnomedLocation() );
				row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( sdoc.getSnomedTestName() );	
				row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( sdoc.getSnomedDiagnosisName() );
				row.createCell( titleIdx.get("검사 결과 TEXT") ).setCellValue( sdoc.getOriginalText() );
				rowIdx++;
			}
		}
		System.out.println();
	}	
}
