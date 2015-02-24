
package asw1028.access;

import asw1028.db.PagesXml;
import asw1028.db.structs.Datafile;
import asw1028.db.structs.Datafiles;
import asw1028.db.structs.Page;
import asw1028.db.structs.Pages;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Questa servlet si occupa di aggiungere una nuova pagina nell'apposita sezione.
 * Una pagina può essere aggiunta solo da un insegnante.
 * Le pagine sono memorizzate in blocchi secondo in base alle sezioni.
 * @author Pievis
 */
public class NewPage extends HttpServlet {

    ManageXML mxml;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        InputStream in = request.getInputStream();
        OutputStream out = response.getOutputStream();
        HttpSession session = request.getSession();
        
        String userid = null, sectionid = null;
        Document doc = null;
        
        try {
            mxml = new ManageXML();
            doc = mxml.parse(in);
            userid = WebUtils.getContentFromNode(doc, new String[] { "userid"});
            sectionid = WebUtils.getContentFromNode(doc, new String[] { "sectionid"});
        } catch (Exception e){
            sendError("Impossibile ottenere le informazioni necessarie dalla richiesta", out);
            e.printStackTrace();
            return;
        }
        
//        System.out.println("ui " + userid + "\nSessione: " + session.getAttribute("userid"));
        
        if(!userid.equals(session.getAttribute("userid"))){
            //L'utente non è quello in sessione, è un errore
            sendError("Errore con la sessione utente", out);
            return;
        }
        
        if(session.getAttribute("teacher") == null){
           //Non è un isegnante, bloccare l'inserimento 
            sendError("Utente non autorizzato a creare una nuova pagina", out);
            return;
        }
        
        //procedo prendendo i dati dal content
//        <data>
//            <userid/>
//            <sectionid/>        
//            <content>
//                    <pagename/>
//                    <description/>
//                    <msg/>
//                    <files>
//                            <file>
//                              <path/>
//                              <name/>
//                            </file>
//                    </files>
//            </content>
//        </data>
        String pagename = null, description = null, msg = null;
        try{
            pagename = WebUtils.getContentFromNode(doc, new String[] { "content", "pagename"});
            description = WebUtils.getContentFromNode(doc, new String[] { "content", "description"});
            msg = WebUtils.getContentFromNode(doc, new String[] { "content", "msg"});
        }catch(Exception e){
            e.printStackTrace();
            sendError("Errore nel formato dati ricevuto", out);
            return;
        }
        //Controlli lato server
        if(pagename.length() < 4){
            sendError("Nome pagina troppo corto", out);
            return;
        }
        if(msg.length() < 4){
            sendError("Contenuto pagina troppo corto", out);
            return;
        }
        //Ottengo i riferimenti ai files
        Datafiles attachements = new Datafiles();
        NodeList filesN = doc.getElementsByTagName("file");
        for(int i = 0; i < filesN.getLength(); i++){
            Node n = filesN.item(i);
            if(n.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element) n;
                Datafile df = new Datafile();
                String name = e.getElementsByTagName("name").item(0).getTextContent();
                String path = e.getElementsByTagName("path").item(0).getTextContent();
                df.setUrl(path);
                df.setName(name);
                attachements.addDataFile(df);
//                System.out.println("FILES: " + name + " " + path);
            }
        }
        String xmlDbPagesPath = getServletContext().getRealPath(SysKb.getPagesPathForSection(sectionid));
        try{
            //nuovo id pagina
//            System.out.println("PATH: " + xmlDbPagesPath);
            String pageId = PagesXml.getNewPageId(xmlDbPagesPath);
            //Passa alla creazione della pagina con nuovo messaggio
            createNewPage(pageId, pagename, description, userid, sectionid, msg, attachements);
            //Rispondi al client con success e le info sulla nuova pagina
            sendSuccessMsg(pageId, out);
        }catch(Exception e){
            e.printStackTrace();
            sendError("Errore nella creazione del messaggio.", out);
        }
    }
    
    private void sendSuccessMsg(String pageId, OutputStream out){
        List<SimpleEntry<String, String>> elements;
        elements = new ArrayList<SimpleEntry<String,String>>();
        elements.add(new SimpleEntry<String,String>("pageid", pageId));
        WebUtils.sendElementsMessage("success", elements, out);
    }
    
    private void createNewPage(String pageId, String pagename, String description, String autor, String sectionid, String msg, Datafiles attachements){
        String xmlDbPagesPath = getServletContext().getRealPath(SysKb.getPagesPathForSection(sectionid));
        if(pageId == null){
            //la sezione è vuota
            pageId = "0";
        }
        Page newPage = new Page();
        newPage.setId(pageId);
        newPage.setAutor(autor);
        newPage.setMsg(msg);
        newPage.setTitle(pagename);
        newPage.setDescription(description);
        if(attachements.getDatafile() != null)
            newPage.setDatafiles(attachements);
        //Scrivi sul file xml / db
        try{
            Pages pages = PagesXml.getPages(xmlDbPagesPath);
            pages.getPage().add(newPage);
            PagesXml.setPages(pages, xmlDbPagesPath);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void sendError(String text, OutputStream out){
        System.out.println(text);
        WebUtils.sendErrorMessage(text, out);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
