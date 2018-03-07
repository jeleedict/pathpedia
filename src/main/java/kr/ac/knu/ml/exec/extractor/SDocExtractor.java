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
import kr.ac.knu.ml.writer.SReportWriter;

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
 * To extract information from SP reports <br/>
 * Analyze SNUH reports starts with S code <br/>
 * 
 * @author Hyun-Je
 * 
 */
public class SDocExtractor extends DocExtractor {
	static final Comparator<SurgeonDocument> SurgeonDocumentComparalbe = new Comparator<SurgeonDocument>() {
		public int compare(SurgeonDocument sdoc1, SurgeonDocument sdoc2) {
			return sdoc1.getPathologyID().compareTo(sdoc2.getPathologyID());
		}
	};

	private void sort(ArrayList<SurgeonDocument> sdocs) throws IOException {
		long start = System.nanoTime();
		Collections.sort(sdocs, SurgeonDocumentComparalbe);
		long end = System.nanoTime();
		System.out.println("Sorting time : " + (end - start) / Math.pow(10, 9)
				+ " second");
	}

	private void write(String outputFileName, ArrayList<SurgeonDocument> sdocs)
			throws IOException {
		long start = System.nanoTime();
		System.out.println("Write '" + outputFileName + "'");

		SReportWriter writer = new SReportWriter();
		writer.write(outputFileName + ".xlsx", sdocs);

		// writer = new SReportWriter( "xml" );
		// writer.write( outputFileName + ".xml", sdocs );

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

		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, idocs, sdocs, csdocs, false);

		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.constructTreeOfSurgeonDocument(sdocs);
		ba.parseTreeBasedSurgeonDocument(sdocs);

		sort(sdocs);
		write(outputFileName, sdocs);
	}

	private void extract(File inputFiles[], String outputFileName,
			String testSDoc) throws InvalidFormatException, IOException {
		ArrayList<Sheet> sheets = getSheets(inputFiles);

		// intermediate result
		ArrayList<SurgeonDocument> sdocs = new ArrayList<SurgeonDocument>();

		BioMedicalDocumentParser bmdp = new BioMedicalDocumentParser();
		bmdp.excelparse(sheets, null, sdocs, null, "S", testSDoc, false);

		BiomedicalAnalyzer ba = new BiomedicalAnalyzer();
		ba.constructTreeOfSurgeonDocument(sdocs);
		ba.parseTreeBasedSurgeonDocument(sdocs);

		sort(sdocs);
		write(outputFileName, sdocs);
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
				outputFileName = "[" + today + "]SP_Output";
			}

			if (line.hasOption("h") || line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SDocExtractor [options]", options);
				System.exit(1);
			}
		} catch (MissingArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("SDocExtractor [options]", options);
			System.exit(1);
		}

		SDocExtractor sde = new SDocExtractor();

		File[] files = null;
		File inputFile = new File(inputFileName);
		if (inputFile.isDirectory())
			files = inputFile.listFiles();
		else
			files = new File[] { inputFile };

		try {
			if (pathlogyID != null)
				sde.extract(files, outputFileName, pathlogyID);
			else
				sde.extract(files, outputFileName);
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
