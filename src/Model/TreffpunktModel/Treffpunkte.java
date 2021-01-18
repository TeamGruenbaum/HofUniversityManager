package Model.TreffpunktModel;



import java.io.Serializable;
import java.util.ArrayList;



public final class Treffpunkte implements Serializable
{
	private final ArrayList<Treffpunkt> treffpunkte;



	public Treffpunkte(ArrayList<Treffpunkt> treffpunkte)
	{
		this.treffpunkte=treffpunkte;
	}


	public ArrayList<Treffpunkt> getTreffpunkte()
	{
		return treffpunkte;
	}
}
