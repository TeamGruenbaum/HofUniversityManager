package Model.NutzerdatenModel;



import Model.Datum;
import Model.Tag;
import Model.Uhrzeit;

import java.io.Serializable;



public class Doppelstunde implements Serializable
{
	//nullable
	private Tag doppelstundentagTag;
	//nullable
	private Datum doppelstundendatumDatum;
	//nullable
	private final Uhrzeit beginnUhrzeit;
	//nullable
	private final Uhrzeit endeUhrzeit;
	//nullable
	private final String raum;
	private String name;
	private String dozent;



	public Doppelstunde(Tag doppelstundentagTag, Datum doppelstundendatumDatum, Uhrzeit beginnUhrzeit, Uhrzeit endeUhrzeit, String raum, String name, String dozent)
	{
		this.doppelstundentagTag=doppelstundentagTag;
		this.doppelstundendatumDatum=doppelstundendatumDatum;
		this.beginnUhrzeit=beginnUhrzeit;
		this.endeUhrzeit=endeUhrzeit;
		this.raum=raum;
		this.name=name;
		this.dozent=dozent;
	}


	public Tag getDoppelstundentag()
	{
		return doppelstundentagTag;
	}

	public void setDoppelstundentag(Tag neuerWert)
	{
		this.doppelstundentagTag=neuerWert;
	}

	public Datum getDoppelstundendatum()
	{
		return doppelstundendatumDatum;
	}

	public void setDoppelstundendatum(Datum neuerWert)
	{
		this.doppelstundendatumDatum=neuerWert;
	}

	public Uhrzeit getBeginnUhrzeit()
	{
		return beginnUhrzeit;
	}

	public Uhrzeit getEndeUhrzeit()
	{
		return endeUhrzeit;
	}

	public String getRaum()
	{
		return raum;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String neuerWert)
	{
		this.name=neuerWert;
	}

	public String getDozent()
	{
		return dozent;
	}

	public void setDozent(String neuerWert)
	{
		this.dozent=neuerWert;
	}
}
