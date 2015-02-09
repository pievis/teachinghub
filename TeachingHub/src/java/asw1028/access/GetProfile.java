/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw.interfaces.IUser;
import asw1028.db.UsersManager;
import asw1028.db.structs.Student;
import asw1028.db.structs.Teacher;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * Questa servlet si occupa di ritornare informazioni relative ad un utente richiesto
 * @author Pievis
 */
@WebServlet(name = "GetProfile", urlPatterns = {"/GetProfile"})
public class GetProfile extends HttpServlet {

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
        OutputStream out = response.getOutputStream();
        
        String userId = request.getParameter("userid");
        IUser user = getUserFromDb(userId);
        if(user == null){
            //Questo utente non esiste
            sendError("L'utente richiesto non esiste", out);
            return;
        }
        user.setPassword(null); //non voglio inviare la password
        if(user instanceof Student){
            //Lavora con studenti
            marshallUser(user, Student.class, out);
            return;
        }
        if(user instanceof Teacher){
            //Lavara con insegnanti
            marshallUser(user, Teacher.class, out);
            return;
        }
        sendError("Errore", out);
    }
    
    /**
     * Codifica l'utente in xml e lo scrive sull'outputstream 
     **/
    private void marshallUser(IUser user, Class c, OutputStream out){
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(c);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(user, System.out);
            jaxbMarshaller.marshal(user, out);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void sendError(String text, OutputStream out){
        WebUtils.sendErrorMessage(text, out);
        System.out.println(text);
    }
    
    //Ritorna null se non trova l'utente nel db
    private IUser getUserFromDb(String userid){
        String studentsPath = getServletContext().getRealPath(SysKb.xmlDbStudents);
        String teachersPath = getServletContext().getRealPath(SysKb.xmlDbTeachers);
        IUser user = null;
        try{
            UsersManager mng = new UsersManager(teachersPath, studentsPath);
            user = mng.getUserById(userid);
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
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
