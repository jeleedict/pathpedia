/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.exec.extractor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import kr.ac.knu.ml.analyzer.BiomedicalAnalyzer;
import kr.ac.knu.ml.document.ChildSurgeonDocument;
import kr.ac.knu.ml.document.ImmunityDocument;
import kr.ac.knu.ml.document.SurgeonDocument;
import kr.ac.knu.ml.parser.BioMedicalDocumentParser;
import kr.ac.knu.ml.writer.CSReportWriter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * To extract information from child SP reports <br/>
 * Analyze SNUH reports starts with CS code <br/>
 * 
 * @author Hyun-Je
 * 
 */
public class CSDocExtractor extends DocExtractor {
	/**
	 *
	 * Sort SP reports with following conditions. default: patientNumber ascending.
	 *
	 */
	static final Comparator<ChildSurgeonDocument> ChildSurgeonDocumentComparalbe = new Comparator<ChildSurgeonDocument>() {
		public int compare(ChildSurgeonDocument csdoc1,
				ChildSurgeonDocument csdoc2) {
			return csdoc1.getPatientNumber().compareTo(
					csdoc2.getPatientNumber());

			// 환자 번호 ID로 출력할 경우, 위 줄을 주석처리하고, 아래 줄을 주석 해제
			// return
			// csdoc1.getPathologyID().compareTo(csdoc2.getPathologyID());
		}
	};

	private void sort(ArrayList<ChildSurgeonDocument> csdocs)
			throws IOException {
		long start = System.nanoTime();
		Collections.sort(csdocs, ChildSurgeonDocumentComparalbe);
		long end = System.nanoTime();
		System.out.println("Sorting time : " + (end - start) / Math.pow(10, 9)
				+ " second");
	}

	private void write(String outputFileName,
			ArrayList<ChildSurgeonDocument> csdocs) throws IOException {
		long start = System.nanoTime();
		System.out.println("Write '" + outputFileName + "'");
		CSReportWriter writer = new CSReportWriter();
		writer.write(outputFileName + ".xlsx", csdocs);
		long end = System.nanoTime();
		System.out.println("Writing time : " + (end - start) / Math.pow(10, 9)
				+ " second");
	}

	private void extract(File inputFiles[], String outputFileName,
			String testCSdoc) throws InvalidFormatException, IOException {
		ArrayList<Sheet> sheets = getSheets(inputFiles);
		// intermediate result
		ArrayList<ChildSurgeonDocument> csdocs = new ArrayList<ChildSurgeonDocument>();

		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, null, null, csdocs, "S", testCSdoc, false);

		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.constructTreeOfChildSurgeonDocument(csdocs);
		ba.parseTreeBasedChildSurgeonDocument(csdocs);

		sort(csdocs);
		write(outputFileName, csdocs);
	}

	private void extract(File inputFiles[], String outputFileName)
			throws InvalidFormatException, IOException {
		ArrayList<Sheet> sheets = getSheets(inputFiles);
		// intermediate result
		ArrayList<ImmunityDocument> idocs = new ArrayList<ImmunityDocument>();
		ArrayList<SurgeonDocument> sdocs = new ArrayList<SurgeonDocument>();
		ArrayList<ChildSurgeonDocument> csdocs = new ArrayList<ChildSurgeonDocument>();

		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, idocs, sdocs, csdocs, false);

		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.constructTreeOfChildSurgeonDocument(csdocs);
		ba.parseTreeBasedChildSurgeonDocument(csdocs);

		sort(csdocs);
		write(outputFileName, csdocs);
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();

		Options options = getOptions();
		String inputFileName = null;
		String outputFileName = null;
		String pathlogyID = null;

		try {
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("i"))
				inputFileName = line.getOptionValue("i");
			if (line.hasOption("inputfile"))
				inputFileName = line.getOptionValue("inputfile");
			if (line.hasOption("p"))
				pathlogyID = line.getOptionValue("p");
			if (line.hasOption("pid"))
				pathlogyID = line.getOptionValue("pid");
			if (line.hasOption("o"))
				outputFileName = line.getOptionValue("o");
			if (line.hasOption("outputfile"))
				outputFileName = line.getOptionValue("outputfile");

			if (outputFileName == null) {
				SimpleDateFormat formatter = new SimpleDateFormat("YYMMdd");
				String today = formatter.format(new Date());
				outputFileName = "[" + today + ", 경북대] CS_Output";
			}

			if (line.hasOption("h") || line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CSDocExtractor [options]", options);
				System.exit(1);
			}
		} catch (MissingArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CSDocExtractor [options]", options);
			System.exit(1);
		}

		// System.out.println( "inputFileName name : " + inputFileName );
		// System.out.println( "OutputFile name : " + outputFileName );
		// String inputFileName = "input/140830_machedCSlist_onlyCS_07-12.xlsx";
		// BiomedicalAnalyzer.deserialize();
		CSDocExtractor csde = new CSDocExtractor();

		File[] files = null;
		File inputFile = new File(inputFileName);
		if (inputFile.isDirectory())
			files = inputFile.listFiles();
		else
			files = new File[] { inputFile };

		try {
			if (pathlogyID != null)
				csde.extract(files, outputFileName, pathlogyID);
			else
				csde.extract(files, outputFileName);
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
