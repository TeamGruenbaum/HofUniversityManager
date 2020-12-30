package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import javax.swing.*;
import java.net.URL;
import java.util.*;

import static Model.Tag.*;

public class StundenplanViewController implements Initializable
{
    ObservableList<Doppelstunde> montagObservableList, dienstagObservableList, mittwochObservableList, donnerstagObservableList, freitagObservableList;

    @FXML
    private TableView<Doppelstunde> montagTableView;

    @FXML
    private TableColumn<Doppelstunde, String> montagTableColumn;


    @FXML
    private TableView<Doppelstunde> dienstagTableView;

    @FXML
    public TableColumn<Doppelstunde, String> dienstagTableColumn;


    @FXML
    private TableView<Doppelstunde> mittwochTableView;

    @FXML
    private TableColumn<Doppelstunde, String> mittwochTableColumn;


    @FXML
    private TableView<Doppelstunde> donnerstagTableView;

    @FXML
    private TableColumn<Doppelstunde, String> donnerstagTableColumn;


    @FXML
    private TableView<Doppelstunde> freitagTableView;

    @FXML
    private TableColumn<Doppelstunde, String> freitagTableColumn;


    @FXML
    Accordion faecherSelect;
    @FXML
    TableColumn<Aufgabe, Datum> datumTableColumn;
    @FXML
    TableColumn<Aufgabe, Uhrzeit> uhrzeitTableColumn;
    @FXML
    TableColumn<Aufgabe, String> inhaltCol;
    @FXML
    TableColumn<Notiz, String> notizenCol;
    @FXML
    TableColumn<Note, String> artCol;
    @FXML
    TableColumn<Note, Integer> notenCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ArrayList<Doppelstunde> testDoppelstunden = new ArrayList<>(List.of(
                new Doppelstunde("WA", "Trapp","FB009", DIENSTAG, new Uhrzeit(9,0,0),new Uhrzeit(9,45,0)),
                new Doppelstunde("OOP1", "Ashauer","FB009", DIENSTAG, new Uhrzeit(8,0,0),new Uhrzeit(9,45,0)),
                new Doppelstunde("DM", "Schaller","FB009", Tag.MITTWOCH, new Uhrzeit(9,0,0),new Uhrzeit(9,45,0))
        ));

        montagObservableList=FXCollections.observableArrayList();
        dienstagObservableList=FXCollections.observableArrayList();
        mittwochObservableList=FXCollections.observableArrayList();
        donnerstagObservableList=FXCollections.observableArrayList();
        freitagObservableList=FXCollections.observableArrayList();

        stundenplanAktualisieren(testDoppelstunden);

        Callback<TableColumn.CellDataFeatures<Doppelstunde,String>,ObservableValue<String>> cellValueFactory = cellData ->
        {
            return new SimpleStringProperty(
                    cellData.getValue().getBeginn()+"-"+cellData.getValue().getEnde()+", "+cellData.getValue().getRaum()+"\n"+
                            cellData.getValue().getName()+"\n"+
                            cellData.getValue().getDozent());
        };


        montagTableView.setItems(montagObservableList);
        montagTableColumn.setCellValueFactory(cellValueFactory);

        dienstagTableView.setItems(dienstagObservableList);
        dienstagTableColumn.setCellValueFactory(cellValueFactory);

        mittwochTableView.setItems(mittwochObservableList);
        mittwochTableColumn.setCellValueFactory(cellValueFactory);

        donnerstagTableView.setItems(donnerstagObservableList);
        donnerstagTableColumn.setCellValueFactory(cellValueFactory);

        freitagTableView.setItems(freitagObservableList);
        freitagTableColumn.setCellValueFactory(cellValueFactory);




        //Datenabrufer.stundenplanAbrufen();

        //SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(System.out::println);

        /*fachdaten = new ArrayList<>();
        fachdaten.add(new FachDatensatz("Margeding", new ArrayList<Aufgabe>(), new ArrayList<Notiz>(), new ArrayList<Note>()));*/

        /*nutzer.getDoppelstunden().forEach(a ->{
            if(!fachdaten.contains(a.getFachDatensatz())){
                fachdaten.add(a.getFachDatensatz());
            }
        });

        fachdaten.forEach(this::erzeugeAkkordion);*/
    }

    private void stundenplanAktualisieren(ArrayList<Doppelstunde> alleStunden) {
        //ArrayList<Doppelstunde> alleStunden = SchreiberLeser.getNutzerdaten().getDoppelstunden();

        Comparator<Doppelstunde> doppelstundeComparator=new Comparator<Doppelstunde>()
        {
            @Override
            public int compare(Doppelstunde o1, Doppelstunde o2)
            {
                return o1.getBeginn().compareTo(o2.getBeginn());
            }
        };

        alleStunden.forEach(item -> {
            switch(item.getTag())
            {
                case MONTAG:
                {
                    montagObservableList.add(item);
                    montagObservableList.sort(doppelstundeComparator);
                } break;
                case DIENSTAG:
                {
                    dienstagObservableList.add(item);
                    dienstagObservableList.sort(doppelstundeComparator);
                } break;
                case MITTWOCH:
                {
                    mittwochObservableList.add(item);
                    mittwochObservableList.sort(doppelstundeComparator);
                } break;
                case DONNERSTAG:
                {
                    donnerstagObservableList.add(item);
                    donnerstagObservableList.sort(doppelstundeComparator);
                } break;
                case FREITAG:
                {
                    freitagObservableList.add(item);
                    freitagObservableList.sort(doppelstundeComparator);
                } break;
            }
        });
    }

    @FXML
    public void doppelstundeHinzufuegen(ActionEvent actionEvent)
    {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(8);

        //Name
        gridPane.add(new Label("Name"), 0, 0);
        TextField nameTextField = new TextField();
        gridPane.add(nameTextField, 1, 0);

        //Dozent
        gridPane.add(new Label("Dozent"), 0, 1);
        TextField dozentTextField = new TextField();
        gridPane.add(dozentTextField, 1, 1);

        //Tag
        gridPane.add(new Label("Tag"), 0, 5);
        ChoiceBox<Tag> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(Tag.MONTAG, DIENSTAG, Tag.MITTWOCH, Tag.DONNERSTAG, Tag.FREITAG);
        gridPane.add(choiceBox, 1, 3);

        //Beginnzeit
        Spinner<Integer> beginnStundeSpinner = new Spinner<>();
        beginnStundeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        beginnStundeSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        beginnStundeSpinner.setMaxWidth(70);

        Spinner<Integer> beginnMinuteSpinner = new Spinner<>();
        beginnMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Calendar.getInstance().get(Calendar.MINUTE)));
        beginnMinuteSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        beginnMinuteSpinner.setMaxWidth(70);

        HBox beginnHBox = new HBox();
        beginnHBox.getChildren().addAll(beginnStundeSpinner, new Label(" : "),beginnMinuteSpinner);

        gridPane.add(new Label("Beginn:"), 0, 4);
        gridPane.add(beginnHBox, 1, 4);

        //Ende Zeit
        Spinner<Integer> endeStundeSpinner = new Spinner<>();
        endeStundeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        endeStundeSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        endeStundeSpinner.setMaxWidth(70);

        Spinner<Integer> endeMinuteSpinner = new Spinner<>();
        endeMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Calendar.getInstance().get(Calendar.MINUTE)));
        endeMinuteSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        endeMinuteSpinner.setMaxWidth(70);

        HBox endeHBox = new HBox();
        beginnHBox.getChildren().addAll(endeStundeSpinner, new Label(" : "),endeMinuteSpinner);

        gridPane.add(new Label ("Ende:"), 0, 5);
        gridPane.add(endeHBox, 1, 5);

        //Raum
        gridPane.add(new Label("Raum:"), 0, 2);
        TextField raumTextField = new TextField();
        gridPane.add(raumTextField, 1, 6);






        Button button = new Button("Speichern");
        /*button.setOnAction(btn ->
        {
            if (!sekundenA.getText().matches("\\d*") || !minutenA.getText().matches("\\d*") || !stundenA.getText().matches("\\d*") || !sekundenB.getText().matches("\\d*") || !minutenB.getText().matches("\\d*") || !stundenB.getText().matches("\\d*")) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Bitte nur numerische Zahlen als Zeiten!");
                alert2.showAndWait();
            }else {

                Doppelstunde neueStunde = new Doppelstunde(name.getText(), dozent.getText(), raum.getText(), tag.getValue(), new Uhrzeit(Integer.parseInt(stundenA.getText()), Integer.parseInt(minutenA.getText()), Integer.parseInt(sekundenA.getText())), new Uhrzeit(Integer.parseInt(stundenB.getText()), Integer.parseInt(minutenB.getText()), Integer.parseInt(sekundenB.getText())));

                nutzerdaten.getDoppelstunden().add(neueStunde);

                doppelstundeZuordnen(neueStunde, neueStunde.getTag());

            }
        });*/
        gridPane.add(button, 1, 6);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(gridPane);

        Dialog dialog = new Dialog();
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
    }


    private void erzeugeAkkordion(FachDatensatz a) {
        ObservableList<Note> notenList = FXCollections.observableArrayList();
        ObservableList<Aufgabe> aufgabenList = FXCollections.observableArrayList();
        ObservableList<Notiz> notizenList = FXCollections.observableArrayList();


        TitledPane pane = new TitledPane();
        AnchorPane anchor = new AnchorPane();
        GridPane grid = new GridPane();

        TableView<Aufgabe> aufgabenTabelle = new TableView<>();
        TableView<Note> notenTabelle = new TableView<>();
        TableView<Notiz> notizenTabelle = new TableView<>();

        AnchorPane plus1 = new AnchorPane();
        AnchorPane plus2 = new AnchorPane();
        AnchorPane plus3 = new AnchorPane();

        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();


        plus1.getChildren().add(button1);
        plus2.getChildren().add(button2);
        plus3.getChildren().add(button3);


        pane.setContent(anchor);

        anchor.getChildren().add(grid);

        grid.add(new Label("Aufgaben"),0,0);
        grid.add(new Label("Notizen"), 1, 0);
        grid.add(new Label("Noten"), 2, 0);

        grid.add(aufgabenTabelle, 0, 1);
        grid.add(notizenTabelle, 1, 1);
        grid.add(notenTabelle, 2, 1);

        aufgabenTabelle.getColumns().add(new TableColumn<>("Datum"));
        aufgabenTabelle.getColumns().add(new TableColumn<>("Uhrzeit"));
        aufgabenTabelle.getColumns().add(new TableColumn<>("Inhalt"));

        notizenTabelle.getColumns().add(new TableColumn<>("Überschrift"));

        notenTabelle.getColumns().add(new TableColumn<>("Art"));
        notenTabelle.getColumns().add(new TableColumn<>("Note"));

        grid.add(plus1, 0, 0);
        grid.add(plus2, 0, 1);
        grid.add(plus3, 0, 2);

        button1.setOnAction( btn ->{
            newAufgabe(a, aufgabenTabelle);
        });

        button2.setOnAction( btn ->{
            newNotiz(a, notizenTabelle);
        });

        button3.setOnAction( btn ->{
            newNote(a, notenTabelle);
        });

        datumTableColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
        inhaltCol.setCellValueFactory(new PropertyValueFactory<>("inhalt"));
        uhrzeitTableColumn.setCellValueFactory(new PropertyValueFactory<>("zeit"));

        notizenCol.setCellValueFactory(new PropertyValueFactory<>("ueberschrift"));

        notenCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        artCol.setCellValueFactory(new PropertyValueFactory<>("art"));

        notenList.addAll(a.getNoten());
        aufgabenList.addAll(a.getAufgaben());
        notizenList.addAll(a.getNotizen());

        notenList.forEach(note -> {
            notenTabelle.getItems().add(note);
        });
        aufgabenList.forEach(aufgabe -> {
            aufgabenTabelle.getItems().add(aufgabe);
        });
        notizenList.forEach(notiz -> {
            notizenTabelle.getItems().add(notiz);
        });

        notenTabelle.setRowFactory( x -> {
            TableRow<Note> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(event.getClickCount() == 2 && !row.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = new DialogPane();

                    Button ok = new Button("Bitte Löschen!");
                    ok.setOnAction(btn ->{
                        Note item = notenTabelle.getSelectionModel().getSelectedItem();
                        notenList.remove(item);
                        notenTabelle.getItems().remove(item);
                        alert.close();
                    });

                    Button nein = new Button("Nein!");
                    nein.setOnAction(btn ->{
                        alert.close();
                    });

                    dialogPane.getChildren().addAll(new Label("Möchten sie das Feld wirklich löschen?"), ok, nein);

                    alert.setDialogPane(dialogPane);

                    alert.showAndWait();
                }
            });
            return row;
        });


        notizenTabelle.setRowFactory( x -> {
            TableRow<Notiz> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(event.getClickCount() == 2 && !row.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = new DialogPane();

                    Button ok = new Button("Löschen!");
                    ok.setOnAction(btn ->{
                        Note item = notenTabelle.getSelectionModel().getSelectedItem();
                        notenList.remove(item);
                        notenTabelle.getItems().remove(item);
                        alert.close();
                    });

                    Button nein = new Button("Zurück");
                    nein.setOnAction(btn ->{
                        alert.close();
                    });

                    dialogPane.getChildren().addAll(new Label(row.getItem().getInhalt()), ok, nein);

                    alert.setDialogPane(dialogPane);

                    alert.showAndWait();
                }
            });
            return row;
        });

        aufgabenTabelle.setRowFactory( x -> {
            TableRow<Aufgabe> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(event.getClickCount() == 2 && !row.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = new DialogPane();

                    Button ok = new Button("Bitte Löschen!");
                    ok.setOnAction(btn ->{
                        Aufgabe item = aufgabenTabelle.getSelectionModel().getSelectedItem();
                        aufgabenList.remove(item);
                        aufgabenTabelle.getItems().remove(item);
                        alert.close();
                    });

                    Button nein = new Button("Nein!");
                    nein.setOnAction(btn ->{
                        alert.close();
                    });

                    dialogPane.getChildren().addAll(new Label("Möchten sie das Feld wirklich löschen?"), ok, nein);

                    alert.setDialogPane(dialogPane);

                    alert.showAndWait();
                }
            });
            return row;
        });

        faecherSelect.getPanes().add(pane);
    }




    private void newNote(FachDatensatz a, TableView<Note> notenTabelle) {
        DialogPane pane = new DialogPane();
        GridPane grid = new GridPane();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        TextField notenArt = new TextField();

        TextField note = new TextField();


        Label label1 = new Label("Notenart:");
        Label label2 = new Label("Note:");

        Button button1 = new Button("Speichern");
        button1.setOnAction(btn ->{
            if (!note.getText().matches("\\d*")) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Bitte nur numerische Zahlen im Feld \"Noten\"");
                alert2.showAndWait();
            }else {
                Note neueNote = new Note(notenArt.getText(), Integer.parseInt(note.getText()));
                a.getNoten().add(neueNote);
                notenTabelle.getItems().add(neueNote);
            }
        });

        Button button2 = new Button("Schließen");
        button2.setOnAction(btn ->{
            alert.close();
        });

        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(label1, 0, 0);
        grid.add(label2, 0, 1);

        grid.add(notenArt, 1, 0);
        grid.add(note, 1, 1);

        grid.add(button1, 0, 2);
        grid.add(button2, 1, 2);


        pane.setContent(grid);

        alert.setDialogPane(pane);

        alert.showAndWait();
    }

    private void newNotiz(FachDatensatz a, TableView<Notiz> notizenTabelle) {
        DialogPane pane = new DialogPane();
        GridPane grid = new GridPane();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        TextField uebertext = new TextField();

        TextField text = new TextField();


        Label label1 = new Label("Überschrift:");
        Label label2 = new Label("Text:");

        Button button1 = new Button("Speichern");
        button1.setOnAction(btn ->{

            Notiz neueNotiz = new Notiz(uebertext.getText(), text.getText());
            a.getNotizen().add(neueNotiz);
            notizenTabelle.getItems().add(neueNotiz);

        });

        Button button2 = new Button("Schließen");
        button2.setOnAction(btn ->{
            alert.close();
        });

        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(label1, 0, 0);
        grid.add(label2, 0, 1);

        grid.add(uebertext, 1, 0);
        grid.add(text, 1, 1);

        grid.add(button1, 0, 2);
        grid.add(button2, 1, 2);


        pane.setContent(grid);

        alert.setDialogPane(pane);

        alert.showAndWait();
    }

    private void newAufgabe(FachDatensatz a, TableView<Aufgabe> aufgabenTabelle) {
        System.out.println("Hesasdal");

        DialogPane pane = new DialogPane();
        GridPane grid = new GridPane();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        DatePicker datum = new DatePicker();

        HBox uhrzeit = new HBox();
        TextField stunden = new TextField();
        stunden.setPrefColumnCount(2);
        TextField minuten = new TextField();
        minuten.setPrefColumnCount(2);
        TextField sekunden = new TextField();
        sekunden.setPrefColumnCount(2);

        uhrzeit.getChildren().addAll(stunden, minuten, sekunden);

        TextField inhalt = new TextField();

        Label label1 = new Label("Datum:");
        Label label2 = new Label("Uhrzeit:");
        Label label3 = new Label("Inhalt:");

        Button button1 = new Button("Speichern");
        button1.setOnAction(btn ->{
            if (!sekunden.getText().matches("\\d*") || !minuten.getText().matches("\\d*") || !stunden.getText().matches("\\d*")) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Bitte nur numerische Zahlen in den Feldern \"Uhrzeit\"");
                alert2.showAndWait();
            }else {

                Aufgabe neueAufgabe = new Aufgabe(inhalt.getText(), new Datum(datum.getValue().getDayOfMonth(), datum.getValue().getMonthValue(), datum.getValue().getYear()), new Uhrzeit(Integer.parseInt(stunden.getText()), Integer.parseInt(minuten.getText()), Integer.parseInt(sekunden.getText())));
                a.getAufgaben().add(neueAufgabe);
                aufgabenTabelle.getItems().add(neueAufgabe);
            }
        });

        Button button2 = new Button("Schließen");
        button2.setOnAction(btn ->{
            alert.close();
        });

        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(label1, 0, 0);
        grid.add(label2, 0, 1);
        grid.add(label3, 0, 2);

        grid.add(datum, 1, 0);
        grid.add(uhrzeit, 1, 1);
        grid.add(inhalt, 1, 2);

        grid.add(button1, 0, 3);
        grid.add(button2, 1, 3);


        pane.setContent(grid);

        alert.setDialogPane(pane);

        alert.showAndWait();
    }
}
