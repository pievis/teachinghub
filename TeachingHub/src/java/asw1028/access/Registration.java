/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.StudentsXml;
import asw1028.db.UsersManager;
import asw1028.db.structs.Student;
import asw1028.db.structs.Students;
import asw1028.db.structs.User;
import asw1028.db.structs.Users;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 *
 * @author Lorenzo
 */
@WebServlet(name = "Registration", urlPatterns = {"/Registration"})
public class Registration extends HttpServlet {

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
        InputStream in = request.getInputStream();
        response.setContentType("text/html;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        boolean xmlReceived = true;
        
        String userid = null;
        String pass = null;
        String nome = null;
        String cognome = null;
        String classe = null;
        String email = null;
        
        ManageXML mxml;
        try {
            mxml = new ManageXML();
            Document doc = mxml.parse(in);
            userid = WebUtils.getContentFromNode(doc, new String[] { "userid"});
            pass = WebUtils.getContentFromNode(doc, new String[] { "password"});
            nome = WebUtils.getContentFromNode(doc, new String[] { "name"});
            cognome = WebUtils.getContentFromNode(doc, new String[] { "surname"});
            classe = WebUtils.getContentFromNode(doc, new String[] { "class"});
            email = WebUtils.getContentFromNode(doc, new String[] { "email"});
        } catch (Exception e){
            e.printStackTrace();
            WebUtils.sendErrorMessage("Error(s) in received data", out);
            xmlReceived = false;
        }
        
        //TODO: registration checks here
        
        //Creo il nuovo utente
        if(xmlReceived) {
            Student newUser = new Student();
            newUser.setId(userid);
            newUser.setFirstname(nome);
            newUser.setLastname(cognome);
            newUser.setEmail(email);
            newUser.setPassword(pass);
            newUser.setClasse(classe);
            newUser.setAvatar(SysKb.defaultAvatar);
        
            //Salvo sul db
            try {
                saveInDb(newUser);
            } catch (JAXBException ex) {
                Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                WebUtils.sendErrorMessage("Error(s) while saving the data", out);
                System.out.println("Errore durante il salvataggio su db.");
            }

            WebUtils.sendSimpleMessage("success", "Registrazione avvenuta con successo", out);
            System.out.println("Registration Invio...");
        }
        /*
        try (PrintWriter out = response.getWriter()) {
            //i dati di registrzioni non sono dati di sessione, ma devono rimanere associati alla web application
//            ServletContext application = getServletContext();
//            application.setAttribute("nome", request.getParameter("nome"));
//            application.setAttribute("cognome", request.getParameter("cognome"));
//            application.setAttribute("user", request.getParameter("user"));
//            application.setAttribute("pass", request.getParameter("pass"));
            
            //Leggo la richiesta
            Student newUser = new Student();
            newUser.setId(request.getParameter("user"));
            newUser.setFirstname(request.getParameter("nome"));
            newUser.setLastname(request.getParameter("cognome"));
            newUser.setEmail(request.getParameter("email"));
            newUser.setPassword(request.getParameter("pass"));
            newUser.setClasse(request.getParameter("classe"));
            newUser.setAvatar(SysKb.defaultAvatar);            
            
            //TODO CHECK SE UTENTE ESISTE
            
            //TODO aggiungere check da remoto + robustezza
            
            //Salvo sul db
            try {
                saveInDb(newUser);
            } catch (JAXBException ex) {
                Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Errore durante il salvataggio su db.");
            }
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Registration</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Registration at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
        */
    }
    
    /*
    * Saves the registration data in the db
    */
    private void saveInDb(Student newUser) throws JAXBException
    {
        String teachersPath = getServletContext().getRealPath("/WEB-INF/xml/teachers.xml");
        String studentsPath = getServletContext().getRealPath("/WEB-INF/xml/students.xml");
        System.out.println("PRINT: " + studentsPath);
        UsersManager mng = new UsersManager(teachersPath, studentsPath);
        mng.addStudentToDb(newUser);
//        System.out.println("SAVED");
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
