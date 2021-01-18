package Model.TreffpunktModel;



import java.io.Serializable;



public class Freizeitaktivitaet extends Treffpunkt implements Serializable
{
	private final String ambiente;



	public Freizeitaktivitaet(String name, String ort, boolean wetterunabhaengig, String information, String ambiente)
	{
		super(name, ort, wetterunabhaengig, information);
		this.ambiente=ambiente;
	}


	public String getAmbiente()
	{
		return ambiente;
	}
}
