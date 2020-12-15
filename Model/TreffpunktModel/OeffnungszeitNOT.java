package Model.TreffpunktModel;
import Model.Tag;
import Model.Uhrzeit;

public class OeffnungszeitNOT
{
    private Tag tag;
    private Uhrzeit beginn;
    private Uhrzeit ende;

    public OeffnungszeitNOT(Tag tag, Uhrzeit beginn, Uhrzeit ende)
    {
        this.tag = tag;
        this.beginn = beginn;
        this.ende = ende;
    }

    public Tag getTag() {
        return tag;
    }

    public Uhrzeit getBeginn() {
        return beginn;
    }

    public Uhrzeit getEnde() {
        return ende;
    }
}