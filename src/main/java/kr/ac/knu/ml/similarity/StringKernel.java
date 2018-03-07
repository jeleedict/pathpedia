/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.similarity;

import java.util.HashMap;

/** 
 * "Subsequence Kernels for Relation Extraction" (NIPS 2005).
 * 
 */
public class StringKernel extends HashMap<String, Double>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final boolean DEFAULT_NORMALIZE_CASE = false;
	static final boolean DEFAULT_NORMALIZE = true;
	static final double DEFAULT_LAMBDA = 0.5;
	static final int DEFAULT_LENGTH = 2; 
	static final boolean DEFAULT_CACHE = true; 

	boolean normalizeCase; 
	boolean normalize;
	double lambda;
	int n;
	int ngram;
	boolean cache; 
	
	/**		 
		 @param lam 0-1 penalty for gaps between matches.
		 @param length max length of subsequences to compare
		 @param cache true if we should cache previous kernel computations. recommended!
	 */
	public StringKernel (boolean normalize, double lam, int length,  boolean cache) {
		this.normalize = normalize;
		this.lambda = lam;
		this.n = length;		
		this.cache = cache;
	}

	public StringKernel () {
		this(DEFAULT_NORMALIZE, DEFAULT_LAMBDA, DEFAULT_LENGTH, DEFAULT_CACHE);
	}
	
	public StringKernel (double lam, int length) {
		this (DEFAULT_NORMALIZE, lam, length,  DEFAULT_CACHE);
	}
	
	public void setLambda( double lam ) {
		this.lambda = lam;
	}
	
	public void setLength( int length ) {
		this.n = length;
	}
	
	public double K (String s, String t) {
		// compute self kernels if not in hashmap
		
		double ss,tt;
		Double sstmp = (Double)get (s);
		Double tttmp = (Double)get (t);
		
		if (sstmp == null) {
			ss = stringKernel (s,s,n,lambda);
			if (cache)
				put (s, new Double (ss));
		}
		else
			ss = sstmp.doubleValue();
		
		if (tttmp == null) {
			tt = stringKernel (t,t,n,lambda);
			if (cache)
				put (t, new Double (tt));
		}
		else
			tt = tttmp.doubleValue();
	
		double st = stringKernel (s,t,n,lambda);		
		
		assert ss != 0;
		assert tt != 0;
		
		if (st == 0)
			return 0;

		// normalize
		if ( normalize == true ) 
			return st / Math.sqrt (ss*tt);
		else
			return st;		
	}
	/** 
	 * The algorithm corresponds to the recursive computation from Figure 1
	 * in the paper "Subsequence Kernels for Relation Extraction" (NIPS 2005),
	 * 
	 * where:
	 * - K stands for K;
	 * - Kp stands for K';
	 * - Kpp stands for K'';
	 * - common stands for c;
	 */
	private double stringKernel(String s, String t, int n, double lambda)
	{
		int sl = s.length();
		int tl = t.length();

		double [][][]Kp = new double[n + 1][sl+1][tl+1];
		
		for (int j = 0; j < sl; j++)
			for (int k = 0; k < tl; k++)
				Kp[0][j][k] = 1;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < sl; j++) {
				double Kpp = 0.0;
				for (int k = 0; k < tl; k++) {	
					if ( s.charAt(j) == t.charAt(k) )
						Kpp = lambda * (Kpp + lambda * Kp[i][j][k]);
					else
						Kpp = lambda * Kpp;
					Kp[i + 1][j + 1][k + 1] = lambda * Kp[i + 1][j][k + 1] + Kpp;				
				}
			}		
		}
		
		double[] K = new double[n];
		for (int i = 0; i < K.length; i++) {			
			K[i] = 0.0;			
			for (int j = 0; j < sl; j++) {
				for (int k = 0; k < tl; k++)
					if ( s.charAt(j) == t.charAt(k) )
						K[i] += Math.pow(lambda, 2) * Kp[i][j][k];
			}
		}
		return K[n-1];
	}

	private static double stringKernel(String s, String t) {
		int sl = s.length();
		int tl = t.length();

		double [][][]Kp = new double[DEFAULT_LENGTH + 1][sl+1][tl+1];
		
		for (int j = 0; j < sl; j++)
			for (int k = 0; k < tl; k++)
				Kp[0][j][k] = 1;

		for (int i = 0; i < DEFAULT_LENGTH; i++) {
			for (int j = 0; j < sl; j++) {
				double Kpp = 0.0;
				for (int k = 0; k < tl; k++) {	
					if ( s.charAt(j) == t.charAt(k) )
						Kpp = DEFAULT_LAMBDA * (Kpp + DEFAULT_LAMBDA * Kp[i][j][k]);
					else
						Kpp = DEFAULT_LAMBDA * Kpp;
					Kp[i + 1][j + 1][k + 1] = DEFAULT_LAMBDA * Kp[i + 1][j][k + 1] + Kpp;				
				}
			}		
		}
		
		double[] K = new double[DEFAULT_LENGTH];
		for (int i = 0; i < K.length; i++) {			
			K[i] = 0.0;			
			for (int j = 0; j < sl; j++) {
				for (int k = 0; k < tl; k++)
					if ( s.charAt(j) == t.charAt(k) )
						K[i] += Math.pow(DEFAULT_LAMBDA, 2) * Kp[i][j][k];
			}
		}
		return K[DEFAULT_LENGTH-1];
	}
	
	public static double similarity( String s, String t ) {
		double ss = stringKernel (s,s);
		double tt = stringKernel (t,t);					
		double st = stringKernel (s,t);		
		
		if (st == 0)
			return 0;

		return st / Math.sqrt (ss*tt);			
	}
	
	public static void main(String args[]) {
		String sentence1 = "car";
		String sentence2 = "cat";
		
		StringKernel ssk = new StringKernel();			
		ssk.setLength( 2 );
		
		System.out.println ( "The similarity between sentence1 and sentence2 is : " 
				+ ssk.K( sentence1, sentence2 ) );
		
		System.out.println ( similarity ( sentence1, sentence2 ));
	}
}
