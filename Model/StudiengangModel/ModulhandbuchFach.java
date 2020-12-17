package Model.StudiengangModel;

import java.io.Serializable;

public class ModulhandbuchFach implements Serializable
{

    private String fachName;
    private String fachDozent;
    private String fachZweitPruefer;
    private int fachStudienjahr;
    private String fachArt;
    private int fachECTS;
    private int fachSWS;
    private int fachPraesenzZeit;
    private int fachPruefungsVorbereitung;
    private String fachSprache;
    private String fachLehrinhalte;
    private String fachLernziel;
    private String fachVoraussetzung;
    private String fachLiteraturliste;
    private String fachPruefungsdurchfuehrung;
    private String fachHilfsmittel;
    private String fachMedienformen;
    private String fachHaeufigkeit;

    public ModulhandbuchFach(String fachName, String fachDozent, String fachZweitPruefer, int fachStudienjahr, String fachArt, int fachECTS, int fachSWS, int fachPraesenzZeit, int fachPruefungsVorbereitung, String fachSprache, String fachLehrinhalte, String fachLernziel, String fachVoraussetzung, String fachLiteraturliste, String fachPruefungsdurchfuehrung, String fachHilfsmittel, String fachMedienformen, String fachHaeufigkeit) {
        this.fachName = fachName;
        this.fachDozent = fachDozent;
        this.fachZweitPruefer = fachZweitPruefer;
        this.fachStudienjahr = fachStudienjahr;
        this.fachArt = fachArt;
        this.fachECTS = fachECTS;
        this.fachSWS = fachSWS;
        this.fachPraesenzZeit = fachPraesenzZeit;
        this.fachPruefungsVorbereitung = fachPruefungsVorbereitung;
        this.fachSprache = fachSprache;
        this.fachLehrinhalte = fachLehrinhalte;
        this.fachLernziel = fachLernziel;
        this.fachVoraussetzung = fachVoraussetzung;
        this.fachLiteraturliste = fachLiteraturliste;
        this.fachPruefungsdurchfuehrung = fachPruefungsdurchfuehrung;
        this.fachHilfsmittel = fachHilfsmittel;
        this.fachMedienformen = fachMedienformen;
        this.fachHaeufigkeit = fachHaeufigkeit;
    }

    public String getFachName() {
        return fachName;
    }

    public String getFachDozent() {
        return fachDozent;
    }

    public String getFachZweitPruefer() {
        return fachZweitPruefer;
    }

    public int getFachStudienjahr() {
        return fachStudienjahr;
    }

    public String getFachArt() {
        return fachArt;
    }

    public int getFachECTS() {
        return fachECTS;
    }

    public int getFachSWS() {
        return fachSWS;
    }

    public int getFachPraesenzZeit() {
        return fachPraesenzZeit;
    }

    public int getFachPruefungsVorbereitung() {
        return fachPruefungsVorbereitung;
    }

    public String getFachSprache() {
        return fachSprache;
    }

    public String getFachLehrinhalte() {
        return fachLehrinhalte;
    }

    public String getFachLernziel() {
        return fachLernziel;
    }

    public String getFachVoraussetzung() {
        return fachVoraussetzung;
    }

    public String getFachLiteraturliste() {
        return fachLiteraturliste;
    }

    public String getFachPruefungsdurchfuehrung() {
        return fachPruefungsdurchfuehrung;
    }

    public String getFachHilfsmittel() {
        return fachHilfsmittel;
    }

    public String getFachMedienformen() {
        return fachMedienformen;
    }

    public String getFachHaeufigkeit() {
        return fachHaeufigkeit;
    }
}
