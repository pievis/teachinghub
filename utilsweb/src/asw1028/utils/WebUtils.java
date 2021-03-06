/*
 * Methods in this class are meant to help the developer.
 */
package asw1028.utils;

import asw1028.utils.xml.ManageXML;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Static methods used for communication purpose
 * @author Pievis
 */
public class WebUtils {
    
    /**
     * Writes on the output stream the standard xml error message
     * @param text error message
     * @param out the stream where the message is sent
     **/
    public static void sendErrorMessage(String text, OutputStream out)
    {
        sendSimpleMessage("error", text, out);
    }
    
    /**
     * Writes on the output stream a simple xml in 
     * the format of {@code <rootTag>text</rootTag> }
     * @param text
     * @param rootTag
     * @param out
     **/
    public static void sendSimpleMessage(String rootTag, String text, OutputStream out)
    {
        try {
            ManageXML mXml = new ManageXML();
            //root elem is error
            Document doc = mXml.newDocument(rootTag);
            Text el = doc.createTextNode(text);
            doc.getDocumentElement().appendChild(el);
            mXml.transform(out, doc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio del messaggio: " + text);
        }
    }
    
     /**
     * Writes on the output stream an xml specified with rootTag and elements 
     * in the format of {@code <rootTag><key1>value1</key1> ...</rootTag> }
     * @param elements a pair of key values where the key is the tag name and the
     * value is the tag content
     * @param rootTag
     * @param out
     **/
    public static void sendElementsMessage(String rootTag, List<SimpleEntry<String, String>> elements, OutputStream out)
    {
        try {
            ManageXML mXml = new ManageXML();
            //root elem is error
            Document doc = mXml.newDocument(rootTag);
            for(SimpleEntry<String, String> e : elements){
                Element elem = doc.createElement(e.getKey());
                elem.appendChild(doc.createTextNode(e.getValue()));
                doc.getDocumentElement().appendChild(elem);
            }
            mXml.transform(out, doc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio del messaggio");
        }
    }
    
    /**
     * Returns the content of a node of the xml document as string.
     * @param doc
     * @param path is an array of strings where each string is a node to visit
     * to get to the interested content.
     * Example:
     * {@code
     * <root>
     *  <tag1>
     *   <tag2>
     *       content
     *   </tag2>
     *  </tag1>
     * </root> }
     * path = ["tag1","tag2"]
     * @return the content as string
     **/
    public static String getContentFromNode(Document doc, String[] path)
    {
        String content = null;
        try{
            Element holder = doc.getDocumentElement();
            for(String str : path){
//                System.out.println("debug "+ holder.getNodeName());
                Node nholder = holder.getElementsByTagName(str).item(0);
                if(nholder.getNodeType() == Node.ELEMENT_NODE)
                    holder = (Element) nholder;
            }
            content = holder.getTextContent();
        }catch(Exception e){
            System.out.println("Errore dutante la ricerca del contenuto.");
            e.printStackTrace();
        }
        
        return content;
    }
    
    /**
     * Get the content of the xml file and puts it on the outputstream
     * @param filePath
     * @param out
     **/
    public static void fileToOutputStream(String filePath, OutputStream out){
        try {
            FileInputStream in = new FileInputStream(filePath);
            copyStream(in, out);
        } catch (Exception ex) {
            Logger.getLogger(WebUtils.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Copies the input stream to the output
     * @param input
     * @param output
     * @throws java.io.IOException
     **/
    public static void copyStream(InputStream input, OutputStream output) throws IOException{
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
    
    /**
     * Validates the email address passed as input.
     * @param email
     * @return true if the email is valid
     **/
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    
    /**
     * Returns a date string in the format of dd/MM/yyyy.
     * Eg: 22/04/1991
     * @param date
     * @return date as String (dd/MM/yyyy)
     **/
    public static String getDateStrFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
    
    /**
     * Returns a time string in the format of kk:mm:ss.
     * Eg: 15:30:22
     * @param date
     * @return time of date as String (kk:mm:ss)
     **/
    public static String getTimeStrFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("kk:mm:ss");
        return df.format(date);
    }
    
    /**
     * Returns a new file name in the forms of: prefixNUMBERS.extension
     * where the numbers are based on current creation time.
     * @param filename the name of the file you want to change
     * @param prefix the new prefix
     * @return a new file name as string
     **/
    public static String newFileName(String filename, String prefix){
        String extension = filename.split("\\.")[1];
        String numbs = getTimeStrFromDate(new Date()).replace(":", "");
        return prefix + numbs + "." + extension;
    }
    
    @Deprecated
    public static String getFileNameFromContentDisp(String contentDisposition){
//        String cd = file.getHeader("content-disposition");
//        System.out.println("TEST C: " + contentDisposition);
        String filename = contentDisposition.split("filename=")[1];
        filename = filename.split(";")[0];
        filename = filename.replace("\"", "");
        return filename;
    }
    
    /**
     * Uses JAXB to convert a POJO into an xml document.
     * @param c classof obj
     * @param obj object you want to marshall
     * @return a Document that is structured as the input POJO obj
     * @throws javax.xml.bind.PropertyException 
     * @throws javax.xml.bind.JAXBException 
     * @throws javax.xml.parsers.ParserConfigurationException 
     */
    public static Document marshallObjInDocument(Class c, Object obj) throws PropertyException, JAXBException, ParserConfigurationException
    {
        // Create the Document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        JAXBContext jaxbContext = JAXBContext.newInstance(c);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(obj, doc);
        return doc;
    }
}
