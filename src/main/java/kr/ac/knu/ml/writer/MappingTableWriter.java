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

import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.parser.immunity.SlideTissue;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MappingTableWriter {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
		
	private final String titles[] = {
			"면역화학 보고서번호", "병리학적 진단번호", "추출된 슬라이드 key 번호", "조직 슬라이드 추가 설명", "최종 외과병리보고서번호", "매핑에 쓰인 병리학적 진단번호", "환자번호"
	};	
	
	private HashMap<String, Integer> titleIdx;
	
	public MappingTableWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("PA_S_map");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
	}
	
	public void writeTable( String fileName, ArrayList<ImmunityDocument> idocs ) throws IOException {
		writeTitle();
		write( idocs );
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
		
		// Set Column Widths
		sheet.setColumnWidth(0, 5500); 
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 7000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 5000);
	}
	
	private void writeRow( Row row, String pathologyID, String diagnosisName, String keyNum, String slideInfo, String paNum, String candidateSurgeonDocID, String patientNumber ) {
		row.createCell( titleIdx.get("면역화학 보고서번호") ).setCellValue( pathologyID );
		row.createCell( titleIdx.get("병리학적 진단번호") ).setCellValue( diagnosisName );
		row.createCell( titleIdx.get("추출된 슬라이드 key 번호") ).setCellValue( keyNum );
		row.createCell( titleIdx.get("조직 슬라이드 추가 설명") ).setCellValue( slideInfo );		
		row.createCell( titleIdx.get("최종 외과병리보고서번호") ).setCellValue( candidateSurgeonDocID );
		row.createCell( titleIdx.get("매핑에 쓰인 병리학적 진단번호") ).setCellValue( paNum );
		row.createCell( titleIdx.get("환자번호") ).setCellValue( patientNumber );	
	}
	
	private void write( ArrayList<ImmunityDocument> idocs ) {
		int count = 1;
		for ( ImmunityDocument idoc : idocs ) {
			if ( count % 1000 == 0 )
				System.out.print(".");
			
			for ( PADiagnosis pad : idoc.getDiagnosises() ) {
				String pathologyNum = pad.getPathologyNum();
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;

				StringBuilder num = new StringBuilder();
				StringBuilder id = new StringBuilder();
				StringBuilder info = new StringBuilder();
				
				ArrayList<SlideTissue> tissues = pad.getSlides();
				if ( tissues.size() > 0 ) {
					num.append( tissues.get(0).getSlideTissueName() );
					id.append( tissues.get(0).getSlideTissueKeyID() );
					if ( tissues.get(0).getSlideTissueInformation() != null )
						info.append( tissues.get(0).getSlideTissueInformation() );
					
					for ( int i = 1; i < tissues.size(); i++ )  {
						num.append( "," + tissues.get(i).getSlideTissueName() );
						id.append( "," + tissues.get(i).getSlideTissueKeyID() );
						if ( tissues.get(i).getSlideTissueInformation() != null )
							info.append( "," + tissues.get(i).getSlideTissueInformation() );
					}
				}
				
				String pathologyID = idoc.getPathologyID();
				String diagnosisName = pad.getDiagnosisName();
				String keyNum = num.toString();
				String slideInfo = info.toString();				
				String patientNumber = idoc.getPatientNumber();
				
				ArrayList<SurgeonDocument> sdocs = pad.getSurgeonDocuments();
				ArrayList<ChildSurgeonDocument> csdocs = pad.getChildSurgeonDocuments();
				if ( sdocs.size() == 0 && csdocs.size() == 0 ) {					
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );
									
					String candidateSurgeonDocID = "NotFound"; 				
					String paNum = pad.getPathologyNum() + "," + pad.getRevisedPathologyNum() + "X";
					writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, paNum, candidateSurgeonDocID, patientNumber );
					
					rowIdx++;	
				}
				else {
					for ( SurgeonDocument sd : sdocs ) {				
						Row row = sheet.getRow( rowIdx );
						if ( row == null )
							row = sheet.createRow( rowIdx );
						
						String candidateSurgeonDocID = sd.getPathologyID(); 
						writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, "", candidateSurgeonDocID, patientNumber );											
						
						rowIdx++;						
					}
					
					for ( ChildSurgeonDocument csd : csdocs ) {				
						Row row = sheet.getRow( rowIdx );
						if ( row == null )
							row = sheet.createRow( rowIdx );
						
						String candidateSurgeonDocID = csd.getPathologyID(); 
						writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, "", candidateSurgeonDocID, patientNumber );											
						
						rowIdx++;						
					}
				}
			}
			
			for ( MorphometricAnalysis ma : idoc.getMorphometricAnalysis() ) {
				String pathologyNum = ma.getPathologyNum();
				if ( pathologyNum == null || pathologyNum.length() == 0 )
					continue;

				StringBuilder num = new StringBuilder();
				StringBuilder id = new StringBuilder();
				StringBuilder info = new StringBuilder();
				
				ArrayList<SlideTissue> tissues = ma.getSlides();
				if ( tissues.size() > 0 ) {
					num.append( tissues.get(0).getSlideTissueName() );
					id.append( tissues.get(0).getSlideTissueKeyID() );
					if ( tissues.get(0).getSlideTissueInformation() != null )
						info.append( tissues.get(0).getSlideTissueInformation() );
					
					for ( int i = 1; i < tissues.size(); i++ )  {
						num.append( "," + tissues.get(i).getSlideTissueName() );
						id.append( "," + tissues.get(i).getSlideTissueKeyID() );
						if ( tissues.get(i).getSlideTissueInformation() != null )
							info.append( "," + tissues.get(i).getSlideTissueInformation() );
					}				
				}
				
				String pathologyID = idoc.getPathologyID();
				String diagnosisName = ma.getDiagnosisName();
				String keyNum = num.toString();
				String slideInfo = info.toString();				
				String patientNumber = idoc.getPatientNumber();
				
				ArrayList<SurgeonDocument> sdocs = ma.getCandidateSurgeonDocuments();
				ArrayList<ChildSurgeonDocument> csdocs = ma.getChildSurgeonDocuments();
				if ( sdocs.size() == 0 && csdocs.size() == 0 ) {					
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );

					String candidateSurgeonDocID = "NotFound";
					String maNum = ma.getPathologyNum() + "," + ma.getRevisedPathologyNum() + "X";
					writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, maNum, candidateSurgeonDocID, patientNumber );
					
					rowIdx++;
				}
				else {
					for ( SurgeonDocument sd : sdocs ) {				
						Row row = sheet.getRow( rowIdx );
						if ( row == null )
							row = sheet.createRow( rowIdx );
						
						String candidateSurgeonDocID = sd.getPathologyID(); 
						writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, "", candidateSurgeonDocID, patientNumber );											
						
						rowIdx++;						
					}
					
					for ( ChildSurgeonDocument csd : csdocs ) {				
						Row row = sheet.getRow( rowIdx );
						if ( row == null )
							row = sheet.createRow( rowIdx );
						
						String candidateSurgeonDocID = csd.getPathologyID(); 
						writeRow ( row, pathologyID, diagnosisName, keyNum, slideInfo, "", candidateSurgeonDocID, patientNumber );											
						
						rowIdx++;
					}
				}
			}
		}
		System.out.println();
	}	
}
