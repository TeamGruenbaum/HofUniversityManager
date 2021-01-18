package Model.OberflaechenModel;



import Model.NutzerdatenModel.Anwendung;

import java.io.Serializable;



public class MenuepunktInformation implements Serializable
{
	private final Anwendung zielanwendungAnwendung;
	private final String iconDateiname;
	private final String fxmlDateiname;



	public MenuepunktInformation(Anwendung zielanwendungAnwendung, String iconDateiname, String fxmlDateiname)
	{
		this.zielanwendungAnwendung=zielanwendungAnwendung;
		this.iconDateiname=iconDateiname;
		this.fxmlDateiname=fxmlDateiname;
	}


	public Anwendung getZielanwendung()
	{
		return zielanwendungAnwendung;
	}

	public String getIconDateiname()
	{
		return iconDateiname;
	}

	public String getFxmlDateiname()
	{
		return fxmlDateiname;
	}
}
