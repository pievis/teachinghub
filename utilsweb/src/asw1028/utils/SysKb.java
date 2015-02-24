/*
 * Methods and constants in this class are meant to help the developer.
 */
package asw1028.utils;

/**
 * Information about the built system.
 * @author Pievis
 */
public class SysKb {
    
    //Web constant
    public static final String avatarPath = "/multimedia/avatars/";
    public static final String attachmentsPath = "/multimedia/attaches/";
    public static final String defaultAvatar = "avatar0.png";
    //xml
    public static final String xmlDbSections = "/WEB-INF/xml/sections/sections.xml";
    public static final String msgXmlPrefix = "msgs";
    public static final String xmlDbPath = "/WEB-INF/xml/";
    public static final String xmlDbStudents = "/WEB-INF/xml/students.xml";
    public static final String xmlDbTeachers = "/WEB-INF/xml/teachers.xml";
    public static final String xmlDbPendingTeachers = "/WEB-INF/xml/pendingteachers.xml";
    
    private static final String xmlDbThreadsRel = "/WEB-INF/xml/sections/[SECTION]/threads/threads.xml";
    private static final String xmlDbPagesRel = "/WEB-INF/xml/sections/[SECTION]/pages.xml";
    private static final String xmlDbMsgsRel = "/WEB-INF/xml/sections/[SECTION]/threads/" + msgXmlPrefix + "[ID].xml";

    /**
     * Returns a relative path for threads.xml for the specified section
     * @param section
     * @return relative path for threads.xml
     */
    public static String getThreadsPathForSection(String section) {
        return xmlDbThreadsRel.replace("[SECTION]", section);
    }
    /**
     * Returns a relative path for pages.xml for the specified section
     * @param section
     * @return relative path for pages.xml
     */
    public static String getPagesPathForSection(String section) {
        return xmlDbPagesRel.replace("[SECTION]", section);
    }
    /**
     * Returns a relative path for msgID.xml for the specified discussion
     * @param section
     * @param threadId the id of the discussion
     * @return relative path for msgID.xml
     */
    public static String getMsgsPath(String section, String threadId) {
        return xmlDbMsgsRel.replace("[SECTION]", section).replace("[ID]", threadId);
    }
    
}
