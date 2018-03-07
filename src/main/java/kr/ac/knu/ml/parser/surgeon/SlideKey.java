/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

/**
 * 외과병리보고서에 존재하는 슬라이드 키를 다루는 클래스
 * 
 * @author Hyun-Je
 *
 */
public class SlideKey {
	private String slide;
	private String name;
	
	public SlideKey(String slide, String name) {
		this.slide = slide;
		this.name = name;
	}
	
	public String getSlide() {
		return slide;
	}
	public void setSlide(String slide) {
		this.slide = slide;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "SlideKey [slide=" + slide + ", name=" + name + "]";
	}
}
