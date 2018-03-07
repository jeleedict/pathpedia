/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.buffer;

/**
 * 파싱시 idx를 관리하는 베이스 클래스
 * 
 * @author Hyun-Je
 *
 */
public class ParseBuffer {
	private int idx;
	
	public ParseBuffer() {
		idx = 0;
	}	
	public int getIdx() {
		return idx;
	}
	public int getAddedIdx() {
		return ++idx;
	}
	public int getSubIdx() {
		return idx - 1;
	}	
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void addIdx(int idx) {
		this.idx += idx;
	}
	public void init() {
		idx = 0;
	}
	public void addIdx() {
		idx++;
	}
	public void subIdx() {
		idx--;
	}
	
	@Override
	public String toString() {
		return "ParseBuffer [idx=" + idx + "]";
	}
}
