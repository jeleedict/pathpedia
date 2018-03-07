/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class PathInfoRepositoryLoader {
	private HashMap<String, ArrayList<String[]>> sheetContentMap;
	
	public PathInfoRepositoryLoader() {
		sheetContentMap = new HashMap<String, ArrayList<String[]>>();
	}
	
	public void load( String fileName ) {
		HashMap<String, Sheet> sheetMap = loadSheets ( fileName );		
		System.out.println ( "'" + fileName + "' loaded");

		for ( String sheet : sheetMap.keySet() ) {
			sheetContentMap.put( sheet.toLowerCase(), parse ( sheetMap.get( sheet ) ) );
		}
	}
	
	public ArrayList<String[]> getContents( String sheetName ) {
		if ( sheetContentMap == null || !sheetContentMap.containsKey( sheetName.toLowerCase() ) )
			return null;
		
		return sheetContentMap.get( sheetName.toLowerCase() );
	}
	
	public HashMap<String, Sheet> loadSheets( String fileName ) {		
		try {
			InputStream in = new FileInputStream( fileName );
			Workbook wb = WorkbookFactory.create(in);
			HashMap<String, Sheet> sheetMap = new HashMap<String, Sheet>();
			int numOfSheet = wb.getNumberOfSheets();
			for ( int i = 0; i < numOfSheet; i++ ) {
				Sheet sheet = wb.getSheetAt( i );
				String sheetName = sheet.getSheetName();
				sheetMap.put( sheetName, sheet );
			}
			return sheetMap;
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String[]> parse( Sheet sheet ) {
		System.out.print ( "Parsing '" + sheet.getSheetName() + "' sheet");		
		Row titleRow = sheet.getRow(0);
		ArrayList<Integer> indices = parseTitleRow ( titleRow );				
		ArrayList<String[]> values = new ArrayList<String[]>();	
		
		System.out.print ( ", # of row in '" + sheet.getSheetName() + "' sheet is " + sheet.getLastRowNum() );		
		for ( int i = 1; i <= sheet.getLastRowNum(); i++ ) { 			
			String value[] = new String[indices.size()];
			Row row = sheet.getRow( i );
			
			int midx = 0;
			for ( int idx : indices ) {			
				Cell cell = row.getCell(idx);								
				if ( cell == null )
					continue;

				switch (cell.getCellType()) {				
					case Cell.CELL_TYPE_STRING:
						String strValue = cell.getStringCellValue();
						value[midx] = strValue;	
						break;
					case Cell.CELL_TYPE_NUMERIC:	      
						double valueDou = cell.getNumericCellValue();
						value[midx] = String.valueOf( valueDou );
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						boolean valueBoolean = cell.getBooleanCellValue();
						value[midx] = String.valueOf( valueBoolean );
						break;			
					case Cell.CELL_TYPE_BLANK:
						break;
					case Cell.CELL_TYPE_ERROR:
						break;
					case Cell.CELL_TYPE_FORMULA:
						break;
					default:
						System.err.println( cell.toString() );
						break;
				}
				midx++;
			}	
			
			boolean flag = true;
			for ( String v : value ) {			
				if ( v == null )
					continue;
				
				if ( v.trim().length() != 0 )
					flag = false;
			}
			
			if ( flag == true )
				continue;
			
			values.add( value );
		}	
		
		System.out.println( "\tComplete");
		return values;
	}
	
	private ArrayList<Integer> parseTitleRow ( Row titleRow ) {
		// title row Parsing
		ArrayList<Integer> nonEmptyCellIndices = new ArrayList<Integer>();
		for ( int i = 0; i < titleRow.getLastCellNum(); i++ ) {
			Cell cell = titleRow.getCell(i);
			if ( cell == null )
				continue;
			
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					String valueStr = cell.getStringCellValue();
					if ( valueStr.trim().length() != 0 )
						nonEmptyCellIndices.add( i );
					break;
				case Cell.CELL_TYPE_NUMERIC:	      
					double valueDou = cell.getNumericCellValue();
					String valueDouStr = String.valueOf( valueDou );
					if ( valueDouStr.trim().length() != 0 )
						nonEmptyCellIndices.add( i );
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					boolean valueBoolean = cell.getBooleanCellValue();
					String valueBooleanStr = String.valueOf( valueBoolean );
					if ( valueBooleanStr.trim().length() != 0 )
						nonEmptyCellIndices.add( i );
					break;
				case Cell.CELL_TYPE_FORMULA:
					String valueFormula = cell.getCellFormula();
					if ( valueFormula.trim().length() != 0 )
						nonEmptyCellIndices.add( i );
					break;
				default:
					System.err.println("Title Parsing Error!");
					break;
			}
		}
		return nonEmptyCellIndices;
	}
}
