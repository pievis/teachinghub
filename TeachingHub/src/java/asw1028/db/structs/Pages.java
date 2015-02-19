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
@XmlRootElement(name = "pages")
@XmlAccessorType (XmlAccessType.FIELD)
public class Pages
{
    private List<Page> page;

    public List<Page> getPage ()
    {
        if(page == null)
            page = new ArrayList<Page>();
        return page;
    }

    public void setPage (List<Page> page)
    {
        this.page = page;
    }
}
