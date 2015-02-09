
package asw1028.db.structs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "msgs")
@XmlAccessorType (XmlAccessType.FIELD)
public class Msgs
{
    private String threadid;

    private List<Msg> msg;

    public String getThreadid ()
    {
        return threadid;
    }

    public void setThreadid (String threadid)
    {
        this.threadid = threadid;
    }

    public List<Msg> getMsg ()
    {
        if(msg == null)
            msg = new ArrayList<Msg>();
        return msg;
    }

    public void setMsg (List<Msg> msg)
    {
        this.msg = msg;
    }
}
