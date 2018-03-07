/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.similarity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wcohen.ss.SoftTFIDF;

/**
 * William SoftTFIDF interface
 * Needs to round half_up (0.9999999999 -> 1, 1.0000000000002 -> 1 ) 
 * @author Hyun-Je
 *
 */
public class SoftTFIDFDistance {
	public static double similarity( String s, String t ) {
		SoftTFIDF dist = new SoftTFIDF();
		return round(dist.score(s, t), 10);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) 
	    	throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
