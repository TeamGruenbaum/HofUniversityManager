package Model.StudiengangModel;

import java.util.ArrayList;

public final class Modulhandbuch {
    private ArrayList<ModulhandbuchFach> faecher;

    public Modulhandbuch(){}

    public ModulhandbuchFach getFach (int index){
        return faecher.get(index);
    }
}
