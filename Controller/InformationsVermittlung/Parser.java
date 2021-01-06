package Controller.InformationsVermittlung;

import Model.Datum;
import Model.DropdownModel.DropdownMenue;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.Doppelstunde;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import Model.StundenplanaenderungModel.Stundenplanaenderung;
import Model.StundenplanaenderungModel.Stundenplanaenderungen;
import Model.StundenplanaenderungModel.Termin;
import Model.Tag;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;
import Model.Uhrzeit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser
{
    //Studiengang parsen
    public static StudiengangInformationen studiengangParsen(StudiengangDokumente studiengangDokumente)
    {
        ArrayList<Document> faecherDokumente=studiengangDokumente.getFaecherDokumente();

        ArrayList<ModulhandbuchFach> faecher=new ArrayList<>();
        for(int i=0; i<faecherDokumente.size(); i++)
        {
            faecher.add(_getModulhandbuchFach(faecherDokumente.get(i)));
        }

        return new StudiengangInformationen(faecher);
    }

    private static ModulhandbuchFach _getModulhandbuchFach(Document dokument)
    {
        Element tabelle=dokument.getElementsByTag("tbody").first();

        return new ModulhandbuchFach(
                dokument.select("h2:not([class])").text(),
                tabelle.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text(),
                tabelle.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text(),
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

    //Treffpunkte parsen
    public static Treffpunkte treffpunkteParsen(JSONObject jsonObject)
    {
        ArrayList<Treffpunkt> treffpunkte=new ArrayList<Treffpunkt>();

        JSONArray restaurants=jsonObject.getJSONArray("restaurants");
        for(int i=0; i<restaurants.length(); i++)
        {
            JSONObject aktuellesJsonObject=restaurants.getJSONObject(i);

            treffpunkte.add(new Restaurant
                    (
                        aktuellesJsonObject.getString("name"),
                        aktuellesJsonObject.getString("ort"),
                        aktuellesJsonObject.getBoolean("wetterunabhaengig"),
                        aktuellesJsonObject.getString("information"),
                        aktuellesJsonObject.getString("art"),
                        aktuellesJsonObject.getString("nationalitaet"),
                        aktuellesJsonObject.getBoolean("lieferdienst")
                    )
            );
        }

        JSONArray freizeitaktivitaeten=jsonObject.getJSONArray("freizeitaktivitaeten");
        for(int i=0; i<freizeitaktivitaeten.length(); i++)
        {
            JSONObject aktuellesJsonObject=freizeitaktivitaeten.getJSONObject(i);

            treffpunkte.add(new Freizeitaktivitaet
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

    //Stundenplan parsen
    public static ArrayList<Doppelstunde> stundenplanParsen(Document stundenplanDokument)
    {
        ArrayList<Doppelstunde> doppelstunden=new ArrayList<Doppelstunde>();
        String wochentag="";
        boolean letzteTabelle=false;

        for(int i=0;i<stundenplanDokument.getElementsByTag("table").size();i++)
        {
            if(stundenplanDokument.getElementsByTag("table").get(i).select("thead").size()!=0)
            {
                wochentag=stundenplanDokument.getElementsByTag("table").get(i).select("thead").get(0).text();
                System.out.println("Wochentag: "+wochentag);
            }
            else break;


            switch(wochentag)
            {
                case "Tag Beginn Ende Veranstaltung Dozent Typ Raum":
                    if(stundenplanDokument.getElementsByTag("table").get(i).select("tbody").size()==1)
                    {
                        _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, null, i);
                        letzteTabelle=true;
                    } break;
                case "Montag":_doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.MONTAG, i); break;
                case "Dienstag": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.DIENSTAG, i); break;
                case "Mittwoch": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.MITTWOCH, i); break;
                case "Donnerstag": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.DONNERSTAG, i); break;
                case "Freitag": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.FREITAG, i); break;
                case "Samstag": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, Tag.SAMSTAG, i); break;
                case "weitere Veranstaltungen": _doppelstundeHinzufuegen(doppelstunden, stundenplanDokument, null, i); letzteTabelle=true; break;
            }


            if((wochentag.compareTo("Freitag")==0 && stundenplanDokument.getElementsByTag("table").get(i+1).select("thead").size()!=0 && stundenplanDokument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("Samstag")!=0 && stundenplanDokument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("weitere Veranstaltungen")!=0) ||
                (wochentag.compareTo("Samstag")==0 && stundenplanDokument.getElementsByTag("table").get(i+1).select("thead").size()!=0 && stundenplanDokument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("weitere Veranstaltungen")!=0) ||
                letzteTabelle)
            {
                System.out.println("abbrechen");
                break;
            }

        }
        return doppelstunden;
    }

    private static void _doppelstundeHinzufuegen(ArrayList<Doppelstunde> doppelstunden, Document dokument, Tag tag, int zaehler)
    {
        //Hilfsvariablen anlegen
        Element aktuelleDoppelstunde=null;
        Datum datum=null;
        Tag doppelstundenTag=tag;

        for(int j=0;j<dokument.getElementsByTag("table").get(zaehler).select("tbody>tr").size();j++)
        {
            aktuelleDoppelstunde=dokument.getElementsByTag("table").get(zaehler).select("tbody>tr").get(j);
            datum=_getDoppelstundenDatum(aktuelleDoppelstunde);

            //Ermittle den Wochentag der Doppelstunde über ihr Datum; ignoriere hierbei "weitere Veranstaltungen", die kein Datum besitzen
            if(tag==null && datum!=null)
            {
                switch(LocalDate.of(datum.getJahr(),datum.getMonat(),datum.getTag()).getDayOfWeek())
                {
                    case MONDAY:doppelstundenTag=Tag.MONTAG; break;
                    case TUESDAY:doppelstundenTag=Tag.DIENSTAG; break;
                    case WEDNESDAY:doppelstundenTag=Tag.MITTWOCH; break;
                    case THURSDAY:doppelstundenTag=Tag.DONNERSTAG; break;
                    case FRIDAY:doppelstundenTag=Tag.FREITAG; break;
                    case SATURDAY:doppelstundenTag=Tag.SAMSTAG; break;
                    case SUNDAY:doppelstundenTag=Tag.SONNTAG; break;
                }
            }

            //Füge die Doppelstunde zur ArrayList hinzu
            doppelstunden.add(new Doppelstunde
                    (
                            datum,
                            aktuelleDoppelstunde.getElementsByTag("td").get(3).text(),
                            aktuelleDoppelstunde.getElementsByTag("td").get(4).text(),
                            aktuelleDoppelstunde.getElementsByTag("td").get(6).text(),
                            doppelstundenTag,
                            _getDoppelstundenUhrzeit(aktuelleDoppelstunde,1),
                            _getDoppelstundenUhrzeit(aktuelleDoppelstunde,2)
                    )
            );
        };
    }

    //Gib das Datum einer Doppelstunde zurück, wenn vorhanden
    private static Datum _getDoppelstundenDatum(Element aktuelleDoppelstunde)
    {
        Datum datum=null;

        if(aktuelleDoppelstunde.getElementsByTag("td").get(0).text().compareTo("")!=0)
        {
            datum=new Datum
            (
                    Integer.parseInt(aktuelleDoppelstunde.getElementsByTag("td").get(0).text().substring(0,2)),
                    Integer.parseInt(aktuelleDoppelstunde.getElementsByTag("td").get(0).text().substring(3,5)),
                    Integer.parseInt(aktuelleDoppelstunde.getElementsByTag("td").get(0).text().substring(6,10))
            );
        }

        return datum;
    }

    //Gib die Uhrzeit einer Doppelstunde zurück, wenn vorhanden
    private static Uhrzeit _getDoppelstundenUhrzeit(Element aktuelleDoppelstunde, int index)
    {
        Uhrzeit uhrzeit=null;

        if(aktuelleDoppelstunde.getElementsByTag("td").get(index).text().compareTo("")!=0)
        {
            uhrzeit=new Uhrzeit
            (
                    Integer.parseInt(aktuelleDoppelstunde.getElementsByTag("td").get(index).text().substring(0,2)),
                    Integer.parseInt(aktuelleDoppelstunde.getElementsByTag("td").get(index).text().substring(3,5))
            );
        }

        return uhrzeit;
    }

    //DropdownMenue parsen
    public static DropdownMenue dropdownMenueParsen(DropdownMenueDokumente dropdownMenueDokumente)
    {
        ArrayList<KuerzelDokumentPaar> kuerzelDokumentPaare= dropdownMenueDokumente.getKuerzelDokumentePaare();

        //Hilfsvariablen anlegen
        Document stundenplanDokument=null;
        String studiengangkuerzel="";
        String studiengangname="";

        ArrayList<Studiengang> eintraege=new ArrayList<Studiengang>();

        for(int i=0;i<kuerzelDokumentPaare.size();i++)
        {
            stundenplanDokument=kuerzelDokumentPaare.get(i).getStundenplanDokument();
            studiengangname=kuerzelDokumentPaare.get(i).getStudiengangName();
            studiengangkuerzel=kuerzelDokumentPaare.get(i).getStudiengangKuerzel();

            ArrayList<Studiensemester> studiensemester=new ArrayList<Studiensemester>();
            for(int j=1;j<stundenplanDokument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().size();j++)
            {
                studiensemester.add(new Studiensemester
                (
                    stundenplanDokument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().get(j).text(),
                    stundenplanDokument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().get(j).attr("value")
                ));
            }

            eintraege.add(new Studiengang
            (
                studiengangname, studiengangkuerzel, studiensemester
            ));
        }

        return new DropdownMenue(eintraege);
    }

    //Stundenplanaenderungen parsen
    public static Stundenplanaenderungen stundenplanaenderungenParsen(Document stundenplanaenderungenDokument)
    {
        ArrayList<Stundenplanaenderung> aenderungen=new ArrayList<Stundenplanaenderung>();

        //Hilfsvariablen anlegen
        Element tabelle=stundenplanaenderungenDokument.getElementsByTag("table").get(0);
        Element zeile=null;

        if(tabelle.getElementsByTag("tbody").size()!=0)
        {
            for(int i=0; i<tabelle.select("tbody>tr").size(); i++)
            {
                zeile=tabelle.select("tbody>tr").get(i);
                aenderungen.add(new Stundenplanaenderung
                (
                        zeile.getElementsByTag("td").get(1).text(),
                        zeile.getElementsByTag("td").get(2).text(),
                        _getTermin(zeile.getElementsByTag("td").get(3).text()),
                        _getTermin(zeile.getElementsByTag("td").get(4).text())
                ));
            }
        }
        return new Stundenplanaenderungen(aenderungen);
    }

    //Gib einen Termin basierend auf einem String, der aus Datum, Uhrzeit und Raum besteht, zurück
    private static Termin _getTermin(String termintext)
    {
        Pattern pattern=Pattern.compile("(^\\d{2}.\\d{2}.\\d{4}\\n)(\\d{2}:\\d{2}\\sUhr\\n)(.+)");
        Matcher matcher=pattern.matcher(termintext);
        matcher.matches();
        Termin termin=null;

        if(matcher.matches())
        {
            termin=new Termin
            (
                new Datum(Integer.parseInt(matcher.group(1).substring(0,2)),Integer.parseInt(matcher.group(1).substring(3,5)),Integer.parseInt(matcher.group(1).substring(6,10))),
                new Uhrzeit(Integer.parseInt(matcher.group(2).substring(0,2)),Integer.parseInt(matcher.group(2).substring(3,5))),
                matcher.group(3)
            );
        }
        return termin;
    }
}
