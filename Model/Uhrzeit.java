package Model;

import java.io.Serializable;

public class Uhrzeit implements Serializable, Comparable<Uhrzeit>
{
    private int stunde;
    private int minute;
    private int sekunde;

    public Uhrzeit(int stunde, int minute, int sekunde)
    {
        this.stunde=stunde;
        this.minute=minute;
        this.sekunde=sekunde;
    }

    public int getStunde() {
        return stunde;
    }

    public void setStunde(int stunde) {
        this.stunde = stunde;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSekunde() {
        return sekunde;
    }

    public void setSekunde(int sekunde) {
        this.sekunde = sekunde;
    }

    @Override
    public int compareTo(Uhrzeit comparingTime)
    {
        if ((stunde - comparingTime.stunde) == 0) {
            return (minute - comparingTime.minute);
        }
        return (stunde - comparingTime.stunde);
    }
}
