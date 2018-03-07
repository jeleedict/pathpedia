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
import java.util.List;

import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.parser.immunity.SlideTissue;
import kr.ac.knu.ml.unit.immunity.BioMarker;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PAReportWriter {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
		
	private final String titles[] = {
			"ID", "Tissue_Slide_ID", "IHC_report_ID", "Slide_paragraph_ID", "has_SP_report",  "Candidate_SP_reports", "Are_candidate_SP_reports_same", "Tissue_slide_sequence_number",
			"Note_on_tissue_slide", "Biomarker_expression", "Matched_biomarker_expression_from_DB", "Biomarker_preferred_name", "matched_from_DB_lookup", "Test_result_expression", "Test_result_code", "is_addendum", "Addendum_date", "Summary_note", "Note",
			"is_Morphologic_test_done", "PatientID", "Sex", "Age", "SNOMED_CT_ORGAN", "SNOMED_CT_TEST", "SNOMED_CT_DIAGNOSIS"
	};
	
	private HashMap<String, Integer> titleIdx;
	
	public PAReportWriter() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("IHC_report");
		rowIdx = 0;
		titleIdx = new HashMap<String, Integer>();
	}
	
	public void write( String fileName, ArrayList<ImmunityDocument> idocs ) throws IOException {
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
	}
	
	private void write( ArrayList<ImmunityDocument> idocs ) {
		int count = 1;
		for ( ImmunityDocument idoc : idocs ) {
			if ( count % 1000 == 0 ) {
				System.out.print(".");				
			}
			
			String pathologyId = idoc.getPathologyID();
			ArrayList<PADiagnosis> pads = (ArrayList<PADiagnosis>) idoc.getDiagnosises();
			ArrayList<MorphometricAnalysis> mas = (ArrayList<MorphometricAnalysis>) idoc.getMorphometricAnalysis();
			
			if ( pads.size() == 0 && mas.size() == 0) {
				Row row = sheet.getRow( rowIdx );
				if ( row == null )
					row = sheet.createRow( rowIdx );
				
				row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
										
				row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( "#N/A" );
				
				row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
				row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( "#N/A" );
											
				row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "#N/A" );
																					
				row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( "#N/A" );
				 
				row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "#N/A" );
				
				row.createCell( titleIdx.get("is_addendum") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "#N/A" );
				
				row.createCell( titleIdx.get("Summary_note") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("Note") ).setCellValue( "#N/A" );
				row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "#N/A" );
				
				// Patient Info
				writePatientInfo ( row, idoc );
				// SNOMED Info
				writeSNOMEDInfo ( row, idoc );
				rowIdx++;
			}
			else {
				for ( PADiagnosis pad : pads ) {
					// summary
					String summary = pad.getDiagnosisSummary();
					if ( summary == null || summary.length() == 0 )
						summary = "NULL";
					
					// note
					String noteContents = pad.getDiagnosisNote();
					if ( noteContents == null || noteContents.length() == 0 )
						noteContents = "NULL";
					
					// Tissue Info.
					StringBuilder num = new StringBuilder();
					StringBuilder id = new StringBuilder();
					StringBuilder info = new StringBuilder();
					
					ArrayList<SlideTissue> tissues = pad.getSlides();
					if ( tissues.size() > 0 ) {
						SlideTissue st = tissues.get(0);
						num.append( st.getSlideTissueName() );
						if ( st.getSlideTissueKeyID() != null && st.getSlideTissueKeyID().size() > 0 )
							id.append( st.getSlideTissueKeyID() );
						if ( st.getSlideTissueInformation() != null )
							info.append( st.getSlideTissueInformation() );
						
						for ( int i = 1; i < tissues.size(); i++ )  {
							SlideTissue sti = tissues.get(i);
							num.append( "," + sti.getSlideTissueName() );
							if ( sti.getSlideTissueKeyID() != null && sti.getSlideTissueKeyID().size() > 0 )
								id.append( "," + sti.getSlideTissueKeyID() );
							if ( sti.getSlideTissueInformation() != null )
								info.append( "," + sti.getSlideTissueInformation() );
						}
					}
					
					ArrayList<SurgeonDocument> sdocs = pad.getSurgeonDocuments();
					if ( sdocs.size() == 0 ) {			
						List<BioMarker> markers = pad.getMarkers();
						if ( markers.size() == 0 ) {
							Row row = sheet.getRow( rowIdx );
							if ( row == null )
								row = sheet.createRow( rowIdx );
							
							row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
													
							row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
							row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
							row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
							
							row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
							row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( pad.getDiagnosisName() );
														
							row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
							row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
							row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
															
							row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( "#N/A" );
							 
							row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "#N/A" );
							
							row.createCell( titleIdx.get("is_addendum") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "#N/A" );
															
							row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
							row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
							row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "X" );
							
							// Patient Info
							writePatientInfo ( row, idoc );
							// SNOMED Info
							writeSNOMEDInfo ( row, idoc );
							rowIdx++;
						}
						else {
							for ( BioMarker marker : markers )	{
								Row row = sheet.getRow( rowIdx );
								if ( row == null )
									row = sheet.createRow( rowIdx );
								
								String markerName = marker.getMarkerName();
								if ( markerName != null && markerName.trim().length() > 0 ) {
									row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
															
									row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
									
									String slideKeyInfo = marker.getSlideKeyInfo();
									if ( slideKeyInfo != null && slideKeyInfo.length() != 0 ) 
										row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( slideKeyInfo );
									else 
										row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
									
									String slideKeyAdditionalInfo = marker.getSlideAdditionalInfo();
									if ( slideKeyAdditionalInfo != null && slideKeyAdditionalInfo.length() != 0 ) 									
										row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( slideKeyAdditionalInfo );
									else
										row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
									
									row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
									row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( pad.getDiagnosisName() );
																
									row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
									row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
									row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
																
									writeMarkerInfo ( row, marker );
									// Addeundum Info
									writeAddendumInfo ( row, marker );
									
									row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
									row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
									row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "X" );
									
									// Patient Info
									writePatientInfo ( row, idoc );
									// SNOMED Info
									writeSNOMEDInfo ( row, idoc );
									rowIdx++;
								}		
							}
						}
					}
					else {
						for ( SurgeonDocument sd : sdocs ) {			
							List<BioMarker> markers = pad.getMarkers();
							if ( markers.size() == 0 ) {
								Row row = sheet.getRow( rowIdx );
								if ( row == null )
									row = sheet.createRow( rowIdx );
								
								row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
														
								row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
								row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
								row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
								
								row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
								row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( pad.getDiagnosisName() );
															
								row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
								row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
								row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
												
								row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( "#N/A" );
								 
								row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "#N/A" );
								
								row.createCell( titleIdx.get("is_addendum") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "#N/A" );
									
								row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
								row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
								row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "X" );
								
								// Patient Info
								writePatientInfo ( row, idoc );
								// SNOMED Info
								writeSNOMEDInfo ( row, idoc );
								rowIdx++;
							}
							else {
								for ( BioMarker marker : markers )	{
									Row row = sheet.getRow( rowIdx );
									if ( row == null )
										row = sheet.createRow( rowIdx );
									
									String markerName = marker.getMarkerName();
									if ( markerName != null && markerName.trim().length() > 0 ) {
										row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
										
										row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( id.toString() );
										
										String slideKeyInfo = marker.getSlideKeyInfo();
										if ( slideKeyInfo != null && slideKeyInfo.length() != 0 ) 
											row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( slideKeyInfo );
										else 
											row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
										
										String slideKeyAdditionalInfo = marker.getSlideAdditionalInfo();
										if ( slideKeyAdditionalInfo != null && slideKeyAdditionalInfo.length() != 0 ) 									
											row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( slideKeyAdditionalInfo );
										else
											row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
									
										row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
										row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( pad.getDiagnosisName() );
										
										row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "O" );
										row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( sd.getPathologyID() );
										if ( pad.getIsAllSurgeonDocument() == true )
											row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "Same" );
										else
											row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "Not Same" );
																	
										writeMarkerInfo ( row, marker );
										// Addeundum Info
										writeAddendumInfo ( row, marker );
										
										row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
										row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
										row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "X" );
										
										// Patient Info
										writePatientInfo ( row, idoc );
										// SNOMED Info
										writeSNOMEDInfo ( row, idoc );
										rowIdx++;
									}							
								}
							}
						}
					}										
				}			
				for ( MorphometricAnalysis ma : mas ) {
					// summary
					String summary = ma.getDiagnosisSummary();
					if ( summary == null || summary.length() == 0 )
						summary = "NULL";
					
					// note
					String noteContents = ma.getDiagnosisNote();
					if ( noteContents == null || noteContents.length() == 0 )
						noteContents = "NULL";
					
					// Tissue Info.
					StringBuilder num = new StringBuilder();
					StringBuilder id = new StringBuilder();
					StringBuilder info = new StringBuilder();
					
					ArrayList<SlideTissue> tissues = ma.getSlides();
					if ( tissues.size() > 0 ) {
						SlideTissue st = tissues.get(0);
						num.append( st.getSlideTissueName() );
						if ( st.getSlideTissueKeyID() != null && st.getSlideTissueKeyID().size() > 0 )
							id.append( st.getSlideTissueKeyID() );
						if ( st.getSlideTissueInformation() != null )
							info.append( st.getSlideTissueInformation() );
						
						for ( int i = 1; i < tissues.size(); i++ )  {
							SlideTissue sti = tissues.get(i);
							num.append( "," + sti.getSlideTissueName() );
							if ( sti.getSlideTissueKeyID() != null && sti.getSlideTissueKeyID().size() > 0 )
								id.append( "," + sti.getSlideTissueKeyID() );
							if ( sti.getSlideTissueInformation() != null )
								info.append( "," + sti.getSlideTissueInformation() );
						}
					}
					
					ArrayList<SurgeonDocument> sdocs = ma.getSurgeonDocuments();
					if ( sdocs.size() == 0 ) {			
						List<BioMarker> markers = ma.getMarkers();
						if ( markers.size() == 0 ) {
							Row row = sheet.getRow( rowIdx );
							if ( row == null )
								row = sheet.createRow( rowIdx );
							
							row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
													
							row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
							row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
							row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
							
							row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
							row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( ma.getDiagnosisName() );
														
							row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
							row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
							row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
														
							row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( "#N/A" );
							 
							row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "#N/A" );
							
							row.createCell( titleIdx.get("is_addendum") ).setCellValue( "#N/A" );
							row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "#N/A" );
								
							row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
							row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
							row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "O" );
							
							// Patient Info
							writePatientInfo ( row, idoc );
							// SNOMED Info
							writeSNOMEDInfo ( row, idoc );
							rowIdx++;
						}
						else {
							for ( BioMarker marker : markers )	{
								Row row = sheet.getRow( rowIdx );
								if ( row == null )
									row = sheet.createRow( rowIdx );
								
								String markerName = marker.getMarkerName();
								if ( markerName != null && markerName.trim().length() > 0 ) {
									row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
								
		//							row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( id.toString() );
									row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
									
									String slideKeyInfo = marker.getSlideKeyInfo();
									if ( slideKeyInfo != null && slideKeyInfo.length() != 0 ) 
										row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( slideKeyInfo );
									else 
										row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
									
									String slideKeyAdditionalInfo = marker.getSlideAdditionalInfo();
									if ( slideKeyAdditionalInfo != null && slideKeyAdditionalInfo.length() != 0 ) 									
										row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( slideKeyAdditionalInfo );
									else
										row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
									
									row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
									row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( ma.getDiagnosisName() );
																
									row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
									row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
									row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
																
									writeMarkerInfo ( row, marker );
									// Addeundum Info
									writeAddendumInfo ( row, marker );
									
									row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
									row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
									row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "O" );
									
									// Patient Info
									writePatientInfo ( row, idoc );
									// SNOMED Info
									writeSNOMEDInfo ( row, idoc );
									rowIdx++;
								}						
							}
						}
					}
					else {
						for ( SurgeonDocument sd : sdocs ) {			
							List<BioMarker> markers = ma.getMarkers();
							if ( markers.size() == 0 ) {
								Row row = sheet.getRow( rowIdx );
								if ( row == null )
									row = sheet.createRow( rowIdx );
								
								row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
														
								row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( "No Matching S Report" );
								row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
								row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
								
								row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
								row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( ma.getDiagnosisName() );
															
								row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "X" );
								row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( "NULL" );
								row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "NULL" );
													
								row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( "#N/A" );
								 
								row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "#N/A" );
								
								row.createCell( titleIdx.get("is_addendum") ).setCellValue( "#N/A" );
								row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "#N/A" );
									
								row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
								row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
								row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "O" );
								
								// Patient Info
								writePatientInfo ( row, idoc );
								// SNOMED Info
								writeSNOMEDInfo ( row, idoc );
								rowIdx++;
							}
							else {
								for ( BioMarker marker : markers )	{
									Row row = sheet.getRow( rowIdx );
									if ( row == null )
										row = sheet.createRow( rowIdx );
									
									String markerName = marker.getMarkerName();
									if ( markerName != null && markerName.trim().length() > 0 ) {
										row.createCell( titleIdx.get("ID") ).setCellValue( count++ );
										
										row.createCell( titleIdx.get("Tissue_Slide_ID") ).setCellValue( id.toString() );
										
										String slideKeyInfo = marker.getSlideKeyInfo();
										if ( slideKeyInfo != null && slideKeyInfo.length() != 0 ) 
											row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( slideKeyInfo );
										else 
											row.createCell( titleIdx.get("Tissue_slide_sequence_number") ).setCellValue( num.toString() );
										
										String slideKeyAdditionalInfo = marker.getSlideAdditionalInfo();
										if ( slideKeyAdditionalInfo != null && slideKeyAdditionalInfo.length() != 0 ) 									
											row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( slideKeyAdditionalInfo );
										else
											row.createCell( titleIdx.get("Note_on_tissue_slide") ).setCellValue( info.toString() );
									
										row.createCell( titleIdx.get("IHC_report_ID") ).setCellValue( pathologyId );
										row.createCell( titleIdx.get("Slide_paragraph_ID") ).setCellValue( ma.getDiagnosisName() );
										
										row.createCell( titleIdx.get("has_SP_report") ).setCellValue( "O" );
										row.createCell( titleIdx.get("Candidate_SP_reports") ).setCellValue( sd.getPathologyID() );
										if ( ma.getIsAllSurgeonDocument() == true )
											row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "Same" );
										else
											row.createCell( titleIdx.get("Are_candidate_SP_reports_same") ).setCellValue( "Not Same" );
																	
										writeMarkerInfo ( row, marker );
										// Addeundum Info
										writeAddendumInfo ( row, marker );
										
										row.createCell( titleIdx.get("Summary_note") ).setCellValue( summary );
										row.createCell( titleIdx.get("Note") ).setCellValue( noteContents );
										row.createCell( titleIdx.get("is_Morphologic_test_done") ).setCellValue( "O" );
										
										// Patient Info
										writePatientInfo ( row, idoc );
										// SNOMED Info
										writeSNOMEDInfo ( row, idoc );
										rowIdx++;
									}
								}
							}
						}
					}			
				}
			}			
		}
		System.out.println();
	}	
	
	private void writeMarkerInfo( Row row, BioMarker marker ) {
		row.createCell( titleIdx.get("Biomarker_expression") ).setCellValue( marker.getMarkerName() );
		row.createCell( titleIdx.get("Matched_biomarker_expression_from_DB") ).setCellValue( marker.getMarkerNameString() );
		row.createCell( titleIdx.get("Biomarker_preferred_name") ).setCellValue( marker.getRepresentativeMarkerName() );
		
		if ( marker.isMapped() == true ) 
			row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "O" );
		else
			row.createCell( titleIdx.get("matched_from_DB_lookup") ).setCellValue( "X" );
		
		row.createCell( titleIdx.get("Test_result_expression") ).setCellValue( marker.getValue() );
		
		String polarity = marker.getPolarity();
		if ( polarity == null )
			return;
			
		if ( polarity.equalsIgnoreCase( "POSITIVE") || polarity.equalsIgnoreCase( "NEGATIVE") )
			row.createCell( titleIdx.get("Test_result_code") ).setCellValue( marker.getPolarity() );
		else
			row.createCell( titleIdx.get("Test_result_code") ).setCellValue( "ERROR" );
	}
	
	private void writeAddendumInfo( Row row, BioMarker marker ) {
		if ( marker.getAddendum() == true ) {
			row.createCell( titleIdx.get("is_addendum") ).setCellValue( "O" );
			row.createCell( titleIdx.get("Addendum_date") ).setCellValue( marker.getAddendumDate() );
		}
		else {
			row.createCell( titleIdx.get("is_addendum") ).setCellValue( "X" );
			row.createCell( titleIdx.get("Addendum_date") ).setCellValue( "NULL" );
		}
	}
	
	private void writeSNOMEDInfo( Row row, ImmunityDocument idoc ) {
		row.createCell( titleIdx.get("SNOMED_CT_ORGAN") ).setCellValue( idoc.getSnomedLocation() );
		row.createCell( titleIdx.get("SNOMED_CT_TEST") ).setCellValue( idoc.getSnomedTestName() );
		row.createCell( titleIdx.get("SNOMED_CT_DIAGNOSIS") ).setCellValue( idoc.getSnomedDiagnosisName() );
	}
	
	private void writePatientInfo( Row row, ImmunityDocument idoc ) {
		row.createCell( titleIdx.get("PatientID") ).setCellValue( idoc.getPatientNumber() );
		row.createCell( titleIdx.get("Sex") ).setCellValue( idoc.getGender() );
		row.createCell( titleIdx.get("Age") ).setCellValue( idoc.getAge() );
	}
}
