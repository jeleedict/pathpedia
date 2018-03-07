/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

import static org.junit.Assert.*;

import org.junit.Test;

public class SlideExtractorTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public static void main(String[] args) {		
		String bioMarkers[] = { "Glial fibrillary acidic protein", "(Fro#1) Glial fibrillary acidic protein", "Synaptophysin (repeat)", 
				"Estrogen receptor (repeat)", "Low molecular weight cytokeratin (CK19)" };
		for ( String bioMarker : bioMarkers ) {
			SlideExtractor.extractSlideFromBioMarker ( bioMarker );
		}
	}
}
