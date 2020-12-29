package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StundenplanViewController implements Initializable {


    ObservableList<Doppelstunde> MontagStunden, DienstagStunden, MittwochStunden, DonnerstagStunden, FreitagStunden;

    Nutzerdaten nutzer = SchreiberLeser.getNutzerdaten();

    ArrayList<FachDatensatz> fachdaten;

    @FXML
    TableColumn<GridPane, GridPane> montagCol;

    @FXML
    TableColumn<GridPane, GridPane> dienstagCol;

    @FXML
    TableColumn<GridPane, GridPane> mittwochCol;

    @FXML
    TableColumn<GridPane, GridPane> donnerstagCol;

    @FXML
    TableColumn<GridPane, GridPane> freitagCol;

    @FXML
    Accordion faecherSelect;

    @FXML
    TableColumn<Aufgabe, Datum> datumCol;

    @FXML
    TableColumn<Aufgabe, Uhrzeit> uhrzeitCol;

    @FXML
    TableColumn<Aufgabe, String> inhaltCol;

    @FXML
    TableColumn<Notiz, String> notizenCol;

    @FXML
    TableColumn<Note, String> artCol;

    @FXML
    TableColumn<Note, Integer> notenCol;

    @FXML
    Button fachHinzu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stundenRefresh(nutzer);



        nutzer.getDoppelstunden().forEach(a ->{
            if(!fachdaten.contains(a.getFachDatensatz())){
                fachdaten.add(a.getFachDatensatz());
            }
        });

        fachdaten.forEach(this::erzeugeAkkordion);



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

        datumCol.setCellValueFactory(new PropertyValueFactory<>("datum"));
        inhaltCol.setCellValueFactory(new PropertyValueFactory<>("inhalt"));
        uhrzeitCol.setCellValueFactory(new PropertyValueFactory<>("zeit"));

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

    private void stundenRefresh(Nutzerdaten nutzer) {
        ArrayList<Doppelstunde> alleStunden = nutzer.getDoppelstunden();
        alleStunden.forEach(a -> {
            fachEinfuegen(a, a.getTag());
        });
    }

    private void fachEinfuegen(Doppelstunde doppelstunde, Tag tag){
        boolean gefunden = false;
        int index = 0;
        ObservableList<Doppelstunde> list;
        switch(tag){
            case MONTAG: list = MontagStunden; break;
            case DIENSTAG: list = DienstagStunden; break;
            case MITTWOCH: list = MittwochStunden; break;
            case DONNERSTAG: list = DonnerstagStunden; break;
            case FREITAG: list = FreitagStunden; break;
            default:
                throw new IllegalStateException("Unexpected value: " + tag);
        }

        while(!gefunden){
            if(list.get(index) == null){
                list.add(doppelstunde);
            }else if(doppelstunde.getBeginn().compareTo(list.get(index).getBeginn()) > 0) {
                index++;
            }else{
                list.add(index, doppelstunde);
                gefunden = true;
            }
        }
    }

    private void neueDoppelstunde(Nutzerdaten nutzerdaten){


        DialogPane pane = new DialogPane();
        GridPane grid = new GridPane();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);



        HBox uhrzeitA = new HBox();

        TextField stundenA = new TextField();
        stundenA.setPrefColumnCount(2);
        TextField minutenA = new TextField();
        minutenA.setPrefColumnCount(2);
        TextField sekundenA = new TextField();
        sekundenA.setPrefColumnCount(2);

        uhrzeitA.getChildren().addAll(stundenA, minutenA, sekundenA);

        HBox uhrzeitB = new HBox();

        TextField stundenB = new TextField();
        stundenB.setPrefColumnCount(2);
        TextField minutenB = new TextField();
        minutenB.setPrefColumnCount(2);
        TextField sekundenB = new TextField();
        sekundenB.setPrefColumnCount(2);

        uhrzeitB.getChildren().addAll(stundenB, minutenB, sekundenB);

        TextField name = new TextField();
        TextField dozent = new TextField();
        TextField raum = new TextField();

        //TODO - TAG Einfügen
        ChoiceBox<Tag> tag = new ChoiceBox<>();
        tag.getItems().addAll(Tag.MONTAG, Tag.DIENSTAG, Tag.MITTWOCH, Tag.DONNERSTAG, Tag.FREITAG);

        Label label1 = new Label("Name");
        Label label2 = new Label("Dozent");
        Label label3 = new Label("Raum:");
        Label label4 = new Label("Beginn:");
        Label label5 = new Label ("Ende:");
        Label label6 = new Label("Tag");

        Button button1 = new Button("Speichern");
        button1.setOnAction(btn ->{
            if (!sekundenA.getText().matches("\\d*") || !minutenA.getText().matches("\\d*") || !stundenA.getText().matches("\\d*") || !sekundenB.getText().matches("\\d*") || !minutenB.getText().matches("\\d*") || !stundenB.getText().matches("\\d*")) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Bitte nur numerische Zahlen als Zeiten!");
                alert2.showAndWait();
            }else {

                Doppelstunde neueStunde = new Doppelstunde(name.getText(), dozent.getText(), raum.getText(), tag.getValue(), new Uhrzeit(Integer.parseInt(stundenA.getText()), Integer.parseInt(minutenA.getText()), Integer.parseInt(sekundenA.getText())), new Uhrzeit(Integer.parseInt(stundenB.getText()), Integer.parseInt(minutenB.getText()), Integer.parseInt(sekundenB.getText())));

                nutzerdaten.getDoppelstunden().add(neueStunde);

                fachEinfuegen(neueStunde, neueStunde.getTag());

            }
        });

        Button button2 = new Button("Zurück");
        button2.setOnAction(btn ->{
            alert.close();
        });

        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(label1, 0, 0);
        grid.add(label2, 0, 1);
        grid.add(label3, 0, 2);
        grid.add(label4, 0, 3);
        grid.add(label5, 0, 4);
        grid.add(label6, 0, 5);


        grid.add(name, 1, 0);
        grid.add(dozent, 1, 1);
        grid.add(raum, 1, 2);
        grid.add(uhrzeitA, 1, 3);
        grid.add(uhrzeitB, 1, 4);
        grid.add(tag, 1, 5);


        grid.add(button1, 0, 6);
        grid.add(button2, 1, 6);


        pane.setContent(grid);

        alert.setDialogPane(pane);

        alert.showAndWait();


    }


}
