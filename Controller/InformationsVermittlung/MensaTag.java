package Controller.InformationsVermittlung;

import Model.Datum;
import org.jsoup.nodes.Document;

public class MensaTag
{
    private Document tag;
    private Datum datum;

    public MensaTag(Document tag, Datum datum){
        this.tag = tag;
        this.datum = datum;
    }
    public Document getDokument() {
    return tag;
    }

    public Datum getDatum() {
        return datum;
    }
}
