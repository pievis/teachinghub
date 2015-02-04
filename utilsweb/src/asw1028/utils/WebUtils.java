/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.utils;

import asw1028.utils.xml.ManageXML;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;

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
        try {
            ManageXML mXml = new ManageXML();
            //root elem is error
            Document doc = mXml.newDocument("error");
            doc.createTextNode(text);
            mXml.transform(out, doc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio del messaggio: " + text);
        }
        
    }
    
}
