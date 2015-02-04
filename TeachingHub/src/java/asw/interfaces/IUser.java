/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw.interfaces;

/**
 *
 * @author Lorenzo
 */
public interface IUser {
    public String getId ();
    public void setId (String id);

    public String getEmail ();
    public void setEmail (String email);

    public String getLastname ();
    public void setLastname (String lastname);

    public String getFirstname ();
    public void setFirstname (String firstname);

    public String getAvatar ();
    public void setAvatar (String avatar);

    public String getPassword ();
    public void setPassword (String password);
}
