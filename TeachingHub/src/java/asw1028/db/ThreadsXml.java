
package asw1028.db;

import asw1028.db.structs.Threads;
import asw1028.db.structs.Thread;
import asw1028.utils.xml.ManageXML;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

/**
 * Interface for reading and writing to the threads xml as if it's a table
 * @author Pievis
 */
public class ThreadsXml {
    
    /**
     * Getter of threads in the specified section
     * @param  filePath is the path of the xml file that contains the
     * threads information. The path refers to a specific section.
     * @return a collection of threads
     * */
    public static Threads getThreads(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Threads.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
     
        Threads threads = (Threads) jaxbUnmarshaller.unmarshal( new File(filePath) );
        
        return threads;
    }
    
    /**
     * Commit changes to the xml table
     * */
    public static void setThreads(Threads threads, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Threads.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(threads, new File(filePath));
    }
    
    /**
     *  Ritorna l'ultimo id del thread.
     **/
    private static int getLastThreadId(String filePath) throws JAXBException
    {
        int intId = -1;
        Threads threads = getThreads(filePath);
        if(threads.getThread() != null)
            for(Thread t : threads.getThread()){
                int val = Integer.parseInt( t.getId());
                if(intId < val)
                    intId = val;
            }
        return intId;
    }
    
    /**
     * Ottiene un nuovo id per il thread specificato da filePath
     **/
    public static String getNewThreadId(String filePath){
        try{
            int id = getLastThreadId(filePath) + 1;
            return id + "";
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }   
    }
    
    /**
     * Get the thread with given ID
     * @param filePath File position
     * @param id Threads identifier
     * @return Return null if no thread match the given ID
     */
    public static Document getThreadById(String filePath, String id) throws JAXBException, TransformerConfigurationException, ParserConfigurationException {
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Threads.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Threads threads = (Threads) jaxbUnmarshaller.unmarshal( new File(filePath) );
        Thread selectedThread = null;
        for(Thread t : threads.getThread()) {
            if(t.getId().equals(id))
                selectedThread = t;
        }

        if(selectedThread == null) 
            return null;         
        jaxbContext = JAXBContext.newInstance(Thread.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document threadXml = db.newDocument();
        
        jaxbMarshaller.marshal(selectedThread, threadXml);
        return threadXml;
    }
    
}
