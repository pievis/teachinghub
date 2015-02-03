package asw1028.db;

import asw1028.db.structs.User;
import asw1028.db.structs.Users;
import asw1028.utils.xml.XmlFile;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Interface for reading and writing to the Students xml as if it's a table
 * @author Pievis
 */
public class StudentsXml {
    
    static String filePath = "..\\TeachingHub\\web\\WEB-INF\\xml\\students.xml"; 
    //non safe ma utile
    
    public static Users getUsers(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
     
        Users users = (Users) jaxbUnmarshaller.unmarshal( new File(filePath) );
     
//        for(User emp : users.getUsers())
//        {
//            System.out.println(emp.getId());
//            System.out.println(emp.getFirstname());
//        }
        
        return users;
    }
    
    public static Users getUsers() throws JAXBException{
        return getUsers(filePath);
    }
    
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
    
    public static void setUsers(Users users) throws PropertyException, JAXBException{
        setUsers(users, filePath);
    }
    
    //for testing
    public static void main(String[] args) throws JAXBException{
        //read
        Users users = StudentsXml.getUsers();
        User newu = new User();
        newu.setAvatar("avatar0.png");
        newu.setId("Pievis");
        newu.setFirstname("Pierluigi");
        newu.setFirstname("Montagna");
        users.getUsers().add(newu);
        StudentsXml.setUsers(users, filePath);
    }
    
}
