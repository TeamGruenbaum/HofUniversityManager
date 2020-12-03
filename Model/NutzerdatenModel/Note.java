package Model.NutzerdatenModel;

public class Note
{
    private String art;
    private int note;

    public Note(String art, int note)
    {
        this.art=art;
        this.note=note;
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
}
