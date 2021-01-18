package Controller.Speicher;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;



public class Schluesselmeister
{
	private static SecretKey schluesselSecretKey=new SecretKeySpec(new byte[]{3, 7, 4, 5, 3, 7, 4, 5, 3, 7, 4, 5, 3, 7, 4, 5}, "AES");



	public static byte[] verschluesseln(String wert)
	{
		Cipher cipher=null;
		byte[] verschluesseltesErgebnis=null;

		try
		{
			cipher=Cipher.getInstance("AES");


			cipher.init(Cipher.ENCRYPT_MODE, schluesselSecretKey);

			verschluesseltesErgebnis=cipher.doFinal(wert.getBytes(StandardCharsets.UTF_8));
		}
		catch(Exception keineGefahrException)
		{
			//Die Gefahr ist gebannt, da die Verschlüsselungsart und der Schlüssel hartkodiert ist und somit nie falsch sein können
			keineGefahrException.printStackTrace();
		}

		return verschluesseltesErgebnis;
	}

	public static String entschluesseln(byte[] wert)
	{
		Cipher verschluesslerCipher=null;
		String entschluesseltesErgebnis=null;

		try
		{
			verschluesslerCipher=Cipher.getInstance("AES");

			verschluesslerCipher.init(Cipher.DECRYPT_MODE, schluesselSecretKey);

			entschluesseltesErgebnis=new String(verschluesslerCipher.doFinal(wert), StandardCharsets.UTF_8);
		}
		catch(Exception keineGefahrExcpetion)
		{
			//Die Gefahr ist gebannt, da die Verschlüsselungsart und der Schlüssel hartkodiert ist und somit nie falsch sein können
			keineGefahrExcpetion.printStackTrace();
		}

		return entschluesseltesErgebnis;
	}
}
