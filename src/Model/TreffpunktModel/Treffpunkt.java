package Model.TreffpunktModel;



import java.io.Serializable;



public abstract class Treffpunkt implements Serializable
{
	private final String name;
	private final String ort;
	private final boolean wetterunabhaengig;
	private final String information;



	protected Treffpunkt(String name, String ort, boolean wetterunabhaengig, String information)
	{
		this.name=name;
		this.ort=ort;
		this.wetterunabhaengig=wetterunabhaengig;
		this.information=information;
	}


	public String getName()
	{
		return name;
	}

	public String getOrt()
	{
		return ort;
	}

	public boolean getWetterunabhaengig()
	{
		return wetterunabhaengig;
	}

	public String getInformation()
	{
		return information;
	}
}
