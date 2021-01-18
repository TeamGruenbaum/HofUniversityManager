package Model.ModulhandbuchModel;



import java.io.Serializable;



public class ModulhandbuchFach implements Serializable
{
	private final String name;
	private final String dozent;
	private final String zweitpruefer;
	private final String studienjahr;
	private final String art;
	private final String ECTS;
	private final String semesterwochenstunden;
	private final String praesenzzeit;
	private final String pruefungsvorbereitung;
	private final String sprache;
	private final String lehrinhalte;
	private final String lernziel;
	private final String voraussetzung;
	private final String literaturliste;
	private final String pruefungsdurchfuehrung;
	private final String hilfsmittel;
	private final String medienformen;
	private final String haeufigkeit;



	public ModulhandbuchFach(String name, String dozent, String zweitpruefer, String studienjahr, String art, String ECTS, String semesterwochenstunden, String praesenzzeit, String pruefungsvorbereitung, String sprache, String lehrinhalte, String lernziel, String voraussetzung, String literaturliste, String pruefungsdurchfuehrung, String hilfsmittel, String medienformen, String haeufigkeit)
	{
		this.name=name;
		this.dozent=dozent;
		this.zweitpruefer=zweitpruefer;
		this.studienjahr=studienjahr;
		this.art=art;
		this.ECTS=ECTS;
		this.semesterwochenstunden=semesterwochenstunden;
		this.praesenzzeit=praesenzzeit;
		this.pruefungsvorbereitung=pruefungsvorbereitung;
		this.sprache=sprache;
		this.lehrinhalte=lehrinhalte;
		this.lernziel=lernziel;
		this.voraussetzung=voraussetzung;
		this.literaturliste=literaturliste;
		this.pruefungsdurchfuehrung=pruefungsdurchfuehrung;
		this.hilfsmittel=hilfsmittel;
		this.medienformen=medienformen;
		this.haeufigkeit=haeufigkeit;
	}


	public String getName()
	{
		return name;
	}

	public String getDozent()
	{
		return dozent;
	}

	public String getZweitpruefer()
	{
		return zweitpruefer;
	}

	public String getStudienjahr()
	{
		return studienjahr;
	}

	public String getArt()
	{
		return art;
	}

	public String getECTS()
	{
		return ECTS;
	}

	public String getSemesterwochenstunden()
	{
		return semesterwochenstunden;
	}

	public String getPraesenzzeit()
	{
		return praesenzzeit;
	}

	public String getPruefungsvorbereitung()
	{
		return pruefungsvorbereitung;
	}

	public String getSprache()
	{
		return sprache;
	}

	public String getLehrinhalte()
	{
		return lehrinhalte;
	}

	public String getLernziel()
	{
		return lernziel;
	}

	public String getVoraussetzung()
	{
		return voraussetzung;
	}

	public String getLiteraturliste()
	{
		return literaturliste;
	}

	public String getPruefungsdurchfuehrung()
	{
		return pruefungsdurchfuehrung;
	}

	public String getHilfsmittel()
	{
		return hilfsmittel;
	}

	public String getMedienformen()
	{
		return medienformen;
	}

	public String getHaeufigkeit()
	{
		return haeufigkeit;
	}
}
