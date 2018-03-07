/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.Serializable;
import java.util.ArrayList;

import kr.ac.knu.ml.pathinfo.unit.PathInfoOrganNormalize;

public class PathInfoOrganNormalizeRepository implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoOrganNormalize> normlize; 	
	
	public <E> void constructRepository( ArrayList<E> normlize ) {
		this.normlize = new ArrayList<PathInfoOrganNormalize>();
		
		for ( E e : normlize ) {
			if ( e instanceof String[] ) {
				PathInfoOrganNormalize pidn = new PathInfoOrganNormalize ( (String[]) e );
				this.normlize.add( pidn );
			}
			else if ( e instanceof PathInfoOrganNormalize ) {
				this.normlize.add( (PathInfoOrganNormalize) e );
			}
		}		
	}

	public ArrayList<String> getOrganNames () {
		ArrayList<String> organNames = new ArrayList<String>();
		for ( PathInfoOrganNormalize pio : normlize ) {
			organNames.add( pio.getExtracted_OrganName() );			
		}
		return organNames;
	}
}
