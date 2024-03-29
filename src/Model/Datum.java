package Model;



import java.io.Serializable;



public class Datum implements Serializable
{
	private int tag;
	private int monat;
	private int jahr;



	public Datum(int tag, int monat, int jahr)
	{
		this.tag=tag;
		this.monat=monat;
		this.jahr=jahr;
	}


	public int getTag()
	{
		return tag;
	}

	public void setTag(int neuerWert)
	{
		this.tag=neuerWert;
	}

	public int getMonat()
	{
		return monat;
	}

	public int getJahr()
	{
		return jahr;
	}

	@Override public String toString()
	{
		return (tag<10?"0"+tag:String.valueOf(tag))+"."+(monat<10?"0"+monat:String.valueOf(monat))+"."+(jahr<10?"0"+jahr:String.valueOf(jahr));
	}
}
