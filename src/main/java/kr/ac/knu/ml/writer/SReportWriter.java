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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

public class SReportWriter {
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

	public SReportWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("외과병리보고서");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
		type = "excel";
	}

	public SReportWriter( String type ) {
		this.type = type;
	}

	public void write( String fileName, ArrayList<SurgeonDocument> sdocs ) throws IOException {
		if ( type.equals( "xml") ) {
			writeXML ( fileName, sdocs );
		}
		else {
			wb = new XSSFWorkbook();
			sheet = wb.createSheet("외과병리보고서");
			rowIdx = 0;
			titleIdx = new HashMap<String, Integer>();

			writeTitle();
			write( sdocs );
			BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream ( fileName ), 8 * 1024 );
			wb.write(bos);
			bos.close();
		}		
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
	
	public void writeXML( String xml, ArrayList<SurgeonDocument> sdocs ) {	    
		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			Document dom = db.newDocument();

			// create the root element
			Element rootEle = dom.createElement("SurgeonDocument");

			int count = 1;
			for ( SurgeonDocument sdoc : sdocs ) {
				if ( count % 1000 == 0 )
					System.out.print(".");			

				Element doc = dom.createElement("DOC");
				doc.setAttribute("idx", String.valueOf( count ) );
				doc.setAttribute("id", sdoc.getPathologyID() );

				// PATIENT
				Element PATIENT = dom.createElement("PATIENT");
				Element PATIENT_ID = dom.createElement("PATIENT_ID");
				PATIENT_ID.appendChild( dom.createTextNode( sdoc.getPatientNumber() ));

				Element PATIENT_GENDER = dom.createElement("PATIENT_GENDER");
				PATIENT_GENDER.appendChild( dom.createTextNode( sdoc.getGender() ));

				Element PATIENT_AGE = dom.createElement("PATIENT_AGE");
				PATIENT_AGE.appendChild( dom.createTextNode( String.valueOf( sdoc.getAge() ) ));

				PATIENT.appendChild( PATIENT_ID );
				PATIENT.appendChild( PATIENT_GENDER );
				PATIENT.appendChild( PATIENT_AGE );
				doc.appendChild( PATIENT );

				// SNOMED
				Element SNOMED = dom.createElement("SNOMED");
				Element SNOMED_LOCATION = dom.createElement("SNOMED_LOCATION");
				SNOMED_LOCATION.appendChild( dom.createTextNode( sdoc.getSnomedLocation() ));

				Element SNOMED_TEST = dom.createElement("SNOMED_TEST");
				SNOMED_TEST.appendChild( dom.createTextNode( sdoc.getSnomedTestName() ));

				Element SNOMED_DISEASE = dom.createElement("SNOMED_DISEASE");
				SNOMED_DISEASE.appendChild( dom.createTextNode( sdoc.getSnomedDiagnosisName() ));

				SNOMED.appendChild( SNOMED_LOCATION );
				SNOMED.appendChild( SNOMED_TEST );
				SNOMED.appendChild( SNOMED_DISEASE );
				doc.appendChild( SNOMED );

				// SLIDE
				Element SLIDE = dom.createElement("SLIDE");
				Element SLIDE_ID = dom.createElement("SLIDE_ID");
				SLIDE_ID.appendChild( dom.createTextNode( "" ));

				Element SLIDE_KEY = dom.createElement("SLIDE_KEY");
				SLIDE_KEY.appendChild( dom.createTextNode( "" ));

				Element SLIDE_NAME = dom.createElement("SLIDE_NAME");
				SLIDE_NAME.appendChild( dom.createTextNode( "" ));

				Element SLIDE_INFO = dom.createElement("SLIDE_INFO");
				SLIDE_INFO.appendChild( dom.createTextNode( "" ));

				Element SLIDE_NUM = dom.createElement("SLIDE_NUM");
				SLIDE_NUM.appendChild( dom.createTextNode( "" ));

				Element SLIDE_TOTAL = dom.createElement("SLIDE_TOTAL");
				SLIDE_TOTAL.appendChild( dom.createTextNode( "" ));

				SLIDE.appendChild( SLIDE_ID );
				SLIDE.appendChild( SLIDE_KEY );
				SLIDE.appendChild( SLIDE_NAME );
				SLIDE.appendChild( SLIDE_INFO );
				SLIDE.appendChild( SLIDE_NUM );
				SLIDE.appendChild( SLIDE_TOTAL );
				doc.appendChild( SLIDE );

				// MICRO				
				Element MICRO = dom.createElement("MICRO");
				
				Element MICRO_REPRESENTATION = dom.createElement("MICRO_REPRESENTATION");				
				ArrayList<String> micros =  sdoc.getMicros();
				StringBuilder sb = new StringBuilder();
				for ( String micro : micros ) 
					sb.append( micro + "," );

				String micro = null;
				if ( sb.toString().length() > 1 ) 		
					micro = sb.toString().substring(0, sb.toString().length() - 1 );
				MICRO_REPRESENTATION.appendChild( dom.createTextNode( micro ));
				
				Element MICRO_COUNT = dom.createElement("MICRO_COUNT");
				MICRO_COUNT.appendChild( dom.createTextNode( String.valueOf( sdoc.getNumOfSlide() ) ) );
				
				MICRO.appendChild( MICRO_REPRESENTATION );
				MICRO.appendChild( MICRO_COUNT );
				
				doc.appendChild( MICRO );
								
				Element MICROTREE = dom.createElement("MICROTREE");				
				ArrayList<MicroValue> mvs = sdoc.getMvs();
				
				for ( int i = 0; i < mvs.size(); i++ ) {					
					MicroValue mv = mvs.get( i );
					if ( mv.getParentIdx() == -1 ) {
						MICROTREE.appendChild( treeTraverse( mvs, mv, dom, 1 ) );
					}
				}				
				doc.appendChild( MICROTREE );
				
				Element DIAGNOSISES = dom.createElement("DIAGNOSISES");
				ArrayList<SDiagnosis> sds = sdoc.getDiagnosies();
				if ( sds != null ) {
					int diagnosisNum = 1;								
					for ( SDiagnosis sdiagnosis : sdoc.getDiagnosies() ) {
						Element DIAGNOSIS = dom.createElement("DIAGNOSIS");
						DIAGNOSIS.setAttribute("idx", String.valueOf(diagnosisNum));
						
						// Organ
						Element ORGAN = dom.createElement("ORGAN");
						Organ organ = sdiagnosis.getOrgan();
						if ( organ != null ) {
							Element ORGAN_CANDIDATE = dom.createElement("ORGAN_CANDIDATE");
							ORGAN_CANDIDATE.appendChild( dom.createTextNode( organ.getCandidateOrganName() ));
							ORGAN.appendChild( ORGAN_CANDIDATE );
	
							Element ORGAN_MAPPED = dom.createElement("ORGAN_MAPPED");
	
							Element ORGAN_MAPPED_NAME = dom.createElement("ORGAN_MAPPED_NAME");
							ORGAN_MAPPED_NAME.appendChild( dom.createTextNode( organ.getOrganName() ));
	
							Element ORGAN_MAPPED_ID = dom.createElement("ORGAN_MAPPED_ID");
							ORGAN_MAPPED_ID.appendChild( dom.createTextNode( organ.getSNOMEDorganSui() ));
	
							Element ORGAN_MAPPED_SIMILARITY = dom.createElement("ORGAN_MAPPED_SIMILARITY");
							ORGAN_MAPPED_SIMILARITY.appendChild( dom.createTextNode(  String.valueOf( organ.getOrganSimilarity() ) ));
	
							ORGAN_MAPPED.appendChild( ORGAN_MAPPED_NAME );
							ORGAN_MAPPED.appendChild( ORGAN_MAPPED_ID );
							ORGAN_MAPPED.appendChild( ORGAN_MAPPED_SIMILARITY );
							ORGAN.appendChild( ORGAN_MAPPED );
						}		
						DIAGNOSIS.appendChild( ORGAN );
						
						ArrayList<Disease> diseases = sdiagnosis.getDiseases();
						Element DISEASES = dom.createElement("DISEASES");		
						if ( diseases != null ) { 
							int diseaseNum = 1;
							for ( Disease disease : diseases ) {
								Element DISEASE = dom.createElement("DISEASE");		
								DISEASE.setAttribute("idx", String.valueOf(diseaseNum));
								// Candidate
								Element DISEASE_CANDIDATE = dom.createElement("DISEASE_CANDIDATE");
	
								if ( disease.getCandidateDiseaseName() != null  ) {
									Element DISEASE_CANDIDATE_NAME = dom.createElement("DISEASE_CANDIDATE_NAME");
									DISEASE_CANDIDATE_NAME.appendChild( dom.createTextNode( disease.getCandidateDiseaseName() ));
	
									Element DISEASE_CANDIDATE_CONSISTENTWITH = dom.createElement("DISEASE_CANDIDATE_CONSISTENTWITH");
									if ( disease.isContainConsistentWith() == true ) 
										DISEASE_CANDIDATE_CONSISTENTWITH.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_CONSISTENTWITH.appendChild( dom.createTextNode( "X" ));
	
									Element DISEASE_CANDIDATE_NEGATION = dom.createElement("DISEASE_CANDIDATE_NEGATION");
									if ( disease.isContainNegation() == true ) 
										DISEASE_CANDIDATE_NEGATION.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_NEGATION.appendChild( dom.createTextNode( "X" ));
	
									Element DISEASE_CANDIDATE_METASTATIC = dom.createElement("DISEASE_CANDIDATE_METASTATIC");
									if ( disease.isContainMetastatic() == true ) 
										DISEASE_CANDIDATE_METASTATIC.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_METASTATIC.appendChild( dom.createTextNode( "X" ));
	
									Element DISEASE_CANDIDATE_MOSTLIKELY = dom.createElement("DISEASE_CANDIDATE_MOSTLIKELY");
									if ( disease.isContainMostLikely() == true ) 
										DISEASE_CANDIDATE_MOSTLIKELY.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_MOSTLIKELY.appendChild( dom.createTextNode( "X" ));
	
									Element DISEASE_CANDIDATE_INVOLVEMENT = dom.createElement("DISEASE_CANDIDATE_INVOLVEMENT");
									if ( disease.isContainInvolvement() == true ) 
										DISEASE_CANDIDATE_INVOLVEMENT.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_INVOLVEMENT.appendChild( dom.createTextNode( "X" ));
	
									Element DISEASE_CANDIDATE_SUSPICIOUS = dom.createElement("DISEASE_CANDIDATE_SUSPICIOUS");
									if ( disease.isContainSuspicious() == true ) 
										DISEASE_CANDIDATE_SUSPICIOUS.appendChild( dom.createTextNode( "O" ));
									else
										DISEASE_CANDIDATE_SUSPICIOUS.appendChild( dom.createTextNode( "X" ));
	
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_NAME );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_CONSISTENTWITH );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_NEGATION );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_METASTATIC );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_MOSTLIKELY );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_INVOLVEMENT );
									DISEASE_CANDIDATE.appendChild( DISEASE_CANDIDATE_SUSPICIOUS );
								}
	
								DISEASE.appendChild( DISEASE_CANDIDATE );
	
								// Mapped
								Element DISEASE_MAPPED = dom.createElement("DISEASE_MAPPED");
	
								if ( disease.getDiseaseName() != null  ) {
									Element DISEASE_MAPPED_NAME = dom.createElement("DISEASE_MAPPED_NAME");
									DISEASE_MAPPED_NAME.appendChild( dom.createTextNode( disease.getDiseaseName() ));
									DISEASE_MAPPED.appendChild( DISEASE_MAPPED_NAME );
								}
	
								if ( disease.getUMLSdiseaseSui() != null ) {
									Element DISEASE_MAPPED_ID = dom.createElement("DISEASE_MAPPED_ID");
									DISEASE_MAPPED_ID.appendChild( dom.createTextNode( disease.getUMLSdiseaseSui() ));
									DISEASE_MAPPED.appendChild( DISEASE_MAPPED_ID );
	
									Element DISEASE_MAPPED_SIMILARITY = dom.createElement("DISEASE_MAPPED_SIMILARITY"); 
									DISEASE_MAPPED_SIMILARITY.appendChild( dom.createTextNode( String.valueOf( disease.getDiseaseSimilarity() ) ));
									DISEASE_MAPPED.appendChild( DISEASE_MAPPED_SIMILARITY );
								}
								DISEASE.appendChild( DISEASE_MAPPED );
								
								Element HISTOLOGIC_TYPE = dom.createElement("HISTOLOGIC_TYPE");
								Element GROSS_TYPE = dom.createElement("GROSS_TYPE");
								Element LOCATION_OF_TUMOR = dom.createElement("LOCATION_OF_TUMOR");					
								Element DEPTH_OF_INVASION = dom.createElement("DEPTH_OF_INVASION");
								Element LYMPH_NODE_METASTASIS = dom.createElement("LYMPH_NODE_METASTASIS");
								Element DCIS = dom.createElement("DCIS");
								
								DISEASE.appendChild( HISTOLOGIC_TYPE );
								DISEASE.appendChild( GROSS_TYPE );
								DISEASE.appendChild( LOCATION_OF_TUMOR );
								DISEASE.appendChild( DEPTH_OF_INVASION );
								DISEASE.appendChild( LYMPH_NODE_METASTASIS );
								DISEASE.appendChild( DCIS );
								
								DISEASES.appendChild( DISEASE );
								diseaseNum++;
							}
						}					
						DIAGNOSIS.appendChild( DISEASES );
						DIAGNOSISES.appendChild( DIAGNOSIS );
						diagnosisNum++;
					}		      
				}
				doc.appendChild(DIAGNOSISES);
				rootEle.appendChild( doc );
			}
			dom.appendChild(rootEle);

			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				//	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xml)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}

	private void write( ArrayList<SurgeonDocument> sdocs ) {
		int count = 1;
		for ( SurgeonDocument sdoc : sdocs ) {
			if ( count % 1000 == 0 )
				System.out.print(".");					

			if ( sdoc.getDiagnosies() == null ) {
				Row row = sheet.getRow( rowIdx );
				if ( row == null )
					row = sheet.createRow( rowIdx );

				row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
				row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( sdoc.getPathologyID() );

				// Slide
				writeSlideInfo ( row );				
				// MICRO
				writeMICROInfo ( row, sdoc );
				// OrganInfo
				writeOrganInfo ( row, null );
				writeDiseaseInfo ( row, null );
				// Info.
				writeFields ( row, null );																				
				// Patient info
				writePatientInfo ( row, sdoc );							
				// SNOMED info
				writeSNOMEDInfo ( row, sdoc );
				// Error info
				writeError ( row, sdoc.getErrorStr() );
				//				writeError( row, sdoc );
				rowIdx++;			

				continue;
			}

			for ( SDiagnosis sdiagnosis : sdoc.getDiagnosies() ) {
				if ( sdiagnosis.getDiseases() == null ) {
					Row row = sheet.getRow( rowIdx );
					if ( row == null )
						row = sheet.createRow( rowIdx );

					row.createCell( titleIdx.get("번호") ).setCellValue( count++ );
					row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( sdoc.getPathologyID() );

					// Slide
					writeSlideInfo ( row );				
					// MICRO
					writeMICROInfo ( row, sdoc );
					// OrganInfo
					writeOrganInfo ( row, sdiagnosis );
					writeDiseaseInfo ( row, null );
					// Info.
					writeFields ( row, sdiagnosis );																							
					// Patient info
					writePatientInfo ( row, sdoc );							
					// SNOMED info
					writeSNOMEDInfo ( row, sdoc );					
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
						row.createCell( titleIdx.get("외과병리보고서 ID") ).setCellValue( sdoc.getPathologyID() );

						// Slide
						writeSlideInfo ( row );				
						// MICRO
						writeMICROInfo ( row, sdoc );
						// OrganInfo
						writeOrganInfo ( row, sdiagnosis );
						writeDiseaseInfo ( row, disease );
						// Info.
						writeFields ( row, sdiagnosis );																							
						// Patient info
						writePatientInfo ( row, sdoc );							
						// SNOMED info
						writeSNOMEDInfo ( row, sdoc );
						// Error info												
						writeError ( row, disease );						
						// writeError( row, sdoc );
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

	private void writeMICROInfo( Row row, SurgeonDocument sdoc ) {
		ArrayList<String> micros =  sdoc.getMicros();		
		StringBuilder sb = new StringBuilder();
		for ( String micro : micros ) 
			sb.append( micro + "," );

		String micro = null;
		if ( sb.toString().length() > 1 ) 		
			micro = sb.toString().substring(0, sb.toString().length() - 1 );	

		row.createCell( titleIdx.get("현미경 설명 추출 내용") ).setCellValue( micro );
		row.createCell( titleIdx.get("현미경에 따른 조직개수") ).setCellValue( sdoc.getNumOfSlide() );
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

	private void writeSNOMEDInfo( Row row, SurgeonDocument sdoc ) {
		row.createCell( titleIdx.get("스노메드 채취부위") ).setCellValue( sdoc.getSnomedLocation() );
		row.createCell( titleIdx.get("스노메드 검사명") ).setCellValue( sdoc.getSnomedTestName() );	
		row.createCell( titleIdx.get("스노메드 진단명") ).setCellValue( sdoc.getSnomedDiagnosisName() );
	}

	private void writePatientInfo( Row row, SurgeonDocument sdoc ) {
		row.createCell( titleIdx.get("환자번호") ).setCellValue( sdoc.getPatientNumber() );
		row.createCell( titleIdx.get("성별") ).setCellValue( sdoc.getGender() );
		row.createCell( titleIdx.get("나이") ).setCellValue( sdoc.getAge() );
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
