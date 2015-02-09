
package asw1028.db.structs;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.input.DataFormat;
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

    private Lastupdate lastupdate;

    private String title;

    private String autor;

    private String description;

    private Creationdate creationdate;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Lastupdate getLastupdate ()
    {
        return lastupdate;
    }

    public void setLastupdate (Lastupdate lastupdate)
    {
        this.lastupdate = lastupdate;
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

    public Creationdate getCreationdate ()
    {
        return creationdate;
    }

    public void setCreationdate (Creationdate creationdate)
    {
        this.creationdate = creationdate;
    }
    
    public void setCreationdate(Date date){
        Creationdate cd = new Creationdate();
        cd.setDatetime(new Datetime(date));
        setCreationdate(cd);
    }

    public void setLastupdate(Date date, String autor) {
        Lastupdate lu = new Lastupdate();
        lu.setAutor(autor);
        lu.setDatetime(new Datetime(date));
        setLastupdate(lu);
    }
}