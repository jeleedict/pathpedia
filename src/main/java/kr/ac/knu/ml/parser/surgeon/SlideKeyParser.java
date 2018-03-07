/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

import java.util.ArrayList;

import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.SurgeonDocument;

/**
 * 외과병리보고서에서의 슬라이드 키들을 파싱하는 클래스
 * 
 * @author Hyun-Je
 *
 */
public class SlideKeyParser {
	private final static String slideKeyIndicators[] = {"(slide key:", "slide key:"};  
	
	public void analysisSlideKey( SurgeonDocument sdoc ) throws NullPointerException {
		ArrayList<String> slideKeys = sdoc.getSlideKeyStrs();		
		for ( String slideKey : slideKeys ) {
			ArrayList<SlideKey> analysisKeys = slidesSplitor( slideKey );
			sdoc.addSlideKeys( analysisKeys );
		}			
	}
	
	public void analysisSlideKey( ChildSurgeonDocument csdoc ) throws NullPointerException {
		ArrayList<String> slideKeys = csdoc.getSlideKeyStrs();		
		for ( String slideKey : slideKeys ) {
			ArrayList<SlideKey> analysisKeys = slidesSplitor( slideKey );
			csdoc.addSlideKeys( analysisKeys );
		}			
	}
	
	public ArrayList<SlideKey> slidesSplitor ( String line ) {
		ArrayList<SlideKey> slides = new ArrayList<SlideKey>();
		
		for ( String slideKeyIndicator : slideKeyIndicators ) {
			if ( line.startsWith( slideKeyIndicator ) ) { 
				line = line.substring( slideKeyIndicator.length() );
				break;
			}
		}
		
		int count = 0;
		int startIdx = 0;		
		boolean slideKey = true;
		String slide = null;
		String name = null;
		
		for ( int i = 0; i < line.length(); i++ ) {
			char ch = line.charAt( i );
			
			if ( ch == '(' ) 
				count++;
			else if ( ch == ')' ) {
				if ( count != 0 )
					count--;			
			}
			else if ( ch == ':' && slideKey == true ) {
				slide = line.substring( startIdx, i ).trim();
				startIdx = i + 1;
				slideKey = false;
			}
			else if ( ch == ',' && slideKey == false ) {
				if ( count != 0 )
					continue;
				name = line.substring( startIdx, i ).trim();
				SlideKey sk = new SlideKey(slide, name);
				slides.add( sk );
				
				startIdx = i + 1;
				slideKey = true;
				slide = null;
				name = null;
			}
		}
		
		name = line.substring( startIdx ).trim();		
		if ( name.length() == 0 || slide == null )
			return slides;
		
		if ( name.charAt( name.length() - 1 ) == ')' ) {
			name = name.substring( 0, name.length() - 1);			
		}
		
		SlideKey sk = new SlideKey(slide, name);
		slides.add( sk );
		
		return slides;
	}
}
