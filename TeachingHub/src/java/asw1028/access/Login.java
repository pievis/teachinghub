/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.StudentsXml;
import asw1028.db.structs.User;
import asw1028.db.structs.Users;
import java.io.IOException;
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
//        ServletContext application = getServletContext();
        
        User user = null;
        try {
            user = getUserFromDb(userid);
        } catch (JAXBException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(user != null){
            passValue = user.getPassword();
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            // aggiunta
            if(user != null && pass != null && pass.equals(passValue)) {
                HttpSession session = request.getSession();
//                session.setAttribute("nome", user.getFirstname());
//                session.setAttribute("cognome", user.getLastname());
                session.setAttribute("userid", user.getId());
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            }
            else
                out.println("<h2>Login Fallita</h2>");
            out.println("</body>");
            out.println("</html>");
        }
        
    }
    
    //Ritorna null se non trova l'utente nel db
    private User getUserFromDb(String userid) throws JAXBException{
        
        String filePath = getServletContext().getRealPath("/WEB-INF/xml/students.xml");
        String filePath2 = getServletContext().getRealPath("/WEB-INF/xml/teachers.xml");
//        User user = null;
        
        Users users = StudentsXml.getUsers(filePath);
        List<User> userlist = users.getUsers();
        for(User u : userlist){
            if(u.getId().equals(userid))
                return u;
        }
        Users users2 = StudentsXml.getUsers(filePath2);
        List<User> userlist2 = users2.getUsers();
        for(User u : userlist2){
            if(u.getId().equals(userid))
                return u;
        }
        
        return null;
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
