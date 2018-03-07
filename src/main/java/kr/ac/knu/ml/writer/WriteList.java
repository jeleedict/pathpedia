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

public class WriteList {
	private Workbook wb;
	private Sheet sheet;
	
	public WriteList() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Unique marker list");
	}
	
	public void write( String fileName, TreeMap<String, HashSet<String>> markerInfo, boolean horizontal ) throws IOException {
		if ( horizontal == true )
			writehorizontal ( markerInfo );
		else
			writevertical ( markerInfo );
		FileOutputStream fileOut = new FileOutputStream( fileName );
		wb.write(fileOut);
		fileOut.close();
	}
	
	private void writehorizontal( TreeMap<String, HashSet<String>> markerInfo ) {
		int columnIdx = 1;
		for ( String marker : markerInfo.keySet() ) {
			// marker : marker list
			int rowIdx = 0;
			Row row = sheet.getRow( rowIdx );
			if ( row == null )
				row = sheet.createRow( rowIdx );
			row.createCell(columnIdx).setCellValue( marker );
			rowIdx++;
			
			for ( String markerValue : markerInfo.get(marker) ) {
				Row valueRow = sheet.getRow( rowIdx );
				if ( valueRow == null )
					valueRow = sheet.createRow( rowIdx );
				valueRow.createCell(columnIdx).setCellValue( markerValue );
				rowIdx++;
			}
			columnIdx++;
		}
	}
	
	private void writevertical( TreeMap<String, HashSet<String>> markerInfo ) {
		int rowIdx = 0;
		for ( String marker : markerInfo.keySet() ) {
			// marker : marker list
			int columnIdx = 0;
			Row row = sheet.getRow( rowIdx );
			if ( row == null )
				row = sheet.createRow( rowIdx );
			row.createCell(columnIdx).setCellValue( marker );
			columnIdx++;
			
			for ( String markerValue : markerInfo.get(marker) ) {
				Row valueRow = sheet.getRow( rowIdx );
				if ( valueRow == null )
					valueRow = sheet.createRow( rowIdx );
				valueRow.createCell(columnIdx).setCellValue( markerValue );
				columnIdx++;
			}
			rowIdx++;
		}
	}
}
