/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.surgeon;

import java.util.ArrayList;


/**
 * 
 * @author Hyun-Je Song
 *
 */
public class SDiagnosis implements Cloneable{
	private Organ organ;
	private ArrayList<Disease> diseases;		
	private String clinicalDiagnosisName;      
	private String location;
	private String size;
	private String snomed;
	private String depthOfInvasion;
	private String grossType;
	private String lymphNode;
	private String historicalType;
	private String DCIS;
	private String cellType;
	private String errorStr;

	public SDiagnosis() {
		organ = null;
		diseases = null;
		clinicalDiagnosisName = null;
		location = null;
		size = null;
		snomed = null;
		depthOfInvasion = null;
		grossType = null;
		lymphNode = null;
		historicalType = null;
		DCIS = null;
		cellType = null;
		errorStr = null;
	}
	
	public void clear() {
		organ = null;
		diseases = null;
		clinicalDiagnosisName = null;
		location = null;
		size = null;
		snomed = null;
		depthOfInvasion = null;
		grossType = null;
		lymphNode = null;
		historicalType = null;
		DCIS = null;
		cellType = null;
		errorStr = null;
	}
	
	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public void setDiseases( ArrayList<Disease> diseases ) {
		this.diseases = diseases;
	}
	
	public void addDisease( Disease disease ) {
		if ( this.diseases == null )
			this.diseases = new ArrayList<Disease>();
		
		this.diseases.add( disease );
	}
	
	public void addDiseases( ArrayList<Disease> diseases ) {
		if ( this.diseases == null )
			this.diseases = new ArrayList<Disease>();
		
		this.diseases.addAll( diseases );
	}
	
	public void changeDisease( int idx, Disease disease ) {
		this.diseases.set(idx, disease );
	}
	
	public ArrayList<Disease> getDiseases() {
		return diseases;
	}
	
	public Disease getLastDisease() {
		if ( this.diseases == null || this.diseases.size() - 1 < 0 )
			return null;
		
		return this.diseases.get( this.diseases.size() - 1 );
	}
	
	public int getLastDiseaseIdx() {
		return this.diseases.size() - 1;
	}

	public String getClinicalDiagnosisName() {
		return clinicalDiagnosisName;
	}

	public void setClinicalDiagnosisName(String clinicalDiagnosisName) {
		this.clinicalDiagnosisName = clinicalDiagnosisName;
	}

	public void setLocation( String location ) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setSize( String size ) {
		this.size = size;
	}
	
	public String getSize() {
		return size;
	}
	
	public String getSnomed() {
		return snomed;
	}

	public void setSnomed(String snomed) {
		this.snomed = snomed;
	}
	
	public String getDepthOfInvasion() {
		return depthOfInvasion;
	}

	public void setDepthOfInvasion(String depthOfInvasion) {
		this.depthOfInvasion = depthOfInvasion;
	}

	public String getGrossType() {
		return grossType;
	}

	public void setGrossType(String grossType) {
		this.grossType = grossType;
	}

	public String getLymphNode() {
		return lymphNode;
	}

	public void setLymphNode(String lymphNode) {
		this.lymphNode = lymphNode;
	}
	
	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
	
	public String getHistoricalType() {
		return historicalType;
	}

	public void setHistoricalType(String historicalType) {
		this.historicalType = historicalType;
	}

	public String getDCIS() {
		return DCIS;
	}

	public void setDCIS(String dCIS) {
		DCIS = dCIS;
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	public Object clone() throws CloneNotSupportedException {
		SDiagnosis sd = (SDiagnosis)super.clone();
		return sd;
	}

	@Override
	public String toString() {
		return "SDiagnosis [organ=" + organ + ", diseases=" + diseases
				+ ", clinicalDiagnosisName=" + clinicalDiagnosisName
				+ ", location=" + location + ", size=" + size + ", snomed="
				+ snomed + ", depthOfInvasion=" + depthOfInvasion
				+ ", grossType=" + grossType + ", lymphNode=" + lymphNode
				+ ", cellType=" + cellType + "]";
	}		
}
