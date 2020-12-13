package Model.TreffpunktModel;

public abstract class Treffpunkt
{
    private String name;
    private String ort;
    private boolean wetterunabhaengig;
    private String information;

    protected Treffpunkt(String name, String ort, boolean wetterunabhaengig, String information) {
        this.name = name;
        this.ort = ort;
        this.wetterunabhaengig = wetterunabhaengig;
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public String getOrt() {
        return ort;
    }

    public boolean getWetterunabhaengig() {
        return wetterunabhaengig;
    }

    public String getInformation() {
        return information;
    }
}
