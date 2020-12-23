package Controller.InformationsVermittlung;

import java.util.ArrayList;

public class DropdownMenueDokumente
{
    private ArrayList<KuerzelDokumentPaar> kuerzelDokumentePaare;

    DropdownMenueDokumente(ArrayList<KuerzelDokumentPaar> kuerzelDokumentPaare)
    {
        this.kuerzelDokumentePaare=kuerzelDokumentPaare;
    }

    public ArrayList<KuerzelDokumentPaar> getKuerzelDokumentePaare()
    {
        return kuerzelDokumentePaare;
    }
}
