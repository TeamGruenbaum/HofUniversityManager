package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.net.URL;
import java.util.*;
import java.util.List;

import static Model.Tag.*;

public class StundenplanViewController implements Initializable
{

    ObservableList<Doppelstunde> montagObservableList, dienstagObservableList, mittwochObservableList, donnerstagObservableList, freitagObservableList, samstagObservableList;

    @FXML
    public BorderPane allesBorderPane;


    @FXML
    public ProgressIndicator stundenplanZuruecksetzungProgressIndicator;


    @FXML
    private HBox stundenplanHBox;


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
    private TableView<Doppelstunde> samstagTableView;

    @FXML
    private TableColumn<Doppelstunde, String> samstagTableColumn;


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
        montagObservableList=FXCollections.observableArrayList();
        dienstagObservableList=FXCollections.observableArrayList();
        mittwochObservableList=FXCollections.observableArrayList();
        donnerstagObservableList=FXCollections.observableArrayList();
        freitagObservableList=FXCollections.observableArrayList();
        samstagObservableList=FXCollections.observableArrayList();

        //Datenabrufer.stundenplanAbrufen();

        Callback<TableColumn.CellDataFeatures<Doppelstunde,String>,ObservableValue<String>> cellValueFactory = cellData ->
        {
            return new SimpleStringProperty(
                    cellData.getValue().getBeginn()+"-"+cellData.getValue().getEnde()+", "+cellData.getValue().getRaum()+"\n"+
                            cellData.getValue().getName()+"\n"+
                            cellData.getValue().getDozent());
        };

        montagTableView.setItems(montagObservableList);
        montagTableColumn.setCellValueFactory(cellValueFactory);
        addTooltipToColumnCells(montagTableColumn);

        dienstagTableView.setItems(dienstagObservableList);
        dienstagTableColumn.setCellValueFactory(cellValueFactory);


        mittwochTableView.setItems(mittwochObservableList);
        mittwochTableColumn.setCellValueFactory(cellValueFactory);


        donnerstagTableView.setItems(donnerstagObservableList);
        donnerstagTableColumn.setCellValueFactory(cellValueFactory);


        freitagTableView.setItems(freitagObservableList);
        freitagTableColumn.setCellValueFactory(cellValueFactory);

        samstagTableView.setItems(samstagObservableList);
        samstagTableColumn.setCellValueFactory(cellValueFactory);

        stundenplanHBox.getChildren().remove(samstagTableView);

        Datenabrufer.setProgressIndicator(stundenplanZuruecksetzungProgressIndicator);

        stundenplanZuruecksetzungProgressIndicator.progressProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(newValue.doubleValue()==1)
            {
                montagObservableList.clear();
                dienstagObservableList.clear();
                mittwochObservableList.clear();
                donnerstagObservableList.clear();
                freitagObservableList.clear();
                samstagObservableList.clear();

                Comparator<Doppelstunde> doppelstundeComparator=new Comparator<Doppelstunde>()
                {
                    @Override
                    public int compare(Doppelstunde o1, Doppelstunde o2)
                    {
                        return o1.getBeginn().compareTo(o2.getBeginn());
                    }
                };

                SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(item ->
                {
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
                        case SAMSTAG:
                        {
                            stundenplanHBox.getChildren().add(samstagTableView);
                            samstagObservableList.add(item);
                            samstagObservableList.sort(doppelstundeComparator);
                        } break;
                    }
                });


                montagTableView.refresh();
                dienstagTableView.refresh();
                mittwochTableView.refresh();
                donnerstagTableView.refresh();
                freitagTableView.refresh();
                samstagTableView.refresh();

                System.out.println("HAlllo");
            }
        }));
    }

    //???
    private <T> void addTooltipToColumnCells(TableColumn<Doppelstunde,T> column) {

        Callback<TableColumn<Doppelstunde, T>, TableCell<Doppelstunde,T>> existingCellFactory=column.getCellFactory();

        column.setCellFactory(c->
        {
            TableCell<Doppelstunde, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            tooltip.textProperty().bind(cell.itemProperty().asString());

            cell.setTooltip(tooltip);
            return cell ;
        });
    }

    //###########################

    @FXML
    public void stundenplanZuruecksetzen(ActionEvent actionEvent)
    {
        Datenabrufer.stundenplanAbrufen();
    }

    @FXML
    public void doppelstundeHinzufuegen(ActionEvent actionEvent)
    {
        DialogPane dialogPane=new DialogPane();
        try
        {
            dialogPane.setContent((GridPane) FXMLLoader.load(getClass().getResource("../../View/DoppelstundenHinzufuegeView.fxml")));
        }
        catch (IOException ignored){}
        dialogPane.setMinSize(300, 200);
        dialogPane.getButtonTypes().add(ButtonType.OK);
        ((Button) dialogPane.lookupButton(ButtonType.OK)).setText("Hinzufügen");
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);

        TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
        dialogTextfieldlistenerHinzufuegen(dialogPane, nameTextField);

        TextField dozentTextField=(TextField) dialogPane.lookup("#dozentTextField");
        dialogTextfieldlistenerHinzufuegen(dialogPane, dozentTextField);

        TextField raumTextField=(TextField) dialogPane.lookup("#raumTextField");
        dialogTextfieldlistenerHinzufuegen(dialogPane, raumTextField);

        //s.createBooleanBinding(() -> nameTextField.getText().trim().isEmpty() || , nameTextField.textProperty());

        ChoiceBox<Tag> tagChoiceBox=(ChoiceBox) dialogPane.lookup("#tagChoiceBox");
        tagChoiceBox.setItems(FXCollections.observableArrayList(List.of(Tag.values())));
        tagChoiceBox.getSelectionModel().select(0);
        tagChoiceBox.setConverter(new StringConverter<Tag>() {
            @Override
            public String toString(Tag object) {
                return object.toString().substring(0, 1).toUpperCase() + object.toString().substring(1).toLowerCase();
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });

        Spinner<Integer> beginnStundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#beginnStundeUhrzeitSpinner");
        beginnStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

        Spinner<Integer> beginnMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#beginnMinuteUhrzeitSpinner");
        beginnMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Calendar.getInstance().get(Calendar.MINUTE)));

        Spinner<Integer> endeStundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeStundeUhrzeitSpinner");
        endeStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

        Spinner<Integer> endeMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeMinuteUhrzeitSpinner");
        endeMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Calendar.getInstance().get(Calendar.MINUTE)));

        Dialog<Doppelstunde> dialog=new Dialog<>();
        dialog.setTitle("Stunde hinzufügen");
        dialog.setDialogPane(dialogPane);
        dialog.setResultConverter((dialogButton)->
        {
            return new Doppelstunde(
                    nameTextField.getText(),
                    dozentTextField.getText(),
                    raumTextField.getText(),
                    tagChoiceBox.getValue(),
                    new Uhrzeit(
                            beginnStundeUhrzeitSpinner.getValue(),
                            beginnStundeUhrzeitSpinner.getValue()
                    ),
                    new Uhrzeit(
                            endeStundeUhrzeitSpinner.getValue(),
                            endeStundeUhrzeitSpinner.getValue()
                    )
            );
        });
        dialog.initOwner(Main.getPrimaryStage());
        dialog.setOnCloseRequest((windowEvent)->{return;});

        Optional<Doppelstunde> optionalDoppelstunde=dialog.showAndWait();


    }

    private void dialogTextfieldlistenerHinzufuegen(DialogPane dialogPane, TextField textField)
    {
        textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");

        textField.textProperty().addListener((observable, oldValue, newValue)->
        {
            if(textField.getText().trim().compareTo("")==0)
            {
                textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            }
            else
            {
                textField.setStyle("");
            }
        });
    }
}
