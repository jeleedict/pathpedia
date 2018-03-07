/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.writer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.unit.surgeon.Disease;
import kr.ac.knu.ml.unit.surgeon.MicroValue;
import kr.ac.knu.ml.unit.surgeon.Organ;
import kr.ac.knu.ml.unit.surgeon.SDiagnosis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CSReportWriter {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
	private String type;

	private final String titles[] = {
			"번호", "조직슬라이드 Key ID", "외과병리보고서 ID", "추출된 슬라이드 Key 번호", 
			"조직 슬라이드 이름", "조직 슬라이드 추가설명", "조직번호", "조직 개수 총합", "현미경 설명 추출 내용", "현미경에 따른 조직개수",  
			"분석에 사용된 장기명 문구", "추출된 장기명", "맵핑된 장기명", "맵핑된 장기명 ID", "장기명 시소로스와 Exact matching 유뮤", "장기명 시소로스와의 유사도", 
			"분석에 사용된 진단명 문구", "추출된 진단명", "맵핑된 진단명", "매핑된 진단명 ID", "진단명 시소로스와 Exact matching 유무", "진단명 시소로스와의 유사도",
			"Consistent With 유무", "Negation 유무", "Metastatic 유무", "Most-likely 유무", "Involvement 유무", "Suspicious 유무", "Favor 유무", 
			"histologic type", "gross type", "location of tumor", "size of tumor",	"depth of invasion", "lymph node metastasis", "DCIS", "other findings", "장기명 개수", 
			"환자번호", "성별", "나이", "스노메드 채취부위", "스노메드 검사명", "스노메드 진단명", "에러"
	};	

	private HashMap<String, Integer> titleIdx;

	public CSReportWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("외과병리보고서");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
		type = "excel";
	}

	public CSReportWriter( String type ) {
		this.type = type;
	}

	public void write( String fileName, ArrayList<ChildSurgeonDocument> csdocs ) throws IOException {		
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("외과병리보고서");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();

		writeTitle();
		write( csdocs );
		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream ( fileName ), 8 * 1024 );
		wb.write(bos);
		bos.close();
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
	
	public Element treeTraverse( ArrayList<MicroValue> mvs, MicroValue mv, Document dom, int depth ) {
		Element MICROVALUE = dom.createElement("MICROVALUE");
		MICROVALUE.appendChild( dom.createTextNode( mv.getStr() ));
		
		ArrayList<Integer> children = mv.getChildIndices();
		if ( children == null )
			return MICROVALUE;
		
		for ( int i : children ) {
			Element tmp = treeTraverse ( mvs, mvs.get(i), dom, depth + 1 );
			MICROVALUE.appendChild( tmp );
		}
		return MICROVALUE;
	}		

	private void write( ArrayList<ChildSurgeonDocument> csdocs ) {
		int count = 1;
		for ( ChildSurgeonDocument csdoc : csdocs ) {
			if ( count % 1000 == 0 )
				System.out.print(".");					

			if ( csdoc.getDiagnosies() == null ) {
				Row row = sheet.getRow( rowIdx );
				if ( row == null )
					row = sheet.createRow( rowIdx );

				row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
				row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( csdoc.getPathologyID() );

				// Slide
				writeSlideInfo ( row );				
				// MICRO
				writeMICROInfo ( row, csdoc );
				// OrganInfo
				writeOrganInfo ( row, null );
				writeDiseaseInfo ( row, null );
				// Info.
				writeFields ( row, null );																				
				// Patient info
				writePatientInfo ( row, csdoc );							
				// SNOMED info
				writeSNOMEDInfo ( row, csdoc );
				// Error info
				writeError ( row, csdoc.getErrorStr() );
				//				writeError( row, sdoc );
				rowIdx++;			

				continue;
			}

			for ( SDiagnosis sdiagnosis : csdoc.getDiagnosies() ) {
				if ( sdiagnosis.getDiseases() == null ) {
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );

					row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
					row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( csdoc.getPathologyID() );

					// Slide
					writeSlideInfo ( row );				
					// MICRO
					writeMICROInfo ( row, csdoc );
					// OrganInfo
					writeOrganInfo ( row, sdiagnosis );
					writeDiseaseInfo ( row, null );
					// Info.
					writeFields ( row, sdiagnosis );																							
					// Patient info
					writePatientInfo ( row, csdoc );							
					// SNOMED info
					writeSNOMEDInfo ( row, csdoc );					
					// Error info
					writeError( row, sdiagnosis.getErrorStr() );
					//					writeError( row, sdoc );
					rowIdx++;
				}
				else {
					ArrayList<Disease> diseases = sdiagnosis.getDiseases();
					for ( Disease disease : diseases ) {
						Row row = sheet.getRow( rowIdx );
						if ( row == null )
							row = sheet.createRow( rowIdx );

						row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
						row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( csdoc.getPathologyID() );

						// Slide
						writeSlideInfo ( row );				
						// MICRO
						writeMICROInfo ( row, csdoc );
						// OrganInfo
						writeOrganInfo ( row, sdiagnosis );
						writeDiseaseInfo ( row, disease );
						// Info.
						writeFields ( row, sdiagnosis );																							
						// Patient info
						writePatientInfo ( row, csdoc );							
						// SNOMED info
						writeSNOMEDInfo ( row, csdoc );
						// Error info
						writeError ( row, disease );				
						//						writeError( row, sdoc );
						rowIdx++;
					}
				}										
			}			
		}
		System.out.println();
	}	

	private void writeFields( Row row, SDiagnosis sdiagnosis ) {
		if ( sdiagnosis == null ) {
			row.createCell( titleIdx.get("histologic type") ).setCellValue( "" );
			row.createCell( titleIdx.get("gross type") ).setCellValue( "" );				
			row.createCell( titleIdx.get("location of tumor") ).setCellValue( "" );
			row.createCell( titleIdx.get("size of tumor") ).setCellValue( "" );
			row.createCell( titleIdx.get("depth of invasion") ).setCellValue( "" );
			row.createCell( titleIdx.get("lymph node metastasis") ).setCellValue( "" );
			row.createCell( titleIdx.get("DCIS") ).setCellValue( "" );
		}
		else {
			row.createCell( titleIdx.get("histologic type") ).setCellValue( sdiagnosis.getHistoricalType() );
			row.createCell( titleIdx.get("gross type") ).setCellValue( sdiagnosis.getGrossType() );				
			row.createCell( titleIdx.get("location of tumor") ).setCellValue( sdiagnosis.getLocation() );
			row.createCell( titleIdx.get("size of tumor") ).setCellValue( sdiagnosis.getSize() );
			row.createCell( titleIdx.get("depth of invasion") ).setCellValue( sdiagnosis.getDepthOfInvasion() );
			row.createCell( titleIdx.get("lymph node metastasis") ).setCellValue( sdiagnosis.getLymphNode() );
			row.createCell( titleIdx.get("DCIS") ).setCellValue( sdiagnosis.getDCIS() );
		}
	}

	private void writeSlideInfo( Row row ) {
		row.createCell( titleIdx.get("조직슬라이드 Key ID") ).setCellValue( "" );		
		row.createCell( titleIdx.get("추출된 슬라이드 Key 번호") ).setCellValue( "" );
		row.createCell( titleIdx.get("조직 슬라이드 이름") ).setCellValue( "" );
		row.createCell( titleIdx.get("조직 슬라이드 추가설명") ).setCellValue( "" );
		row.createCell( titleIdx.get("조직번호") ).setCellValue( "" );
		row.createCell( titleIdx.get("조직 개수 총합") ).setCellValue( "" );
	}

	private void writeMICROInfo( Row row, ChildSurgeonDocument csdoc ) {
		ArrayList<String> micros = csdoc.getMicros();		
		StringBuilder sb = new StringBuilder();
		for ( String micro : micros ) 
			sb.append( micro + "," );

		String micro = null;
		if ( sb.toString().length() > 1 ) 		
			micro = sb.toString().substring(0, sb.toString().length() - 1 );	

		row.createCell( titleIdx.get("현미경 설명 추출 내용") ).setCellValue( micro );
		row.createCell( titleIdx.get("현미경에 따른 조직개수") ).setCellValue( csdoc.getNumOfSlide() );
	}

	private void writeOrganInfo( Row row, SDiagnosis sdiagnosis ) {
		if ( sdiagnosis == null ) {
			row.createCell( titleIdx.get("추출된 장기명") ).setCellValue( "#N/A" );
			row.createCell( titleIdx.get("맵핑된 장기명") ).setCellValue( "#N/A" );
			row.createCell( titleIdx.get("맵핑된 장기명 ID") ).setCellValue( "#N/A" );			
			row.createCell( titleIdx.get("장기명 시소로스와 Exact matching 유뮤") ).setCellValue( "X" );
			row.createCell( titleIdx.get("장기명 시소로스와의 유사도") ).setCellValue( 0.0 );
		}
		else {
			Organ organ = sdiagnosis.getOrgan(); 
			if ( organ != null ) {
				row.createCell( titleIdx.get("추출된 장기명") ).setCellValue( organ.getCandidateOrganName() );
				row.createCell( titleIdx.get("맵핑된 장기명") ).setCellValue( organ.getOrganName() );

				if ( organ.getSNOMEDorganSui() == null ) 
					row.createCell( titleIdx.get("맵핑된 장기명 ID") ).setCellValue( "#N/A" );
				else 
					row.createCell( titleIdx.get("맵핑된 장기명 ID") ).setCellValue( organ.getSNOMEDorganSui() );

				double similarity = organ.getOrganSimilarity();
				if ( similarity == 1.0 )
					row.createCell( titleIdx.get("장기명 시소로스와 Exact matching 유뮤") ).setCellValue( "O" );
				else
					row.createCell( titleIdx.get("장기명 시소로스와 Exact matching 유뮤") ).setCellValue( "X" );
				row.createCell( titleIdx.get("장기명 시소로스와의 유사도") ).setCellValue( similarity );
			}
			else {
				row.createCell( titleIdx.get("추출된 장기명") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("맵핑된 장기명") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("맵핑된 장기명 ID") ).setCellValue( "#N/A" );			
				row.createCell( titleIdx.get("장기명 시소로스와 Exact matching 유뮤") ).setCellValue( "X" );
				row.createCell( titleIdx.get("장기명 시소로스와의 유사도") ).setCellValue( 0.0 );
			}
		}		
	}

	private void writeDiseaseInfo( Row row, Disease disease ) {
		if ( disease == null ) {
			row.createCell( titleIdx.get("추출된 진단명") ).setCellValue( "#N/A" );
			row.createCell( titleIdx.get("맵핑된 진단명") ).setCellValue( "#N/A" );
			row.createCell( titleIdx.get("매핑된 진단명 ID") ).setCellValue( "#N/A" );
			row.createCell( titleIdx.get("진단명 시소로스와 Exact matching 유무") ).setCellValue( "X" );			
			row.createCell( titleIdx.get("진단명 시소로스와의 유사도") ).setCellValue( 0.0 );
			row.createCell( titleIdx.get("Consistent With 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Negation 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Metastatic 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Most-likely 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Involvement 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Suspicious 유무") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Favor 유무") ).setCellValue( "X" );
		}
		else {
			if ( disease.getCandidateDiseaseName() == null  ) 
				row.createCell( titleIdx.get("추출된 진단명") ).setCellValue( "#N/A" );
			else
				row.createCell( titleIdx.get("추출된 진단명") ).setCellValue( disease.getCandidateDiseaseName() );

			if ( disease.getDiseaseName() == null  )
				row.createCell( titleIdx.get("맵핑된 진단명") ).setCellValue( "#N/A" );
			else				
				row.createCell( titleIdx.get("맵핑된 진단명") ).setCellValue( disease.getDiseaseName() );			

			if ( disease.getUMLSdiseaseSui() == null )
				row.createCell( titleIdx.get("매핑된 진단명 ID") ).setCellValue( "#N/A" );
			else
				row.createCell( titleIdx.get("매핑된 진단명 ID") ).setCellValue( disease.getUMLSdiseaseSui() );

			double similarity = disease.getDiseaseSimilarity();
			if ( similarity == 1.0 )
				row.createCell( titleIdx.get("진단명 시소로스와 Exact matching 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("진단명 시소로스와 Exact matching 유무") ).setCellValue( "X" );

			row.createCell( titleIdx.get("진단명 시소로스와의 유사도") ).setCellValue( similarity );
			if ( disease.isContainConsistentWith() == true ) 
				row.createCell( titleIdx.get("Consistent With 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Consistent With 유무") ).setCellValue( "X" );

			if ( disease.isContainNegation() == true )
				row.createCell( titleIdx.get("Negation 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Negation 유무") ).setCellValue( "X" );

			if ( disease.isContainMetastatic() == true )
				row.createCell( titleIdx.get("Metastatic 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Metastatic 유무") ).setCellValue( "X" );

			if ( disease.isContainMostLikely() == true )
				row.createCell( titleIdx.get("Most-likely 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Most-likely 유무") ).setCellValue( "X" );

			if ( disease.isContainInvolvement() == true )
				row.createCell( titleIdx.get("Involvement 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Involvement 유무") ).setCellValue( "X" );

			if ( disease.isContainSuspicious() == true )
				row.createCell( titleIdx.get("Suspicious 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Suspicious 유무") ).setCellValue( "X" );
			
			if ( disease.isContainFavor() == true )
				row.createCell( titleIdx.get("Favor 유무") ).setCellValue( "O" );
			else
				row.createCell( titleIdx.get("Favor 유무") ).setCellValue( "X" );
		}
	}

	private void writeSNOMEDInfo( Row row, ChildSurgeonDocument csdoc ) {
		row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( csdoc.getSnomedLocation() );
		row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( csdoc.getSnomedTestName() );	
		row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( csdoc.getSnomedDiagnosisName() );
	}

	private void writePatientInfo( Row row, ChildSurgeonDocument csdoc ) {
		row.createCell( titleIdx.get("환자번호") ).setCellValue( csdoc.getPatientNumber() );
		row.createCell( titleIdx.get("성별") ).setCellValue( csdoc.getGender() );
		row.createCell( titleIdx.get("나이") ).setCellValue( csdoc.getAge() );
	}

	private void writeError( Row row, SurgeonDocument sdoc ) {
		ArrayList<String> errors = sdoc.getErrorStrs();
		if ( errors != null ) {
			row.createCell( titleIdx.get("에러") ).setCellValue( errors.toString() );
		}
	}

	private void writeError( Row row, Disease disease ) {
		if ( disease == null ) 
			row.createCell( titleIdx.get("에러") ).setCellValue( "#N/A" );
		else 
			row.createCell( titleIdx.get("에러") ).setCellValue( disease.getErrorStr() );
	}
	
	private void writeError( Row row, String errorStr ) {
		row.createCell( titleIdx.get("에러") ).setCellValue( errorStr );
	}
}
