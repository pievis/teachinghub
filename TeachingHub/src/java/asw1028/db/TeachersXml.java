package asw1028.db;

import asw1028.db.structs.Teacher;
import asw1028.db.structs.Teachers;
import java.io.File;
import java.util.List;
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
    public static Teachers getTeachers(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Teachers.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Teachers users = (Teachers) jaxbUnmarshaller.unmarshal( new File(filePath) );
        return users;
    }
    
    public static Teachers getTeachers() throws JAXBException{
        return getTeachers(filePath);
    }
    
    /**
     * Return the (unique) teacher having the required id
     * @param id the id to seach
     * @return The teacher, null otherwise
    **/
    public static Teacher getTeacherById(String id, String filePath) throws JAXBException {
        Teachers teachers = getTeachers(filePath);
        for(Teacher t : teachers.getTeacherList())
            if(t.getId().equals(id))
                return t;
        return null;
    }
    /*
    * Set the modified data in the xml db and commit the changes
    */
    public static void setTeachers(Teachers users, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Teachers.class);
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
    public static void setTeachers(Teachers users) throws PropertyException, JAXBException{
        setTeachers(users, filePath);
    }
    
    //for testing
    public static void main(String[] args) throws JAXBException{
        //read
        Teachers users = TeachersXml.getTeachers();
        Teacher newu = new Teacher();
        newu.setAvatar("avatar0.png");
        newu.setId("Pievis");
        newu.setFirstname("Pierluigi");
        newu.setLastname("Montagna");
        users.getTeacherList().add(newu);
        TeachersXml.setTeachers(users, filePath);
    }
}
