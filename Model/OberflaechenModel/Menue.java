package Model.OberflaechenModel;

import Model.NutzerdatenModel.Anwendung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static Model.NutzerdatenModel.Anwendung.EINSTELLUNGEN;

public final class Menue implements Serializable
{
    private static ArrayList<MenuepunktInformation> menuepunkte=new ArrayList<MenuepunktInformation>(
            List.of(
                new MenuepunktInformation(Anwendung.STUNDENPLAN,"platzhalter-icon.png", "Platzhalter.fxml"),
                new MenuepunktInformation(Anwendung.MENSAPLAN,"mensaplan-icon.png", "MensaPlanView.fxml"),
                new MenuepunktInformation(Anwendung.STUDIENGANG,"platzhalter-icon.png", "Platzhalter.fxml"),
                new MenuepunktInformation(Anwendung.MOODLE,"moodle-icon.png", "QuicklinksView.fxml"),
                new MenuepunktInformation(Anwendung.PANOPTO,"panopto-icon.png", "Platzhalter.fxml"),
                new MenuepunktInformation(Anwendung.NEXTCLOUD,"nextcloud-icon.png", "Platzhalter.fxml"),
                new MenuepunktInformation(Anwendung.CAMPUSSPORT,"campussport-icon.png", "QuicklinksView.fxml"),
                new MenuepunktInformation(Anwendung.TREFFPUNKTE,"treffpunkt-icon.png", "TreffpunktView.fxml"),
                new MenuepunktInformation(Anwendung.BAYERNFAHRPLAN,"bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new MenuepunktInformation(Anwendung.PRIMUSS,"primuss-icon.png", "QuicklinksView.fxml"),
                new MenuepunktInformation(EINSTELLUNGEN,"einstellungen-icon.png", "EinstellungenView.fxml")
            )
    );

    private Menue(){}

    public static ArrayList<MenuepunktInformation> getMenuepunkte()
    {
        return menuepunkte;
    }
}
