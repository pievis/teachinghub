
package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "lastupdate")
@XmlAccessorType (XmlAccessType.FIELD)
public class Lastupdate
{
    private String autor;

    private Datetime datetime;

    public String getAutor ()
    {
        return autor;
    }

    public void setAutor (String autor)
    {
        this.autor = autor;
    }

    public Datetime getDatetime ()
    {
        return datetime;
    }

    public void setDatetime (Datetime datetime)
    {
        this.datetime = datetime;
    }
}