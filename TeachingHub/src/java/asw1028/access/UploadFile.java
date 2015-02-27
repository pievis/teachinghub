
package asw1028.access;

import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.transform.TransformerException;
//import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import javax.servlet.http.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Pievis
 */
@MultipartConfig
public class UploadFile extends HttpServlet {

    private boolean isMultipart;
//    ManageXML mxml;
    HttpSession session;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. The servlet stores a file into the server based on the received
     * request filetype (eg: avatar, attachement)
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        session = request.getSession();
        String userid = null;
        //Sono in sessione?
        if(session != null)
            userid = (String) session.getAttribute("userid");
        else{
            //Non posso caricare nulla se non sono un utente loggato
            sendError("Impossibile caricare il file come utente non loggato.", out);
            return;
        }
        
//        isMultipart = ServletFileUpload.isMultipartContent(request);
        isMultipart = true;
        if(!isMultipart){
            sendError("La request non è multipart", out);
            return;
        }
        String fileType = request.getParameter("filetype");
        //        System.out.println("FILE TYPE: " + fileType + "\nPart " + filePart);
        List<SimpleEntry<String,String>> tags = new ArrayList<SimpleEntry<String,String>>(); //per memorizzare i nomi dei file da ritornare al client
        //switch sul filetype
        switch(fileType){
            case "avatar":
                Part filePart = request.getPart("file"); // un solo file
                String avatarPath = getServletContext().getRealPath(SysKb.avatarPath);
                 //il nome dell'utente è il prefisso per il file
                String newFileName = storeFileOnServerDebug(avatarPath, userid, filePart, out);
                if(newFileName == null)
                {
                    //Error detected
                    return;
                }
                tags.add(new SimpleEntry("avatar", newFileName));
                WebUtils.sendElementsMessage("success", tags, out); //ritorna il nome dell'avatar al client
                return;
            case "attachement":
                ManageXML mxml = null;
        try {
            mxml = new ManageXML();
        } catch (Exception e){
            e.printStackTrace();
            sendError("Errore del server (manage xml)", out);
            return;
        }
                Document successDoc = mxml.newDocument("success");
                String filesPath = getServletContext().getRealPath(SysKb.attachmentsPath);
                String prefix = userid;
                String nfn;
                int i = 0;
                for(Part fp : request.getParts()){
                    if(!fp.getName().equals("filetype")){ //quel datapart è null
                        nfn = storeFileOnServerDebug(filesPath, prefix+i, fp,out);
                        if(nfn == null){
                            //error detected
                            return;
                        }
                        //aggiungo le info al successDoc
                        Element fE = successDoc.createElement("file");
                        Element fnE = successDoc.createElement("name");
                        fnE.appendChild(successDoc.createTextNode(fp.getSubmittedFileName()));
                        Element fpE = successDoc.createElement("path");
                        fpE.appendChild(successDoc.createTextNode(nfn));
                        fE.appendChild(fpE);
                        fE.appendChild(fnE);
                        successDoc.getDocumentElement().appendChild(fE);
                    }
                    i++;
                }
        {
            try {
                mxml.transform(out, successDoc); //ritorna i nomi dei file al client
            } catch (TransformerException ex) {
                Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                return;
            default:
                sendError("Impossibile eseguire l'azione per il filetype: " + fileType , out);
                return;
        }
    }
    
    /**
     * Salva il file sul server nel percorso filePath.
     * La funzione assegna un nome specifico al file salvato.
     * @return una stringa con il nome del file salvato sul server.
     * null = errore.
     **/
    private String storeFileOnServer(String filePath, String newFilePrefix, Part fileP){
        //Genero un nuovo nome per il file
        System.out.println("Storing file part: " + fileP.getSubmittedFileName());
        System.out.println("FP name: " + fileP.getName());
        System.out.println("Ct: " + fileP.getContentType());
        System.out.println("at path: " + filePath);
        String filename = fileP.getSubmittedFileName();
        //new file name eg: userid220915.jpg
        String newFileName = WebUtils.newFileName(filename, newFilePrefix);
        String newFilePath = filePath + File.separator + newFileName;
//        System.out.println("NEW FILE: " + newFilePath);
        try {
            InputStream in = fileP.getInputStream();
            WebUtils.copyStream(in, new FileOutputStream(newFilePath));
        } catch (IOException ex) {
            Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newFileName;
    }
    
    private String storeFileOnServerDebug(String filePath, String newFilePrefix, Part fileP, OutputStream out){
        //Genero un nuovo nome per il file
        System.out.println("Storing file part: " + fileP.getSubmittedFileName());
        System.out.println("FP name: " + fileP.getName());
        System.out.println("Ct: " + fileP.getContentType());
        System.out.println("at path: " + filePath);
        String filename = fileP.getSubmittedFileName();
        //new file name eg: userid220915.jpg
        String newFileName = WebUtils.newFileName(filename, newFilePrefix);
        String newFilePath = filePath + File.separator + newFileName;
//        System.out.println("NEW FILE: " + newFilePath);
        try {
            InputStream in = fileP.getInputStream();
            WebUtils.copyStream(in, new FileOutputStream(newFilePath));
        } catch (IOException ex) {
            Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
            sendError(filePath, out);
            newFileName = null;
        }
        return newFileName;
    }
    
    void sendError(String text, OutputStream out){
        WebUtils.sendErrorMessage(text, out);
        System.out.println(text);
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
