
package asw1028.db;

import asw1028.db.structs.Threads;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

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
    
    
}
