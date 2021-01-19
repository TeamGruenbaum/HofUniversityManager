package Model.OberflaechenModel;



import static Model.Anwendung.EINSTELLUNGEN;

import Model.Anwendung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public final class Menue implements Serializable
{
	private static ArrayList<MenuepunktInformation> menuepunktInformationen=new ArrayList<MenuepunktInformation>(List.of(new MenuepunktInformation(Anwendung.STUNDENPLAN, "stundenplan-icon.png", "StundenplanView.fxml"), new MenuepunktInformation(Anwendung.MENSAPLAN, "mensaplan-icon.png", "MensaplanView.fxml"), new MenuepunktInformation(Anwendung.MODULHANDBUCH, "modulhandbuch-icon.png", "ModulhandbuchView.fxml"), new MenuepunktInformation(Anwendung.MOODLE, "moodle-icon.png", "QuicklinksView.fxml"), new MenuepunktInformation(Anwendung.PANOPTO, "panopto-icon.png", "Platzhalter.fxml"), new MenuepunktInformation(Anwendung.NEXTCLOUD, "nextcloud-icon.png", "Platzhalter.fxml"), new MenuepunktInformation(Anwendung.CAMPUSSPORT, "campussport-icon.png", "QuicklinksView.fxml"), new MenuepunktInformation(Anwendung.TREFFPUNKTE, "treffpunkt-icon.png", "TreffpunktView.fxml"), new MenuepunktInformation(Anwendung.BAYERNFAHRPLAN, "bayernfahrplan-icon.png", "QuicklinksView.fxml"), new MenuepunktInformation(Anwendung.PRIMUSS, "primuss-icon.png", "QuicklinksView.fxml"), new MenuepunktInformation(EINSTELLUNGEN, "einstellungen-icon.png", "EinstellungenView.fxml")));

	private Menue()
	{
	}



	public static ArrayList<MenuepunktInformation> getMenuepunktInformationen()
	{
		return menuepunktInformationen;
	}
}
