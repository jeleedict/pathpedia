/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.unit.immunity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.common.utils.StringUtils;

/**
 *
 * 
 * @author Hyun-Je
 *
 */
public class BioMarker {
	private String markerName;
	private String markerNameString;
	private String representativeMarkerName;
	private boolean isMapped;
	private String value;
	private String normalizedValue;
	private boolean addendum;
	private String addendumDate;
	private String slideKeyInfo;
	private String slideAdditionalInfo;
	private Confidence confidence;
	private boolean fromTable;
	private ArrayList<BioMarkerValue> analyzedValue;	
	
	public BioMarker(String markerName, String value){
		this ( markerName, value, false, Confidence.FAIR, null );
	}
	
	public BioMarker(String markerName, String value, boolean fromTable){
		this ( markerName, value, fromTable, Confidence.FAIR, null );
	}
	
	public BioMarker(String markerName, String value, Confidence confidence){
		this( markerName, value, false, confidence );
	}
	
	public BioMarker(String markerName, String value, boolean fromTable, Confidence confidence ){
		this ( markerName, value, fromTable, confidence, null );
	}

	public BioMarker(String markerName, String value, boolean fromTable, Confidence confidence, String slideKeyInfo ){
		this ( markerName, value, fromTable, confidence, slideKeyInfo, null );
	}
	
	public BioMarker(String markerName, String value, boolean fromTable, Confidence confidence, String slideKeyInfo, String slideAdditionalInfo ){
		this.markerName = markerName;
		if ( value.endsWith( "(SEE NOTE)" )) 
			this.value = value.substring(0, value.indexOf("(SEE NOTE)") );
		else 
			this.value = value;

		this.fromTable = fromTable;
		this.confidence = confidence;
		this.slideKeyInfo = slideKeyInfo;
		this.slideAdditionalInfo = slideAdditionalInfo;
		this.analyzedValue = new ArrayList<BioMarkerValue>();
	}
	
	public String getMarkerName() {
		return markerName;
	}

	public void setMarkerName(String markerName) {
		this.markerName = markerName;
	}

	public String getMarkerNameString() {
		return markerNameString;
	}

	public void setMarkerNameString(String markerNameString) {
		this.markerNameString = markerNameString;
	}

	public String getRepresentativeMarkerName() {
		return representativeMarkerName;
	}

	public void setRepresentativeMarkerName(String representativeMarkerName) {
		this.representativeMarkerName = representativeMarkerName;
	}

	public boolean isMapped() {
		return isMapped;
	}

	public void setMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}

	public void setNormalizedValue(String normalizedValue) {
		this.normalizedValue = normalizedValue;
	}
	
	public boolean getAddendum() {
		return addendum;
	}

	public void setAddendum(boolean addendum) {
		this.addendum = addendum;
	}	

	public String getAddendumDate() {
		return addendumDate;
	}

	public void setAddendumDate(String addendumDate) {
		this.addendumDate = addendumDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		valueAnalysis();
	}

	public void setConfidence( Confidence confidence ) {
		this.confidence = confidence;
	}
	
	public String getConfidence() {
		return confidence.name();
	}
	
	public String getNormalizedValue() {
		return normalizedValue;
	}
	
	public boolean isFromTable() {
		return fromTable;
	}
	
	public void setFromTable( boolean fromTable ) {
		this.fromTable = fromTable;
	}
	
	public String getDegree() {
		if ( analyzedValue.size() > 0 )
			return analyzedValue.get(0).getDegree();
		return null;
	}
	
	public String getPolarity() {
		if ( analyzedValue.size() > 0 )
			return analyzedValue.get(0).getPolarity();
		return null;
	}
	
	public String getLocation() {
		if ( analyzedValue.size() > 0 )
			return analyzedValue.get(0).getLocation();
		return null;
	}
	
	public boolean hasSupplementDegree() {
		if ( analyzedValue.size() > 1 )
			return true;
		return false;
	}
	
	public String getSupplementDegree() {
		return analyzedValue.get(1).getDegree();
	}
	
	public String getSupplementPolarity() {
		return analyzedValue.get(1).getPolarity();
	}
	
	public String getSupplementLocation() {
		return analyzedValue.get(1).getLocation();
	}
	
	public String getSlideKeyInfo() {
		return slideKeyInfo;
	}

	public void setSlideInfo(String slideKeyInfo) {
		this.slideKeyInfo = slideKeyInfo;
	}
	
	public String getSlideAdditionalInfo() {
		return slideAdditionalInfo;
	}

	public void setSlideAdditionalInfo(String slideAdditionalInfo) {
		this.slideAdditionalInfo = slideAdditionalInfo;
	}

	public void normalization() {
		Pattern fullPercentage = Pattern.compile( "(\\d*\\d\\.)?\\d*\\d%(\\s*(~|-)*\\s*)(\\d*\\d\\.)?\\d*\\d%");
		Matcher matcher = fullPercentage.matcher( value );
		boolean isMatch = false;
		while ( matcher.find() ) {
			normalizedValue = matcher.replaceAll( "숫자% ~ 숫자%");
			isMatch = true;
		}
	
		Pattern variationfullPercentage = Pattern.compile( "(\\d*\\d\\.)?\\d*\\d(\\s*(~|-)\\s*)(\\d*\\d\\.)?\\d*\\d%");
		matcher = variationfullPercentage.matcher( value );
		while ( matcher.find() && !isMatch ) {
			normalizedValue = matcher.replaceAll( "숫자% ~ 숫자%");
			isMatch = true;
		}
		
		Pattern percentage = Pattern.compile( "(\\d*\\d\\.)?\\d*\\d\\s*%");
		matcher = percentage.matcher( value );
		while ( matcher.find() && !isMatch ) {
			normalizedValue = matcher.replaceAll( "숫자%");
			isMatch = true;
		}
		
		if ( !isMatch )
			normalizedValue = value;
		
		Pattern ratio = Pattern.compile( "\\(\\s*\\d+\\s*/\\s*\\d+\\s*\\)");
		matcher = ratio.matcher( normalizedValue );
		while ( matcher.find() ) {
			normalizedValue = matcher.replaceAll( "개수/전체개수");
		}
		
		Pattern ratioWithoutParenthesis = Pattern.compile( "\\s*\\d+\\s*/\\s*\\d+\\s*");
		matcher = ratioWithoutParenthesis.matcher( normalizedValue );
		while ( matcher.find() ) {
			normalizedValue = matcher.replaceAll( "개수/전체개수");
		}
		
		normalizedValue = normalizedValue.trim();
		normalizedValue = normalizedValue.replaceAll("\\s+", " ");
	}
	
	public void valueAnalysis() {
		Pattern parenthesisPattern = Pattern.compile( "\\(.+\\)");
		Pattern polarityPattern = Pattern.compile( "POSITIVE|NEGATIVE");		
		Matcher matcher = parenthesisPattern.matcher( value );
		
		boolean matched = false;		
		while ( matcher.find() ) {
			int startIdx = matcher.start();
			int endIdx = matcher.end();
			Matcher polarityMatcher = polarityPattern.matcher( matcher.group() );
			while ( polarityMatcher.find() ) {
				if ( startIdx ==  0)
					continue;
				
				analyzedValue.add( StringUtils.getBioMarkerValue( value.substring( 0, startIdx - 1)) ); 
				analyzedValue.add( StringUtils.getBioMarkerValue( value.substring( startIdx + 1, endIdx - 1)) );
				matched = true;
			}
		}
		if ( !matched )
			analyzedValue.add( StringUtils.getBioMarkerValue( value ) );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "MakerName : " + markerName + "\tValue : " + value + "\tAddendum : " + addendum + "\n");
		return sb.toString();
	}
}
