/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1028.utils;

/**
 * Classe di appoggio
 * @author Pievis
 */
public class SysKb {
    
    //Web constant
    public static final String avatarPath = "/multimedia/avatars/";
    public static final String defaultAvatar = "avatar0.png";
    //xml
    public static final String xmlDbSections = "/WEB-INF/xml/sections/sections.xml";
    public static final String msgXmlPrefix = "msgs";
    public static final String xmlDbPath = "/WEB-INF/xml/";
    public static final String xmlDbStudents = "/WEB-INF/xml/students.xml";
    public static final String xmlDbTeachers = "/WEB-INF/xml/teachers.xml";
    public static final String xmlDbPendingTeachers = "/WEB-INF/xml/pendingteachers.xml";
    
    private static final String xmlDbThreadsRel = "/WEB-INF/xml/sections/[SECTION]/threads/threads.xml";
    private static final String xmlDbMsgsRel = "/WEB-INF/xml/sections/[SECTION]/threads/" + msgXmlPrefix + "[ID].xml";

    public static String getThreadsPathForSection(String section) {
        return xmlDbThreadsRel.replace("[SECTION]", section);
    }
    
    public static String getMsgsPath(String section, String threadId) {
        return xmlDbMsgsRel.replace("[SECTION]", section).replace("[ID]", threadId);
    }
    
}
