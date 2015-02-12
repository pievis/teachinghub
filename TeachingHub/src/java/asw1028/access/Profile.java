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
import asw1028.db.structs.Students;
import asw1028.db.structs.Teacher;
import asw1028.db.structs.Teachers;
import asw1028.utils.SysKb;
import asw1028.utils.WebUtils;
import asw1028.utils.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Questa servlet si occupa di:
 * ritornare informazioni relative ad un utente richiesto
 * aggiornare le informazioni di uno specifico utente
 * @author Pievis
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile"})
public class Profile extends HttpServlet {
    ManageXML mxml;
    HttpSession session;

    /**
     * Processa le richieste HTTP la cui request contiene un xml con root tag:
     * get - risponde con il profilo specificato in userid
     * update - risponde con success se ha aggiornato il profilo
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
        InputStream in = request.getInputStream();
        session = request.getSession();
        String action = null;
        Document doc = null;
        
        try {
            mxml = new ManageXML();
//            WebUtils.copyStream(in, System.out); //se lo leggo non lo posso parsare
            doc = mxml.parse(in);
            action = doc.getDocumentElement().getTagName();
        } catch (Exception ex) {
            ex.printStackTrace();
            sendError("Errore nella lettura della richiesta", out);
            return;
        }
        switch(action){
            case "get":
                getProfileForClient(doc,out);
                return;
            case "update":
                updateProfile(doc,out);
                return;
            default:
                sendError("Impossibile eseguire comando specificato", out);
                return;
        }
        
    }
    
    /**
     * Aggiorna il profilo utente e ritorna un messaggio semplice di success
     * se la modifica è avvenuta senza problemi.
     **/
    private void updateProfile(Document doc, OutputStream out){
        //studente o docente?
        String userTag = "student";
        boolean isStudent = doc.getElementsByTagName("student").getLength() > 0;
        if(!isStudent)
            userTag = "teacher";
        //controllo che la modifica sia sull'utente in sessione
        String reqUser = WebUtils.getContentFromNode(doc, new String[]{userTag, "userid"});
        if(session == null || !reqUser.equals(session.getAttribute("userid"))){
            sendError("Impossibile modificare l'utente", out);
            return;
        }
        //Leggo il documento in ingresso con i valori aggiornati del profilo utente
        String firstname = WebUtils.getContentFromNode(doc, new String[]{userTag, "firstname"});
        String lastname = WebUtils.getContentFromNode(doc, new String[]{userTag, "lastname"});
        String email = WebUtils.getContentFromNode(doc, new String[]{userTag, "email"});
        String avatar = WebUtils.getContentFromNode(doc, new String[]{userTag, "avatar"});
        //e aggiorno il db
        if(isStudent){
            String classe = WebUtils.getContentFromNode(doc, new String[]{userTag, "classe"});
            String hobby = WebUtils.getContentFromNode(doc, new String[]{userTag, "hobby"});
            String xmlDbStudentsPath = getServletContext().getRealPath(SysKb.xmlDbStudents);
            try {
                Students stds = StudentsXml.getStudents(xmlDbStudentsPath);
                List<Student> students = stds.getUserList();
                Student updStd = null;
                for(Student s : students){
                    if(s.getId().equals(reqUser)){
                        updStd = s;
                    }
                }
                if(updStd == null){
                    sendError("Impossibile trovare lo studente richiesto", out);
                    return;
                }
                updStd.setFirstname(firstname);
                updStd.setLastname(lastname);
                updStd.setEmail(email);
                updStd.setAvatar(avatar);
                updStd.setHobby(hobby);
                updStd.setClasse(classe);
                StudentsXml.setStudents(stds, xmlDbStudentsPath); //commit
            } catch (JAXBException ex) {
                ex.printStackTrace();
                sendError("Errore nel salvataggio dello studente", out);
                return;
            }
        }
        else{
            //è un isegnante
            String xmlDbTeachersPath = getServletContext().getRealPath(SysKb.xmlDbTeachers);
            try {
                Teachers ths = TeachersXml.getTeachers(xmlDbTeachersPath);
                List<Teacher> teachers = ths.getTeacherList();
                Teacher updTh = null;
                for(Teacher t : teachers){
                    if(t.getId().equals(reqUser)){
                        updTh = t;
                    }
                }
                if(updTh == null){
                    sendError("Impossibile trovare l'insegnante richiesto", out);
                    return;
                }
                updTh.setFirstname(firstname);
                updTh.setLastname(lastname);
                updTh.setEmail(email);
                updTh.setAvatar(avatar);
                TeachersXml.setTeachers(ths, xmlDbTeachersPath); //commit
            } catch (JAXBException ex) {
                ex.printStackTrace();
                sendError("Errore nel salvataggio dello studente", out);
                return;
            }
        }
        WebUtils.sendSimpleMessage("success", "Update del profilo riuscito", out);
    }
    
    /*
    * Ritorna al client le informazioni sull'utente
    */
    private void getProfileForClient(Document doc, OutputStream out){
        String userId = WebUtils.getContentFromNode(doc, new String[]{"userid"});
        IUser user = getUserFromDb(userId);
//        System.out.println("Getting profile of "  + userId);
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
//            jaxbMarshaller.marshal(user, System.out);
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
