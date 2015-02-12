/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.db;

import asw.interfaces.IStudent;
import asw.interfaces.ITeacher;
import asw.interfaces.IUser;
import asw1028.db.structs.Student;
import asw1028.db.structs.Students;
import asw1028.db.structs.Teacher;
import asw1028.db.structs.Teachers;
import asw1028.utils.SysKb;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Lorenzo
 */
public class UsersManager {
    /**
     * The class provide an unified access object to both students and teachers xml files
     */
    String teachersPath; 
    String studentsPath;
    
    public UsersManager(String teachersPath, String studentsPath) {
        this.teachersPath = teachersPath;
        this.studentsPath = studentsPath;
    }
    
    public UsersManager(String contextPath) {
        this.teachersPath = contextPath + SysKb.xmlDbTeachers;
        this.studentsPath = contextPath + SysKb.xmlDbStudents;
        //System.out.println("\n TP:"+teachersPath+"\n SP:" + studentsPath +"\n--------");
    }
    
    public List<IUser> getAllUsers(){
        //TODO add code here
        return null;
    }
    
    public List<ITeacher> getAllTeacher() {
        //TODO add code here
        return null;
    }
    
    public List<IStudent> getAllStudents() {
        //TODO add code here
        return null;
    }
    
    public IUser getUserById(String userid) throws JAXBException {
        IUser users = getStudentByID(userid);
        if(users == null) {
            users = getTeacherByID(userid);
        }        
        return users;
    }
    
    public IStudent getStudentByID(String userid) throws JAXBException {
        return StudentsXml.getStudentById(userid, studentsPath);
    }
    
    public ITeacher getTeacherByID(String userid) throws JAXBException {
        return TeachersXml.getTeacherById(userid, teachersPath);
    }
    
    public void addStudentToDb(Student newUser) throws JAXBException {
        Students users = StudentsXml.getStudents(studentsPath);
        List<Student> tl = users.getUserList();
        if(tl != null)
            tl.add(newUser);
        else{
            tl = new ArrayList<Student>();
            tl.add(newUser);
            users.setUserList(tl);
        }  
        StudentsXml.setStudents(users,studentsPath);
    }
    
    public void addTeacherToDb(Teacher newUser) throws JAXBException {
        Teachers users = TeachersXml.getTeachers(teachersPath);
        List<Teacher> tl = users.getTeacherList();
        if(tl != null)
            tl.add(newUser);
        else{
            tl = new ArrayList<Teacher>();
            tl.add(newUser);
            users.setTeacherList(tl);
        }    
        TeachersXml.setTeachers(users,teachersPath);
    }
}
