package Controller.InformationsVermittlung;



import Controller.InformationsVermittlung.Hilfsklassen.NameKuerzelDocumentTripel;
import Controller.InformationsVermittlung.Hilfsklassen.MensaplanTupel;

import Model.Datum;
import Model.DropdownModel.*;
import Model.MensaplanModel.*;
import Model.ModulhandbuchModel.Modulhandbuch;
import Model.ModulhandbuchModel.ModulhandbuchFach;
import Model.NutzerdatenModel.Doppelstunde;
import Model.StundenplanaenderungModel.*;
import Model.Tag;
import Model.TreffpunktModel.*;
import Model.Uhrzeit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public class Parser
{
	public static Modulhandbuch modulhandbuchParsen(ArrayList<Document> Documents)
	{
		ArrayList<ModulhandbuchFach> modulhandbuchFaecher=new ArrayList<>();

		for(int i=0;i<Documents.size();i++)
		{
			modulhandbuchFaecher.add(getModulhandbuchFach(Documents.get(i)));
		}


		return new Modulhandbuch(modulhandbuchFaecher);
	}

	public static Mensaplan mensaplanParsen(MensaplanTupel mensaplanTupel)
	{
		Tagesplan montagsTagesplan=getTagesplan(mensaplanTupel.getMontag().getPaarDokument(), Tag.MONTAG, mensaplanTupel.getMontag().getPaarDatum());
		Tagesplan dienstagsTagesplan=getTagesplan(mensaplanTupel.getDienstag().getPaarDokument(), Tag.DIENSTAG, mensaplanTupel.getDienstag().getPaarDatum());
		Tagesplan mittwochsTagesplan=getTagesplan(mensaplanTupel.getMittwoch().getPaarDokument(), Tag.MITTWOCH, mensaplanTupel.getMittwoch().getPaarDatum());
		Tagesplan donnerstagsTagesplan=getTagesplan(mensaplanTupel.getDonnerstag().getPaarDokument(), Tag.DONNERSTAG, mensaplanTupel.getDonnerstag().getPaarDatum());
		Tagesplan freitagsTagesplan=getTagesplan(mensaplanTupel.getFreitag().getPaarDokument(), Tag.FREITAG, mensaplanTupel.getFreitag().getPaarDatum());
		Tagesplan samstagsTagesplan=getTagesplan(mensaplanTupel.getSamstag().getPaarDokument(), Tag.SAMSTAG, mensaplanTupel.getSamstag().getPaarDatum());

		ArrayList<Tagesplan> tagesplaene=new ArrayList<Tagesplan>();
		tagesplaene.addAll(Arrays.asList(montagsTagesplan, dienstagsTagesplan, mittwochsTagesplan, donnerstagsTagesplan, freitagsTagesplan, samstagsTagesplan));


		return new Mensaplan(tagesplaene);
	}

	public static Treffpunkte treffpunkteParsen(JSONObject jsonObject)
	{
		ArrayList<Treffpunkt> treffpunkte=new ArrayList<Treffpunkt>();

		JSONArray restaurants=jsonObject.getJSONArray("restaurants");
		for(int i=0;i<restaurants.length();i++)
		{
			JSONObject aktuellesJsonObject=restaurants.getJSONObject(i);

			treffpunkte.add(new Restaurant(aktuellesJsonObject.getString("name"), aktuellesJsonObject.getString("ort"), aktuellesJsonObject.getBoolean("wetterunabhaengig"), aktuellesJsonObject.getString("information"), aktuellesJsonObject.getString("art"), aktuellesJsonObject.getString("nationalitaet"), aktuellesJsonObject.getBoolean("lieferdienst")));
		}

		JSONArray freizeitaktivitaeten=jsonObject.getJSONArray("freizeitaktivitaeten");
		for(int i=0;i<freizeitaktivitaeten.length();i++)
		{
			JSONObject aktuellesJsonObject=freizeitaktivitaeten.getJSONObject(i);

			treffpunkte.add(new Freizeitaktivitaet(aktuellesJsonObject.getString("name"), aktuellesJsonObject.getString("ort"), aktuellesJsonObject.getBoolean("wetterunabhaengig"), aktuellesJsonObject.getString("information"), aktuellesJsonObject.getString("ambiente")));
		}


		return new Treffpunkte(treffpunkte);
	}

	public static ArrayList<Doppelstunde> stundenplanParsen(Document stundenplanDocument)
	{
		ArrayList<Doppelstunde> doppelstunden=new ArrayList<Doppelstunde>();

		String wochentag="";
		boolean letzteTabelle=false;

		for(int i=0;i<stundenplanDocument.getElementsByTag("table").size();i++)
		{
			if(stundenplanDocument.getElementsByTag("table").get(i).select("thead").size()!=0)
			{
				wochentag=stundenplanDocument.getElementsByTag("table").get(i).select("thead").get(0).text();
			}
			else
			{
				break;
			}

			switch(wochentag)
			{
				case "Tag Beginn Ende Veranstaltung Dozent Typ Raum":
					if(stundenplanDocument.getElementsByTag("table").get(i).select("tbody").size()==1)
					{
						doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, null, i);
						letzteTabelle=true;
					}
					break;
				case "Montag":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.MONTAG, i);
					break;
				case "Dienstag":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.DIENSTAG, i);
					break;
				case "Mittwoch":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.MITTWOCH, i);
					break;
				case "Donnerstag":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.DONNERSTAG, i);
					break;
				case "Freitag":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.FREITAG, i);
					break;
				case "Samstag":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, Tag.SAMSTAG, i);
					break;
				case "weitere Veranstaltungen":
					doppelstundeHinzufuegen(doppelstunden, stundenplanDocument, null, i);
					letzteTabelle=true;
					break;
			}

			if((wochentag.compareTo("Freitag")==0&&stundenplanDocument.getElementsByTag("table").get(i+1).select("thead").size()!=0&&stundenplanDocument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("Samstag")!=0&&stundenplanDocument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("weitere Veranstaltungen")!=0)||(wochentag.compareTo("Samstag")==0&&stundenplanDocument.getElementsByTag("table").get(i+1).select("thead").size()!=0&&stundenplanDocument.getElementsByTag("table").get(i+1).select("thead").get(0).text().compareTo("weitere Veranstaltungen")!=0)||letzteTabelle)
			{
				break;
			}
		}


		return doppelstunden;
	}

	public static DropdownMenue dropdownMenueParsen(ArrayList<NameKuerzelDocumentTripel> nameKuerzelDocumentTripel)
	{
		Document stundenplanDocument=null;
		String studiengangkuerzel="";
		String studiengangname="";

		ArrayList<Studiengang> studiengaenge=new ArrayList<Studiengang>();
		for(int i=0;i<nameKuerzelDocumentTripel.size();i++)
		{
			stundenplanDocument=nameKuerzelDocumentTripel.get(i).getStundenplanDokument();
			studiengangname=nameKuerzelDocumentTripel.get(i).getStudiengangName();
			studiengangkuerzel=nameKuerzelDocumentTripel.get(i).getStudiengangKuerzel();

			ArrayList<Studiensemester> studiensemester=new ArrayList<Studiensemester>();
			for(int j=1;j<stundenplanDocument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().size();j++)
			{
				studiensemester.add(new Studiensemester(stundenplanDocument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().get(j).text(), stundenplanDocument.select("select[name='tx_stundenplan_stundenplan[semester]']").first().children().get(j).attr("value")));
			}

			studiengaenge.add(new Studiengang(studiengangname, studiengangkuerzel, studiensemester));
		}


		return new DropdownMenue(studiengaenge);
	}

	public static Stundenplanaenderungen stundenplanaenderungenParsen(Document stundenplanaenderungenDocument)
	{
		ArrayList<Stundenplanaenderung> stundenplanaenderungen=new ArrayList<Stundenplanaenderung>();

		Element tabelleElement=stundenplanaenderungenDocument.getElementsByTag("table").get(0);
		Element zeileElement=null;

		if(tabelleElement.getElementsByTag("tbody").size()!=0)
		{
			for(int i=0;i<tabelleElement.select("tbody>tr").size();i++)
			{
				zeileElement=tabelleElement.select("tbody>tr").get(i);
				stundenplanaenderungen.add(new Stundenplanaenderung(zeileElement.getElementsByTag("td").get(1).text(), zeileElement.getElementsByTag("td").get(2).text(), getTermin(zeileElement.getElementsByTag("td").get(3).text()), getTermin(zeileElement.getElementsByTag("td").get(4).text())));
			}
		}


		return new Stundenplanaenderungen(stundenplanaenderungen);
	}


	private static ModulhandbuchFach getModulhandbuchFach(Document modulhandbuchFachDocument)
	{
		Element tabelleElement=modulhandbuchFachDocument.getElementsByTag("tbody").first();


		return new ModulhandbuchFach(modulhandbuchFachDocument.select("h2:not([class])").text(), tabelleElement.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(11).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(12).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(13).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(14).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(15).getElementsByTag("td").get(1).text(), tabelleElement.getElementsByTag("tr").get(16).getElementsByTag("td").get(1).text());
	}

	private static Tagesplan getTagesplan(Document tagesplanDocument, Tag tagesplanTagTag, Datum tagesplanDatumDatum)
	{
		ArrayList<Gericht> gerichte=new ArrayList<Gericht>();
		Element gerichtElement=null;

		if(tagesplanDocument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").size()!=0)
		{
			for(int i=0;i<(tagesplanDocument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").get(0).getElementsByTag("tr")).size();i++)
			{
				gerichtElement=tagesplanDocument.select("div.tx-bwrkspeiseplan__hauptgerichte tbody").get(0).getElementsByTag("tr").get(i);
				gerichte.add(new Gericht("Hauptgericht", gerichtElement.getElementsByTag("td").get(0).ownText(), getBeschreibung(gerichtElement.getElementsByTag("td").get(2)), Float.parseFloat(gerichtElement.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2, 6).replace(',', '.'))));
			}
		}

		if(tagesplanDocument.select("div.tx-bwrkspeiseplan__beilagen").size()!=0)
		{
			for(int i=0;i<(tagesplanDocument.select("div.tx-bwrkspeiseplan__beilagen tbody").get(0).getElementsByTag("tr")).size();i++)
			{
				gerichtElement=tagesplanDocument.select("div.tx-bwrkspeiseplan__beilagen tbody").get(0).getElementsByTag("tr").get(i);
				gerichte.add(new Gericht("Beilage", gerichtElement.getElementsByTag("td").get(0).ownText(), getBeschreibung(gerichtElement.getElementsByTag("td").get(2)), Float.parseFloat(gerichtElement.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2, 6).replace(',', '.'))));
			}
		}

		if(tagesplanDocument.select("div.tx-bwrkspeiseplan__desserts").size()!=0)
		{
			for(int i=0;i<(tagesplanDocument.select("div.tx-bwrkspeiseplan__desserts tbody").get(0).getElementsByTag("tr")).size();i++)
			{
				gerichtElement=tagesplanDocument.select("div.tx-bwrkspeiseplan__desserts tbody").get(0).getElementsByTag("tr").get(i);
				gerichte.add(new Gericht("Nachspeise", gerichtElement.getElementsByTag("td").get(0).ownText(), getBeschreibung(gerichtElement.getElementsByTag("td").get(2)), Float.parseFloat(gerichtElement.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2, 6).replace(',', '.'))));
			}
		}

		if(tagesplanDocument.select("div.tx-bwrkspeiseplan__salatsuppen").size()!=0)
		{
			for(int i=0;i<(tagesplanDocument.select("div.tx-bwrkspeiseplan__salatsuppen tbody").get(0).getElementsByTag("tr")).size();i++)
			{
				gerichtElement=tagesplanDocument.select("div.tx-bwrkspeiseplan__salatsuppen tbody").get(0).getElementsByTag("tr").get(i);
				gerichte.add(new Gericht("Snack, Salat", gerichtElement.getElementsByTag("td").get(0).ownText(), getBeschreibung(gerichtElement.getElementsByTag("td").get(2)), Float.parseFloat(gerichtElement.getElementsByTag("td").get(1).getElementsByTag("span").first().html().substring(2, 6).replace(',', '.'))));
			}
		}


		return new Tagesplan(gerichte, tagesplanTagTag, tagesplanDatumDatum);
	}

	private static String getBeschreibung(Element iconElement)
	{
		String beschreibung="";

		for(int i=0;i<iconElement.childrenSize();i++)
		{
			if((iconElement.getElementsByTag("span")).size()!=0)
			{
				if(iconElement.child(i).getElementsByTag("span").get(0).attr("class").equals("icon__not-veggie"))
				{
					beschreibung+="Tier (Lab/Gelatine/Honig)";
				}
			}
			else
			{
				if(iconElement.child(i).getElementsByTag("i").size()!=0)
				{
					switch(iconElement.child(i).getElementsByTag("i").get(0).attr("class"))
					{
						case "icon icon-schwein":
							beschreibung+="Schwein";
							break;
						case "icon icon-hahn":
							beschreibung+="Geflügel";
							break;
						case "icon icon-blaetter":
							beschreibung+="Vegetarisch";
							break;
						case "icon icon-topf":
							beschreibung+="Hausgemacht";
							break;
						case "icon icon-kuh":
							beschreibung+="Rind";
							break;
						case "icon icon-fisch":
							beschreibung+="Fisch";
							break;
						case "icon icon-franken":
							beschreibung+="Regional";
							break;
						case "icon icon-schaf":
							beschreibung+="Schaf";
							break;
						case "icon icon-krabbe":
							beschreibung+="Meeresfrüchte";
							break;
						case "icon icon-hirsch":
							beschreibung+="Wild";
							break;
						default:
							beschreibung+="Nachhaltiger Fang";
							break;
					}
				}
				else
				{
					if(iconElement.getElementsByTag("img").size()!=0)
					{
						switch(iconElement.child(i).getElementsByTag("img").get(0).attr("src"))
						{
							case "fileadmin/templates/default/img/icons/uEA03-karotte.png":
								beschreibung+="Vegan";
								break;
							case "fileadmin/templates/default/img/icons/uEA04-kraeuter.png":
								beschreibung+="Kräuterküche";
								break;
							case "fileadmin/templates/default/img/logos/icon_mensavital.png":
								beschreibung+="Mensa-Vital - eine Marke der Studentenwerke";
								break;
						}
					}
				}
			}

			if(i<(iconElement.childrenSize()-1))
			{
				beschreibung+=", ";
			}
		}


		return beschreibung;
	}

	private static void doppelstundeHinzufuegen(ArrayList<Doppelstunde> doppelstunden, Document stundenplanDocument, Tag doppelstundenTagTag, int zaehler)
	{
		Element aktuelleDoppelstundeElement=null;
		Datum doppelstundeDatum=null;
		Tag datumsTag=doppelstundenTagTag;

		for(int j=0;j<stundenplanDocument.getElementsByTag("table").get(zaehler).select("tbody>tr").size();j++)
		{
			aktuelleDoppelstundeElement=stundenplanDocument.getElementsByTag("table").get(zaehler).select("tbody>tr").get(j);
			doppelstundeDatum=getDoppelstundenDatum(aktuelleDoppelstundeElement);

			if(doppelstundenTagTag==null&&doppelstundeDatum!=null)
			{
				switch(LocalDate.of(doppelstundeDatum.getJahr(), doppelstundeDatum.getMonat(), doppelstundeDatum.getTag()).getDayOfWeek())
				{
					case MONDAY:
						datumsTag=Tag.MONTAG;
						break;
					case TUESDAY:
						datumsTag=Tag.DIENSTAG;
						break;
					case WEDNESDAY:
						datumsTag=Tag.MITTWOCH;
						break;
					case THURSDAY:
						datumsTag=Tag.DONNERSTAG;
						break;
					case FRIDAY:
						datumsTag=Tag.FREITAG;
						break;
					case SATURDAY:
						datumsTag=Tag.SAMSTAG;
						break;
					case SUNDAY:
						datumsTag=Tag.SONNTAG;
						break;
				}
			}

			doppelstunden.add(new Doppelstunde(datumsTag, doppelstundeDatum, getDoppelstundenUhrzeit(aktuelleDoppelstundeElement, 1), getDoppelstundenUhrzeit(aktuelleDoppelstundeElement, 2), aktuelleDoppelstundeElement.getElementsByTag("td").get(6).text().compareTo("")==0?null:aktuelleDoppelstundeElement.getElementsByTag("td").get(6).text(), aktuelleDoppelstundeElement.getElementsByTag("td").get(3).text().replace("\n", "").replace("\r", ""), aktuelleDoppelstundeElement.getElementsByTag("td").get(4).text()

			));
		}
	}

	private static Datum getDoppelstundenDatum(Element aktuelleDoppelstundeElement)
	{
		Datum datum=null;

		if(aktuelleDoppelstundeElement.getElementsByTag("td").get(0).text().compareTo("")!=0)
		{
			datum=new Datum(Integer.parseInt(aktuelleDoppelstundeElement.getElementsByTag("td").get(0).text().substring(0, 2)), Integer.parseInt(aktuelleDoppelstundeElement.getElementsByTag("td").get(0).text().substring(3, 5)), Integer.parseInt(aktuelleDoppelstundeElement.getElementsByTag("td").get(0).text().substring(6, 10)));
		}


		return datum;
	}

	private static Uhrzeit getDoppelstundenUhrzeit(Element aktuelleDoppelstundeElement, int index)
	{
		Uhrzeit uhrzeit=null;

		if(aktuelleDoppelstundeElement.getElementsByTag("td").get(index).text().compareTo("")!=0)
		{
			uhrzeit=new Uhrzeit(Integer.parseInt(aktuelleDoppelstundeElement.getElementsByTag("td").get(index).text().substring(0, 2)), Integer.parseInt(aktuelleDoppelstundeElement.getElementsByTag("td").get(index).text().substring(3, 5)));
		}


		return uhrzeit;
	}

	private static Termin getTermin(String termintext)
	{
		Pattern pattern=Pattern.compile("(^\\d{2}.\\d{2}.\\d{4}\\n)(\\d{2}:\\d{2}\\sUhr\\n)(.+)");
		Matcher matcher=pattern.matcher(termintext);
		matcher.matches();
		Termin termin=null;

		if(matcher.matches())
		{
			termin=new Termin(new Datum(Integer.parseInt(matcher.group(1).substring(0, 2)), Integer.parseInt(matcher.group(1).substring(3, 5)), Integer.parseInt(matcher.group(1).substring(6, 10))), new Uhrzeit(Integer.parseInt(matcher.group(2).substring(0, 2)), Integer.parseInt(matcher.group(2).substring(3, 5))), matcher.group(3));
		}


		return termin;
	}
}
