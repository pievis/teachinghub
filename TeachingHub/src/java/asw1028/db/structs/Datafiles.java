
package asw1028.db.structs;

import asw1028.db.structs.Datafile;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "datafiles")
@XmlAccessorType (XmlAccessType.FIELD)
public class Datafiles
{
    private List<Datafile> datafile;

    public List<Datafile> getDatafile ()
    {
        return datafile;
    }

    public void setDatafile (List<Datafile> datafile)
    {
        this.datafile = datafile;
    }
    
    public void addDataFile(Datafile df){
        if(datafile == null)
            datafile = new ArrayList<Datafile>();
        datafile.add(df);
    }
}
	
