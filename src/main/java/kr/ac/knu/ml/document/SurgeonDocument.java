/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.document;

import java.util.ArrayList;

import kr.ac.knu.ml.parser.surgeon.SlideKey;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;
import kr.ac.knu.ml.unit.surgeon.MicroValue;
import kr.ac.knu.ml.unit.surgeon.SDiagnosis;

/**
 * Class for SP reports <br/>
 *
 * @author hjsong
 */
public class SurgeonDocument extends BioMedicalDocument implements Cloneable{
	private String abbrPathologyID;
	private String grossFinding;
	private int numOfSlide;
	private ArrayList<String> slideKeyStrs;
	private ArrayList<SDiagnosis> diagnosies;	
	private ArrayList<String> micros;
	private ArrayList<MicroValue> mvs;
	private ArrayList<SlideKey> slideKeys;
	
	private PADiagnosis mappedPADiagnosis;
	private MorphometricAnalysis mappedMorphometricAnalysis;	
	private String errorStr;
	
	public SurgeonDocument(){
		super();
		abbrPathologyID = null;
		grossFinding = null;
		slideKeyStrs = null;
		micros = null;
		numOfSlide = 0;
		diagnosies = null;
		mappedPADiagnosis = null;
		mappedMorphometricAnalysis = null;		
		mvs = null;
		slideKeys = null;
		errorStr = null;
	}
	
	public SurgeonDocument(String pathologyID, String abbrPathologyID ){
		super( pathologyID );
		this.abbrPathologyID = abbrPathologyID;
		grossFinding = null;
		slideKeyStrs = null;
		micros = null;
		numOfSlide = 0;
		diagnosies = null;
		mappedPADiagnosis = null;
		mappedMorphometricAnalysis = null;
		mvs = null;
		slideKeys = null;
		errorStr = null;
	}

	public String getAbbrPathologyID() {
		return abbrPathologyID;
	}

	public void setAbbrPathologyID(String abbrPathologyID) {
		this.abbrPathologyID = abbrPathologyID;
	}
	
	public String getGrossFinding() {
		return grossFinding;
	}

	public void setGrossFinding(String grossFinding) {
		this.grossFinding = grossFinding;
	}

	public ArrayList<String> getSlideKeyStrs() {
		return slideKeyStrs;
	}

	public void setSlideKeyStrs(ArrayList<String> slideKeys) {
		this.slideKeyStrs = slideKeys;
	}
	
	public void addSlideKeyStr(String slideKeyStr) {
		if ( this.slideKeyStrs == null )
			this.slideKeyStrs = new ArrayList<String>();
		
		this.slideKeyStrs.add( slideKeyStr );
	}

	public ArrayList<String> getMicros() {
		return micros;
	}

	public void setMicro(ArrayList<String> micros) {
		this.micros = micros;
	}
	
	public void addMicro(String micro) {
		if ( this.micros == null )
			this.micros = new ArrayList<String>();
		
		this.micros.add( micro );
	}
	
	public int getNumOfSlide() {
		return numOfSlide;
	}

	public void setNumOfSlide(int numOfSlide) {
		this.numOfSlide = numOfSlide;
	}
	
	public void addDiagnosis ( SDiagnosis diagnosis ) {
		if ( diagnosies == null )
			diagnosies = new ArrayList<SDiagnosis>();
		
		diagnosies.add( diagnosis );
	}
	
	public void addDiagnosies ( ArrayList<SDiagnosis> diagnosies ) {
		if ( this.diagnosies == null )
			this.diagnosies = new ArrayList<SDiagnosis>();
	
		this.diagnosies.addAll( diagnosies );
	}
	
	public void setDiagnosies ( ArrayList<SDiagnosis> diagnosies ) {
		this.diagnosies = diagnosies;
	}
	
	public ArrayList<SDiagnosis> getDiagnosies() {
		return diagnosies;
	}
	
	public SDiagnosis getLastDiagnosis() {
		if ( this.diagnosies == null || this.diagnosies.size() - 1 < 0 )
			return null;
			
		return this.diagnosies.get( this.diagnosies.size() - 1 );
	}	
		
	public PADiagnosis getMappedPADiagnosis() {
		return mappedPADiagnosis;
	}

	public void setMappedPADiagnosis(PADiagnosis mappedPADiagnosis) {
		this.mappedPADiagnosis = mappedPADiagnosis;
	}

	public MorphometricAnalysis getMappedMorphometricAnalysis() {
		return mappedMorphometricAnalysis;
	}

	public void setMappedMorphometricAnalysis(
			MorphometricAnalysis mappedMorphometricAnalysis) {
		this.mappedMorphometricAnalysis = mappedMorphometricAnalysis;
	}
	
	public ArrayList<MicroValue> getMvs() {
		return mvs;
	}

	public void setMvs(ArrayList<MicroValue> mvs) {
		this.mvs = mvs;
	}
	
	public void addMvs(ArrayList<MicroValue> mvs) {
		this.mvs.addAll( mvs );
	}
	
	public void addMv(MicroValue mv) {
		this.mvs.add( mv );
	}	
	
	public ArrayList<SlideKey> getSlideKeys() {
		return slideKeys;
	}
	
	public void setSlideKeys( ArrayList<SlideKey> slideKeys ) {
		this.slideKeys = slideKeys;
	}
	
	public void addSlideKeys( ArrayList<SlideKey> slideKeys ) {
		if ( this.slideKeys == null )
			this.slideKeys = new ArrayList<SlideKey>();
		
		this.slideKeys.addAll( slideKeys );
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	public Object clone() throws CloneNotSupportedException {
		SurgeonDocument sd = (SurgeonDocument)super.clone();
		return sd;
	}	
}
