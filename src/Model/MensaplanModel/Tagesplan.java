package Model.MensaplanModel;



import Model.Tag;
import Model.Datum;


import java.io.Serializable;
import java.util.ArrayList;



public class Tagesplan implements Serializable
{
	private final ArrayList<Gericht> gerichte;
	private final Tag tagesplanTag;
	private final Datum tagesplanDatum;

	public Tagesplan(ArrayList<Gericht> gerichte, Tag tagesplanTag, Datum tagesplanDatum)
	{

		this.gerichte=gerichte;
		this.tagesplanTag=tagesplanTag;
		this.tagesplanDatum=tagesplanDatum;
	}

	public ArrayList<Gericht> getGerichte()
	{

		return gerichte;
	}

	public Tag getTagesplanTag()
	{

		return tagesplanTag;
	}

	public Datum getTagesplanDatum()
	{

		return tagesplanDatum;
	}
}