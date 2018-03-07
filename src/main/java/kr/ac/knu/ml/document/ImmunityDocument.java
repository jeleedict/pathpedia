/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.document;
import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;

/**
 * Class for IHC reports <br/>
 * 
 * @author hjsong
 * 
 */
public class ImmunityDocument extends BioMedicalDocument{	
	private List<PADiagnosis> diagnosisList;
	private List<MorphometricAnalysis> maList;
	
	public ImmunityDocument(){
		super();
		diagnosisList = new ArrayList<PADiagnosis>();
		maList = new ArrayList<MorphometricAnalysis>();
	}
	
	public void addDiagnosis( PADiagnosis diagnosis ) {
		diagnosisList.add( diagnosis );
	}
	
	public List<PADiagnosis> getDiagnosises() {
		return diagnosisList;
	}
	
	public PADiagnosis getLastDiagnosis() {
		if ( diagnosisList == null || diagnosisList.size() == 0 )
			return null;
		
		return diagnosisList.get( diagnosisList.size() - 1 );
	}
	
	public void addMorphometricAnalysis( MorphometricAnalysis ma ) {
		maList.add( ma );
	}
	
	public List<MorphometricAnalysis> getMorphometricAnalysis() {
		return maList;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( super.toString() + "\n");
		for ( PADiagnosis d : diagnosisList ) {
			sb.append( d.toString() );
		}
		for ( MorphometricAnalysis ma : maList ) {
			sb.append( ma.toString() );
		}
		return sb.toString();
	}
}
