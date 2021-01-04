package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class StundenplanViewController implements Initializable
{

    ObservableList<Doppelstunde> montagObservableList, dienstagObservableList, mittwochObservableList, donnerstagObservableList, freitagObservableList, samstagObservableList, ohneTagObservableList;


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
    private TableView<Doppelstunde> ohneTagTableView;

    @FXML
    private TableColumn<Doppelstunde, String> ohneTagTableColumn;

    @FXML
	private Button stundenplanZuruecksetzen;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Hier werden die für die sieben verschiedenen TableViews ObservableLists initialisiert.
        montagObservableList=FXCollections.observableArrayList();
        dienstagObservableList=FXCollections.observableArrayList();
        mittwochObservableList=FXCollections.observableArrayList();
        donnerstagObservableList=FXCollections.observableArrayList();
        freitagObservableList=FXCollections.observableArrayList();
        samstagObservableList=FXCollections.observableArrayList();


        //Im Folgenden wird ein Callback Objekt erstellt, welches jeder Column übergeben wird, sodass beim Hovern
        //über eine Zelle ihr ganzer Inhalt angezeigt wird.
        Callback<TableColumn.CellDataFeatures<Doppelstunde,String>,ObservableValue<String>> cellValueFactory = cellData ->
        {
            return new SimpleStringProperty(
                    ((cellData.getValue().getDatum() == null) ? "" : (cellData.getValue().getDatum()+" "))+cellData.getValue().getBeginn()+"-"+cellData.getValue().getEnde()+"\n"+
                            cellData.getValue().getRaum()+"\n"+
                            cellData.getValue().getName()+"\n"+
                            cellData.getValue().getDozent());
        };


        //Nun werden allen Tabellen ihre ObservableLists
        montagTableView.setItems(montagObservableList);
        kontextMenueHinzufuegen(montagTableView);
        montagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(montagTableColumn);

        dienstagTableView.setItems(dienstagObservableList);
        kontextMenueHinzufuegen(dienstagTableView);
        dienstagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(dienstagTableColumn);

        mittwochTableView.setItems(mittwochObservableList);
        kontextMenueHinzufuegen(mittwochTableView);
        mittwochTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(mittwochTableColumn);

        donnerstagTableView.setItems(donnerstagObservableList);
        kontextMenueHinzufuegen(donnerstagTableView);
        donnerstagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(donnerstagTableColumn);

        freitagTableView.setItems(freitagObservableList);
        kontextMenueHinzufuegen(freitagTableView);
        freitagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(freitagTableColumn);

        samstagTableView.setItems(samstagObservableList);
        kontextMenueHinzufuegen(samstagTableView);
        samstagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(samstagTableColumn);

        ohneTagTableView.setItems(ohneTagObservableList);
        kontextMenueHinzufuegen(ohneTagTableView);
        ohneTagTableColumn.setCellValueFactory(cellValueFactory);
        tooltipZuZelleHinzufuegen(ohneTagTableColumn);


        stundenplanHBox.getChildren().remove(samstagTableView);
        stundenplanHBox.getChildren().remove(ohneTagTableView);

        stundenplanZuruecksetzungProgressIndicator.progressProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(newValue.doubleValue()==1)
            {
                stundenplanLaden();

                stundenplanZuruecksetzen.setDisable(false);
            }
        }));


        stundenplanLaden();
    }

    @FXML
    public void stundenplanZuruecksetzen(ActionEvent actionEvent)
    {
        Datenabrufer.setProgressIndicator(stundenplanZuruecksetzungProgressIndicator);
        Datenabrufer.stundenplanAbrufen();
        stundenplanZuruecksetzen.setDisable(true);
    }

    @FXML
    public void doppelstundeHinzufuegen(ActionEvent actionEvent)
    {

        oeffneDoppelstundeDialog("Stunde hinzufügen","Hinzufügen","", "", "", Tag.MONTAG, 0, 0, 0, 0)
        .ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().add(item));
        stundenplanLaden();
    }

    private void stundenplanLaden()
    {
        montagObservableList.clear();
        dienstagObservableList.clear();
        mittwochObservableList.clear();
        donnerstagObservableList.clear();
        freitagObservableList.clear();
        samstagObservableList.clear();

		stundenplanHBox.getChildren().remove(samstagTableView);
		stundenplanHBox.getChildren().remove(ohneTagTableView);

        Comparator<Doppelstunde> doppelstundeComparator=(o1, o2) -> o1.getBeginn().compareTo(o2.getBeginn());

        SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(item ->
        {
            if (item.getTag() == null)
            {
                stundenplanHBox.getChildren().add(ohneTagTableView);
                ohneTagObservableList.add(item);
                ohneTagObservableList.sort(doppelstundeComparator);
            }
            else
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
            }
        });

        montagTableView.refresh();
        dienstagTableView.refresh();
        mittwochTableView.refresh();
        donnerstagTableView.refresh();
        freitagTableView.refresh();
        samstagTableView.refresh();
    }

    private Optional<Doppelstunde> oeffneDoppelstundeDialog(String fensterTitel, String buttonTitel, String namePrompt, String dozentPrompt, String raumPrompt, Tag tagPrompt, int beginnStundeUhrzeitPrompt, int beginnMinuteUhrzeitPrompt, int endeStundeUhrzeitPrompt, int endeMinuteUhrzeitPrompt)
    {
        DialogPane dialogPane=new DialogPane();
        try
        {
            dialogPane.setContent(FXMLLoader.load(getClass().getResource("../../View/DoppelstundenHinzufuegeView.fxml")));
        }
        catch (IOException ignored){}
        dialogPane.setMinSize(300, 200);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
        ((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);

        TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
        TextField dozentTextField=(TextField) dialogPane.lookup("#dozentTextField");
        TextField raumTextField=(TextField) dialogPane.lookup("#raumTextField");
        Bindings.createBooleanBinding(() ->
                nameTextField.getText().trim().isEmpty(), nameTextField.textProperty())
                .or(Bindings.createBooleanBinding(() -> dozentTextField.getText().trim().isEmpty(), dozentTextField.textProperty()))
                .or(Bindings.createBooleanBinding(() -> raumTextField.getText().trim().isEmpty(), raumTextField.textProperty()))
                .addListener(((observable, oldValue, newValue) ->
                {
                    dialogPane.lookupButton(ButtonType.OK).setDisable(newValue);
                }));
        nameTextField.setText(namePrompt);
        dozentTextField.setText(dozentPrompt);
        raumTextField.setText(raumPrompt);

        ChoiceBox<Tag> tagChoiceBox=(ChoiceBox<Tag>) dialogPane.lookup("#tagChoiceBox");
        tagChoiceBox.setItems(FXCollections.observableArrayList(List.of(Tag.values())));
        tagChoiceBox.getSelectionModel().select(0);
        tagChoiceBox.setConverter(new StringConverter<Tag>()
        {
            @Override
            public String toString(Tag object)
            {
                return object.toString().substring(0, 1).toUpperCase() + object.toString().substring(1).toLowerCase();
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });
        tagChoiceBox.setValue(tagPrompt);

        Spinner<Integer> beginnStundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#beginnStundeUhrzeitSpinner");
        beginnStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, beginnStundeUhrzeitPrompt));

        Spinner<Integer> beginnMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#beginnMinuteUhrzeitSpinner");
        beginnMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, beginnMinuteUhrzeitPrompt));

        Spinner<Integer> endeStundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeStundeUhrzeitSpinner");
        endeStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, endeStundeUhrzeitPrompt));

        Spinner<Integer> endeMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeMinuteUhrzeitSpinner");
        endeMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, endeMinuteUhrzeitPrompt));

        Dialog<Doppelstunde> dialog=new Dialog<>();
        dialog.setTitle(fensterTitel);
        dialog.setDialogPane(dialogPane);
        dialog.setResultConverter((dialogButton)->
        {
            if(dialogButton.getButtonData().isCancelButton())
            {
                return null;
            }
            else
            {
                return new Doppelstunde(
                        null,
                        nameTextField.getText().trim(),
                        dozentTextField.getText().trim(),
                        raumTextField.getText().trim(),
                        tagChoiceBox.getValue(),
                        new Uhrzeit(
                                beginnStundeUhrzeitSpinner.getValue(),
                                beginnMinuteUhrzeitSpinner.getValue()
                        ),
                        new Uhrzeit(
                                endeStundeUhrzeitSpinner.getValue(),
                                endeMinuteUhrzeitSpinner.getValue()
                        )
                );
            }
        });
        dialog.initOwner(Main.getPrimaryStage());

        return dialog.showAndWait();
    }

    private void kontextMenueHinzufuegen(TableView<Doppelstunde> tableView)
    {
        ContextMenu contextMenu=new ContextMenu();

        MenuItem loeschenMenuItem=new MenuItem("Löschen");
        loeschenMenuItem.setOnAction((actionEvent)->
        {
            SchreiberLeser.getNutzerdaten().getDoppelstunden().remove(tableView.getSelectionModel().getSelectedItem());
            stundenplanLaden();
        });
        contextMenu.getItems().add(loeschenMenuItem);

        MenuItem aendernMenuItem=new MenuItem("Ändern");
        aendernMenuItem.setOnAction((actionEvent)->
        {
            oeffneDoppelstundeDialog(
                "Stunde ändern",
                "Ändern",
                tableView.getSelectionModel().getSelectedItem().getName(),
                tableView.getSelectionModel().getSelectedItem().getDozent(),
                tableView.getSelectionModel().getSelectedItem().getRaum(),
                tableView.getSelectionModel().getSelectedItem().getTag(),
                tableView.getSelectionModel().getSelectedItem().getBeginn().getStunde(),
                tableView.getSelectionModel().getSelectedItem().getBeginn().getMinute(),
                tableView.getSelectionModel().getSelectedItem().getEnde().getStunde(),
                tableView.getSelectionModel().getSelectedItem().getEnde().getMinute()
            )
            .ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().set(SchreiberLeser.getNutzerdaten().getDoppelstunden().indexOf(tableView.getSelectionModel().getSelectedItem()), item));
            stundenplanLaden();
        });
        contextMenu.getItems().add(aendernMenuItem);

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->
        {
            if(mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                contextMenu.show(tableView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });
    }

    private <T> void tooltipZuZelleHinzufuegen(TableColumn<Doppelstunde,T> column)
    {

        Callback<TableColumn<Doppelstunde, T>, TableCell<Doppelstunde,T>> aktuelleCellFactory=column.getCellFactory();

        column.setCellFactory((tableColumn)->
        {
            TableCell<Doppelstunde, T> tableCell =  aktuelleCellFactory.call(tableColumn);

            Tooltip tooltip = new Tooltip();
            tooltip.textProperty().bind(tableCell.itemProperty().asString());
            tableCell.setTooltip(tooltip);

            return tableCell ;
        });
    }
}
