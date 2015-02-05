package asw1028.utils.xml;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ManageXML {

    private Transformer transformer;
    private DocumentBuilder builder;
    
    public ManageXML() throws TransformerConfigurationException, ParserConfigurationException {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        //Instanzio il transformer con la factory
        transformer=TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
    }

    /**
     * Create a new DOM document
     * @param rootTag the name of the root tag
     * @return a new XML DOM document
     **/
    public Document newDocument(String rootTag) {
        Document newDoc = builder.newDocument(); 
        newDoc.appendChild(newDoc.createElement(rootTag));
        return newDoc;
    }

    public void transform(OutputStream out, Document document) throws TransformerException, IOException {
        transformer.transform(new DOMSource(document), new StreamResult(out));
    }

    /**
     * Parse an xml document from an input stream
     * @param in the input stream
     * @return the parsed xml document
     * @throws SAXException
     * @throws IOException
     **/
    public Document parse(InputStream in) throws IOException, SAXException {
        return builder.parse(in);
    }
}
