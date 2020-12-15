package Controller.InformationsVermittlung;

import Model.Datum;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import Model.Tag;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;


public class Parser
{
    //Studiengang parsen
    public static StudiengangInformationen studiengangParsen(StudiengangDokumente studiengangDokumente)
    {
        Document studiengangDokument=studiengangDokumente.getStudiengangDokument();

        //Studiengangleiter parsen
        String studiengangLeiter="";
        for(int i=0; i<(studiengangDokument.select("div.pers.bg")).size(); i++)
        {
            for(int j=0; j<(studiengangDokument.select("div.pers.bg").get(i).select("p")).size();j++)
            {
                if (studiengangDokument.select("div.pers.bg").get(i).select("p").get(j).text().contains("Studiengangleit"))
                {
                    studiengangLeiter=studiengangDokument.select("div.pers.bg").get(i).select("h4").text();
                    break;
                }
            }
        }
        //Ausnahmebehandlung für Studiengänge mit unklarer Angabe des Studiengangleiters
        if (studiengangLeiter.equals(""))
        {
            switch(studiengangDokument.getElementsByTag("h1").html())
            {
                case "Verwaltungsinformatik (Dipl.)":studiengangLeiter="Prof. Dr. Thomas Schaller"; break;
                case "Master Informatik (M.Sc.)":studiengangLeiter="Prof. Dr. René Peinl"; break;
                case "Berufsbegleitender Bachelor Digitale Verwaltung (B.A.)":studiengangLeiter="Prof. Dr. Thomas Meuche"; break;
                case "Sustainable Textiles (M.Eng.)":studiengangLeiter="Prof. Dr. Michael Rauch"; break;
                case "Vollzeitmaster Software Engineering for Industrial Applications (M.Eng.)":studiengangLeiter="Prof. Dr. Jürgen Heym"; break;
                default:studiengangLeiter="Prof. Dr. Friedwart Lender"; break;
            }
        }

        //SPO-Link parsen
        //Bisher wird hier nur der Link zur allgemeinen Seite aller SPOs geliefert
        String spoDataURL="https://www.hof-university.de"+studiengangDokument.select("a[href*=spo-studien-und-pruefungsordnungen]").attr("href");

        ArrayList<Document> faecherDokumente=studiengangDokumente.getFaecherDokumente();
        //Modulhandbuch parsen
        ArrayList<ModulhandbuchFach> faecher=new ArrayList<>();
        for(int i=0; i<faecherDokumente.size(); i++)
        {
            faecher.add(_getModulhandbuchFach(faecherDokumente.get(i)));
        }

        StudiengangInformationen studiengangInformationen=new StudiengangInformationen(studiengangLeiter, "Bachelor/Master", spoDataURL, faecher);
        return studiengangInformationen;
    }

    public static Treffpunkte treffpunkteParsen(JSONObject jsonObject)
    {
        ArrayList<Treffpunkt> treffpunkte=new ArrayList<Treffpunkt>();

        JSONArray restaurants=jsonObject.getJSONArray("restaurants");
        for(int i=0; i<restaurants.length(); i++)
        {
            JSONObject aktuellesJsonObject=restaurants.getJSONObject(i);

            treffpunkte.add(
                    new Restaurant
                            (
                                    aktuellesJsonObject.getString("name"),
                                    aktuellesJsonObject.getString("ort"),
                                    aktuellesJsonObject.getBoolean("wetterunabhaengig"),
                                    aktuellesJsonObject.getString("information"),
                                    aktuellesJsonObject.getString("art"),
                                    aktuellesJsonObject.getString("nationalitaet"),
                                    aktuellesJsonObject.getBoolean("lieferdienst"))
            );
        }

        JSONArray freizeitaktivitaeten=jsonObject.getJSONArray("freizeitaktivitaeten");
        for(int i=0; i<freizeitaktivitaeten.length(); i++)
        {
            JSONObject aktuellesJsonObject=freizeitaktivitaeten.getJSONObject(i);

            treffpunkte.add(
                    new Freizeitaktivitaet
                            (
                                    aktuellesJsonObject.getString("name"),
                                    aktuellesJsonObject.getString("ort"),
                                    aktuellesJsonObject.getBoolean("wetterunabhaengig"),
                                    aktuellesJsonObject.getString("information"),
                                    aktuellesJsonObject.getString("ambiente")
                            )
            );
        }

        return new Treffpunkte(treffpunkte);
    }

    //Mensaplan parsen
    public static Mensaplan mensaplanParsen(MensaplanDokumente mensaplanDokumente)
    {
        Tagesplan montagsplan=_getTagesplan(mensaplanDokumente.getMontag().getDokument(), Tag.MONTAG, mensaplanDokumente.getMontag().getDatum());
        Tagesplan dienstagsplan=_getTagesplan(mensaplanDokumente.getDienstag().getDokument(), Tag.DIENSTAG, mensaplanDokumente.getDienstag().getDatum());
        Tagesplan mittwochsplan=_getTagesplan(mensaplanDokumente.getMittwoch().getDokument(), Tag.MITTWOCH, mensaplanDokumente.getMittwoch().getDatum());
        Tagesplan donnerstagsplan=_getTagesplan(mensaplanDokumente.getDonnerstag().getDokument(), Tag.DONNERSTAG, mensaplanDokumente.getDonnerstag().getDatum());
        Tagesplan freitagsplan=_getTagesplan(mensaplanDokumente.getFreitag().getDokument(), Tag.FREITAG, mensaplanDokumente.getFreitag().getDatum());
        Tagesplan samstagsplan=_getTagesplan(mensaplanDokumente.getSamstag().getDokument(), Tag.SAMSTAG, mensaplanDokumente.getSamstag().getDatum());

        ArrayList<Tagesplan> wochenplan=new ArrayList<Tagesplan>();
        wochenplan.addAll(Arrays.asList(montagsplan, dienstagsplan, mittwochsplan, donnerstagsplan, freitagsplan, samstagsplan));
        return new Mensaplan(wochenplan);
    }

    private static ModulhandbuchFach _getModulhandbuchFach(Document dokument)
    {
        Element tabelle=dokument.getElementsByTag("tbody").first();

        return new ModulhandbuchFach(
                dokument.select("h2:not([class])").text(),
                tabelle.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text(),
                Integer.parseInt(tabelle.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text()),
                tabelle.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text(),
                Integer.parseInt(tabelle.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text()),
                Integer.parseInt(tabelle.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text()),
                Integer.parseInt(tabelle.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text()),
                Integer.parseInt(tabelle.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text()),
                tabelle.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(11).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(12).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(13).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(14).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(15).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(16).getElementsByTag("td").get(1).text()
        );
    }

    private static Tagesplan _getTagesplan(Document dokument, Tag tag, Datum datum)
    {
        ArrayList<Gericht> gerichte=new ArrayList<Gericht>();

        //Hauptgerichte
        if (dokument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").size()!=0)
        {
            for(int i=0; i<(dokument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").get(0).getElementsByTag("tr")).size(); i++)
            {
                Element gericht=dokument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").get(0).getElementsByTag("tr").get(i);
                gerichte.add(
                        new Gericht("Hauptgericht",
                                gericht.getElementsByTag("td").get(0).ownText(),
                                _getBeschreibung(dokument,gericht.getElementsByTag("td").get(2)),
                                Float.parseFloat(gericht.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2,6).replace(',', '.')))
                );
            }
        }

        //Beilagen
        if (dokument.select("div.tx-bwrkspeiseplan__beilagen").size()!=0)
        {
            for(int i=0; i<(dokument.select("div.tx-bwrkspeiseplan__beilagen tbody").get(0).getElementsByTag("tr")).size(); i++)
            {
                Element gericht=dokument.select("div.tx-bwrkspeiseplan__beilagen tbody").get(0).getElementsByTag("tr").get(i);
                gerichte.add(
                        new Gericht("Beilage",
                                gericht.getElementsByTag("td").get(0).ownText(),
                                _getBeschreibung(dokument,gericht.getElementsByTag("td").get(2)),
                                Float.parseFloat(gericht.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2,6).replace(',', '.')))
                );
            }
        }


        //Nachspeisen
        if (dokument.select("div.tx-bwrkspeiseplan__desserts").size()!=0)
        {
            for(int i=0; i<(dokument.select("div.tx-bwrkspeiseplan__desserts tbody").get(0).getElementsByTag("tr")).size(); i++)
            {
                Element gericht=dokument.select("div.tx-bwrkspeiseplan__desserts tbody").get(0).getElementsByTag("tr").get(i);
                gerichte.add(
                        new Gericht("Nachspeise",
                                gericht.getElementsByTag("td").get(0).ownText(),
                                _getBeschreibung(dokument,gericht.getElementsByTag("td").get(2)),
                                Float.parseFloat(gericht.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2,6).replace(',', '.')))
                );
            }
        }

        //Snacks, Salate
        if (dokument.select("div.tx-bwrkspeiseplan__salatsuppen").size()!=0)
        {
            for(int i=0; i<(dokument.select("div.tx-bwrkspeiseplan__salatsuppen tbody").get(0).getElementsByTag("tr")).size(); i++)
            {
                Element gericht=dokument.select("div.tx-bwrkspeiseplan__salatsuppen tbody").get(0).getElementsByTag("tr").get(i);
                gerichte.add(
                        new Gericht("Snack, Salat",
                        gericht.getElementsByTag("td").get(0).ownText(),
                        _getBeschreibung(dokument,gericht.getElementsByTag("td").get(2)),
                        Float.parseFloat(gericht.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2,6).replace(',', '.')))
                );
            }
        }

        return new Tagesplan(gerichte, tag, datum);
    }

    private static String _getBeschreibung(Document dokument, Element icon)
    {
        String beschreibung="";

        for(int i=0; i<icon.childrenSize(); i++)
        {
            if ((icon.getElementsByTag("span")).size() != 0) {
                if (icon.getElementsByTag("span").get(i).attr("class").equals("icon__not-veggie"))
                {
                    beschreibung += "Tier (Lab/Gelatine/Honig)";
                }
            } else if (icon.getElementsByTag("i").size() != 0) {
                switch (icon.getElementsByTag("i").get(i).attr("class")) {
                    case "icon icon-schwein":
                        beschreibung += "Schwein";
                        break;
                    case "icon icon-hahn":
                        beschreibung += "Geflügel";
                        break;
                    case "icon icon-blaetter":
                        beschreibung += "vegetarisch";
                        break;
                    case "icon icon-topf":
                        beschreibung += "hausgemacht";
                        break;
                    case "icon icon-kuh":
                        beschreibung += "Rind";
                        break;
                    case "icon icon-fisch":
                        beschreibung += "Fisch";
                        break;
                    case "icon icon-franken":
                        beschreibung += "Regional";
                        break;
                    case "icon icon-schaf":
                        beschreibung += "Schaf";
                        break;
                    case "icon icon-krabbe":
                        beschreibung += "Meeresfrüchte";
                        break;
                    case "icon icon-hirsch":
                        beschreibung += "Wild";
                        break;
                    default:
                        beschreibung += "Nachhaltiger Fang";
                        break;
                }
            } else if (icon.getElementsByTag("img").size() != 0) {
                switch (icon.getElementsByTag("img").get(i).attr("src")) {
                    case "fileadmin/templates/default/img/icons/uEA03-karotte.png":
                        beschreibung += "vegan";
                        break;
                    default:
                        beschreibung += "Kräuterküche";
                        break;
                }
            } else {
                beschreibung += "Mensa-Vital, eine Marke der Studentenwerke";
            }

            if(i<(icon.childrenSize()-1))
            {
                beschreibung+=", ";
            }

        }

        return beschreibung;
    }
}
