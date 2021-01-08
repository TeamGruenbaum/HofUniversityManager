package Controller.InformationsVermittlung.Hilfsklassen;



import org.jsoup.nodes.Document;



public class NameKuerzelDocumentTripel
{
    private String studiengangName;
    private String studiengangKuerzel;
    private Document stundenplanDokument;



    public NameKuerzelDocumentTripel(String studiengangName, String studiengangKuerzel, Document stundenplanDokument)
    {
        this.studiengangName=studiengangName;
        this.studiengangKuerzel=studiengangKuerzel;
        this.stundenplanDokument=stundenplanDokument;
    }


    public String getStudiengangName()
    {
        return studiengangName;
    }

    public String getStudiengangKuerzel()
    {
        return studiengangKuerzel;
    }

    public Document getStundenplanDokument()
    {
        return stundenplanDokument;
    }
}
