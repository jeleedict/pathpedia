/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.util.ArrayList;

import kr.ac.knu.ml.pathinfo.unit.PathInfoAbbreviation;

public class PathInfoAbbreviationRepository {
	private ArrayList<PathInfoAbbreviation> abbreviations;
	
	public <E> void constructRepository( ArrayList<E> abbreviations ) {
		this.abbreviations = new ArrayList<PathInfoAbbreviation>();
		
		for ( E e : abbreviations ) {
			if ( e instanceof String[] ) {
				
				PathInfoAbbreviation pia = new PathInfoAbbreviation ( (String[]) e );
				this.abbreviations.add( pia );
			}
			else if ( e instanceof PathInfoAbbreviation ) {
				this.abbreviations.add( (PathInfoAbbreviation) e );
			}
		}
	}
	
	public ArrayList<String> getNormalTerm( String abbr ) {
		ArrayList<String> normalTerms = new ArrayList<String>();
		for ( PathInfoAbbreviation pia : abbreviations ) {
			if ( pia.getAbbr().equals( abbr ) ) {
				normalTerms.add( pia.getNormalTerm() );
			}
		}
		return normalTerms;
	}
	
	public String getAbbr( String id ) {
		for ( PathInfoAbbreviation pia : abbreviations ) {
			if ( pia.getId().equals( id ) )
				return pia.getAbbr();
		}
		return null;
	}
}
