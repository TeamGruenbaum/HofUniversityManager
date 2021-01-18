package Controller.Sonstiges;

public class TextHelfer
{
	public static String grossschreiben(String wort)
	{
		return wort.substring(0, 1).toUpperCase()+wort.substring(1).toLowerCase();
	}
}
