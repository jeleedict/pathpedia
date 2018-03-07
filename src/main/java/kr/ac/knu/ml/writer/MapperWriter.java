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
import java.util.HashSet;

import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapperWriter {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
		
	private final String titles[] = {
			"번호", "환자번호",  "성별", "나이", "외과병리보고서 병리번호", "스노메드 채취부위", "스노메드 검사명", "스노메드 진단명", "외과병리보고서 검사결과 내용", "면역화학보고서 병리번호", "병리학적 진단코드", "면역화확보고서 검사결과 내용"
	};	
	
	private HashMap<String, Integer> titleIdx;
	
	public MapperWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("면역병리보고서-외과병리보고서");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
	}
	
	public void writeImmunityDocument( String fileName, ArrayList<ImmunityDocument> idocs ) throws IOException {
		writeTitle();
		writeImmunityDocument( idocs );
		FileOutputStream fileOut = new FileOutputStream( fileName );
		wb.write(fileOut);
		fileOut.close();
	}
	
	public void writeSurgeonDocument( String fileName, ArrayList<SurgeonDocument> sdocs ) throws IOException {
		writeTitle();
		writeSurgeonDocument( sdocs );
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
	
	private void writeSurgeonDocument( ArrayList<SurgeonDocument> sdocs ) {
		int count = 1;
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count % 1000 == 0 )
				System.out.print(".");
			
			Row row = sheet.getRow( rowIdx );
			if ( row == null )
				row = sheet.createRow( rowIdx );			
			
			row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
			row.createCell( titleIdx.get("환자번호") ).setCellValue( sdoc.getPatientNumber() );
			row.createCell( titleIdx.get("성별") ).setCellValue( sdoc.getGender() );
			row.createCell( titleIdx.get("나이") ).setCellValue( sdoc.getAge() );			
			row.createCell( titleIdx.get("외과병리보고서 병리번호") ).setCellValue( sdoc.getPathologyID() );					
			row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( sdoc.getSnomedLocation() );
			row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( sdoc.getSnomedTestName() );
			row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( sdoc.getSnomedDiagnosisName() );
			row.createCell( titleIdx.get("외과병리보고서 검사결과 내용") ).setCellValue( sdoc.getOriginalText() );			
			
			PADiagnosis pa = sdoc.getMappedPADiagnosis();
			if ( pa != null ) {
				
			}
			
			MorphometricAnalysis ma = sdoc.getMappedMorphometricAnalysis();
			if ( pa != null ) {
				
			}
		}
	}
	
	private void writeImmunityDocument( ArrayList<ImmunityDocument> idocs ) {
		int count = 1;
		for ( ImmunityDocument idoc : idocs ) {
			if ( count % 1000 == 0 )
				System.out.print(".");
			
			String pathologyID = idoc.getPathologyID();
			String originalText = idoc.getOriginalText();
			for ( PADiagnosis pad : idoc.getDiagnosises() ) {
				String pathologyNum = pad.getPathologyNum();
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;
				
//				ArrayList<SurgeonDocument> sdocs = pad.getCandidateSurgeonDocuments();
				ArrayList<SurgeonDocument> sdocs = pad.getSurgeonDocuments();
				for ( SurgeonDocument sd : sdocs ) {
//					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
//						continue;
//					
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );

					row.createCell( titleIdx.get("번호") ).setCellValue( count++ );

					row.createCell( titleIdx.get("환자번호") ).setCellValue( idoc.getPatientNumber() );
					row.createCell( titleIdx.get("성별") ).setCellValue( idoc.getGender() );
					row.createCell( titleIdx.get("나이") ).setCellValue( idoc.getAge() );

					row.createCell( titleIdx.get("면역화학보고서 병리번호") ).setCellValue( pathologyID );
					row.createCell( titleIdx.get("병리학적 진단코드") ).setCellValue( pad.getDiagnosisName() );
					row.createCell( titleIdx.get("면역화확보고서 검사결과 내용") ).setCellValue( originalText );
					
					row.createCell( titleIdx.get("외과병리보고서 병리번호") ).setCellValue( sd.getPathologyID() );					
					row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( sd.getSnomedLocation() );
					row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( sd.getSnomedTestName() );
					row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( sd.getSnomedDiagnosisName() );
					
					row.createCell( titleIdx.get("외과병리보고서 검사결과 내용") ).setCellValue( sd.getOriginalText() );
					rowIdx++;
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				String pathologyNum = ma.getPathologyNum();
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;

				ArrayList<SurgeonDocument> sdocs = ma.getCandidateSurgeonDocuments();
				for ( SurgeonDocument sd : sdocs ) {
					if ( !sd.getPatientNumber().equals( idoc.getPatientNumber() ) )
						continue;
					
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );

					row.createCell( titleIdx.get("번호") ).setCellValue( count++ );

					row.createCell( titleIdx.get("환자번호") ).setCellValue( idoc.getPatientNumber() );
					row.createCell( titleIdx.get("성별") ).setCellValue( idoc.getGender() );
					row.createCell( titleIdx.get("나이") ).setCellValue( idoc.getAge() );

					row.createCell( titleIdx.get("면역화학보고서 병리번호") ).setCellValue( pathologyID );
					row.createCell( titleIdx.get("병리학적 진단코드") ).setCellValue( ma.getDiagnosisName() );
					row.createCell( titleIdx.get("면역화확보고서 검사결과 내용") ).setCellValue( originalText );
					
					row.createCell( titleIdx.get("외과병리보고서 병리번호") ).setCellValue( sd.getPathologyID() );					
					row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( sd.getSnomedLocation() );
					row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( sd.getSnomedTestName() );
					row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( sd.getSnomedDiagnosisName() );
					
					row.createCell( titleIdx.get("외과병리보고서 검사결과 내용") ).setCellValue( sd.getOriginalText() );
					rowIdx++;
				}
			}
		}
		System.out.println();
	}	
}

