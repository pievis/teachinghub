
package asw1028.db.structs;

import asw.interfaces.IUser;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pievis
 */
@XmlRootElement(name = "user")
@XmlAccessorType (XmlAccessType.FIELD)
public class User implements IUser
{
    
    private String id;

    private String email;

    private String lastname;

    private String firstname;

    private String avatar;

    private String password;

    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String getEmail ()
    {
        return email;
    }

    @Override
    public void setEmail (String email)
    {
        this.email = email;
    }

    @Override
    public String getLastname ()
    {
        return lastname;
    }

    @Override
    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    @Override
    public String getFirstname ()
    {
        return firstname;
    }

    @Override
    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    @Override
    public String getAvatar ()
    {
        return avatar;
    }

    @Override
    public void setAvatar (String avatar)
    {
        this.avatar = avatar;
    }

    @Override
    public String getPassword ()
    {
        return password;
    }

    @Override
    public void setPassword (String password)
    {
        this.password = password;
    }
}
