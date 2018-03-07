/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

/**
 * @author Hyun-Je
 *
 */
public class PATableEntity implements Cloneable {
	private String str;
	private int indentation;
	private int length;
	
	public PATableEntity( String str, int indentation, int length ) {
		this.str = str;
		this.indentation = indentation;
		this.length = length;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	public void addStr(String str) {
		String tmp = this.str + " " + str;		
		this.str = tmp.trim();
	}

	public int getIndentation() {
		return indentation;
	}

	public void setIndentation(int indentation) {
		this.indentation = indentation;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public String toString() {
		return str;
	}
}
