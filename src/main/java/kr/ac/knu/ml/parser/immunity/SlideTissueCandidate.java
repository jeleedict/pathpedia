/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.immunity;

public class SlideTissueCandidate {
	private String originalSurfaceForm;
	private boolean isparsed;
	private String additionalInfo;
	
	public SlideTissueCandidate( String originalSurfaceForm, boolean isparsed ) {
		this.originalSurfaceForm = originalSurfaceForm;
		this.isparsed = isparsed;
	}

	public String getOriginalSurfaceForm() {
		return originalSurfaceForm;
	}

	public void setOriginalSurfaceForm(String originalSurfaceForm) {
		this.originalSurfaceForm = originalSurfaceForm;
	}

	public boolean isIsparsed() {
		return isparsed;
	}

	public void setIsparsed(boolean isparsed) {
		this.isparsed = isparsed;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String toString() {
		return "SlideTissueCandidate [originalSurfaceForm="
				+ originalSurfaceForm + ", isparsed=" + isparsed
				+ ", additionalInfo=" + additionalInfo + "]";
	}
}
