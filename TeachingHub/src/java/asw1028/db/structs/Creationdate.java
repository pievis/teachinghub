
package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "creationdate")
@XmlAccessorType (XmlAccessType.FIELD)
public class Creationdate
{
    private Datetime datetime;

    public Datetime getDatetime ()
    {
        return datetime;
    }

    public void setDatetime (Datetime datetime)
    {
        this.datetime = datetime;
    }
}
