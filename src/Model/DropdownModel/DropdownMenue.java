package Model.DropdownModel;



import java.io.Serializable;
import java.util.ArrayList;



public class DropdownMenue implements Serializable
{
	private ArrayList<Studiengang> studiengaenge;



	public DropdownMenue(ArrayList<Studiengang> studiengaenge)
	{
		this.studiengaenge=studiengaenge;
	}


	public ArrayList<Studiengang> getStudiengaenge()
	{
		return studiengaenge;
	}
}
