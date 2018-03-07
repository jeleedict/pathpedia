/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

import java.util.ArrayList;

/**
 * 면역화학보고서에서 테이블을 분석하는 클래스
 * 
 * @author Hyun-Je
 *
 */
public class PATable implements Cloneable {
	private int nCol;
	private int nRow;
	private PATableEntity[] title;
	private ArrayList<PATableEntity[]> table;
		
	public PATable() {
		nCol = -1;
		nRow = -1;
		table = new ArrayList<PATableEntity[]>();
	}
	
	public void setTitle( PATableEntity[] title ) {
		nCol = title.length;		
		this.title = title;
	}
	
	public void setTable( ArrayList<PATableEntity[]> table ) {	
		this.table = table;
	}
	
	public void addRow( PATableEntity[] row ) {
		table.add( row );
	}
	
	public PATableEntity[] getLastRow() { 
		if ( table.size() != 0 )
			return table.get( table.size() - 1 );
		else
			return null;
	}
	
	public int getNRow() {
		return nRow;
	}
	
	public int getNCol() {
		return nCol;
	}
	
	public PATableEntity[] getTitle(){
		return title;
	}
	
	public ArrayList<PATableEntity[]> getTable() {
		return table;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		PATable cloned = (PATable)super.clone();
	    cloned.setTitle( (PATableEntity []) cloned.getTitle().clone() );
	    cloned.setTable( (ArrayList<PATableEntity[]>) cloned.getTable().clone() );
	    return cloned;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		for ( PATableEntity t : title ) {
			sb.append( t + "\t" );
		}
		sb.append("\n");
		return sb.toString();
	}
}
