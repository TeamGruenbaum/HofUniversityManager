package Controller.InformationsVermittlung.Hilfsklassen;



import Model.Datum;
import org.jsoup.nodes.Document;



public class DatumDocumentPaar
{
	private Datum paarDatumDatum;
	private Document paarDokumentDocument;



	public DatumDocumentPaar(Datum paarDatumDatum, Document paarDokumentDocument)
	{
		this.paarDatumDatum=paarDatumDatum;
		this.paarDokumentDocument=paarDokumentDocument;
	}


	public Datum getPaarDatum()
	{
		return paarDatumDatum;
	}

	public Document getPaarDokument()
	{
		return paarDokumentDocument;
	}
}
