/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.access;

import asw1028.db.UsersManager;
import asw1028.db.structs.Student;
import asw1028.db.structs.Subjects;
import asw1028.db.structs.Teacher;
import asw1028.db.structs.User;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        boolean xmlReceived = true;
        
        String userid = null;
        String pass = null;
        String nome = null;
        String cognome = null;
        String classe = null;
        String email = null;
        String materia = null;
        boolean isStudent = false;
        ManageXML mxml;
        try {
            mxml = new ManageXML();
            Document doc = mxml.parse(in);
             //se ha il parametro classe allora lo è
            if(doc.getDocumentElement().getElementsByTagName("class").getLength() != 0)
                isStudent = true;
            userid = WebUtils.getContentFromNode(doc, new String[] { "userid"});
            pass = WebUtils.getContentFromNode(doc, new String[] { "password"});
            nome = WebUtils.getContentFromNode(doc, new String[] { "name"});
            cognome = WebUtils.getContentFromNode(doc, new String[] { "surname"});
            if(isStudent)
                classe = WebUtils.getContentFromNode(doc, new String[] { "class"});
            else
                materia = WebUtils.getContentFromNode(doc, new String[] { "subject"});
            email = WebUtils.getContentFromNode(doc, new String[] { "email"});
        } catch (Exception e){
            e.printStackTrace();
            WebUtils.sendErrorMessage("Error(s) in received data", out);
            xmlReceived = false;
        }
        //registration checks here
        String errors= "Errori nei campi inviati - ";
        boolean error = false;
        if(pass.length() < 5){
            errors+= "Password troppo corta. ";
            error = true;
        }
        if(!WebUtils.isValidEmailAddress(email)){
            errors+= "Email non valida. ";
            error = true;
        }
        String teachersPath = getServletContext().getRealPath(SysKb.xmlDbTeachers);
        String studentsPath = getServletContext().getRealPath(SysKb.xmlDbStudents);
        UsersManager um = new UsersManager(teachersPath, studentsPath);
        try {
            if(um.getUserById(userid) != null){
                errors+= "L'username selezionato è in uso. ";
                error = true;
            }
        } catch (JAXBException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            error = true;
        }
        
        if(error){
            WebUtils.sendErrorMessage(errors, out);
            return; //termina la servlet
        }
        //Creo il nuovo utente
        if(xmlReceived) {
//            User newUser;
//            newUser = new User();
//            newUser.setId(userid);
//            newUser.setFirstname(nome);
//            newUser.setLastname(cognome);
//            newUser.setEmail(email);
//            newUser.setPassword(pass);
//            newUser.setAvatar(SysKb.defaultAvatar);
            if(isStudent){
                Student newUser;
                newUser = new Student();
                newUser.setId(userid);
                newUser.setFirstname(nome);
                newUser.setLastname(cognome);
                newUser.setEmail(email);
                newUser.setPassword(pass);
                newUser.setAvatar(SysKb.defaultAvatar);
                newUser.setClasse(classe);
                
                //Salvo sul db
                try {
                    saveInDb(newUser);
                } catch (JAXBException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                    WebUtils.sendErrorMessage("Error(s) while saving the data", out);
                    System.out.println("Errore durante il salvataggio su db.");
                }
                //notifico
                WebUtils.sendSimpleMessage("success", "Registrazione avvenuta con successo", out);
            }else{
                //E' un insegnante
                Teacher newUser;
                newUser = new Teacher();
                newUser.setId(userid);
                newUser.setFirstname(nome);
                newUser.setLastname(cognome);
                newUser.setEmail(email);
                newUser.setPassword(pass);
                newUser.setAvatar(SysKb.defaultAvatar);
                Subjects sbjs = new Subjects();
                sbjs.setSubject(new String[]{materia});
                newUser.setSubjects(sbjs);
                
                //Salvo sul db
                try {
                    saveInDb(newUser);
                } catch (JAXBException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                    WebUtils.sendErrorMessage("Error(s) while saving the data", out);
                    System.out.println("Errore durante il salvataggio su db.");
                }
                //notifico
                WebUtils.sendSimpleMessage("success", "Richiesta di registrazione inviata, attendere notizie dagli amministratori.", out);
            }
        }
    }
    
    /*
    * Saves the registration data in the db for a student
    */
    private void saveInDb(Student newUser) throws JAXBException
    {
        String teachersPath = getServletContext().getRealPath("/WEB-INF/xml/teachers.xml");
        String studentsPath = getServletContext().getRealPath("/WEB-INF/xml/students.xml");
//        System.out.println("PRINT: " + studentsPath);
        UsersManager mng = new UsersManager(teachersPath, studentsPath);
        mng.addStudentToDb(newUser);
//        System.out.println("SAVED");
    }
    
    /*
    * Saves the registration data in the pending db for the teacher
    */
    private void saveInDb(Teacher newUser) throws JAXBException
    {
        String teachersPath = getServletContext().getRealPath(SysKb.xmlDbPendingTeachers);
        String studentsPath = getServletContext().getRealPath("/WEB-INF/xml/students.xml");
        UsersManager mng = new UsersManager(teachersPath, studentsPath);
        mng.addTeacherToDb(newUser);
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
