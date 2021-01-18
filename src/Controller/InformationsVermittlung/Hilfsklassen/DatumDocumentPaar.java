package Controller.InformationsVermittlung.Hilfsklassen;



import Model.Datum;
import org.jsoup.nodes.Document;



public class DatumDocumentPaar
{
	private final Datum datum;
	private final Document tag;



	public DatumDocumentPaar(Datum datum, Document tag)
	{
		this.datum=datum;
		this.tag=tag;
	}


	public Datum getDatum()
	{
		return datum;
	}

	public Document getDokument()
	{
		return tag;
	}
}
