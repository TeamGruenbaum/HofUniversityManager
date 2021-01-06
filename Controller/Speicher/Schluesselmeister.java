package Controller.Speicher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class Schluesselmeister
{
    public static byte[] verschluesseln(String wert)
    {
        Cipher cipher=null;
        byte[] ergebnis=null;

        try
        {
            cipher=Cipher.getInstance("AES");
            SecretKey secretKey=new SecretKeySpec(new byte[]{3, 7, 4, 5, 3, 7, 4, 5, 3, 7, 4, 5, 3, 7, 4, 5}, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            ergebnis=cipher.doFinal(wert.getBytes(Charset.forName("UTF-8")));
        }catch(Exception keineGefahrException)
        {
            //Die Gefahr ist gebannt, da die Verschlüsselungsart und der Schlüssel hartkodiert ist und somit nie falsch sein können
            keineGefahrException.printStackTrace();
        }


        return ergebnis;
    }

    public static String entschluesseln(byte[] wert)
    {
        Cipher cipher=null;
        String ergebnis=null;

        try
        {
            cipher = Cipher.getInstance("AES");
            SecretKey secretKey = new SecretKeySpec(new byte[]{3,7,4,5,3,7,4,5,3,7,4,5,3,7,4,5}, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            ergebnis=new String(cipher.doFinal(wert), Charset.forName("UTF-8"));
        }
        catch(Exception keineGefahrExcpetion)
        {
            //Die Gefahr ist gebannt, da die Verschlüsselungsart und der Schlüssel hartkodiert ist und somit nie falsch sein können
            keineGefahrExcpetion.printStackTrace();
        }

        return ergebnis;
    }
}
