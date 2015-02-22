package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model di aboutus.xml
 * @author Pievis
 */
@XmlRootElement(name = "aboutus")
@XmlAccessorType (XmlAccessType.FIELD)
public class Aboutus
{
    private String content;

    private Lastupdate lastupdate;

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    public Lastupdate getLastupdate ()
    {
        return lastupdate;
    }

    public void setLastupdate (Lastupdate lastupdate)
    {
        this.lastupdate = lastupdate;
    }
}
