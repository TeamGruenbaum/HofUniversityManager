package Controller.InformationsVermittlung.Hilfsklassen;



public class MensaplanTupel
{
	private DatumDocumentPaar montagDatumDocumentPaar;
	private DatumDocumentPaar dienstagDatumDocumentPaar;
	private DatumDocumentPaar mittwochDatumDocumentPaar;
	private DatumDocumentPaar donnerstagDatumDocumentPaar;
	private DatumDocumentPaar freitagDatumDocumentPaar;
	private DatumDocumentPaar samstagDatumDocumentPaar;

	public MensaplanTupel(DatumDocumentPaar montagDatumDocumentPaar, DatumDocumentPaar dienstagDatumDocumentPaar, DatumDocumentPaar mittwochDatumDocumentPaar, DatumDocumentPaar donnerstagDatumDocumentPaar, DatumDocumentPaar freitagDatumDocumentPaar, DatumDocumentPaar samstagDatumDocumentPaar)
	{
		this.montagDatumDocumentPaar=montagDatumDocumentPaar;
		this.dienstagDatumDocumentPaar=dienstagDatumDocumentPaar;
		this.mittwochDatumDocumentPaar=mittwochDatumDocumentPaar;
		this.donnerstagDatumDocumentPaar=donnerstagDatumDocumentPaar;
		this.freitagDatumDocumentPaar=freitagDatumDocumentPaar;
		this.samstagDatumDocumentPaar=samstagDatumDocumentPaar;
	}

	public DatumDocumentPaar getMontag()
	{
		return montagDatumDocumentPaar;
	}

	public DatumDocumentPaar getDienstag()
	{
		return dienstagDatumDocumentPaar;
	}

	public DatumDocumentPaar getMittwoch()
	{
		return mittwochDatumDocumentPaar;
	}

	public DatumDocumentPaar getDonnerstag()
	{
		return donnerstagDatumDocumentPaar;
	}

	public DatumDocumentPaar getFreitag()
	{
		return freitagDatumDocumentPaar;
	}

	public DatumDocumentPaar getSamstag()
	{
		return samstagDatumDocumentPaar;
	}
}
