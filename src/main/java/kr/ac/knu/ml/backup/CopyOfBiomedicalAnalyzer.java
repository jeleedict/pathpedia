/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/

package kr.ac.knu.ml.backup;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.ml.common.unit.Pair;
import kr.ac.knu.ml.common.utils.StringUtils;
import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.parser.immunity.SlideParser;
import kr.ac.knu.ml.parser.surgeon.SlideKeyParser;
import kr.ac.knu.ml.pathinfo.repository.PathInfoBioMarkerRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoBioMarkerStringRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoDiseaseNormlizeRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoDiseaseRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoOrganNormalizeRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoOrganRelationRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoOrganRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoRepositoryLoader;
import kr.ac.knu.ml.pathinfo.repository.PathInfoSCT_OrganStringRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoSNUMICROFieldRepository;
import kr.ac.knu.ml.pathinfo.repository.PathInfoUMLS_DiseaseStringRepository;
import kr.ac.knu.ml.unit.immunity.BioMarker;
import kr.ac.knu.ml.unit.immunity.MorphometricAnalysis;
import kr.ac.knu.ml.unit.immunity.PADiagnosis;
import kr.ac.knu.ml.unit.surgeon.Disease;
import kr.ac.knu.ml.unit.surgeon.MicroValue;
import kr.ac.knu.ml.unit.surgeon.Organ;
import kr.ac.knu.ml.unit.surgeon.SDiagnosis;

/**
 * @author Hyun-Je
 * 
 */
public class CopyOfBiomedicalAnalyzer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static PathInfoOrganRepository pior;
	private static PathInfoDiseaseRepository pidr;
	private static PathInfoOrganRelationRepository piorr;
	private static PathInfoBioMarkerRepository pibmr;
	private static PathInfoBioMarkerStringRepository pibmsr;
	private static PathInfoUMLS_DiseaseStringRepository piumls_dsr;
	private static PathInfoSCT_OrganStringRepository pisct_osr;
	private static PathInfoOrganNormalizeRepository pionr;
	private static PathInfoDiseaseNormlizeRepository pidnr;
	private static PathInfoSNUMICROFieldRepository piSMFr;

	private final static Pattern sizePattern = Pattern
			.compile("\\d(.\\d+)*\\s*x\\s*\\d(.\\d+)*\\s*x\\s*\\d(.\\d+)*\\s*cm");
	private final static String startSymbols[] = {
			"consistent with metastatic", "consistent with involvement of",
			"consistent with", "most-likely metastatic", "most-likely",
			"most likely", "metastatic", "involvement of", "involvement",
			"suspicious for metastatic", "suspicious for", "suspicious",
			"suggestive of", "favor", "no evidence of", "there is no", "no " };

	private static HashMap<String, Disease> dieseaseCash;

	public static void serialize() {
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(
					"thesaurus.ser"));

			outputStream.writeObject(pibmr);
			outputStream.writeObject(pibmsr);
			outputStream.writeObject(pior);
			outputStream.writeObject(pidr);
			outputStream.writeObject(piorr);
			outputStream.writeObject(piumls_dsr);
			outputStream.writeObject(pisct_osr);
			outputStream.writeObject(pionr);
			outputStream.writeObject(pidnr);
			outputStream.writeObject(piSMFr);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void dieseaseSerialize() {
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(
					"diesease.ser"));
			outputStream.writeObject(dieseaseCash);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void deserialize() {
		System.out.println("Deserialzing the thesaurus from 'thesaurus.ser'");
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					"thesaurus.ser"));
			pibmr = (PathInfoBioMarkerRepository) inputStream.readObject();
			pibmsr = (PathInfoBioMarkerStringRepository) inputStream
					.readObject();
			pior = (PathInfoOrganRepository) inputStream.readObject();
			pidr = (PathInfoDiseaseRepository) inputStream.readObject();
			piorr = (PathInfoOrganRelationRepository) inputStream.readObject();
			piumls_dsr = (PathInfoUMLS_DiseaseStringRepository) inputStream
					.readObject();
			pisct_osr = (PathInfoSCT_OrganStringRepository) inputStream
					.readObject();
			pionr = (PathInfoOrganNormalizeRepository) inputStream.readObject();
			pidnr = (PathInfoDiseaseNormlizeRepository) inputStream
					.readObject();
			piSMFr = (PathInfoSNUMICROFieldRepository) inputStream.readObject();
		} catch (EOFException ex) { // This exception will be caught when EOF is
			// reached
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
		} catch (EOFException ex) { // This exception will be caught when EOF is
			// reached
			System.out.println("End of file reached.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			System.err.println("There is no diesease cash file");
			dieseaseCash = new HashMap<String, Disease>();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void init(String thesaurusFile) {
		System.out.println("Loading thesaurus from '" + thesaurusFile + "'");
		PathInfoRepositoryLoader loader = new PathInfoRepositoryLoader();
		loader.load(thesaurusFile);

		// Load "Biomarker" Sheet
		pibmr = new PathInfoBioMarkerRepository();
		pibmr.constructRepository(loader.getContents("Biomarker"));

		// Load "BiomarkerString" Sheet
		pibmsr = new PathInfoBioMarkerStringRepository();
		pibmsr.constructRepository(loader.getContents("BiomarkerString"));

		// Load "Organ" Sheet
		pior = new PathInfoOrganRepository();
		pior.constructRepository(loader.getContents("Organ"));

		// Load "Disease" Sheet
		pidr = new PathInfoDiseaseRepository();
		pidr.constructRepository(loader.getContents("Disease"));

		// Load "OrganDiseaseMapping" Sheet
		piorr = new PathInfoOrganRelationRepository();
		piorr.constructRepository(loader.getContents("OrganDiseaseMapping"));

		// Load "SCT_organstring" Sheet
		pisct_osr = new PathInfoSCT_OrganStringRepository();
		pisct_osr.constructRepository(loader.getContents("SCT_organstring"));

		// Load "OrganNomarlize" Sheet
		pionr = new PathInfoOrganNormalizeRepository();
		pionr.constructRepository(loader.getContents("OrganNomarlize"));

		// Load "UMLS_DiseaseString" Sheet
		piumls_dsr = new PathInfoUMLS_DiseaseStringRepository();
		piumls_dsr
				.constructRepository(loader.getContents("UMLS_DiseaseString"));

		// Load "DiseaseNomarlize" Sheet
		pidnr = new PathInfoDiseaseNormlizeRepository();
		pidnr.constructRepository(loader.getContents("DiseaseNomarlize"));

		// Load "SNUMICROField" Sheet
		piSMFr = new PathInfoSNUMICROFieldRepository();
		piSMFr.constructRepository(loader.getContents("SNUMICROField"));
	}

	public static void analysisImmunityDocument(
			ArrayList<ImmunityDocument> idocs) {
		System.out.println("Analyze the ImmunityDocument.");
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		int count = 1;
		for (ImmunityDocument idoc : idocs) {
			if (count++ % 1000 == 0)
				System.out.print(".");

			for (PADiagnosis pa : idoc.getDiagnosises()) {
				try {
					pa.cleansingDiagnosisName();
					pa.setPathologyNum();

					for (BioMarker marker : pa.getMarkers()) {
						String markerName = marker.getMarkerName();
						if (markerName != null
								&& markerName.trim().length() > 0) {

							String pnid = pibmsr
									.getPNIDusingMarkerName(markerName.trim());
							if (pnid != null) {
								marker.setMarkerNameString(pibmsr
										.getStringusingMarkerName(markerName
												.trim()));
								marker.setRepresentativeMarkerName(pibmr
										.getPreferredName(pnid));
								marker.setMapped(true);
							} else {
								pnid = pibmsr
										.getMostSimilarMarkerName(markerName);
								marker.setRepresentativeMarkerName(pibmr
										.getPreferredName(pnid));
							}
							marker.normalization();
							marker.valueAnalysis();
						}
					}
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}

			for (MorphometricAnalysis ma : idoc.getMorphometricAnalysis()) {
				try {
					ma.cleansingDiagnosisName();
					ma.setPathologyNum();

					for (BioMarker marker : ma.getMarkers()) {
						String markerName = marker.getMarkerName();
						if (markerName != null
								&& markerName.trim().length() > 0) {
							String pnid = pibmsr
									.getPNIDusingMarkerName(markerName.trim());
							if (pnid != null) {
								marker.setRepresentativeMarkerName(pibmr
										.getPreferredName(pnid));
								marker.setMarkerNameString(pibmsr
										.getStringusingMarkerName(markerName
												.trim()));
								marker.setMapped(true);
							} else {
								pnid = pibmsr
										.getMostSimilarMarkerName(markerName);
								marker.setRepresentativeMarkerName(pibmr
										.getPreferredName(pnid));
							}
							marker.normalization();
							marker.valueAnalysis();
						}
					}
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Analyzing complete.");
		System.out.println("Analyzing time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("진단명과 관련한 에러들 : \n" + sb.toString());
	}

	public static void analysisImmunityDocumentSlide(
			ArrayList<ImmunityDocument> idocs) {
		System.out.println("Analyze slides in ImmunityDocument.");
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		SlideParser ps = new SlideParser();

		int count = 1;
		for (ImmunityDocument idoc : idocs) {
			if (count++ % 1000 == 0)
				System.out.print(".");

			for (PADiagnosis pa : idoc.getDiagnosises()) {
				try {
					pa.setSlides(ps.parseSlides(pa.getDiagnosisName(),
							pa.getSurgeonDocuments()));
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}

			for (MorphometricAnalysis ma : idoc.getMorphometricAnalysis()) {
				try {
					ma.setSlides(ps.parseSlides(ma.getDiagnosisName(),
							ma.getSurgeonDocuments()));
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Analyzing complete.");
		System.out.println("Analyzing time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("슬라이드 키를 분석하는 도중 발생한 에러들: \n" + sb.toString());
	}

	public static void analysisImmunityDocumentForFindingDuplicates(
			ArrayList<ImmunityDocument> idocs) {
		System.out.println("Analyze slides in ImmunityDocument.");
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		int count = 1;
		for (ImmunityDocument idoc : idocs) {
			if (count++ % 1000 == 0)
				System.out.print(".");

			for (PADiagnosis pa : idoc.getDiagnosises()) {
				try {
					boolean flag = true;
					ArrayList<SurgeonDocument> sdoc = pa.getSurgeonDocuments();
					if (sdoc.size() > 1) {
						String text = sdoc.get(0).getOriginalText();
						for (int i = 1; i < sdoc.size(); i++) {
							if (!text.equals(sdoc.get(i).getOriginalText())) {
								flag = false;
								break;
							}
						}
					}
					pa.setIsAllSurgeonDocument(flag);
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ pa.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}

			for (MorphometricAnalysis ma : idoc.getMorphometricAnalysis()) {
				try {
					boolean flag = true;
					ArrayList<SurgeonDocument> sdoc = ma.getSurgeonDocuments();
					if (sdoc.size() > 1) {
						String text = sdoc.get(0).getOriginalText();
						for (int i = 1; i < sdoc.size(); i++) {
							if (!text.equals(sdoc.get(i).getOriginalText())) {
								flag = false;
								break;
							}
						}
					}
					ma.setIsAllSurgeonDocument(flag);
				} catch (NullPointerException e) {
					sb.append("NullPointerException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				} catch (StringIndexOutOfBoundsException e) {
					sb.append("StringIndexOutOfBoundsException \t 병리학적 진단 : "
							+ ma.getDiagnosisName() + ", 병리번호 : "
							+ idoc.getPathologyID() + "\n");
				}
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Analyzing complete.");
		System.out.println("Analyzing time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("Error: \n" + sb.toString());
	}

	public static void constructTreeOfSurgeonDocument(
			ArrayList<SurgeonDocument> sdocs) {
		System.out
				.println("Analysis MICRO in SurgeonDocument based on a tree-based approach.");
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (SurgeonDocument sdoc : sdocs) {
			if (count++ % 1000 == 0)
				System.out.print(".");

			try {
				ArrayList<MicroValue> mvs = sdoc.getMvs();
				if (mvs == null)
					continue;

				boolean flag = false;
				for (int i = 0; i < mvs.size(); i++) {
					MicroValue mv = mvs.get(i);

					int currentDepth = mv.getDepth();
					int currentIdx = mv.getIdx();
					int relativeIdx = mv.getRelativeIdx();

					// root
					if (relativeIdx == 0) {
						mv.setParentIdx(-1);
						continue;
					}

					// Find the same depth in previous,
					// backtracking하면서 같은 depth를 찾지
					// if yes, choose same value.
					// Otherwise, choose previous one.
					boolean matched = false;
					for (int j = i - 1; j >= 0; j--) {
						MicroValue previousMV = mvs.get(j);
						int previousIdx = previousMV.getIdx();
						int previousRelativeIdx = previousMV.getRelativeIdx();
						int previoudDepth = previousMV.getDepth();

						// 현재 depth가 내 앞에 있는 줄과 depth가 같다 = 현재 depth가 previous
						// mv와 depth가 같다.
						if (currentDepth == previoudDepth) {
							// 생각해볼 수 있는 경우,
							// 한 줄로 모든 Micro가 적혀져 있는 경우 (relativeIdx == 0)
							if (previousRelativeIdx == 0) {
								// relativeIdx == 0인데 시작하는게 아니다!!
								// 그럼 한줄로 적혀 있는 경우이다...
								if (mv.isStartWithSymbols() == false) {
									mv.setParentIdx(previousIdx);
								} else { // relativeIdx == 0
									boolean lflag = false;
									// character symbol로 시작하면
									if (mv.isStartWithCharacterSymbol()) {
										String currentSymbol = mv
												.getCharacterSymbol();
										if (currentSymbol.equals(",")
												|| currentSymbol.equals(";")) {
											mv.setParentIdx(previousIdx);
											lflag = true;
										}
									}
									// character symbol로 시작하지 않으면...
									if (lflag == false) {
										previousIdx = i - 1;
										mv.setParentIdx(previousIdx);
									}
								}
							} else if (mv.isStartWithDigitSymbol()) {
								boolean lflag = false;
								String previousDigitSymbol = previousMV
										.getDigitSymbol();
								String currentDigitSymbol = mv.getDigitSymbol();

								if (previousDigitSymbol != null
										&& previousDigitSymbol
												.equals(currentDigitSymbol)) {
									previousIdx = previousMV.getParentIdx();
									mv.setParentIdx(previousIdx);
									lflag = true;
								} else {
									MicroValue tmp = mvs.get(i - 1);
									previousDigitSymbol = tmp.getDigitSymbol();

									// indentation이 잘못 되어서, 바로 앞에꺼 말고, 이 전에 것이랑
									// 연결되는 것을 방지
									if (previousDigitSymbol != null
											&& previousDigitSymbol
													.equals(currentDigitSymbol)) {
										previousIdx = tmp.getParentIdx();
										mv.setParentIdx(previousIdx);
										lflag = true;
									}
								}
								if (lflag == false)
									mv.setParentIdx(previousIdx);
							}
							// 문자로 시작할 경우,
							else if (mv.isStartWithCharacterSymbol()) {
								boolean lflag = false;
								String previousCharacterSymbol = previousMV
										.getCharacterSymbol();
								String currentCharacterSymbol = mv
										.getCharacterSymbol();

								if (previousCharacterSymbol != null
										&& previousCharacterSymbol
												.equals(currentCharacterSymbol)) {
									previousIdx = previousMV.getParentIdx();
									mv.setParentIdx(previousIdx);
									lflag = true;
								} else {
									MicroValue tmp = mvs.get(i - 1);
									previousCharacterSymbol = tmp
											.getCharacterSymbol();

									// indentation이 잘못 되어서, 바로 앞에꺼 말고, 이 전에 것이랑
									// 연결되는 것을 방지
									// 여기 손 좀 봐야할 부분~
									if (previousCharacterSymbol != null
											&& previousCharacterSymbol
													.equals(currentCharacterSymbol)) {
										previousIdx = tmp.getParentIdx();
										mv.setParentIdx(previousIdx);
										lflag = true;
									}
								}
								if (lflag == false)
									mv.setParentIdx(previousIdx);
							} else {
								// 같다고 항상 같은 부모를 공유하는가?
								// 아닌 경우가 존재하는데.. 어떻게 알지?
								previousIdx = previousMV.getParentIdx();
								mv.setParentIdx(previousIdx);
							}

							MicroValue tmp = mvs.get(previousIdx);
							tmp.addChild(currentIdx);
							matched = true;
							break;
						}

						// 현재 depth가 이전의 depth보다 깊다. 그럼 대부분 자식이 되지. 아닌 경우를 찾아봐야
						// 할 듯.
						else if (currentDepth > previousMV.getDepth()) {
							mv.setParentIdx(previousIdx);
							MicroValue tmp = mvs.get(previousIdx);
							tmp.addChild(currentIdx);
							matched = true;
							break;
						}
					}

					// choose previous one
					// backtracking하면서 위 조건에 만족시키는 경우가 없을 경우인거지..
					if (matched == false) {
						System.out.println(sdoc.getPathologyID()); // 이런 경우가
																	// 없나?ㅋㅋㅋ
						int previousIdx = -1;

						// 여기도 정확히 확인해볼 필요가 있는 부분..
						if (mv.isStartWithDigitSymbol()) {
							previousIdx = mvs.get(i - 1).getParentIdx();
						} else
							previousIdx = mvs.get(i - 1).getIdx();

						mv.setParentIdx(previousIdx);
						MicroValue tmp = mvs.get(previousIdx);
						tmp.addChild(currentIdx);
					}
				}
				if (flag == true)
					System.out.println(sdoc.getPathologyID());
			} catch (ArrayIndexOutOfBoundsException e) {
				sdoc.addErrorStr("현미경 관찰 Text를 트리로 변환하는 도중 에러가 발생");
				sb.append("ArrayIndexOutOfBoundsException , 병리번호 : "
						+ sdoc.getPathologyID() + "\n");
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Analyzing complete.");
		System.out.println("# of SurgeonDocument : " + count);
		System.out.println("Analyzing time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("Error: \n" + sb.toString());
	}

	private static void getChildSent(MicroValue grandChildMV,
			ArrayList<MicroValue> mvs, ArrayList<Integer> lists) {
		ArrayList<Integer> children = grandChildMV.getChildIndices();
		if (children == null)
			return;
		else {
			for (int i : children) {
				lists.add(i);
				getChildSent(mvs.get(i), mvs, lists);
			}
		}
	}

	private static void parseTreeOthersParsing(
			ArrayList<Integer> grandChildIndices, ArrayList<MicroValue> mvs,
			SDiagnosis diagnosis) {
		if (grandChildIndices == null)
			return;

		for (int grandChildIdx : grandChildIndices) {
			MicroValue grandChildMV = mvs.get(grandChildIdx);
			ArrayList<Integer> children = new ArrayList<Integer>();
			getChildSent(grandChildMV, mvs, children);
			String grandChildSent = grandChildMV.getStr();
			for (int i : children) {
				grandChildSent += "\n" + mvs.get(i).getStr();
				mvs.get(i).setProcessed(true);
			}

			analysisFields(grandChildSent, diagnosis);
			grandChildMV.setProcessed(true);
		}
	}

	public static void parseTreeBasedSurgeonDocument(
			ArrayList<SurgeonDocument> sdocs) {
		System.out.println("Extract information from SurgeonDocument.");
		long start = System.nanoTime();

		int organ = 0;
		int disorder = 0;
		StringBuilder sb = new StringBuilder();

		SlideKeyParser skp = new SlideKeyParser();
		for (SurgeonDocument sdoc : sdocs) {
			try {
				// if ( count++ % 1000 == 0 )
				// System.out.print(".");
				System.out.println(sdoc.getPathologyID());
				analysisMicroInfo(sdoc);
				skp.analysisSlideKey(sdoc);

				SDiagnosis diagnosis = null;
				ArrayList<MicroValue> mvs = sdoc.getMvs();
				for (int i = 0; i < mvs.size(); i++) {
					MicroValue mv = mvs.get(i);
					if (mv.getParentIdx() == -1) {
						String parentSent = mv.getStr();
						if (diagnosis == null)
							diagnosis = new SDiagnosis();
						else {
							try {
								sdoc.addDiagnosis((SDiagnosis) diagnosis
										.clone());
								diagnosis.clear();
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}

						boolean organResult = analysisOrganInfo(parentSent,
								diagnosis);
						if (organResult == false) {
							SDiagnosis diagnosisTmp = sdoc.getLastDiagnosis();
							if (diagnosisTmp == null)
								continue;

							diagnosisTmp
									.setErrorStr("'"
											+ parentSent
											+ "'에서 OrganString 시소로스에 존재하는 장기명을 발견할 수 없음.");
						}

						organ++;
						mv.setProcessed(true);

						ArrayList<Integer> childIndices = mv.getChildIndices();
						if (childIndices == null) {
							continue;
						}

						for (int childIdx : childIndices) {
							MicroValue childMV = mvs.get(childIdx);
							String childSent = childMV.getStr();

							ArrayList<Integer> grandChildIndices = childMV
									.getChildIndices();
							if (grandChildIndices == null) {
								Pair<Boolean, Disease> tuple = analysisDiseaseInfo(childSent);
								if (tuple.getA() == true)
									diagnosis.addDisease(tuple.getB());
								else {
									Disease lastDisease = diagnosis
											.getLastDisease();
									if (lastDisease == null)
										continue;

									lastDisease
											.setErrorStr("'"
													+ childSent
													+ "'에서 Disorder 시소로스에 존재하는 질병명을 발견할 수 없음.");
									continue;
								}
								disorder++;
								childMV.setProcessed(true);
							} else {
								int firstGrandChildIdx = grandChildIndices
										.get(0);
								MicroValue grandChildMV = mvs
										.get(firstGrandChildIdx);
								String grandChildSent = grandChildMV.getStr();

								Pair<Boolean, Disease> tuple = analysisDiseaseInfo(
										childSent, grandChildSent);
								if (tuple.getA() == true)
									diagnosis.addDisease(tuple.getB());
								else {
									Disease lastDisease = diagnosis
											.getLastDisease();
									if (lastDisease == null)
										continue;

									lastDisease
											.setErrorStr("'"
													+ childSent
													+ "'에서 Disorder 시소로스에 존재하는 질병명을 발견할 수 없음.");
									continue;
								}

								disorder++;
								childMV.setProcessed(true);

								parseTreeOthersParsing(
										childMV.getChildIndices(), mvs,
										diagnosis);
							}
						}
					}
				}

				try {
					sdoc.addDiagnosis((SDiagnosis) diagnosis.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			} catch (StringIndexOutOfBoundsException e) {
				sdoc.addErrorStr("MICRO (.)이 잘못 작성되어 있음");
				sb.append("MICRO (.)이 잘못 작성되어 있음. 병리번호 : "
						+ sdoc.getPathologyID() + "\n");
			} catch (NullPointerException e) {
				sdoc.setErrorStr("DIAGNOSIS: 또는 이와 준하는 식별자가 존재하지 않음.");
				sb.append("DIAGNOSIS: 또는 이와 준하는 식별자가 존재하지 않음 : "
						+ sdoc.getPathologyID() + "\n");
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Extracting complete.");
		System.out.println("# of organ : " + organ + "\t# of disorder : "
				+ disorder);
		System.out.println("Extracting time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("트리로 분석시 발생한 에러: \n" + sb.toString());
	}

	private static void analysisMicroInfo(SurgeonDocument sdoc)
			throws NullPointerException {
		ArrayList<String> micros = sdoc.getMicros();
		int numofSlide = 0;
		for (String micro : micros) {
			if (micro.indexOf(")") == -1)
				micro = micro.substring(micro.indexOf("(") + 1);
			else
				micro = micro.substring(micro.indexOf("(") + 1,
						micro.indexOf(")"));

			numofSlide += StringUtils.juiceNumFromString(micro);
		}
		sdoc.setNumOfSlide(numofSlide);
	}

	private static ArrayList<Organ> analsysiOrgan(String organStrs[]) {
		ArrayList<Organ> organs = new ArrayList<Organ>();
		for (String organStr : organStrs) {
			if (pisct_osr.contains(organStr) == true) {
				Organ organ = new Organ();
				organ.setOrganName(organStr);
				organ.setCandidateOrganName(organStr);
				organ.setSNOMEDorganSui(pisct_osr.getSNOMEDorganSui(organStr));
				organ.setOrganSimilarity(1.0);
				organs.add(organ);
			} else {
				if (organStr.startsWith("Tissue labeled")) {
					Organ organ = new Organ();
					organ.setOrganName(organStr);
					organ.setCandidateOrganName(organStr);
					organs.add(organ);
				} else {
					Organ organ = pisct_osr.getSimilarOrgans(organStr);
					if (organ.getOrganSimilarity() >= 0.7) {
						organs.add(organ);
					}
				}
			}
		}
		return organs;
	}

	private static boolean analysisOrganInfo(String sent, SDiagnosis diagnosis) {
		String organNameCandidate = null;

		int idx = sent.indexOf(",");
		if (idx != -1)
			organNameCandidate = sent.substring(0, idx);
		else
			organNameCandidate = sent;

		// more than double space -> single space
		organNameCandidate = organNameCandidate.trim().replaceAll(" +", " ");
		organNameCandidate = StringUtils
				.removeSpecialCharacter(organNameCandidate);

		boolean setFlag = false;
		if (pisct_osr.contains(organNameCandidate) == true) {
			Organ organ = new Organ();
			organ.setOrganName(organNameCandidate);
			organ.setCandidateOrganName(organNameCandidate);
			organ.setSNOMEDorganSui(pisct_osr
					.getSNOMEDorganSui(organNameCandidate));
			organ.setOrganSimilarity(1.0);
			diagnosis.setOrgan(organ);
			setFlag = true;
		} else {
			if (organNameCandidate.startsWith("Tissue labeled")
					|| organNameCandidate.startsWith("Tissue from")
					|| organNameCandidate.startsWith("Specimen labeled")) {

				Organ organ = new Organ();
				organ.setCandidateOrganName(organNameCandidate);

				String tmp = null;
				if (organNameCandidate.startsWith("Tissue labeled")) {
					tmp = organNameCandidate.substring(
							"Tissue labeled".length()).trim();
				} else if (organNameCandidate.startsWith("Tissue from")) {
					tmp = organNameCandidate.substring("Tissue from".length())
							.trim();
				} else {
					tmp = organNameCandidate.substring(
							"Specimen labeled".length()).trim();
				}

				tmp = tmp.replace("\"", "");
				if (pisct_osr.contains(tmp) == true) {
					organ.setOrganName(tmp);
					organ.setSNOMEDorganSui(pisct_osr.getSNOMEDorganSui(tmp));
					organ.setOrganSimilarity(1.0);
					diagnosis.setOrgan(organ);
				} else {
					organ.setOrganName(organNameCandidate);
					diagnosis.setOrgan(organ);
				}
				setFlag = true;
			} else {
				Organ organ = pisct_osr.getSimilarOrgans(organNameCandidate);
				if (organ.getOrganSimilarity() >= 0.7) {
					diagnosis.setOrgan(organ);
					setFlag = true;
				}
			}
		}

		if (setFlag == false) {
			// 장기명이 and를 통해 2개 이상 나타난 경우, 처리하기 위한 코드
			if (organNameCandidate.contains(" and ")) {
				String organStrs[] = organNameCandidate.split(" and ");
				ArrayList<Organ> organs = analsysiOrgan(organStrs);
				if (organs.size() == 0) {
					diagnosis.setOrgan(null);
				} else {
					Organ organ = organs.get(0);
					String organName = organ.getOrganName();
					String candidateOrganName = organ.getCandidateOrganName();
					String SNOMEDorganSui = organ.getSNOMEDorganSui();
					double similarity = organ.getOrganSimilarity();

					for (int i = 1; i < organs.size(); i++) {
						organ = organs.get(i);
						organName += "," + organ.getOrganName();
						candidateOrganName += ","
								+ organ.getCandidateOrganName();
						SNOMEDorganSui += "," + organ.getSNOMEDorganSui();
						similarity += organ.getOrganSimilarity();
					}

					Organ combinedOrgan = new Organ();
					combinedOrgan.setOrganName(organName);
					combinedOrgan.setCandidateOrganName(candidateOrganName);
					combinedOrgan.setSNOMEDorganSui(SNOMEDorganSui);
					combinedOrgan
							.setOrganSimilarity(similarity / organs.size());
					diagnosis.setOrgan(combinedOrgan);
					setFlag = true;
				}
			} else {
				diagnosis.setOrgan(null);
			}
		}

		return setFlag;
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

	private static void analysisFields(String sent, SDiagnosis diagnosis) {
		String fieldName = piSMFr.startWithFieldName(sent);
		if (fieldName == null)
			return;

		// SIZE
		// LYMPH
		// MARGIN
		// GROSS
		// LOCATION
		// HISTOLOGIC_TYPE	
		// DEPTHOFINVASION

		String type = piSMFr.getTypeofFieldName(fieldName);
		if (type == null)
			return;

		int idx = sent.indexOf(":");
		if (type.equals("SIZE")) {
			Matcher matcher = sizePattern.matcher(sent);
			while (matcher.find()) {
				diagnosis.setSize(matcher.group());
			}
		} else if (type.equals("LYMPH")) {
			if (idx != -1)
				diagnosis.setLymphNode(sent.substring(idx + 1).trim());
			else
				diagnosis.setLymphNode(sent.trim());
		} else if (type.equals("MARGIN")) {
			// if ( idx != -1 )
			// diagnosis.setLymphNode( sent.substring( idx + 1 ).trim() );
			// else
			// diagnosis.setLymphNode( sent.trim() );
		} else if (type.equals("GROSS")) {
			if (idx != -1)
				diagnosis.setGrossType(sent.substring(idx + 1).trim());
			else
				diagnosis.setGrossType(sent.trim());
		} else if (type.equals("LOCATION")) {
			if (idx != -1)
				diagnosis.setLocation(sent.substring(idx + 1).trim());
			else
				diagnosis.setLocation(sent.trim());
		} else if (type.equals("HISTOLOGIC_TYPE")) {
			if (idx != -1)
				diagnosis.setHistoricalType(sent.substring(idx + 1).trim());
			else
				diagnosis.setHistoricalType(sent.trim());
		} else if (type.equals("DEPTHOFINVASION")) {
			if (idx != -1)
				diagnosis.setDepthOfInvasion(sent.substring(idx + 1).trim());
			else
				diagnosis.setDepthOfInvasion(sent.trim());
		} else if (type.equals("DCIS")) {
			if (idx != -1)
				diagnosis.setDCIS(sent.substring(idx + 1).trim());
			else
				diagnosis.setDCIS(sent.trim());
		}
	}

	public static void constructTreeOfChildSurgeonDocument(
			ArrayList<ChildSurgeonDocument> csdocs) {
		System.out
				.println("Analysis MICRO in ChildSurgeonDocument based on a tree-based approach.");
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (ChildSurgeonDocument csdoc : csdocs) {
			if (count++ % 1000 == 0)
				System.out.print(".");

			try {
				ArrayList<MicroValue> mvs = csdoc.getMvs();
				if (mvs == null)
					continue;

				boolean flag = false;
				for (int i = 0; i < mvs.size(); i++) {
					MicroValue mv = mvs.get(i);

					int currentDepth = mv.getDepth();
					int currentIdx = mv.getIdx();
					int relativeIdx = mv.getRelativeIdx();

					// root
					if (relativeIdx == 0) {
						mv.setParentIdx(-1);
						continue;
					}

					// Find the same depth in previous,
					// backtracking하면서 같은 depth를 찾지
					// if yes, choose same value.
					// Otherwise, choose previous one.
					boolean matched = false;
					for (int j = i - 1; j >= 0; j--) {
						MicroValue previousMV = mvs.get(j);
						int previousIdx = previousMV.getIdx();
						int previousRelativeIdx = previousMV.getRelativeIdx();
						int previoudDepth = previousMV.getDepth();

						// 현재 depth가 내 앞에 있는 줄과 depth가 같다 = 현재 depth가 previous
						// mv와 depth가 같다.
						if (currentDepth == previoudDepth) {
							// 생각해볼 수 있는 경우,
							// 한 줄로 모든 Micro가 적혀져 있는 경우 (relativeIdx == 0)
							if (previousRelativeIdx == 0) {
								// relativeIdx == 0인데 시작하는게 아니다!!
								// 그럼 한줄로 적혀 있는 경우이다...
								if (mv.isStartWithSymbols() == false) {
									mv.setParentIdx(previousIdx);
								} else { // relativeIdx == 0
									boolean lflag = false;
									// character symbol로 시작하면
									if (mv.isStartWithCharacterSymbol()) {
										String currentSymbol = mv
												.getCharacterSymbol();
										if (currentSymbol.equals(",")
												|| currentSymbol.equals(";")) {
											mv.setParentIdx(previousIdx);
											lflag = true;
										}
									}
									// character symbol로 시작하지 않으면...
									if (lflag == false) {
										previousIdx = i - 1;
										mv.setParentIdx(previousIdx);
									}
								}
							} else if (mv.isStartWithDigitSymbol()) {
								boolean lflag = false;
								String previousDigitSymbol = previousMV
										.getDigitSymbol();
								String currentDigitSymbol = mv.getDigitSymbol();

								if (previousDigitSymbol != null
										&& previousDigitSymbol
												.equals(currentDigitSymbol)) {
									previousIdx = previousMV.getParentIdx();
									mv.setParentIdx(previousIdx);
									lflag = true;
								} else {
									MicroValue tmp = mvs.get(i - 1);
									previousDigitSymbol = tmp.getDigitSymbol();

									// indentation이 잘못 되어서, 바로 앞에꺼 말고, 이 전에 것이랑
									// 연결되는 것을 방지
									if (previousDigitSymbol != null
											&& previousDigitSymbol
													.equals(currentDigitSymbol)) {
										previousIdx = tmp.getParentIdx();
										mv.setParentIdx(previousIdx);
										lflag = true;
									}
								}
								if (lflag == false)
									mv.setParentIdx(previousIdx);
							}
							// 문자로 시작할 경우,
							else if (mv.isStartWithCharacterSymbol()) {
								boolean lflag = false;
								String previousCharacterSymbol = previousMV
										.getCharacterSymbol();
								String currentCharacterSymbol = mv
										.getCharacterSymbol();

								if (previousCharacterSymbol != null
										&& previousCharacterSymbol
												.equals(currentCharacterSymbol)) {
									previousIdx = previousMV.getParentIdx();
									mv.setParentIdx(previousIdx);
									lflag = true;
								} else {
									MicroValue tmp = mvs.get(i - 1);
									previousCharacterSymbol = tmp
											.getCharacterSymbol();

									// indentation이 잘못 되어서, 바로 앞에꺼 말고, 이 전에 것이랑
									// 연결되는 것을 방지
									// 여기 손 좀 봐야할 부분~
									if (previousCharacterSymbol != null
											&& previousCharacterSymbol
													.equals(currentCharacterSymbol)) {
										previousIdx = tmp.getParentIdx();
										mv.setParentIdx(previousIdx);
										lflag = true;
									}
								}
								if (lflag == false)
									mv.setParentIdx(previousIdx);
							} else {
								// 같다고 항상 같은 부모를 공유하는가?
								// 아닌 경우가 존재하는데.. 어떻게 알지?
								previousIdx = previousMV.getParentIdx();
								mv.setParentIdx(previousIdx);
							}

							MicroValue tmp = mvs.get(previousIdx);
							tmp.addChild(currentIdx);
							matched = true;
							break;
						}

						// 현재 depth가 이전의 depth보다 깊다. 그럼 대부분 자식이 되지. 아닌 경우를 찾아봐야
						// 할 듯.
						else if (currentDepth > previousMV.getDepth()) {
							mv.setParentIdx(previousIdx);
							MicroValue tmp = mvs.get(previousIdx);
							tmp.addChild(currentIdx);
							matched = true;
							break;
						}
					}

					// choose previous one
					// backtracking하면서 위 조건에 만족시키는 경우가 없을 경우인거지..
					if (matched == false) {
						System.out.println(csdoc.getPathologyID()); // 이런 경우가
																	// 없나?ㅋㅋㅋ
						int previousIdx = -1;

						// 여기도 정확히 확인해볼 필요가 있는 부분..
						if (mv.isStartWithDigitSymbol()) {
							previousIdx = mvs.get(i - 1).getParentIdx();
						} else
							previousIdx = mvs.get(i - 1).getIdx();

						mv.setParentIdx(previousIdx);
						MicroValue tmp = mvs.get(previousIdx);
						tmp.addChild(currentIdx);
					}
				}
				if (flag == true)
					System.out.println(csdoc.getPathologyID());
			} catch (ArrayIndexOutOfBoundsException e) {
				csdoc.addErrorStr("현미경 관찰 Text를 트리로 변환하는 도중 에러가 발생");
				sb.append("ArrayIndexOutOfBoundsException , 병리번호 : "
						+ csdoc.getPathologyID() + "\n");
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Analyzing complete.");
		System.out.println("# of ChildSurgeonDocument : " + count);
		System.out.println("Analyzing time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("Error: \n" + sb.toString());
	}

	public static void parseTreeBasedChildSurgeonDocument(
			ArrayList<ChildSurgeonDocument> csdocs) {
		System.out.println("Extract information from ChildSurgeonDocument.");
		long start = System.nanoTime();

		int count = 0;
		int organ = 0;
		int disorder = 0;
		StringBuilder sb = new StringBuilder();

		SlideKeyParser skp = new SlideKeyParser();
		for (ChildSurgeonDocument csdoc : csdocs) {
			try {
				if (count++ % 1000 == 0)
					System.out.print(".");

				analysisMicroInfo(csdoc);
				skp.analysisSlideKey(csdoc);

				SDiagnosis diagnosis = null;
				ArrayList<MicroValue> mvs = csdoc.getMvs();
				for (int i = 0; i < mvs.size(); i++) {
					MicroValue mv = mvs.get(i);
					if (mv.getParentIdx() == -1) {
						String parentSent = mv.getStr();
						if (diagnosis == null)
							diagnosis = new SDiagnosis();
						else {
							try {
								csdoc.addDiagnosis((SDiagnosis) diagnosis
										.clone());
								diagnosis.clear();
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}

						boolean organResult = analysisOrganInfo(parentSent,
								diagnosis);
						if (organResult == false) {
							SDiagnosis diagnosisTmp = csdoc.getLastDiagnosis();
							diagnosisTmp
									.setErrorStr("'"
											+ parentSent
											+ "'에서 OrganString 시소로스에 존재하는 장기명을 발견할 수 없음.");
							continue;
						}

						organ++;
						mv.setProcessed(true);

						ArrayList<Integer> childIndices = mv.getChildIndices();
						if (childIndices == null) {
							continue;
						}

						for (int childIdx : childIndices) {
							MicroValue childMV = mvs.get(childIdx);
							String childSent = childMV.getStr();

							ArrayList<Integer> grandChildIndices = childMV
									.getChildIndices();
							if (grandChildIndices == null) {
								Pair<Boolean, Disease> tuple = analysisDiseaseInfo(
										childSent, "");
								if (tuple.getA() == true)
									diagnosis.addDisease(tuple.getB());
								else {
									Disease lastDisease = diagnosis
											.getLastDisease();
									lastDisease
											.setErrorStr("'"
													+ childSent
													+ "'에서 Disorder 시소로스에 존재하는 질병명을 발견할 수 없음.");
									continue;
								}
								disorder++;
								childMV.setProcessed(true);
							} else {
								int firstGrandChildIdx = grandChildIndices
										.get(0);
								MicroValue grandChildMV = mvs
										.get(firstGrandChildIdx);
								String grandChildSent = grandChildMV.getStr();
								Pair<Boolean, Disease> tuple = analysisDiseaseInfo(
										childSent, grandChildSent);
								if (tuple.getA() == true)
									diagnosis.addDisease(tuple.getB());
								else {
									Disease lastDisease = diagnosis
											.getLastDisease();
									lastDisease
											.setErrorStr("'"
													+ childSent
													+ "'에서 Disorder 시소로스에 존재하는 질병명을 발견할 수 없음.");
									continue;
								}

								disorder++;
								childMV.setProcessed(true);

								parseTreeOthersParsing(
										childMV.getChildIndices(), mvs,
										diagnosis);
							}
						}
					}
				}

				try {
					csdoc.addDiagnosis((SDiagnosis) diagnosis.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			} catch (StringIndexOutOfBoundsException e) {
				csdoc.addErrorStr("MICRO (.)이 잘못 작성되어 있음");
				sb.append("MICRO (.)이 잘못 작성되어 있음. 병리번호 : "
						+ csdoc.getPathologyID() + "\n");
			} catch (NullPointerException e) {
				csdoc.setErrorStr("DIAGNOSIS: 또는 이와 준하는 식별자가 존재하지 않음.");
				sb.append("DIAGNOSIS: 또는 이와 준하는 식별자가 존재하지 않음 : "
						+ csdoc.getPathologyID() + "\n");
			}
		}

		long end = System.nanoTime();
		System.out.println();
		System.out.println("Extracting complete.");
		System.out.println("# of organ : " + organ + "\t# of disorder : "
				+ disorder);
		System.out.println("Extracting time : " + (end - start)
				/ Math.pow(10, 9) + " second");
		if (sb.toString().trim().length() != 0)
			System.err.println("트리로 분석시 발생한 에러: \n" + sb.toString());
	}

	private static void analysisMicroInfo(ChildSurgeonDocument csdoc)
			throws NullPointerException {
		ArrayList<String> micros = csdoc.getMicros();
		int numofSlide = 0;
		for (String micro : micros) {
			if (micro.indexOf(")") == -1)
				micro = micro.substring(micro.indexOf("(") + 1);
			else
				micro = micro.substring(micro.indexOf("(") + 1,
						micro.indexOf(")"));

			numofSlide += StringUtils.juiceNumFromString(micro);
		}
		csdoc.setNumOfSlide(numofSlide);
	}
}
