/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteBioMappingTable {
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx;
	
	private final String titles[] = {
			"번호", "마커명", "추출된 양성률표현", "매핑된 양성률 표현"
	};	
	
	public WriteBioMappingTable() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Sheet1");
		rowIdx = 0;
	}
	
	public void write( String fileName, TreeMap<String, HashSet<String>> markerInfo ) throws IOException {
		writeTitle();
		writeContents( markerInfo );
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
		}
	}
	
	private void writeContents( TreeMap<String, HashSet<String>> markerInfo ) {
		int idx = 0;
		for ( String marker : markerInfo.keySet() ) {			
			for ( String markerValue : markerInfo.get(marker) ) {
				int columnIdx = 0;
				Row row = sheet.getRow( rowIdx );
				if ( row == null )
					row = sheet.createRow( rowIdx );
				
				row.createCell(columnIdx).setCellValue( idx );
				columnIdx++;
				
				row.createCell(columnIdx).setCellValue( marker );
				columnIdx++;
				
				row.createCell(columnIdx).setCellValue( markerValue );
				columnIdx++;
				rowIdx++;
			}
			idx++;
		}
	}	
}
