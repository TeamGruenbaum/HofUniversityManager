package Model.TreffpunktModel;

public class Freizeitaktivitaet extends Treffpunkt
{
    private String ambiente;

    public Freizeitaktivitaet(String name, String ort, boolean wetterunabhaengig, String information,String ambiente)
    {
        super(name, ort, wetterunabhaengig, information);
        this.ambiente = ambiente;
    }

    public String getAmbiente() {
        return ambiente;
    }
}
