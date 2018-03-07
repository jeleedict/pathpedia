/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.exec.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public abstract class DocExtractor {
	protected static Options getOptions() {
		Options options = new Options();

		options.addOption("h", "help", false, "show help");

		@SuppressWarnings("static-access")
		Option inputFile = OptionBuilder.withArgName("file")
				.withLongOpt("inputfile").isRequired(true).hasArg()
				.withDescription("input folder/file").create("i");

		@SuppressWarnings("static-access")
		Option outputFile = OptionBuilder.withArgName("file")
				.withLongOpt("outputfile").hasArg()
				.withDescription("output file").create("o");

		@SuppressWarnings("static-access")
		Option pid = OptionBuilder.withArgName("id").withLongOpt("pid")
				.hasArg().withDescription("pathlogy id").create("p");

		options.addOption(inputFile);
		options.addOption(outputFile);
		options.addOption(pid);

		return options;
	}

	protected ArrayList<Sheet> getSheets(File[] inputFiles) {
		ArrayList<Sheet> sheets = new ArrayList<Sheet>();
		for (File f : inputFiles) {
			Sheet sheet = load(f.toString());
			if (sheet == null) {
				System.err.println(f.toString());
				continue;
			}

			info(sheet);
			sheets.add(sheet);
		}
		return sheets;
	}

	private void info(Sheet sheet) {
		System.out.println("Name of Sheet : " + sheet.getSheetName());
		System.out.println("# of Rows : " + sheet.getLastRowNum());
	}

	private Sheet load(String inputFileName) {
		long start = System.nanoTime();
		try {
			System.out.println("Loading from '" + inputFileName + "'");
			InputStream inp = new FileInputStream(inputFileName);
			Workbook wb = WorkbookFactory.create(inp);
			System.out.println("'" + inputFileName + "' loaded.");
			long end = System.nanoTime();
			System.out.println("Loading time : " + (end - start)
					/ Math.pow(10, 9) + " second");
			return wb.getSheetAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
