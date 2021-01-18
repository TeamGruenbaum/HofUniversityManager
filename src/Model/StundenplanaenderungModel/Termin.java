package Model.StundenplanaenderungModel;



import Model.Datum;
import Model.Uhrzeit;



public class Termin
{
	private final Datum ausfalldatumDatum;
	private final Uhrzeit ausfalluhrzeitUhrzeit;
	private final String raum;



	public Termin(Datum ausfalldatumDatum, Uhrzeit ausfalluhrzeitUhrzeit, String raum)
	{
		this.ausfalldatumDatum=ausfalldatumDatum;
		this.ausfalluhrzeitUhrzeit=ausfalluhrzeitUhrzeit;
		this.raum=raum;
	}


	public Datum getAusfalldatum()
	{
		return ausfalldatumDatum;
	}
}
