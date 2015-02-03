package asw1028.db;

import asw1028.db.structs.User;
import asw1028.db.structs.Users;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Pievis
 */
public class TeachersXml {
    static String filePath = "..\\TeachingHub\\web\\WEB-INF\\xml\\teachers.xml"; 
    //non safe ma utile
    
    /*
    * Get the data from the xml db
    */
    public static Users getUsers(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Users users = (Users) jaxbUnmarshaller.unmarshal( new File(filePath) );
        return users;
    }
    
    public static Users getUsers() throws JAXBException{
        return getUsers(filePath);
    }
    
    /*
    * Set the modified data in the xml db and commit the changes
    */
    public static void setUsers(Users users, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Marshal the employees list in console
        //jaxbMarshaller.marshal(users, System.out);

        //Marshal the employees list in file
        jaxbMarshaller.marshal(users, new File(filePath));
    }
    
    /*
    * Set the modified data in the xml db and commit the changes
    */
    public static void setUsers(Users users) throws PropertyException, JAXBException{
        setUsers(users, filePath);
    }
    
    //for testing
    public static void main(String[] args) throws JAXBException{
        //read
        Users users = TeachersXml.getUsers();
        User newu = new User();
        newu.setAvatar("avatar0.png");
        newu.setId("Pievis");
        newu.setFirstname("Pierluigi");
        newu.setLastname("Montagna");
        users.getUsers().add(newu);
        TeachersXml.setUsers(users, filePath);
    }
}
