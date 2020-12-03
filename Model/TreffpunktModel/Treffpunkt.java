package Model.TreffpunktModel;

public abstract class Treffpunkt {
    public String treffpunktName;
    public String ort;
    public boolean wetterunabhaengig;
    public Oeffnungszeit[] oeffnungszeit;
    public String information;
    public int bewertung;

    public Treffpunkt(String treffpunktName, String ort, boolean wetterunabhaengig, Oeffnungszeit[] oeffnungszeit, String information, int bewertung) {
        this.treffpunktName = treffpunktName;
        this.ort = ort;
        this.wetterunabhaengig = wetterunabhaengig;
        this.oeffnungszeit = oeffnungszeit;
        this.information = information;
        this.bewertung = bewertung;
    }

    public String getTreffpunktName() {
        return treffpunktName;
    }

    public String getOrt() {
        return ort;
    }

    public boolean isWetterunabhaengig() {
        return wetterunabhaengig;
    }

    public Oeffnungszeit[] getOeffnungszeit() {
        return oeffnungszeit;
    }

    public String getInformation() {
        return information;
    }

    public int getBewertung() {
        return bewertung;
    }
}
