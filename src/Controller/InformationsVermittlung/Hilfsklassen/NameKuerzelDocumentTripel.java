package Controller.InformationsVermittlung.Hilfsklassen;



import org.jsoup.nodes.Document;



public class NameKuerzelDocumentTripel
{
	private String studiengangName;
	private String studiengangKuerzel;
	private Document stundenplanDokumentDocument;



	public NameKuerzelDocumentTripel(String studiengangName, String studiengangKuerzel, Document stundenplanDokument)
	{
		this.studiengangName=studiengangName;
		this.studiengangKuerzel=studiengangKuerzel;
		this.stundenplanDokumentDocument=stundenplanDokument;
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
		return stundenplanDokumentDocument;
	}
}
