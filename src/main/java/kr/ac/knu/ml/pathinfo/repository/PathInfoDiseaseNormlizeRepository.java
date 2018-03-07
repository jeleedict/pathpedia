/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.ac.knu.ml.common.unit.Pair;
import kr.ac.knu.ml.pathinfo.unit.PathInfoDiseaseNormalize;

public class PathInfoDiseaseNormlizeRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoDiseaseNormalize> normlize;

	static final Comparator<PathInfoDiseaseNormalize> DiseaseNormalizeComparalbe = new Comparator<PathInfoDiseaseNormalize>() {
		public int compare(PathInfoDiseaseNormalize pidn1,
				PathInfoDiseaseNormalize pidn2) {
			return pidn1.getExtracted_diseaseName().toLowerCase()
					.compareTo(pidn2.getExtracted_diseaseName().toLowerCase());
		}
	};

	public <E> void constructRepository(ArrayList<E> normlize) {
		this.normlize = new ArrayList<PathInfoDiseaseNormalize>();

		for (E e : normlize) {
			if (e instanceof String[]) {
				PathInfoDiseaseNormalize pidn = new PathInfoDiseaseNormalize(
						(String[]) e);
				this.normlize.add(pidn);
			} else if (e instanceof PathInfoDiseaseNormalize) {
				this.normlize.add((PathInfoDiseaseNormalize) e);
			}
		}
	}

	public String getMapped_UMLSdiseaseID(int idx) {
		return normlize.get(idx).getMapped_UMLSdiseaseID();
	}

	public Pair<Boolean, Integer> contains(String disaseName) {
		disaseName = disaseName.trim();
		PathInfoDiseaseNormalize pidn = new PathInfoDiseaseNormalize(disaseName);

		int idx = Collections.binarySearch(normlize, pidn,
				DiseaseNormalizeComparalbe);
		boolean ret = false;
		if (idx >= 0)
			ret = true;

		Pair<Boolean, Integer> tuple = new Pair<Boolean, Integer>(ret, idx);
		return tuple;
	}

	public Pair<Boolean, Integer> contains2(String disaseName) {
		disaseName = disaseName.trim();
		PathInfoDiseaseNormalize pidn = new PathInfoDiseaseNormalize(disaseName);

		int idx = Collections.binarySearch(normlize, pidn,
				DiseaseNormalizeComparalbe);
		boolean ret = false;
		if (idx >= 0)
			ret = true;

		Pair<Boolean, Integer> tuple = new Pair<Boolean, Integer>(ret, idx);
		return tuple;
	}
}
