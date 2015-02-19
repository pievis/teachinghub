/*
 * Classe per accedere al database di pagine su file xml
 * Ogni pagina è relativa ad una specifica sezione.
 */
package asw1028.db;

import asw1028.db.structs.Page;
import asw1028.db.structs.Pages;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Interface for reading and writing to the pages xml as if it's a table
 * @author Pievis
 */
public class PagesXml {
     /**
     * Getter of pages in the specified section
     * @param  filePath is the path of the xml file that contains the
     * pages information. The path refers to a specific section.
     * @return a collection of pages
     * */
    public static Pages getPages(String filePath) throws JAXBException{
        //unmarshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Pages.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
     
        Pages pages = (Pages) jaxbUnmarshaller.unmarshal( new File(filePath) );
        
        return pages;
    }
    
    /**
     * Commit changes to the xml table
     * */
    public static void setPages(Pages pages, String filePath) throws PropertyException, JAXBException{
        //marshall
        JAXBContext jaxbContext = JAXBContext.newInstance(Pages.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(pages, new File(filePath));
    }
    
    public static void addPage(Page page, String filePath) throws JAXBException {
        Pages pages = getPages(filePath);
        int last = pages.getPage().size();
        page.setId(last+"");
        pages.getPage().add(page);
        setPages(pages, filePath);
    }
    
    /**
     *  Ritorna l'ultimo id delle pagine
     **/
    private static int getLastPageId(String filePath) throws JAXBException
    {
        int intId = -1;
        Pages pages = getPages(filePath);
        if(pages.getPage() != null)
            for(Page t : pages.getPage()){
                int val = 0;
                if( t.getId() != null)
                    val = Integer.parseInt( t.getId());
                if(intId < val)
                    intId = val;
            }
        return intId;
    }
    
    /**
     * Ottiene un nuovo id per la una pagina la cui raccolta è specificata da filePath
     **/
    public static String getNewPageId(String filePath){
        try{
            int id = getLastPageId(filePath) + 1;
            return id + "";
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }   
    }
}
