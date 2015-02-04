/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw.interfaces.IUser;
import asw1028.db.StudentsXml;
import asw1028.db.TeachersXml;
import asw1028.db.UsersManager;
import asw1028.db.structs.Student;
import asw1028.db.structs.Teacher;
import asw1028.db.structs.User;
import asw1028.db.structs.Users;
import asw1028.utils.WebUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Lorenzo
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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
        
        String userid = request.getParameter("user");
        String pass = request.getParameter("pass");
        String passValue = null;
        boolean isTeacher = false;
//        ServletContext application = getServletContext();
        
        IUser user = null;
        try {
            user = getUserFromDb(userid);
        } catch (JAXBException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(user != null){
            passValue = user.getPassword();
            if(user instanceof Teacher)
                isTeacher = true;
        }
            // aggiunta
            if(user != null && pass != null && pass.equals(passValue)) {
                HttpSession session = request.getSession();
//                session.setAttribute("nome", user.getFirstname());
//                session.setAttribute("cognome", user.getLastname());
                session.setAttribute("userid", user.getId());
                //condizionale
                if(isTeacher)
                    session.setAttribute("teacher", true);
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            }
            else{
                response.setContentType("text/xml;charset=UTF-8");
                //Sends back an error message to the client
                OutputStream out = response.getOutputStream();
                WebUtils.sendErrorMessage("Nome utente o password errata.", out);
            }
        
    }
    
    //Ritorna null se non trova l'utente nel db
    private IUser getUserFromDb(String userid) throws JAXBException{
        String studentsPath = getServletContext().getRealPath("/WEB-INF/xml/students.xml");
        String teachersPath = getServletContext().getRealPath("/WEB-INF/xml/teachers.xml");
//        System.out.println(studentsPath + "\n" + teachersPath);
        UsersManager mng = new UsersManager(teachersPath, studentsPath);
        IUser users = mng.getUserById(userid);
        
        return users;
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
