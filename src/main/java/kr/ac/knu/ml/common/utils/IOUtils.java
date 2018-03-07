/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class IOUtils {
	private IOUtils(){		
	}
	
	public static BufferedReader getReader( String fileName ) throws IOException {
		return new BufferedReader ( new InputStreamReader 
				( getInputStream (fileName), "utf-8" ));		
	}
	
	public static BufferedReader getReader( String fileName, String encoding ) throws IOException {
		return new BufferedReader ( new InputStreamReader ( getInputStream (fileName), encoding ));		
	}
	public static InputStream getInputStream( String fileName ) throws IOException {
		InputStream in = new FileInputStream ( fileName );
		in = new BufferedInputStream( in );
		
		// gzip
		if ( fileName.endsWith( ".gz"))
			in = new GZIPInputStream (in);
		
		return in;
	}
	
	public static String[] readFile ( String fileName ) throws IOException {
		BufferedReader in = getReader( fileName );
		ArrayList<String> list = new ArrayList<String> ();
		
		String line;
		while (( line = in.readLine()) != null )
			list.add(line);
		
		return (String[]) list.toArray(new String[0]);
	}
	
	public static String[][] readExcelFile ( String fileName ) throws InvalidFormatException, IOException {
		return readExcelFile ( fileName, 0 );
	}

	public static String[][] readExcelFile ( String fileName, int sheetNum ) throws InvalidFormatException, IOException {
	    Workbook workbook = WorkbookFactory.create( getInputStream (fileName));
	    Sheet sheet = workbook.getSheetAt(sheetNum);		 
	  	    
	    int rows = sheet.getLastRowNum();
	    int cols = sheet.getRow( sheet.getFirstRowNum() ).getLastCellNum();
		String list[][] = new String[rows][cols];

		for ( int row = 0; row < rows; row++ ) {
			for ( int col = 0; col < cols; col++ ) {
				Cell cell = sheet.getRow(row).getCell(col);		
				if ( cell == null )
					list[row][col] = "";
				else if ( cell.getCellType() == Cell.CELL_TYPE_NUMERIC )
					list[row][col] = String.valueOf( cell.getNumericCellValue() );
				else
					list[row][col] = cell.getStringCellValue();
			}
		}
		return list;
	}
	
	public static void getFiles( String name, ArrayList<String> fileList ) {
		getFiles ( new File(name), fileList );
	}
	
	public static void getFiles( File name, ArrayList<String> fileList ) {
		if (name.isDirectory()) {
			File files[] = name.listFiles();
			for (File fileName : files) {
				getFiles(fileName, fileList);
			}
		} else
			fileList.add(name.toString());
	}
}
