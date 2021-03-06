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
 * @author Lorenzo
 */
@XmlRootElement(name = "teachers")
@XmlAccessorType (XmlAccessType.FIELD)
public class Teachers {
    
    @XmlElement(name = "teacher")
    private List<Teacher> teacher = null;

    public List<Teacher> getTeacherList ()
    {
        return teacher;
    }

    public void setTeacherList (List<Teacher> teacher)
    {
        this.teacher = teacher;
    }
}
