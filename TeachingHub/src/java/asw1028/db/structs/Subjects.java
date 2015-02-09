package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Subjects è una raccolta di materie
 * Eg: Fisica, Matematica...
 * @author Pievis
 */

@XmlRootElement(name = "subjects")
@XmlAccessorType (XmlAccessType.FIELD)
public class Subjects {
    private String[] subject;

    public String[] getSubject ()
    {
        return subject;
    }

    public void setSubject (String[] subject)
    {
        this.subject = subject;
    }
}
