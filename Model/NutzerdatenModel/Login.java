package Model.NutzerdatenModel;



import Controller.Speicher.Schluesselmeister;

import java.io.Serializable;



public class Login implements Serializable
{
    private byte[] benutzername;
    private byte[] passwort;



    public Login (String benutzername, String passwort)
    {
        this.benutzername= Schluesselmeister.verschluesseln(benutzername);
        this.passwort=Schluesselmeister.verschluesseln(passwort);
    }


    public String getName()
    {
        return Schluesselmeister.entschluesseln(benutzername);
    }

    public void setName(String neuerWert)
    {
        benutzername=Schluesselmeister.verschluesseln(neuerWert);
    }

    public String getPasswort()
    {
        return Schluesselmeister.entschluesseln(passwort);
    }

    public void setPasswort(String neuerWert)
    {
        this.passwort=Schluesselmeister.verschluesseln(neuerWert);
    }


}
