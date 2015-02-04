
package asw1028.db.structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "thread")
@XmlAccessorType (XmlAccessType.FIELD)
public class Thread
{
    private String id;

    private String title;

    private String autor;

    private String description;

    private String creationdate;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAutor ()
    {
        return autor;
    }

    public void setAutor (String autor)
    {
        this.autor = autor;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getCreationdate ()
    {
        return creationdate;
    }

    public void setCreationdate (String creationdate)
    {
        this.creationdate = creationdate;
    }
}
			
			
