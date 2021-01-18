package Model;



import java.io.Serializable;



public class Uhrzeit implements Serializable, Comparable<Uhrzeit>
{
	private final int stunde;
	private final int minute;



	public Uhrzeit(int stunde, int minute)
	{
		this.stunde=stunde;
		this.minute=minute;
	}


	public int getStunde()
	{
		return stunde;
	}

	public int getMinute()
	{
		return minute;
	}

	@Override public int compareTo(Uhrzeit comparingTime)
	{
		return (((stunde-comparingTime.stunde)==0)?(minute-comparingTime.minute):(stunde-comparingTime.stunde));
	}

	@Override public String toString()
	{
		return (stunde<10?"0"+stunde:String.valueOf(stunde))+":"+(minute<10?"0"+minute:String.valueOf(minute));
	}
}
