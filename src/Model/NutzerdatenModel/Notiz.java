package Model.NutzerdatenModel;



import java.io.Serializable;



public class Notiz implements Serializable
{
	private String titel;
	private String inhalt;
	private String fach;



	public Notiz(String titel, String inhalt, String fach)
	{
		this.titel=titel;
		this.inhalt=inhalt;
		this.fach=fach;
	}


	public String getTitel()
	{
		return titel;
	}

	public String getInhalt()
	{
		return inhalt;
	}

	public void setInhalt(String neuerWert)
	{
		this.inhalt=neuerWert;
	}

	public String getFach()
	{
		return fach;
	}
}
