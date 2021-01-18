package Controller.InformationsVermittlung;



import java.net.URL;



public class Internetverbindungskontrolleur
{
	public static boolean isInternetVerbindungVorhanden(String url)
	{
		try
		{
			new URL(url).openConnection().connect();

			return true;
		}
		catch(Exception keineGefahrException)
		{
			//Es ist gewollt, dass manchmal hier reingesprungen wird, nämlich dann wenn keine Verbindung zum Ziel aufgebaut werden kann
			return false;
		}
	}
}
