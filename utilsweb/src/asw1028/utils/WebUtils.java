/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.utils;

import asw1028.utils.xml.ManageXML;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
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
     **/
    public static void sendErrorMessage(String text, OutputStream out)
    {
        sendSimpleMessage("error", text, out);
    }
    
    /**
     * Writes on the output stream a simple xml in 
     * the format of <rootTag>text</rootTag>
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
     * in the format of <rootTag><key1>value1</key1> ...</rootTag>
     * @param elements a pair of key values where the key is the tag name and the
     * value is the tag content
     * @param rootTag
     * @param out
     **/
    public static void sendElementsMessage(String rootTag, List<Pair<String, String>> elements, OutputStream out)
    {
        try {
            ManageXML mXml = new ManageXML();
            //root elem is error
            Document doc = mXml.newDocument(rootTag);
            for(Pair<String, String> e : elements){
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
     * <root>
     *  <tag1>
     *   <tag2>
     *       content
     *   </tag2>
     *  </tag1>
     * </root>
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
    
    public static void copyStream(InputStream input, OutputStream output) throws IOException{
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
    
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    
    public static String getDateStrFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
    
    public static String getTimeStrFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("kk:mm:ss");
        return df.format(date);
    }
    
    /**
     * Returns a new file name in the forms of: prefixNUMBERS.extension
     * where the numbers are based on current creation time.
     * @param filename the name of the file you want to change
     * @param prefix the new prefix
     * @return a new file name
     **/
    public static String newFileName(String filename, String prefix){
        String extension = filename.split("\\.")[1];
        String numbs = getTimeStrFromDate(new Date()).replace(":", "");
        return prefix + numbs + "." + extension;
    }
    
    public static String getFileNameFromContentDisp(String contentDisposition){
//        String cd = file.getHeader("content-disposition");
        String filename = contentDisposition.split("filename=")[1];
        filename = filename.split(";")[0];
        filename = filename.replace("\"", "");
        return filename;
    }
}
