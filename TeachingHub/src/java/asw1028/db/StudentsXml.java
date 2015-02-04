package asw1028.db;

import asw1028.db.structs.Student;
import asw1028.db.structs.Students;
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
    
    public static Students getStudents(String filePath) throws JAXBException{
        
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
     
        Students users = (Students) jaxbUnmarshaller.unmarshal( new File(filePath) );
     
//        for(User emp : users.getUsers())
//        {
//            System.out.println(emp.getId());
//            System.out.println(emp.getFirstname());
//        }
        
        return users;
    }
    
    public static Students getStudents() throws JAXBException{
        return getStudents(filePath);
    }
    
    /**
     * Return the (unique) student having the required id
     * @param id the id to seach
     * @return The student, null otherwise
    **/
    public static Student getStudentById(String id, String filePath) throws JAXBException {
        Students users = getStudents(filePath);
        for(Student t : users.getUserList())
            if(t.getId().equals(id))
                return t;
        return null;
    }
    
    public static void setStudents(Students users, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Marshal the employees list in console
        //jaxbMarshaller.marshal(users, System.out);

        //Marshal the employees list in file
        jaxbMarshaller.marshal(users, new File(filePath));
    }
    
    public static void setStudents(Students users) throws PropertyException, JAXBException{
        setStudents(users, filePath);
    }
    
    //for testing
    public static void main(String[] args) throws JAXBException{
        //read
        Students users = StudentsXml.getStudents();
        Student newu = new Student();
        newu.setAvatar("avatar0.png");
        newu.setId("Pievis");
        newu.setFirstname("Pierluigi");
        newu.setFirstname("Montagna");
        users.getUserList().add(newu);
        StudentsXml.setStudents(users, filePath);
    }
    
}
