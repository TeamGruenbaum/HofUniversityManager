package Model.NutzerdatenModel;



import java.io.Serializable;



public class Note implements Serializable
{
    private String herkunft;
    private String note;
    private String bemerkung;
    private String fach;



    public Note(String herkunft, String note, String bemerkung, String fach)
    {
        this.herkunft=herkunft;
        this.note=note;
        this.bemerkung=bemerkung;
        this.fach=fach;
    }


    public String getHerkunft()
    {
        return herkunft;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String neuerWert)
    {
        this.note=neuerWert;
    }

    public String getBemerkung()
    {
        return bemerkung;
    }

    public String getFach()
    {
        return fach;
    }
}
