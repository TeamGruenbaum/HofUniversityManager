package Model.DropdownModel;



import java.io.Serializable;



public class Studiensemester implements Serializable
{
	private String name;
	private String kuerzel;



	public Studiensemester(String name, String kuerzel)
	{
		this.name=name;
		this.kuerzel=kuerzel;
	}


	public String getName()
	{
		return name;
	}

	public String getKuerzel()
	{
		return kuerzel;
	}
}
