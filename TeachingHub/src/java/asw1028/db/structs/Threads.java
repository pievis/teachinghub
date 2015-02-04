
package asw1028.db.structs;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "threads")
@XmlAccessorType (XmlAccessType.FIELD)
public class Threads
{
    @XmlElement(name = "thread")
    private List<Thread> thread = null;

    private String section;

    public List<Thread> getThread ()
    {
        return thread;
    }

    public void setThread (List<Thread> thread)
    {
        this.thread = thread;
    }

    public String getSection ()
    {
        return section;
    }

    public void setSection (String section)
    {
        this.section = section;
    }
}