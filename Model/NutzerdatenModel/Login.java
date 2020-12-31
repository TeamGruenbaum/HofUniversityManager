package Model.NutzerdatenModel;

import Controller.Speicher.Schluesselmeister;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Arrays;

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

    public void setName(String name)
    {
        benutzername=Schluesselmeister.verschluesseln(name);
    }

    public String getPasswort()
    {
        return Schluesselmeister.entschluesseln(passwort);
    }

    public void setPasswort(String passwort)
    {
        this.passwort=Schluesselmeister.verschluesseln(passwort);
    }


}
