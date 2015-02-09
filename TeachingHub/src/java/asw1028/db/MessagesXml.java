/*
 * Classe per accedere al database di messaggi su file xml
 * Ogni file è situato in una precisa cartella che specifica la sezione.
 * Ogni file contiene i messaggi di una discussione.
 */
package asw1028.db;

import asw1028.db.structs.Msg;
import asw1028.db.structs.Msgs;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Interface for reading and writing to the messages xml as if it's a table
 * @author Pievis
 */
public class MessagesXml {
    
     /**
     * Getter of messages in the specified section
     * @param  filePath is the path of the xml file that contains the
     * msgs information. The path refers to a specific section.
     * @return a collection of msgs
     * */
    public static Msgs getMessages(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Msgs.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
     
        Msgs msgs = (Msgs) jaxbUnmarshaller.unmarshal( new File(filePath) );
        
        return msgs;
    }
    
    /**
     * Commit changes to the xml table
     * */
    public static void setMsgs(Msgs msgs, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Msgs.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(msgs, new File(filePath));
    }
    
    public static void addMsg(Msg message, String filePath) throws JAXBException {
        Msgs messages = getMessages(filePath);
        int last = messages.getMsg().size();
        message.setId(last+"");
        messages.getMsg().add(message);
        setMsgs(messages, filePath);
    }
}
