package Controller.InformationsVermittlung.Hilfsklassen;



public class MensaplanTupel
{
	private final DatumDocumentPaar montag;
	private final DatumDocumentPaar dienstag;
	private final DatumDocumentPaar mittwoch;
	private final DatumDocumentPaar donnerstag;
	private final DatumDocumentPaar freitag;
	private final DatumDocumentPaar samstag;

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
