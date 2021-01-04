package Model.NutzerdatenModel;

import java.io.Serializable;

public class Note implements Serializable
{
    private String art;
    private int note;
    private String bemerkung;
    private String fach;

    public Note(String art, int note, String bemerkung, String fach)
    {
        this.art=art;
        this.note=note;
        this.bemerkung=bemerkung;
        this.fach=fach;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getBemerkung()
    {

        return bemerkung;
    }

    public void setBemerkung(String bemerkung)
    {

        this.bemerkung=bemerkung;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }
}
