package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
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
	//Alles
	public BorderPane allesBorderPane;

	//Tab Fächer
	@FXML private TableView<Aufgabe> aufgabenTableView;
	@FXML private TableColumn<Aufgabe, String> aufgabenNameTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenInhaltTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenDatumTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenZeitTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenFachTableColumn;

	@FXML private TableView<Notiz> notizenTableView;
	@FXML private TableColumn<Notiz, String> notizenNameTableColumn;
	@FXML private TableColumn<Notiz, String> notizenInhaltTableColumn;
	@FXML private TableColumn<Notiz, String> notizenFachTableColumn;
	private ObservableList<Notiz> notizObservableList;

	@FXML private TableView<Note> notenTableView;
	@FXML private TableColumn<Note, String> notenNoteTableColumn;
	@FXML private TableColumn<Note, String> notenArtTableColumn;
	@FXML private TableColumn<Note, String> notenBemerkungTableColumn;
	@FXML private TableColumn<Note, String> notenFachTableColumn;
	@FXML private TabPane faecherTabPane;


	//Tab Stundenplan
	@FXML private HBox stundenplanHBox;

	@FXML private TableView<Doppelstunde> montagTableView;
	@FXML private TableColumn<Doppelstunde, String> montagTableColumn;
	private ObservableList<Doppelstunde> montagObservableList;

	@FXML private TableView<Doppelstunde> dienstagTableView;
	@FXML private TableColumn<Doppelstunde, String> dienstagTableColumn;
	private ObservableList<Doppelstunde> dienstagObservableList;

	@FXML private TableView<Doppelstunde> mittwochTableView;
	@FXML private TableColumn<Doppelstunde, String> mittwochTableColumn;
	private ObservableList<Doppelstunde> mittwochObservableList;

	@FXML private TableView<Doppelstunde> donnerstagTableView;
	@FXML private TableColumn<Doppelstunde, String> donnerstagTableColumn;
	private ObservableList<Doppelstunde> donnerstagObservableList;

	@FXML private TableView<Doppelstunde> freitagTableView;
	@FXML private TableColumn<Doppelstunde, String> freitagTableColumn;
	private ObservableList<Doppelstunde> freitagObservableList;

	@FXML private TableView<Doppelstunde> samstagTableView;
	@FXML private TableColumn<Doppelstunde, String> samstagTableColumn;
	private ObservableList<Doppelstunde> samstagObservableList;

	@FXML private TableView<Doppelstunde> ohneTagTableView;
	@FXML private TableColumn<Doppelstunde, String> ohneTagTableColumn;
	private ObservableList<Doppelstunde> ohneTagObservableList;

	@FXML private ProgressIndicator stundenplanZuruecksetzungProgressIndicator;
	@FXML private Button stundenplanZuruecksetzen;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		//Alle
		BiConsumer<ActionEvent, TableView<Doppelstunde>> aendernConsumer=(actionEvent, tableView)->
		{
			oeffneDoppelstundeDialog("Stunde ändern",
				"Ändern",
				tableView.getSelectionModel().getSelectedItem().getName(),
				tableView.getSelectionModel().getSelectedItem().getDozent(),
				tableView.getSelectionModel().getSelectedItem().getRaum(),
				tableView.getSelectionModel().getSelectedItem().getTag(),
				tableView.getSelectionModel().getSelectedItem().getBeginn().getStunde(),
				tableView.getSelectionModel().getSelectedItem().getBeginn().getMinute(),
				tableView.getSelectionModel().getSelectedItem().getEnde().getStunde(),
				tableView.getSelectionModel().getSelectedItem().getEnde().getMinute())
				.ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().set(SchreiberLeser.getNutzerdaten().getDoppelstunden().indexOf(tableView.getSelectionModel().getSelectedItem()), item));
			stundenplanLaden();
		};

		BiConsumer<ActionEvent, TableView<Doppelstunde>> loeschConsumer=(actionEvent, tableView)->
		{
			SchreiberLeser.getNutzerdaten().getDoppelstunden().remove(tableView.getSelectionModel().getSelectedItem());
			stundenplanLaden();
		};

		//Stundenplan
		montagObservableList=FXCollections.observableArrayList();
		dienstagObservableList=FXCollections.observableArrayList();
		mittwochObservableList=FXCollections.observableArrayList();
		donnerstagObservableList=FXCollections.observableArrayList();
		freitagObservableList=FXCollections.observableArrayList();
		samstagObservableList=FXCollections.observableArrayList();
		ohneTagObservableList=FXCollections.observableArrayList();

		Callback<TableColumn.CellDataFeatures<Doppelstunde, String>, ObservableValue<String>> cellValueFactory=cellData->
		{
			return new SimpleStringProperty(((cellData.getValue().getDatum()==null)?""
																				   :(cellData.getValue().getDatum()+" "))+cellData.getValue().getBeginn()+"-"+cellData.getValue().getEnde()+"\n"+cellData.getValue().getRaum()+"\n"+cellData.getValue().getName()+"\n"+cellData.getValue().getDozent());
		};

		tableViewInitialisieren(montagTableView, montagTableColumn, montagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(dienstagTableView, dienstagTableColumn, dienstagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(mittwochTableView, mittwochTableColumn, mittwochObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(donnerstagTableView, donnerstagTableColumn, donnerstagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(freitagTableView, freitagTableColumn, freitagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(samstagTableView, samstagTableColumn, samstagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);
		tableViewInitialisieren(ohneTagTableView, ohneTagTableColumn, ohneTagObservableList, cellValueFactory, aendernConsumer, loeschConsumer);

		stundenplanHBox.getChildren().remove(samstagTableView);
		stundenplanHBox.getChildren().remove(ohneTagTableView);

		stundenplanZuruecksetzungProgressIndicator.progressProperty().addListener(((observable, oldValue, newValue)->
		{
			if(newValue.doubleValue()==1)
			{
				stundenplanLaden();

				stundenplanZuruecksetzen.setDisable(false);
			}
		}));

		stundenplanLaden();

		//Fächer
		notenTableView.setItems(FXCollections.observableArrayList());
		notenBemerkungTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getBemerkung());});
		notenNoteTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(Integer.toString(cellData.getValue().getNote()));});
		notenArtTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getArt());});
		notenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});

		aufgabenNameTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getName());});
		aufgabenInhaltTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getInhalt());});
		aufgabenZeitTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getZeit().toString());});
		aufgabenDatumTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getDatum().toString());});
		aufgabenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});


		notizObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFachDatensatz().getNotizen());
		notizenTableView.setItems(notizObservableList);
		aendernLoeschenKontextmenueHinzufuegen(notizenTableView, (actionEvent, notizTableView)->
		{
			oeffneNotizDialog("Notiz ändern", "Ändern",
				notizTableView.getSelectionModel().getSelectedItem().getUeberschrift(),
				notizTableView.getSelectionModel().getSelectedItem().getInhalt(),
				notizTableView.getSelectionModel().getSelectedItem().getFach())
				.ifPresent((item)->
				{
					SchreiberLeser.getNutzerdaten().getFachDatensatz().getNotizen().set(notizTableView.getSelectionModel().getSelectedIndex(), item);
					notizObservableList.set(notizTableView.getSelectionModel().getSelectedIndex(), item);
					notizenTableView.refresh();
				});
		},
		(actionEvent, notizTableView)->
		{
			SchreiberLeser.getNutzerdaten().getFachDatensatz().getNotizen().remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizObservableList.remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizenTableView.refresh();
		});
		notizenNameTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getUeberschrift());});
		notizenInhaltTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getInhalt());});
		notizenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});
	}

	//Stundenplan
	@FXML private void stundenplanZuruecksetzen(ActionEvent actionEvent)
	{
		Datenabrufer.setProgressIndicator(stundenplanZuruecksetzungProgressIndicator);
		Datenabrufer.stundenplanAbrufen();
		stundenplanZuruecksetzen.setDisable(true);
	}

	@FXML private void doppelstundeHinzufuegen(ActionEvent actionEvent)
	{
		oeffneDoppelstundeDialog("Stunde hinzufügen", "Hinzufügen", "", "", "", Tag.MONTAG, 0, 0, 0, 0).ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().add(item));
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

		Comparator<Doppelstunde> doppelstundeComparator=(o1, o2)->o1.getBeginn().compareTo(o2.getBeginn());

		SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(item->
		{
			if(item.getTag()==null)
			{
				stundenplanHBox.getChildren().add(ohneTagTableView);
				ohneTagObservableList.add(item);
				ohneTagObservableList.sort(doppelstundeComparator);
			}else
			{
				switch(item.getTag())
				{
					case MONTAG:
					{
						montagObservableList.add(item);
						montagObservableList.sort(doppelstundeComparator);
					}
					break;
					case DIENSTAG:
					{
						dienstagObservableList.add(item);
						dienstagObservableList.sort(doppelstundeComparator);
					}
					break;
					case MITTWOCH:
					{
						mittwochObservableList.add(item);
						mittwochObservableList.sort(doppelstundeComparator);
					}
					break;
					case DONNERSTAG:
					{
						donnerstagObservableList.add(item);
						donnerstagObservableList.sort(doppelstundeComparator);
					}
					break;
					case FREITAG:
					{
						freitagObservableList.add(item);
						freitagObservableList.sort(doppelstundeComparator);
					}
					break;
					case SAMSTAG:
					{
						stundenplanHBox.getChildren().add(samstagTableView);
						samstagObservableList.add(item);
						samstagObservableList.sort(doppelstundeComparator);
					}
					break;
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
		}catch(IOException ignored)
		{
		}
		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
		TextField dozentTextField=(TextField) dialogPane.lookup("#dozentTextField");
		TextField raumTextField=(TextField) dialogPane.lookup("#raumTextField");
		Bindings.createBooleanBinding(()->nameTextField.getText().trim().isEmpty(), nameTextField.textProperty()).or(Bindings.createBooleanBinding(()->dozentTextField.getText().trim().isEmpty(), dozentTextField.textProperty())).or(Bindings.createBooleanBinding(()->raumTextField.getText().trim().isEmpty(), raumTextField.textProperty())).addListener(((observable, oldValue, newValue)->
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

				return object.toString().substring(0, 1).toUpperCase()+object.toString().substring(1).toLowerCase();
			}

			@Override
			public Tag fromString(String string)
			{

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
			}else
			{
				return new Doppelstunde(null, nameTextField.getText().trim(), dozentTextField.getText().trim(), raumTextField.getText().trim(), tagChoiceBox.getValue(), new Uhrzeit(beginnStundeUhrzeitSpinner.getValue(), beginnMinuteUhrzeitSpinner.getValue()), new Uhrzeit(endeStundeUhrzeitSpinner.getValue(), endeMinuteUhrzeitSpinner.getValue()));
			}
		});
		dialog.initOwner(Main.getPrimaryStage());

		return dialog.showAndWait();
	}

	private void tableViewInitialisieren(TableView<Doppelstunde> tableView, TableColumn<Doppelstunde, String> tableColumn, ObservableList<Doppelstunde> observableList, Callback<TableColumn.CellDataFeatures<Doppelstunde, String>, ObservableValue<String>> callback, BiConsumer<ActionEvent, TableView<Doppelstunde>> aendernConsumer, BiConsumer<ActionEvent, TableView<Doppelstunde>> loeschConsumer)
	{
		tableView.setItems(observableList);
		aendernLoeschenKontextmenueHinzufuegen(montagTableView, aendernConsumer, loeschConsumer);
		tableColumn.setCellValueFactory(callback);
		tooltipZuZelleHinzufuegen(tableColumn);
	}

	//Faecher
	@FXML private void aufgabeNotizNoteHinzufuegen(ActionEvent actionEvent)
	{
		switch(faecherTabPane.getSelectionModel().getSelectedIndex())
		{
			case 0->oeffneAufgabenDialog("Aufgaben hinzufügen", "Hinzufügen", "","").ifPresent((item)->
			{
				/*aufgabenObservableList.add(item);
				SchreiberLeser.getNutzerdaten().getFachDatensatz().getAufgaben().add(item);

				aufgabenTableView.refresh();*/
			});
			case 1->oeffneNotizDialog("Notiz hinzufügen", "Hinzufügen", "","", SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getFachDatensatz().getNotizen().add(item);
				notizObservableList.add(item);
				notizenTableView.refresh();
			});
			case 2->oeffneNotenDialog("Note hinzufügen", "Hinzufügen", "","").ifPresent((item)->
			{
				/*notenObservableList.add(item);
				SchreiberLeser.getNutzerdaten().getFachDatensatz().getNoten().add(item);

				notenTableView.refresh();*/
			});
		}
	}

	private Optional<Aufgabe> oeffneAufgabenDialog(String fensterTitel, String buttonTitel, String namePrompt,String inhaltPrompt)
	{
		return null;
	}

	private Optional<Notiz> oeffneNotizDialog(String fensterTitel, String buttonTitel, String ueberschriftPrompt, String inhaltPrompt, String fachPrompt)
	{
		DialogPane dialogPane=new DialogPane();
		try
		{
			dialogPane.setContent(FXMLLoader.load(getClass().getResource("../../View/NotizHinzufuegeView.fxml")));
		}catch(IOException ignored){}
		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
		TextArea inhaltTextArea=(TextArea) dialogPane.lookup("#inhaltTextField");
		Bindings.createBooleanBinding(
			()->nameTextField.getText().trim().isEmpty(), nameTextField.textProperty())
			.or(Bindings.createBooleanBinding(()->inhaltTextArea.getText().trim().isEmpty(), inhaltTextArea.textProperty()))
			.addListener((observable, oldValue, newValue) ->
		{
			dialogPane.lookupButton(ButtonType.OK).setDisable(newValue);
		});
		nameTextField.setText(ueberschriftPrompt);
		inhaltTextArea.setText(inhaltPrompt);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) dialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachPrompt, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachPrompt);

		Button fachHinzufuegeButton=(Button) dialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			TextInputDialog textInputDialog=new TextInputDialog();
			textInputDialog.showAndWait().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschButton=(Button) dialogPane.lookup("#fachLoeschButton");
		fachLoeschButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Notiz> dialog=new Dialog<>();
		dialog.setTitle(fensterTitel);
		dialog.setDialogPane(dialogPane);
		dialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}else
			{
				return new Notiz(nameTextField.getText().trim(), inhaltTextArea.getText().trim(),
					faecherChoiceBox.getValue());
			}
		});
		dialog.initOwner(Main.getPrimaryStage());

		return dialog.showAndWait();
	}

	private Optional<Note> oeffneNotenDialog(String fensterTitel, String buttonTitel, String namePrompt, String inhaltPrompt)
	{
		return null;
	}

	private void fachnameHinzufuegen(String item, ChoiceBox<String> choiceBox)
	{
		SchreiberLeser.getNutzerdaten().getFaecher().add(item);
		SchreiberLeser.getNutzerdaten().setFaecher(new ArrayList<String>(SchreiberLeser.getNutzerdaten().getFaecher().stream().distinct().collect(Collectors.toList())));
		choiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		choiceBox.getSelectionModel().select(item);
	}

	private void fachnameLoeschen(ChoiceBox<String> choiceBox)
	{
		SchreiberLeser.getNutzerdaten().getFaecher().remove(choiceBox.getValue());
		choiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		choiceBox.getSelectionModel().select(0);
	}

	//Alle
	private <S> void aendernLoeschenKontextmenueHinzufuegen(TableView<S> tableView,
										 BiConsumer<ActionEvent, TableView<S>> aendernConsumer,
										 BiConsumer<ActionEvent, TableView<S>> loeschenConsumer)
	{
		ContextMenu contextMenu=new ContextMenu();
		contextMenu.setAutoHide(true);

		MenuItem aendernMenuItem=new MenuItem("Ändern");
		aendernMenuItem.setOnAction((actionEvent)->
		{
			aendernConsumer.accept(actionEvent, tableView);
		});
		contextMenu.getItems().add(aendernMenuItem);

		MenuItem loeschenMenuItem=new MenuItem("Löschen");
		loeschenMenuItem.setOnAction((actionEvent)->
		{
			loeschenConsumer.accept(actionEvent, tableView);
		});
		contextMenu.getItems().add(loeschenMenuItem);

		tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->
		{
			if(mouseEvent.getButton()==MouseButton.SECONDARY)
			{
				contextMenu.show(tableView, mouseEvent.getScreenX(),
				mouseEvent.getScreenY());
			}
			else
			{
				contextMenu.hide();
			}
		});
	}

	private <T> void tooltipZuZelleHinzufuegen(TableColumn<Doppelstunde, T> column)
	{

		Callback<TableColumn<Doppelstunde, T>, TableCell<Doppelstunde, T>> aktuelleCellFactory=column.getCellFactory();

		column.setCellFactory((tableColumn)->
		{
			TableCell<Doppelstunde, T> tableCell=aktuelleCellFactory.call(tableColumn);

			//TODO - NOT NULL, Contextmenü schließen
			if(tableCell.getItem()!=null)
			{
				Tooltip tooltip=new Tooltip();
				tooltip.textProperty().bind(tableCell.itemProperty().asString());
				tableCell.setTooltip(tooltip);
			}

			return tableCell;
		});
	}
}