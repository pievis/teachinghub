
package asw1028.access;

import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Pievis
 */
@MultipartConfig
public class UploadFile extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
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
        
        isMultipart = ServletFileUpload.isMultipartContent(request);
        if(!isMultipart){
            sendError("La request non è multipart", out);
            return;
        }
        String fileType = request.getParameter("filetype");
        Part filePart = request.getPart("file");
        //        System.out.println("FILE TYPE: " + fileType + "\nPart " + filePart);
        //switch sul filetype
        switch(fileType){
            case "avatar":
                String avatarPath = getServletContext().getRealPath(SysKb.avatarPath);
                 //il nome dell'utente è il prefisso per il file
                String newFileName = storeFileOnServer(avatarPath, userid, filePart);
                List<Pair<String,String>> tags = new ArrayList<Pair<String,String>>();
                tags.add(new Pair("avatar", newFileName));
                WebUtils.sendElementsMessage("success", tags, out); //ritorna il nome dell'avatar al client
                return;
            case "attachement":
                //TODO
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
        String filename = WebUtils.getFileNameFromContentDisp(fileP.getHeader("content-disposition"));
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
