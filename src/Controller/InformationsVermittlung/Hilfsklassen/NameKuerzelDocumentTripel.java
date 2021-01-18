package Controller.InformationsVermittlung.Hilfsklassen;



import org.jsoup.nodes.Document;



public class NameKuerzelDocumentTripel
{
	private final String studiengangName;
	private final String studiengangKuerzel;
	private final Document stundenplanDokument;



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
