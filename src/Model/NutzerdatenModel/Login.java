package Model.NutzerdatenModel;



import Controller.Speicher.Schluesselmeister;

import java.io.Serializable;



public class Login implements Serializable
{
	private String benutzername;
	private byte[] passwort;



	public Login(String benutzername, byte[] passwort)
	{
		this.benutzername=benutzername;
		this.passwort=passwort;
	}


	public String getName()
	{
		return benutzername;
	}

	public void setName(String neuerWert)
	{
		benutzername=neuerWert;
	}

	public byte[] getPasswort()
	{
		return passwort;
	}

	public void setPasswort(byte[] neuerWert)
	{
		this.passwort=neuerWert;
	}
}
