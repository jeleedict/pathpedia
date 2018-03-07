/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.ac.knu.ml.pathinfo.unit.PathInfoSNUMICROField;

public class PathInfoSNUMICROFieldRepository implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<PathInfoSNUMICROField> fieldStrings; 
	
	static final Comparator<PathInfoSNUMICROField> PathInfoSNUMICROFieldComparable = new Comparator<PathInfoSNUMICROField>() {
		public int compare(PathInfoSNUMICROField arg0, PathInfoSNUMICROField arg1) {
			return arg1.getFieldName().compareToIgnoreCase( arg0.getFieldName() );
		}
	};	
	
	
	public <E> void constructRepository( ArrayList<E> fieldString ) {
		this.fieldStrings = new ArrayList<PathInfoSNUMICROField>();
		
		for ( E e : fieldString ) {
			if ( e instanceof String[] ) {
				PathInfoSNUMICROField piSMF = new PathInfoSNUMICROField ( (String[]) e );
				this.fieldStrings.add( piSMF );
			}
			else if ( e instanceof PathInfoSNUMICROField ) {
				this.fieldStrings.add( (PathInfoSNUMICROField) e );
			}
		}			
		Collections.sort( this.fieldStrings, PathInfoSNUMICROFieldComparable );		
	}	
	
	public String startWithFieldName ( String content ) {
		content = content.toLowerCase();
		for ( PathInfoSNUMICROField field : fieldStrings ) {
			String fieldName = field.getFieldName().toLowerCase();			
			if ( content.startsWith( fieldName ) ) {
				return fieldName;
			}
		}				
		return null;
	}
	
	public String getTypeofFieldName ( String fileName ) {		
		int idx = Collections.binarySearch( fieldStrings, new PathInfoSNUMICROField(fileName), PathInfoSNUMICROFieldComparable );
		if ( idx >= 0 )
			return fieldStrings.get( idx ).getType();
		return null;
	}
}
