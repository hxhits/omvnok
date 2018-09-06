package vn.com.omart.backend.application.util;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XML Utils.
 * 
 * @author Win10
 *
 */
public class XMLUtils {
	/**
	 * Excute Xpath Query.
	 * 
	 * @param xml
	 * @param query
	 * @return String
	 */
	public static String excuteXpathQuery(String xml, String query) {
		String value = null;
		try {
			// convert response string to xml
			Document document = convertStringToDocument(xml);
			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();
			// Create XPath object
			XPath xpath = xpathFactory.newXPath();
			// create XPathExpression object
			XPathExpression expr = xpath.compile(query);
			// evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			// set value
			if (nodes.getLength() > 0) {
				value = nodes.item(0).getNodeValue();
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Convert String To Document.
	 * 
	 * @param xmlStr
	 * @return Document
	 */
	public static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get value by query.
	 * 
	 * @param xml
	 * @param query
	 * @return
	 */
	public static String getValueByQuery(String xml, String query) {
		String value = excuteXpathQuery(xml, query);
		if (!StringUtils.isBlank(value)) {
			return value;
		}
		return "";
	}
}
