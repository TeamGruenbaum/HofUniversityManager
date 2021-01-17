package Controller.ViewController;



import Controller.Sonstiges.TextHelfer;
import Controller.Speicher.SchreiberLeser;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Tagesplan;
import Model.Tag;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javafx.util.StringConverter;



public class MensaplanViewController implements Initializable
{
    @FXML private Label titelLabel;
    @FXML private ChoiceBox<Tag> wochentagChoicebox;
    @FXML private VBox contentVBox;



    public void initialize(URL location, ResourceBundle resources)
    {
        titelLabel.setText("Mensaplan für die Kalenderwoche " + getRichtigeKalenderwoche());

        wochentagChoicebox.setItems(FXCollections.observableArrayList(Tag.values()));
        wochentagChoicebox.setConverter(new StringConverter<Tag>() {
            @Override
            public String toString(Tag object) {
                return TextHelfer.grossschreiben(object.toString());
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });
        wochentagChoicebox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->zeigeSpeiseplan(Tag.values()[newValue.intValue()]));
        wochentagChoicebox.getSelectionModel().select(LocalDate.now().getDayOfWeek().getValue()<6?LocalDate.now().getDayOfWeek().getValue()-1:0);
    }

    //
    private void zeigeSpeiseplan(Tag tag) {
        List<Gericht> gerichte = getGerichteListe(tag);
        List<String> kategorien = getKategorienListe(tag);

        Accordion kategorienAccordion = new Accordion();
        kategorienAccordion.setPrefWidth(700);
        contentVBox.getChildren().add(1, kategorienAccordion);

        if(contentVBox.getChildren().size() > 2) contentVBox.getChildren().remove(2);

        if(!gerichte.isEmpty()) {
            kategorien.forEach((item) -> {
                VBox kategorieinhatlVBox = new VBox();
                kategorieinhatlVBox.setSpacing(10);
                kategorienAccordion.getPanes().add(new TitledPane(item, kategorieinhatlVBox));

                gerichte
                    .stream()
                    .filter((item2) -> item2.getGang() == item)
                    .forEach((item2) ->
                    {
                        ArrayList<Label> gerichtTexzeilen = new ArrayList<>();
                        VBox gerichtVBox = new VBox();
                        gerichtTexzeilen.add(new Label(item2.getName()));
                        gerichtTexzeilen.add(new Label(item2.getBeschreibung()));
                        gerichtTexzeilen.add(new Label(formatierePreis(item2.getPreis())));
                        gerichtTexzeilen.forEach((item3) -> item3.setWrapText(true));

                        gerichtVBox.getChildren().addAll(FXCollections.observableArrayList(gerichtTexzeilen));
                        kategorieinhatlVBox.getChildren().add(gerichtVBox);
                    });
            });
        } else {
            Label hinweisLeerLabel = new Label("An diesem Tag stehen leider keine Gerichte zur Verfügung.");
            hinweisLeerLabel.getStyleClass().add("warnhinweis");
            contentVBox.getChildren().add(1, hinweisLeerLabel);
        }
    }


    //
    private String formatierePreis(float number) {
        NumberFormat deuschtesZahlenformatNumberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return deuschtesZahlenformatNumberFormat.format(number);
    }

    //
    private int getRichtigeKalenderwoche(){
        Date aktuellesDatumDate = new Date();

        Calendar kalenderCalendar = Calendar.getInstance();
        kalenderCalendar.setTime(aktuellesDatumDate);
        int wochenNummer = kalenderCalendar.get(Calendar.WEEK_OF_YEAR);

        if((LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY) || (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY)) {
            return wochenNummer+1;
        }
        return wochenNummer;
    }

    //
    private List<String> getKategorienListe(Tag tag) {
        return getGerichteListe(tag).stream()
                .map(Gericht::getGang)
                .distinct()
                .collect(Collectors.toList());
    }

    //
    private List<Gericht> getGerichteListe(Tag tag) {
        List<List<Gericht>> gerichte = SchreiberLeser.getMensaplan().getTagesplaene().stream()
            .filter((obj) -> obj.getTagesplanTag().equals(tag))
            .map(Tagesplan::getGerichte)
            .collect(Collectors.toList());

        return gerichte.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}