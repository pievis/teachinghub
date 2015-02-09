/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.db.structs;

import asw.interfaces.IStudent;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "student")
@XmlAccessorType (XmlAccessType.FIELD)
public class Student extends User implements IStudent
{
    private String classe;
    private String hobby;

    public String getClasse ()
    {
        return classe;
    }

    public void setClasse (String classe)
    {
        this.classe = classe;
    }
    
    public String getHobby ()
    {
        return hobby;
    }

    public void setHobby (String hobby)
    {
        this.hobby = hobby;
    }
}