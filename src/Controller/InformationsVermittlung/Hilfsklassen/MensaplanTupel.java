package Controller.InformationsVermittlung.Hilfsklassen;



public class MensaplanTupel
{
	private DatumDocumentPaar montag;
	private DatumDocumentPaar dienstag;
	private DatumDocumentPaar mittwoch;
	private DatumDocumentPaar donnerstag;
	private DatumDocumentPaar freitag;
	private DatumDocumentPaar samstag;

	public MensaplanTupel(DatumDocumentPaar montag, DatumDocumentPaar dienstag, DatumDocumentPaar mittwoch, DatumDocumentPaar donnerstag, DatumDocumentPaar freitag, DatumDocumentPaar samstag)
	{
		this.montag=montag;
		this.dienstag=dienstag;
		this.mittwoch=mittwoch;
		this.donnerstag=donnerstag;
		this.freitag=freitag;
		this.samstag=samstag;
	}

	public DatumDocumentPaar getMontag()
	{
		return montag;
	}

	public DatumDocumentPaar getDienstag()
	{
		return dienstag;
	}

	public DatumDocumentPaar getMittwoch()
	{
		return mittwoch;
	}

	public DatumDocumentPaar getDonnerstag()
	{
		return donnerstag;
	}

	public DatumDocumentPaar getFreitag()
	{
		return freitag;
	}

	public DatumDocumentPaar getSamstag()
	{
		return samstag;
	}
}
