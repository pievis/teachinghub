/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.db.structs;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "students")
@XmlAccessorType (XmlAccessType.FIELD)
public class Students
{
    @XmlElement(name = "student")
    private List<Student> users = null;
 
    public List<Student> getUserList() {
        return users;
    }
 
    public void setUserList(List<Student> users) {
        this.users = users;
    }
}
