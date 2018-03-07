/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.buffer;

import java.util.ArrayList;

import kr.ac.knu.ml.unit.surgeon.MicroValue;

public class ParseSBuffer extends ParseBuffer {	
	private ArrayList<String> micros;
	private ArrayList<String> slideKeys;
	private ArrayList<MicroValue> mvs;
	
	public ParseSBuffer() {
		super();
		
		micros = new ArrayList<String>();	
		slideKeys = new ArrayList<String>();
		mvs = new ArrayList<MicroValue>();
	}
	
	public void init() {
		super.init();
		
		micros = new ArrayList<String>();	
		slideKeys = new ArrayList<String>();
		mvs = new ArrayList<MicroValue>();
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
	public MicroValue getMV(int idx) {
		return this.mvs.get( idx );
	}
	
	
	public ArrayList<String> getSlideKeys() {
		return slideKeys;
	}
	
	public void setSlideKeys(ArrayList<String> slideKeys) {
		this.slideKeys = slideKeys;
	}
		
	public void addSlideKye(String slideKey) {
		this.slideKeys.add( slideKey );
	}
	

	public ArrayList<String> getMicros() {
		return micros;
	}

	public void setMicros(ArrayList<String> micros) {
		this.micros = micros;
	}
	
	public void addMicro(String micro) {
		this.micros.add( micro );
	}
	
}
