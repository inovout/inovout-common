package org.inovout.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

	public static Node getNode(Node parent, String childrenNodename) {
		NodeList childrenNodes = parent.getChildNodes();
		int childrenNodeCount = childrenNodes.getLength();
		for (int i = 0; i < childrenNodeCount; i++) {
			Node node = childrenNodes.item(i);
			if (node.getNodeName().equals(childrenNodename)) {
				return node;
			}
		}
		return null;
	}

	public static String getAttribute(Node node, String attributeName) {
		return getAttribute((Element) node, attributeName);
	}

	public static String getAttribute(Element element, String attributeName) {
		return element.getAttribute(attributeName);
	}
}
