
package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "msg")
@XmlAccessorType (XmlAccessType.FIELD)
public class Msg
{
    private String id;

    private String content;

    private Lastupdate lastupdate;

    private String autor;

    private Datafiles datafiles;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

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

    public String getAutor ()
    {
        return autor;
    }

    public void setAutor (String autor)
    {
        this.autor = autor;
    }

    public Datafiles getDatafiles ()
    {
        return datafiles;
    }

    public void setDatafiles (Datafiles datafiles)
    {
        this.datafiles = datafiles;
    }
}
			
			
