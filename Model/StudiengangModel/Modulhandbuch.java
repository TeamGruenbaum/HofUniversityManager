package Model.StudiengangModel;

import java.util.ArrayList;

public final class Modulhandbuch {
    private static ArrayList<ModulhandbuchFach> faecher;

    private Modulhandbuch() {

    }

    public static ModulhandbuchFach getFach (int index){
        return faecher.get(index);
    }
}
