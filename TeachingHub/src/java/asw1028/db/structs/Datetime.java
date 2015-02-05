
package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "datetime")
@XmlAccessorType (XmlAccessType.FIELD)
public class Datetime
{
    private String time;

    private String date;

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }
}