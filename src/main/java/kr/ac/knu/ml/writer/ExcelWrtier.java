/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.writer;

import java.util.ArrayList;

import kr.ac.knu.ml.document.SurgeonDocument;

public abstract class ExcelWrtier {
	public abstract void write(String fileName, ArrayList<SurgeonDocument> sdocs);

	private void writeTitle() {
	}
}
