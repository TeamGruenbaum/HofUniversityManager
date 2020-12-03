package Model.TreffpunktModel;

public class Restaurant {
    public String art;
    public String nationalitaet;
    public boolean lieferdienst;

    public Restaurant(String art, String nationalitaet, boolean lieferdienst) {
        this.art = art;
        this.nationalitaet = nationalitaet;
        this.lieferdienst = lieferdienst;
    }

    public String getArt() {
        return art;
    }

    public String getNationalitaet() {
        return nationalitaet;
    }

    public boolean isLieferdienst() {
        return lieferdienst;
    }
}
