package Controller.InformationsVermittlung;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;

public class StudiengangDokumente
{
    ArrayList<Document> faecherDokumente;
    // int i = 0;

    public StudiengangDokumente(ArrayList<Document> faecherDokumente) throws IOException
    {
        this.faecherDokumente = faecherDokumente;
    }

    public ArrayList<Document> getFaecherDokumente()
    {
        return faecherDokumente;
    }

   // Falls man die Fächer nach der Reihe aus der ArrayList bekommen möchte
    /* public Document getFach()
    {
        if(i<= faecherDokumente.length)
        {
        return faecherDokumente.get(i++);
        }
        else
        {
        return null;
        }
    }

    */
}

