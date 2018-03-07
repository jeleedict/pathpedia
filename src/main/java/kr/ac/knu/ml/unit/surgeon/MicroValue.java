/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.surgeon;

import java.util.ArrayList;

public class MicroValue {
	private int idx;		// idx
	private int relativeIdx;
	private String str;		// str
	private int depth;		// current tree depth
	private int parentIdx;	// parent idx
	private ArrayList<Integer> childIndices;
	private int indentation;	
	
	private boolean startWithDigitSymbol;
	private boolean startWithCharacterSymbol;
	
	private String startDigitSymbol;
	private String startCharacterSymbol;
	
	private ArrayList<MicroValueType> types;
	private boolean processed;
		
	public MicroValue(int idx, int relativeIdx, String str, int depth, int indentation, 
			boolean startWithDigitSymbol, boolean startWithCharacterSymbol, String startDigitSymbol, String startCharacterSymbol ) {
		super();
		
		this.idx = idx;
		this.relativeIdx = relativeIdx;
		this.str = str;
		this.depth = depth;
		this.indentation = indentation;
		
		this.startWithDigitSymbol = startWithDigitSymbol;
		this.startWithCharacterSymbol = startWithCharacterSymbol;
		
		this.startDigitSymbol = startDigitSymbol;
		this.startCharacterSymbol = startCharacterSymbol;
		this.processed = false;		
	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}	
	public int getRelativeIdx() {
		return relativeIdx;
	}
	public void setRelativeIdx(int relativeIdx) {
		this.relativeIdx = relativeIdx;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getParentIdx() {
		return parentIdx;
	}
	public void setParentIdx(int parentIdx) {
		this.parentIdx = parentIdx;
	}
	public int getIndentation() {
		return indentation;
	}
	public void setIndentation(int indentation) {
		this.indentation = indentation;
	}
	
	
	public boolean isStartWithSymbols() {
		if ( startWithDigitSymbol == true || startWithCharacterSymbol == true )
			return true;
		else
			return false;
	}
	
	public boolean isStartWithDigitSymbol() {
		return startWithDigitSymbol;
	}
	public void setStartWithDigitSymbol(boolean startWithDigitSymbol) {
		this.startWithDigitSymbol = startWithDigitSymbol;
	}
	public String getDigitSymbol() {
		return startDigitSymbol;
	}
	public void setDigitSymbol(String startDigitSymbol) {
		this.startDigitSymbol = startDigitSymbol;
	}
	
	
	public boolean isStartWithCharacterSymbol() {
		return startWithCharacterSymbol;
	}
	public void setStartWithCharacterSymbol(boolean startWithCharacterSymbol) {
		this.startWithCharacterSymbol = startWithCharacterSymbol;
	}
	public String getCharacterSymbol() {
		return startCharacterSymbol;
	}
	public void setCharcterSymbol(String startCharacterSymbol) {
		this.startCharacterSymbol = startCharacterSymbol;
	}
	
	
	public ArrayList<MicroValueType> getTypes() {
		return types;
	}
	public void addType(MicroValueType type) {
		if ( this.types == null )
			this.types = new ArrayList<MicroValueType>();		
		this.types.add( type );
	}	
	public ArrayList<Integer> getChildIndices() {
		return childIndices;
	}
	public void addChild(int childIdx) {
		if ( this.childIndices == null )
			this.childIndices = new ArrayList<Integer>();
		
		this.childIndices.add( childIdx );
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append ( "MicroValue [idx=" + idx + ", relativeIdx=" + relativeIdx + ", depth=" + depth + ", "
				+ "parentIdx=" + parentIdx + ", indentation=" + indentation + ", "
				+ "startWithDigitSymbol=" + startWithDigitSymbol + ", DigitSymbol=" + startDigitSymbol + ", " 
				+ "startWithCharacterSymbol=" + startWithCharacterSymbol + ", CharacterSymbol=" + startCharacterSymbol +"]\n" );
		sb.append ( "\tStr : " + str + "\t" );
		sb.append ( "Children : " + childIndices );
		return sb.toString();		
	}
}
