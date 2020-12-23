package Controller.InformationsVermittlung;

import org.jsoup.nodes.Document;

public class KuerzelDokumentPaar
{
    private String studiengangKuerzel;
    private Document stundenplanDokument;

    public KuerzelDokumentPaar(String studiengangKuerzel, Document stundenplanDokument)
    {
        this.studiengangKuerzel = studiengangKuerzel;
        this.stundenplanDokument = stundenplanDokument;
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
