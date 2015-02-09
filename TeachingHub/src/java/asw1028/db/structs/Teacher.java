/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.db.structs;

import asw.interfaces.ITeacher;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lorenzo
 */
@XmlRootElement(name = "teacher")
@XmlAccessorType (XmlAccessType.FIELD)
public class Teacher extends User implements ITeacher{
    
    private Subjects subjects;
    
    public Subjects getSubjects ()
    {
        return subjects;
    }

    public void setSubjects (Subjects subjects)
    {
        this.subjects = subjects;
    }
}
