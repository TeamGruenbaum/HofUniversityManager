package Model.StundenplanaenderungModel;



public class Stundenplanaenderung
{
	private String veranstaltung;
	private String dozent;
	private Termin entfallenerTermin;
	private Termin ersatztermin;



	public Stundenplanaenderung(String veranstaltung, String dozent, Termin entfallenerTermin, Termin ersatztermin)
	{
		this.veranstaltung=veranstaltung;
		this.dozent=dozent;
		this.entfallenerTermin=entfallenerTermin;
		this.ersatztermin=ersatztermin;
	}


	public String getVeranstaltung()
	{
		return veranstaltung;
	}

	public String getDozent()
	{
		return dozent;
	}

	public Termin getEntfallenerTermin()
	{
		return entfallenerTermin;
	}

	public Termin getErsatztermin()
	{
		return ersatztermin;
	}
}
