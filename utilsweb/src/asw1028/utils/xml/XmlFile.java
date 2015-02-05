package asw1028.utils.xml;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Used to manage stored xml files
 * reading, writing, access...
 * @author Pievis
 */
public class XmlFile {

    ManageXML mxml;
    File file;
    String filePath;
    Document doc;
    
    /**
     * @param filePath
     **/
    public XmlFile(String filePath) {
        try {
            mxml = new ManageXML();
            this.filePath = filePath;
            file = new File(filePath);
            //System.out.println(file.getAbsolutePath());
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XmlFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @return the File
     **/
    public File getFile(){
        return file;
    }
    
    /**
     * Saves the xml file on disk
     **/
    public void saveFile() throws TransformerException, IOException{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(
            "{http://xml.apache.org/xslt}indent-amount", "5"); //indent the file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(XmlFile.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        mxml.transform(fos, doc);
    }
    
    Document parse() throws FileNotFoundException, IOException, SAXException{
        InputStream is = new FileInputStream(file);
        return mxml.parse(is);
    }
    
    /*
    * @return a new document based on the specified xml file
    */
    public Document getDocument() throws IOException, FileNotFoundException, SAXException, Exception{
        if(doc == null){
            if(file.exists())
                doc = parse();
            else{
                throw new Exception("Missing file: " + filePath);
            }
        }
        return doc;
    }
    
    /**
     * Generates a new document to fill and save 
     * @param rootTag root tag name to give at the file
     * @return Document class
     **/
    public Document newDocument(String rootTag){
        doc = mxml.newDocument(rootTag);
        return doc;
    }
    
    public NodeList getElemetsByTagName(String tagname){
        return doc.getElementsByTagName(tagname);
    }
    
    /**
     * @return the root Element of the document
     */
    public Element getDocumentElement(){
        return doc.getDocumentElement();
    }
    
//    //Used for testing
//    public static void main(String[] args){
//        String ctx = System.getProperty("user.dir");
//        System.out.println("Starting test \nContext:  "+ ctx);
//        testWrite();
////        testRead();
//    }
    
    private static void testRead(){
        try {
            String filePath = "..\\TeachingHub\\web\\WEB-INF\\xml\\students.xml";
            System.out.println("Reading file:  "+ filePath);
            XmlFile xmlf = new XmlFile(filePath);
            Document doc = xmlf.getDocument();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("user"); //nome del tag
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                            System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    }
	}
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private static void testWrite(){
        try{
            String filePath = "..\\TeachingHub\\web\\WEB-INF\\xml\\teachers.xml";
            System.out.println("Writing file:  "+ filePath);
            XmlFile xmlf = new XmlFile(filePath);
            Document doc = xmlf.newDocument("users"); //creates the new doc with the users root
            Element rootElem = doc.getDocumentElement(); //get the rootElem
            //append the content
            Element user = doc.createElement("user");
            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode("yong"));
            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode("mook kim"));
            user.appendChild(lastname);
            user.appendChild(firstname);
            rootElem.appendChild(user);
            //save the file
            xmlf.saveFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
