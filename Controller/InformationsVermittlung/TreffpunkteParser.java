package Controller.InformationsVermittlung;

import Controller.Speicher.SchreiberLeser;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkte;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.URL;
import java.nio.charset.Charset;

public final class TreffpunkteParser
{
    public static void treffpunkteAbrufen()
    {
        try
        {
            SchreiberLeser.treffpunkteSpeichern(treffpunkteParsen(new JSONObject(IOUtils.toString(new URL("https://nebenwohnung.stevensolleder.de/Treffpunkte.json"), Charset.forName("UTF-8")))));
        }
        catch(Exception e){}
    }

    public static Treffpunkte treffpunkteParsen(JSONObject jsonObject)
    {
        Treffpunkte treffpunkte=new Treffpunkte();

        JSONArray restaurants=jsonObject.getJSONArray("restaurants");
        for(int i=0; i<restaurants.length(); i++)
        {
            JSONObject aktuellesJsonObject=restaurants.getJSONObject(i);

            treffpunkte.getTreffpunkte().add(
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

            treffpunkte.getTreffpunkte().add(
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

        return treffpunkte;
    }
}
