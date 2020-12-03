package Model.TreffpunktModel;

public class Oeffnungszeit {
    private Tag tag;
    private Uhrzeit beginn;
    private Uhrzeit ende;

    public Oeffnungszeit(Tag tag, Uhrzeit beginn, Uhrzeit ende) {
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
