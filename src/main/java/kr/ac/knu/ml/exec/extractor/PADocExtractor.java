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
import kr.ac.knu.ml.matcher.Matcher;
import kr.ac.knu.ml.parser.BioMedicalDocumentParser;
import kr.ac.knu.ml.writer.PAReportWriter;

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
 *
 * To extract information from IHC reports <br/>
 * 
 * @author Hyun-Je
 * 
 */
public class PADocExtractor extends DocExtractor {
	public void sort(ArrayList<ImmunityDocument> idocs) throws IOException {
		long start = System.nanoTime();
		Collections.sort(idocs, new Comparator<ImmunityDocument>() {
			public int compare(ImmunityDocument idoc1, ImmunityDocument idoc2) {
				return idoc1.getPatientNumber().compareTo(
						idoc2.getPatientNumber());
			}
		});
		long end = System.nanoTime();
		System.out.println("Sorting time : " + (end - start) / Math.pow(10, 9)
				+ " second");
	}

	public void write(String outputFileName, ArrayList<ImmunityDocument> idocs)
			throws IOException {
		long start = System.nanoTime();
		System.out.println("Write '" + outputFileName + "'");
		PAReportWriter writeExcel = new PAReportWriter();
		writeExcel.write(outputFileName + ".xlsx", idocs);
		long end = System.nanoTime();
		System.out.println("Writing time : " + (end - start) / Math.pow(10, 9)
				+ " second");
	}

	private void extract(File inputFiles[], String outputFileName)
			throws InvalidFormatException, IOException {
		ArrayList<Sheet> sheets = getSheets(inputFiles);

		// intermediate result
		ArrayList<ImmunityDocument> idocs = new ArrayList<ImmunityDocument>();
		ArrayList<SurgeonDocument> sdocs = new ArrayList<SurgeonDocument>();
		ArrayList<ChildSurgeonDocument> csdocs = new ArrayList<ChildSurgeonDocument>();

		// if you do not want to store an original text, set the last parameter
		// to false.
		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, idocs, sdocs, csdocs, false);

		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.processingImmunityDocument(idocs);
		Matcher.matcherImmunityDocumentToSurgeonDocument(idocs, sdocs);
		ba.analysisImmunityDocumentSlide(idocs);
		ba.analysisImmunityDocumentForFindingDuplicates(idocs);

		sort(idocs);

		write(outputFileName, idocs);
	}

	private void extract(File inputFiles[], String outputFileName,
			String testPDoc) throws InvalidFormatException, IOException {
		ArrayList<Sheet> sheets = getSheets(inputFiles);
		// intermediate result
		ArrayList<ImmunityDocument> idocs = new ArrayList<ImmunityDocument>();

		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, idocs, null, null, "PA", testPDoc, false);
//		bmdp.txtSNUparse(sheets, idocs, null, null);
		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.processingImmunityDocument(idocs);
		ba.analysisImmunityDocumentSlide(idocs);
		ba.analysisImmunityDocumentForFindingDuplicates(idocs);

		sort(idocs);
		write(outputFileName, idocs);
	}



	public static void main(String[] args) throws InvalidFormatException,
			IOException {
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
				outputFileName = "[" + today + "]PA_Output";
			}

			if (line.hasOption("h") || line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("PADocExtractor [options]", options);
				System.exit(1);
			}
		} catch (MissingArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("PADocExtractor [options]", options);
			System.exit(1);
		}

		PADocExtractor pae = new PADocExtractor();

		File[] files = null;
		File inputFile = new File(inputFileName);
		if (inputFile.isDirectory())
			files = inputFile.listFiles();
		else
			files = new File[] { inputFile };

		try {
			if (pathlogyID != null)
				pae.extract(files, outputFileName, pathlogyID);
			else
				pae.extract(files, outputFileName);
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
