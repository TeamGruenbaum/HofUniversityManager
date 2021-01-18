package Model.MensaplanModel;



import java.io.Serializable;
import java.util.ArrayList;



public class Mensaplan implements Serializable
{
	private final ArrayList<Tagesplan> tagesplaene;



	public Mensaplan(ArrayList<Tagesplan> tagesplaene)
	{
		this.tagesplaene=tagesplaene;
	}


	public ArrayList<Tagesplan> getTagesplaene()
	{
		return tagesplaene;
	}
}
