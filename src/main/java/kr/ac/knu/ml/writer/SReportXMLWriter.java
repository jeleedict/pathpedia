/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.writer;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SReportXMLWriter {
	private final String titles[] = {
			"번호", "조직슬라이드 Key ID", "외과병리보고서 ID", "추출된 슬라이드 Key 번호", 
			"조직 슬라이드 이름", "조직 슬라이드 추가설명", "조직번호", "조직 개수 총합", "현미경 설명 추출 내용", "현미경에 따른 조직개수",  
			"분석에 사용된 장기명 문구", "추출된 장기명", "맵핑된 장기명", "맵핑된 장기명 ID", "장기명 시소로스와 Exact matching 유뮤", "장기명 시소로스와의 유사도", 
			"분석에 사용된 진단명 문구", "추출된 진단명", "맵핑된 진단명", "매핑된 진단명 ID", "진단명 시소로스와 Exact matching 유무", "진단명 시소로스와의 유사도",
			"Consistent With 유무", "Negation 유무", "Metastatic 유무", "Most-likely 유무", "Involvement 유무", "Suspicious 유무", "Favor 유무",
			"histologic type", "gross type", "location of tumor", "size of tumor",	"depth of invasion", "lymph node metastasis", "DCIS", "other findings", "장기명 개수", 
			"환자번호", "성별", "나이", "스노메드 채취부위", "스노메드 검사명", "스노메드 진단명", "에러"
	};	
	
	private int number;
		
	public void saveToXML(String xml) {
	    Document dom;
	    Element e = null;

	    // instance of a DocumentBuilderFactory
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootEle = dom.createElement("SurgeonDocument");

	        // create data elements and place them under root
	        e = dom.createElement("DOC");
	        e.setAttribute("ID", "1");
//	        e.appendChild(dom.createTextNode(role1));
	        rootEle.appendChild(e);

	        e = dom.createElement("role2");
//	        e.appendChild(dom.createTextNode(role2));
	        rootEle.appendChild(e);

	        e = dom.createElement("role3");
//	        e.appendChild(dom.createTextNode(role3));
	        rootEle.appendChild(e);

	        e = dom.createElement("role4");
//	        e.appendChild(dom.createTextNode(role4));
	        rootEle.appendChild(e);

	        dom.appendChild(rootEle);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xml)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
	}
	
	public static void main(String[] args) {
		SReportXMLWriter writer = new SReportXMLWriter();
		writer.saveToXML("test.xml");
	}
}
