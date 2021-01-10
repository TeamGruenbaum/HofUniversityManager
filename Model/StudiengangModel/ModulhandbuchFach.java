package Model.StudiengangModel;



import java.io.Serializable;



public class ModulhandbuchFach implements Serializable
{
    private String name;
    private String dozent;
    private String zweitpruefer;
    private String studienjahr;
    private String art;
    private String ECTS;
    private String semesterwochenstunden;
    private String praesenzzeit;
    private String pruefungsvorbereitung;
    private String sprache;
    private String lehrinhalte;
    private String lernziel;
    private String voraussetzung;
    private String literaturliste;
    private String pruefungsdurchfuehrung;
    private String hilfsmittel;
    private String medienformen;
    private String haeufigkeit;



    public ModulhandbuchFach(String name, String dozent, String zweitpruefer, String studienjahr, String art, String ECTS, String semesterwochenstunden, String praesenzzeit, String pruefungsvorbereitung, String sprache, String lehrinhalte, String lernziel, String voraussetzung, String literaturliste, String pruefungsdurchfuehrung, String hilfsmittel, String medienformen, String haeufigkeit) {
        this.name=name;
        this.dozent=dozent;
        this.zweitpruefer=zweitpruefer;
        this.studienjahr=studienjahr;
        this.art=art;
        this.ECTS=ECTS;
        this.semesterwochenstunden=semesterwochenstunden;
        this.praesenzzeit=praesenzzeit;
        this.pruefungsvorbereitung=pruefungsvorbereitung;
        this.sprache=sprache;
        this.lehrinhalte=lehrinhalte;
        this.lernziel=lernziel;
        this.voraussetzung=voraussetzung;
        this.literaturliste=literaturliste;
        this.pruefungsdurchfuehrung=pruefungsdurchfuehrung;
        this.hilfsmittel=hilfsmittel;
        this.medienformen=medienformen;
        this.haeufigkeit=haeufigkeit;
    }


    public String getName()
    {
        return name;
    }

    public String getDozent()
    {
        return dozent;
    }

    public String getZweitpruefer()
    {
        return zweitpruefer;
    }

    public String getStudienjahr()
    {
        return studienjahr;
    }

    public String getArt()
    {
        return art;
    }

    public String getECTS()
    {
        return ECTS;
    }

    public String getSemesterwochenstunden()
    {
        return semesterwochenstunden;
    }

    public String getPraesenzzeit()
    {
        return praesenzzeit;
    }

    public String getPruefungsvorbereitung()
    {
        return pruefungsvorbereitung;
    }

    public String getSprache()
    {
        return sprache;
    }

    public String getLehrinhalte()
    {
        return lehrinhalte;
    }

    public String getLernziel()
    {
        return lernziel;
    }

    public String getVoraussetzung()
    {
        return voraussetzung;
    }

    public String getLiteraturliste()
    {
        return literaturliste;
    }

    public String getPruefungsdurchfuehrung()
    {
        return pruefungsdurchfuehrung;
    }

    public String getHilfsmittel()
    {
        return hilfsmittel;
    }

    public String getMedienformen()
    {
        return medienformen;
    }

    public String getHaeufigkeit()
    {
        return haeufigkeit;
    }
}
