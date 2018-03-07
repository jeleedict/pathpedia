/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.backup;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.ml.common.unit.Pair;
import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.pathinfo.repository.PathInfoDiseaseNormlizeRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoDiseaseRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoOrganNormalizeRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoUMLS_DiseaseStringRepository;
import kr.ac.knu.ml.unit.surgeon.Disease;

public class CopyOfDiseaseAnalyzer {
	private static PathInfoDiseaseRepository pidr;
	private static PathInfoUMLS_DiseaseStringRepository piumls_dsr;
	private static PathInfoOrganNormalizeRepository pionr;
	private static PathInfoDiseaseNormlizeRepository pidnr;
	private static HashMap<String, Disease> dieseaseCash;

	private final static String startSymbols[] = {
			"consistent with metastatic", "consistent with involvement of",
			"consistent with", "most-likely metastatic", "most-likely",
			"most likely", "metastatic", "involvement of", "involvement",
			"suspicious for metastatic", "suspicious for", "suspicious",
			"suggestive of", "favor", "no evidence of", "there is no", "no " };

	@SuppressWarnings("unchecked")
	public static void deserialize() {
		System.out.println("Deserialzing the thesaurus from 'thesaurus.ser'");
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					"thesaurus.ser"));
			while (true) {
				Object obj = inputStream.readObject();
				if (obj instanceof PathInfoDiseaseRepository)
					pidr = (PathInfoDiseaseRepository) obj;
				else if (obj instanceof PathInfoUMLS_DiseaseStringRepository)
					piumls_dsr = (PathInfoUMLS_DiseaseStringRepository) obj;
				else if (obj instanceof PathInfoOrganNormalizeRepository)
					pionr = (PathInfoOrganNormalizeRepository) obj;
				else if (obj instanceof PathInfoDiseaseNormlizeRepository)
					pidnr = (PathInfoDiseaseNormlizeRepository) obj;
			}
		} catch (EOFException ex) {
			System.out.println("End of file reached.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectInputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("Deserialzing the thesaurus from 'diesease.ser'");
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					"diesease.ser"));
			dieseaseCash = (HashMap<String, Disease>) inputStream.readObject();
		} catch (EOFException ex) {
			System.out.println("End of file reached.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				dieseaseCash = new HashMap<String, Disease>();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static Disease addDiseaseWithStartSymbol(
			String diseaseNameCandidate, String originalDiaseaName,
			String startSymbol, String UMLSdieseaseSui) {
		Disease disease = new Disease();
		if (startSymbol.equals("")) {
			disease.setCandidateDiseaseName(originalDiaseaName);
			disease.setDiseaseName(diseaseNameCandidate);
			String diseaseName = diseaseNameCandidate.toUpperCase();
			if (diseaseName.startsWith("METASTATIC")
					|| diseaseName.endsWith("METASTATIC"))
				disease.setContainMetastatic(true);
			disease.setUMLSdiseaseSui(UMLSdieseaseSui);
			disease.setDiseaseSimilarity(1.0);
		} else if (startSymbol.equals("consistent with metastatic")
				|| startSymbol.equals("consistent with involvement of")
				|| startSymbol.equals("consistent with")
				|| startSymbol.equals("most-likely metastatic")
				|| startSymbol.equals("most-likely")
				|| startSymbol.equals("most likely")
				|| startSymbol.equals("metastatic")
				|| startSymbol.equals("involvement of")
				|| startSymbol.equals("involvement")
				|| startSymbol.equals("suspicious for metastatic")
				|| startSymbol.equals("suspicious for")
				|| startSymbol.equals("suspicious")
				|| startSymbol.equals("favor")
				|| startSymbol.equals("no evidence of")
				|| startSymbol.equals("there is no")
				|| startSymbol.equals("no ")) {

			disease.setCandidateDiseaseName(originalDiaseaName);
			disease.setDiseaseName(diseaseNameCandidate);
			disease.setUMLSdiseaseSui(UMLSdieseaseSui);
			disease.setDiseaseSimilarity(1.0);

			if (startSymbol.equals("consistent with metastatic")) {
				disease.setContainConsistentWith(true);
				disease.setContainMetastatic(true);
			} else if (startSymbol.equals("consistent with involvement of")) {
				disease.setContainConsistentWith(true);
				disease.setContainInvolvement(true);
			} else if (startSymbol.equals("consistent with"))
				disease.setContainConsistentWith(true);
			else if (startSymbol.equals("most-likely metastatic")) {
				disease.setContainMostLikely(true);
				disease.setContainMetastatic(true);
			} else if (startSymbol.equals("most-likely"))
				disease.setContainMostLikely(true);
			else if (startSymbol.equals("most likely"))
				disease.setContainMostLikely(true);
			else if (startSymbol.equals("metastatic"))
				disease.setContainMetastatic(true);
			else if (startSymbol.equals("involvement of"))
				disease.setContainInvolvement(true);
			else if (startSymbol.equals("involvement"))
				disease.setContainInvolvement(true);
			else if (startSymbol.equals("suspicious for metastatic")) {
				disease.setContainSuspicious(true);
				disease.setContainMetastatic(true);
			} else if (startSymbol.equals("suspicious for"))
				disease.setContainSuspicious(true);
			else if (startSymbol.equals("suspicious"))
				disease.setContainSuspicious(true);
			else if (startSymbol.equals("favor"))
				disease.setContainFavor(true);
			else if (startSymbol.equals("no evidence of"))
				disease.setContainNegation(true);
			else if (startSymbol.equals("there is no"))
				disease.setContainNegation(true);
			else if (startSymbol.equals("no "))
				disease.setContainNegation(true);
		}
		return disease;
	}

	private static Disease addDiseaseWithEndSymbol(String diseaseNameCandidate,
			String originalDiaseaName, String endSymbol, String UMLSdieseaseSui) {
		Disease disease = new Disease();
		disease.setCandidateDiseaseName(originalDiaseaName);
		disease.setDiseaseName(diseaseNameCandidate);
		String diseaseName = diseaseNameCandidate.toUpperCase();
		if (diseaseName.startsWith("METASTATIC")
				|| diseaseName.endsWith("METASTATIC"))
			disease.setContainMetastatic(true);
		disease.setUMLSdiseaseSui(UMLSdieseaseSui);
		disease.setDiseaseSimilarity(1.0);
		return disease;
	}

	private static Pair<Boolean, Disease> matchingDiseaseWithStartSymbol(
			String diseaseNameCandidate, String originalDiaseaName,
			String startSymbol) {
		Pair<Boolean, Integer> tuples = pidr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithStartSymbol(diseaseNameCandidate,
					originalDiaseaName, startSymbol,
					pidr.getDiseaseID(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}
		tuples = pidnr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithStartSymbol(diseaseNameCandidate,
					originalDiaseaName, startSymbol,
					pidnr.getMapped_UMLSdiseaseID(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}

		tuples = piumls_dsr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithStartSymbol(diseaseNameCandidate,
					originalDiaseaName, startSymbol,
					piumls_dsr.getDisorderSui(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}
		return new Pair<Boolean, Disease>(false, null);
	}

	private static Pair<Boolean, Disease> matchingDiseaseWithEndSymbol(
			String diseaseNameCandidate, String originalDiaseaName,
			String endSymbol) {
		Pair<Boolean, Integer> tuples = pidr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithEndSymbol(diseaseNameCandidate,
					originalDiaseaName, endSymbol,
					pidr.getDiseaseID(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}

		tuples = pidnr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithEndSymbol(diseaseNameCandidate,
					originalDiaseaName, endSymbol,
					pidnr.getMapped_UMLSdiseaseID(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}

		tuples = piumls_dsr.contains(diseaseNameCandidate);
		if (tuples.getA() == true) {
			Disease disease = addDiseaseWithEndSymbol(diseaseNameCandidate,
					originalDiaseaName, endSymbol,
					piumls_dsr.getDisorderSui(tuples.getB()));
			Pair<Boolean, Disease> tuple = new Pair<Boolean, Disease>(true,
					disease);
			return tuple;
		}
		return new Pair<Boolean, Disease>(false, null);
	}

	// 추후에 regular expression pattern matching으로 바꿀 것!
	private static String seeNoteProcessor(String diseaseNameCandidate) {
		if (diseaseNameCandidate.endsWith("(see note) with")) {
			diseaseNameCandidate = diseaseNameCandidate.replace(
					"(see note) with", "").trim();
		} else if (diseaseNameCandidate.endsWith("(see note 1):")) {
			diseaseNameCandidate = diseaseNameCandidate.replace(
					"(see note 1):", "").trim();
		} else if (diseaseNameCandidate.endsWith("(see note 1)")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("(see note 1)",
					"").trim();
		} else if (diseaseNameCandidate.endsWith("(see note 2)")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("(see note 2)",
					"").trim();
		} else if (diseaseNameCandidate.endsWith("(see note).")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("(see note).",
					"").trim();
		} else if (diseaseNameCandidate.endsWith("(see note),")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("(see note),",
					"").trim();
		} else if (diseaseNameCandidate.endsWith("(see note)")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("(see note)",
					"").trim();
		}
		return diseaseNameCandidate;
	}

	private static Disease heuristic(String sent, String nextSent, int idx,
			Disease disease) {
		String tmp = null;
		if (idx + 1 < sent.length())
			tmp = sent.substring(idx + 1).trim();

		if (tmp == null || tmp.length() == 0) { // check next sentence!!
			if (nextSent != null && nextSent.trim().startsWith("favor")) {
				Pair<Boolean, Disease> tuple = analysisDiseaseInfo(
						nextSent.trim(), null, false, 1);
				if (tuple.getA() == true)
					return tuple.getB();
			}
		} else {
			if (disease.getDiseaseName().toUpperCase()
					.equals("RENAL CELL CARCINOMA")) {
				int withIdx = sent.lastIndexOf("with");
				if (sent.lastIndexOf("with") != -1) {
					disease.setCandidateDiseaseName(sent.substring(0, withIdx));
					disease.setDiseaseName("RENAL CELL CARCINOMA "
							+ sent.substring(idx + 1, withIdx));
				} else {
					disease.setCandidateDiseaseName(sent);
					disease.setDiseaseName("RENAL CELL CARCINOMA "
							+ sent.substring(idx + 1));
				}
			} else if (disease.getDiseaseName().toUpperCase()
					.equals("NON SMALL CELL CARCINOMA")) {
				Pair<Boolean, Disease> tuple = analysisDiseaseInfo(tmp, null,
						false, 1);
				if (tuple.getA() == true)
					return tuple.getB();

				if (nextSent != null) {
					tuple = analysisDiseaseInfo(nextSent, null, false, 1);
					if (tuple.getA() == true)
						return tuple.getB();
				}
			}
		}
		return disease;
	}

	private static Pair<Boolean, Disease> analysisDiseaseInfo(String sent) {
		return analysisDiseaseInfo(sent, null, true, 0);
	}

	private static Pair<Boolean, Disease> analysisDiseaseInfo(String sent,
			String nextSent) {
		return analysisDiseaseInfo(sent, nextSent, true, 0);
	}

	private static Pair<Boolean, Disease> analysisDiseaseInfo(String sent,
			String nextSent, boolean similaritySearch, int depth) {
		String diseaseNameCandidate = null;

		int idx = sent.indexOf(",");
		if (idx != -1)
			diseaseNameCandidate = sent.substring(0, idx);
		else
			diseaseNameCandidate = sent;

		// more than double space -> single space
		diseaseNameCandidate = diseaseNameCandidate.trim()
				.replaceAll(" +", " ");
		diseaseNameCandidate = seeNoteProcessor(diseaseNameCandidate);

		// 이걸 나중에 하는 이유는 (see note)가 날라가면 애매해지는 경우가 발생함.
		// 나중에 위에 것과 통합해서 하나의 regular expression으로 바꿀 것!
		// remove ( ) contents
		diseaseNameCandidate = diseaseNameCandidate.replaceAll("\\(.*\\)", "");

		// 특수 문자들 제거..
		diseaseNameCandidate = StringUtils
				.removeSpecialCharacter(diseaseNameCandidate);

		// 시작하는 Character들이 대문자로 시작하면 대문자 부분만 가져올 것!
		// 유의미한지 살펴볼 것!
		if (diseaseNameCandidate.length() > 2
				&& Character.isUpperCase(diseaseNameCandidate.charAt(0))
				&& Character.isUpperCase(diseaseNameCandidate.charAt(1))) {

			// 대문자와 띄어쓰기 부분만 가져올 것! TODO: 수정해야 될 부분!!
			String tmp = StringUtils.getCapitalCharacters(diseaseNameCandidate);
			if (!tmp.equals("METASTATIC"))
				diseaseNameCandidate = tmp;
		}

		// 이건 추후에 수정할 필요가 있음. TODO: 이게 현재 수정되어야 할 부분
		if (diseaseNameCandidate.endsWith("with")) {
			diseaseNameCandidate = diseaseNameCandidate.replace("with", "")
					.trim();
		}

		// 수정해야할 것!
		ArrayList<String> organs = pionr.getOrganNames();
		ArrayList<String> organsWithLoation = new ArrayList<String>();
		for (String organ : organs) {
			organsWithLoation.add("in " + organ.toLowerCase());
			organsWithLoation.add("from " + organ.toLowerCase());
		}

		// Disease -> DiseaseNormarlize -> UMLS_DiseaseString
		Pair<Boolean, Disease> tuple = matchingDiseaseWithStartSymbol(
				diseaseNameCandidate, diseaseNameCandidate, "");
		if (tuple.getA() == true) {
			if (depth == 0) {
				Pair<Boolean, Disease> ret = new Pair<Boolean, Disease>(true,
						heuristic(sent, nextSent, idx, tuple.getB()));
				return ret;
			} else
				return tuple;
		}

		String originalDiseaseNameCandidate = diseaseNameCandidate;
		for (String startSymbol : startSymbols) {
			if (diseaseNameCandidate.toLowerCase().startsWith(startSymbol)) {
				diseaseNameCandidate = diseaseNameCandidate.substring(
						startSymbol.length()).trim();
				Pair<Boolean, Disease> tmp = matchingDiseaseWithStartSymbol(
						diseaseNameCandidate, originalDiseaseNameCandidate,
						startSymbol);
				if (tmp.getA() == true) {
					if (depth == 0) {
						Pair<Boolean, Disease> ret = new Pair<Boolean, Disease>(
								true,
								heuristic(sent, nextSent, idx, tmp.getB()));
						return ret;
					} else
						return tmp;
				}
			}
		}

		for (String organ : organsWithLoation) {
			if (diseaseNameCandidate.toLowerCase().endsWith(organ)) {
				diseaseNameCandidate = diseaseNameCandidate.substring(0,
						diseaseNameCandidate.length() - organ.length()).trim();
				if (diseaseNameCandidate
						.charAt(diseaseNameCandidate.length() - 1) == ',')
					diseaseNameCandidate = diseaseNameCandidate.substring(0,
							diseaseNameCandidate.length() - 1);

				Pair<Boolean, Disease> tmp = matchingDiseaseWithEndSymbol(
						diseaseNameCandidate, originalDiseaseNameCandidate,
						organ);
				if (tmp.getA() == true) {
					if (depth == 0) {
						Pair<Boolean, Disease> ret = new Pair<Boolean, Disease>(
								true,
								heuristic(sent, nextSent, idx, tmp.getB()));
						return ret;
					} else
						return tmp;
				}
			}
		}

		Pair<Boolean, Disease> ret;
		if (similaritySearch == true) {
			Disease disease = findMostSimilarDisease(diseaseNameCandidate);
			ret = new Pair<Boolean, Disease>(true, disease);
		} else {
			ret = new Pair<Boolean, Disease>(false, null);
		}
		return ret;
	}

	private static Disease findMostSimilarDisease(String diseaseNameCandidate) {
		// 위에 모든 조건을 만족시키지 못한 경우,
		// 시소로스에 있는 병명이랑 charater-level 유사도를 측정해서 Mapping을 시킴.
		// 속도 향상을 위해 map을 serialize 한 후, 프로그램 실행 시 마다 불러와 사용할 수 있게 함.
		// 향후, analysisDiseaseInfo만 테스트할 수 있는 모듈이 필요할 듯!
		Disease disease = null;
		if (dieseaseCash.containsKey(diseaseNameCandidate))
			disease = dieseaseCash.get(diseaseNameCandidate);
		else {
			disease = piumls_dsr
					.getSimilarDiseaseNameUsingSoftTFIDF(diseaseNameCandidate);
			dieseaseCash.put(diseaseNameCandidate, disease);
		}

		for (String startSymbol : startSymbols) {
			if (diseaseNameCandidate.toLowerCase().startsWith(startSymbol)) {
				if (startSymbol.equals("consistent with metastatic")) {
					disease.setContainConsistentWith(true);
					disease.setContainMetastatic(true);
				} else if (startSymbol.equals("consistent with involvement of")) {
					disease.setContainConsistentWith(true);
					disease.setContainInvolvement(true);
				} else if (startSymbol.equals("consistent with"))
					disease.setContainConsistentWith(true);
				else if (startSymbol.equals("most-likely metastatic")) {
					disease.setContainMostLikely(true);
					disease.setContainMetastatic(true);
				} else if (startSymbol.equals("most-likely"))
					disease.setContainMostLikely(true);
				else if (startSymbol.equals("most likely"))
					disease.setContainMostLikely(true);
				else if (startSymbol.equals("metastatic"))
					disease.setContainMetastatic(true);
				else if (startSymbol.equals("involvement of"))
					disease.setContainInvolvement(true);
				else if (startSymbol.equals("involvement"))
					disease.setContainInvolvement(true);
				else if (startSymbol.equals("suspicious for metastatic")) {
					disease.setContainSuspicious(true);
					disease.setContainMetastatic(true);
				} else if (startSymbol.equals("suspicious for"))
					disease.setContainSuspicious(true);
				else if (startSymbol.equals("suspicious"))
					disease.setContainSuspicious(true);
				else if (startSymbol.equals("favor"))
					disease.setContainFavor(true);
				else if (startSymbol.equals("no evidence of"))
					disease.setContainNegation(true);
				else if (startSymbol.equals("there is no"))
					disease.setContainNegation(true);
				else if (startSymbol.equals("no "))
					disease.setContainNegation(true);
			}
		}
		return disease;
	}

}
