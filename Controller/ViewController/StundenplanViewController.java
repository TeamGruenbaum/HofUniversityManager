package Controller.ViewController;

import Controller.InformationsVermittlung.Internetdatenatenabrufer;
import Controller.Main;
import Controller.InformationsVermittlung.Internetverbindungskontrolleur;
import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;

import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
	//Alles
	@FXML private BorderPane allesBorderPane;
	@FXML private TabPane hauptTabPane;



	//Tab Fächer
	@FXML private TabPane faecherTabPane;

	@FXML private TableView<Aufgabe> aufgabenTableView;
	@FXML private TableColumn<Aufgabe, String> aufgabenNameTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenInhaltTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenDatumTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenZeitTableColumn;
	@FXML private TableColumn<Aufgabe, String> aufgabenFachTableColumn;
	private ObservableList<Aufgabe> aufgabenObservableList;

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
	private ObservableList<Note> notenObservableList;



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
		hauptTabPane.widthProperty().addListener((observable, oldValue, newValue) ->
		{
			hauptTabPane.setTabMinWidth((hauptTabPane.getWidth() / hauptTabPane.getTabs().size())-22);
			hauptTabPane.setTabMaxWidth((hauptTabPane.getWidth() / hauptTabPane.getTabs().size())-22);
		});

		hauptTabPane.setTabMinHeight(40);
		faecherTabPane.setTabMinHeight(40);

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
				stundenplanZuruecksetzungProgressIndicator.setVisible(false);
			}
		}));

		stundenplanLaden();

		//TODO Art und Note verwechselt
		//Fächer
		notenObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getNoten());
		notenTableView.setItems(notenObservableList);
		aendernLoeschenKontextmenueHinzufuegen(notenTableView, (actionEvent, noteTableView)->
			{
				oeffneNoteDialog("Note ändern", "Ändern",
					noteTableView.getSelectionModel().getSelectedItem().getNote(),
					noteTableView.getSelectionModel().getSelectedItem().getHerkunft(),
					noteTableView.getSelectionModel().getSelectedItem().getBemerkung(),
					noteTableView.getSelectionModel().getSelectedItem().getFach())
					.ifPresent((item)->
					{
						SchreiberLeser.getNutzerdaten().getNoten().set(noteTableView.getSelectionModel().getSelectedIndex(), item);
						notenObservableList.set(noteTableView.getSelectionModel().getSelectedIndex(), item);
						noteTableView.refresh();
					});
			},
			(actionEvent, noteTableView)->
			{
				SchreiberLeser.getNutzerdaten().getNoten().remove(noteTableView.getSelectionModel().getSelectedIndex());
				notenObservableList.remove(noteTableView.getSelectionModel().getSelectedIndex());
				noteTableView.refresh();
			});
		notenBemerkungTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getBemerkung());});
		notenNoteTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getNote());});
		notenArtTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getHerkunft());});
		notenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});


		aufgabenObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getAufgaben());
		aufgabenTableView.setItems(aufgabenObservableList);
		aendernLoeschenKontextmenueHinzufuegen(aufgabenTableView, (actionEvent, aufgabenTableView)->
			{
				oeffneAufgabeDialog("Aufgabe ändern", "Ändern",
					aufgabenTableView.getSelectionModel().getSelectedItem().getName(),
					aufgabenTableView.getSelectionModel().getSelectedItem().getInhalt(),
					aufgabenTableView.getSelectionModel().getSelectedItem().getDatum(),
					aufgabenTableView.getSelectionModel().getSelectedItem().getZeit(),
					aufgabenTableView.getSelectionModel().getSelectedItem().getFach())
					.ifPresent((item)->
					{
						SchreiberLeser.getNutzerdaten().getAufgaben().set(aufgabenTableView.getSelectionModel().getSelectedIndex(), item);
						aufgabenObservableList.set(aufgabenTableView.getSelectionModel().getSelectedIndex(), item);
						aufgabenTableView.refresh();
					});
			},
			(actionEvent, aufgabenTableView)->
			{
				SchreiberLeser.getNutzerdaten().getAufgaben().remove(aufgabenTableView.getSelectionModel().getSelectedIndex());
				aufgabenObservableList.remove(aufgabenTableView.getSelectionModel().getSelectedIndex());
				aufgabenTableView.refresh();
			});
		aufgabenNameTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getName());});
		aufgabenInhaltTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getInhalt());});
		aufgabenZeitTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getZeit().toString());});
		aufgabenDatumTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getDatum().toString());});
		aufgabenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});


		notizObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getNotizen());
		notizenTableView.setItems(notizObservableList);
		aendernLoeschenKontextmenueHinzufuegen(notizenTableView, (actionEvent, notizTableView)->
		{
			oeffneNotizDialog("Notiz ändern", "Ändern",
				notizTableView.getSelectionModel().getSelectedItem().getTitel(),
				notizTableView.getSelectionModel().getSelectedItem().getInhalt(),
				notizTableView.getSelectionModel().getSelectedItem().getFach())
				.ifPresent((item)->
				{
					SchreiberLeser.getNutzerdaten().getNotizen().set(notizTableView.getSelectionModel().getSelectedIndex(), item);
					notizObservableList.set(notizTableView.getSelectionModel().getSelectedIndex(), item);
					notizenTableView.refresh();
				});
		},
		(actionEvent, notizTableView)->
		{
			SchreiberLeser.getNutzerdaten().getNotizen().remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizObservableList.remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizenTableView.refresh();
		});
		notizenNameTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getTitel());});
		notizenInhaltTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getInhalt());});
		notizenFachTableColumn.setCellValueFactory((cellData) -> {return new SimpleStringProperty(cellData.getValue().getFach());});
	}

	//Stundenplan
	@FXML private void stundenplanZuruecksetzen(ActionEvent actionEvent)
	{
		if(SchreiberLeser.getNutzerdaten().getStudiengang()==null && SchreiberLeser.getNutzerdaten().getStudiensemester()==null)
		{
			GrundViewController.oeffneFehlenderStudiengangDialog();
		}
		else
		{
			if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.hof-university.de"))
			{
				Internetdatenatenabrufer.setProgressIndicator(stundenplanZuruecksetzungProgressIndicator);
				Internetdatenatenabrufer.stundenplanAbrufen();
				stundenplanZuruecksetzen.setDisable(true);
				stundenplanZuruecksetzungProgressIndicator.setVisible(true);
			}
			else
			{
				GrundViewController.oeffneFehlendeInternetverbindungDialogDienst();
			}
		}
	}

	@FXML private void doppelstundeHinzufuegen(ActionEvent actionEvent)
	{
		oeffneDoppelstundeDialog("Stunde hinzufügen", "Hinzufügen", "", "", "", Tag.MONTAG, 8, 0, 8, 0).ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().add(item));
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


		//TODO Digitale Wirtschaft 2 - SS
		Comparator<Doppelstunde> doppelstundeComparator=(o1, o2)->o1.getBeginn().compareTo(o2.getBeginn());

		SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(item->
		{
			if(item.getTag()==null)
			{
				if(!stundenplanHBox.getChildren().contains(ohneTagTableView))
				{
					stundenplanHBox.getChildren().add(ohneTagTableView);
				}
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
						if(!stundenplanHBox.getChildren().contains(samstagTableView))
						{
							stundenplanHBox.getChildren().add(samstagTableView);
						}
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
			dialogPane.getStylesheets().add(getClass().getResource("../../View/CSS/Application.css").toExternalForm());
			dialogPane.setStyle(Main.getRoot().getStyle());
		}catch(IOException keineGefahrExcepttion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML-Datei hartkodiert ist.
			keineGefahrExcepttion.printStackTrace();
		}
		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);


		TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
		TextField dozentTextField=(TextField) dialogPane.lookup("#dozentTextField");
		TextField raumTextField=(TextField) dialogPane.lookup("#raumTextField");

		ChangeListener<String> changeListener=(observable, oldValue, newValue)->dialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty()||dozentTextField.getText().trim().isEmpty()||raumTextField.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		dozentTextField.textProperty().addListener(changeListener);
		raumTextField.textProperty().addListener(changeListener);

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
		beginnStundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> beginnMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#beginnMinuteUhrzeitSpinner");
		beginnMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, beginnMinuteUhrzeitPrompt));
		beginnMinuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> endeStundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeStundeUhrzeitSpinner");
		endeStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, endeStundeUhrzeitPrompt));
		endeStundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> endeMinuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#endeMinuteUhrzeitSpinner");
		endeMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, endeMinuteUhrzeitPrompt));
		endeMinuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

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
		aendernLoeschenKontextmenueHinzufuegen(tableView, aendernConsumer, loeschConsumer);
		tableColumn.setCellValueFactory(callback);
		tooltipZuZelleHinzufuegen(tableColumn);
	}

	//Faecher
	@FXML private void aufgabeNotizNoteHinzufuegen(ActionEvent actionEvent)
	{
		switch(faecherTabPane.getSelectionModel().getSelectedIndex())
		{
			case 0->oeffneAufgabeDialog("Aufgaben hinzufügen", "Hinzufügen", "","", new Datum(Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR)), new Uhrzeit(8, 0), SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getAufgaben().add(item);
				aufgabenObservableList.add(item);
				aufgabenTableView.refresh();
			});
			case 1->oeffneNotizDialog("Notiz hinzufügen", "Hinzufügen", "","", SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNotizen().add(item);
				notizObservableList.add(item);
				notizenTableView.refresh();
			});
			case 2->oeffneNoteDialog("Note hinzufügen", "Hinzufügen", "","","", SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNoten().add(item);
				notenObservableList.add(item);
				notizenTableView.refresh();
			});
		}
	}

	private Optional<Aufgabe> oeffneAufgabeDialog(String fensterTitel, String buttonTitel, String namePrompt,String inhaltPrompt, Datum datumPrompt, Uhrzeit uhrzeitPrompt, String fachPrompt)
	{
		DialogPane dialogPane=new DialogPane();
		try
		{
			dialogPane.setContent(FXMLLoader.load(getClass().getResource("../../View/AufgabeHinzufuegenView.fxml")));
			dialogPane.getStylesheets().add(getClass().getResource("../../View/CSS/Application.css").toExternalForm());
			dialogPane.setStyle(Main.getRoot().getStyle());
		}catch(IOException ignored){}
		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
		TextField inhaltTextField=(TextField) dialogPane.lookup("#inhaltTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->dialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty() || inhaltTextField.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		inhaltTextField.textProperty().addListener(changeListener);
		nameTextField.setText(namePrompt);
		inhaltTextField.setText(inhaltPrompt);

		Spinner<Integer> tagDatumSpinner=(Spinner<Integer>) dialogPane.lookup("#tagDatumSpinner");
		tagDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, datumPrompt.getTag()));
		tagDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> monatDatumSpinner=(Spinner<Integer>) dialogPane.lookup("#monatDatumSpinner");
		monatDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, datumPrompt.getMonat()));
		monatDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> jahrDatumSpinner=(Spinner<Integer>) dialogPane.lookup("#jahrDatumSpinner");
		jahrDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2402, datumPrompt.getJahr()));
		jahrDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> stundeUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#stundeUhrzeitSpinner");
		stundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, uhrzeitPrompt.getStunde()));
		stundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> minuteUhrzeitSpinner=(Spinner<Integer>) dialogPane.lookup("#minuteUhrzeitSpinner");
		minuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, uhrzeitPrompt.getMinute()));
		minuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) dialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachPrompt, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachPrompt);

		Button fachHinzufuegeButton=(Button) dialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschButton=(Button) dialogPane.lookup("#fachLoeschButton");
		fachLoeschButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Aufgabe> dialog=new Dialog<>();
		dialog.setTitle(fensterTitel);
		dialog.setDialogPane(dialogPane);
		dialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}else
			{
				return new Aufgabe(nameTextField.getText().trim(),
					inhaltTextField.getText().trim(),
					new Datum(tagDatumSpinner.getValue(),monatDatumSpinner.getValue(), jahrDatumSpinner.getValue()),
					new Uhrzeit(stundeUhrzeitSpinner.getValue(), minuteUhrzeitSpinner.getValue()),
					faecherChoiceBox.getValue());
			}
		});
		dialog.initOwner(Main.getPrimaryStage());

		return dialog.showAndWait();
	}

	private Optional<Note> oeffneNoteDialog(String fensterTitel, String buttonTitel, String notePrompt, String artPrompt, String bemerkungPrompt, String fachPrompt)
	{
		DialogPane dialogPane=new DialogPane();
		try
		{
			dialogPane.setContent(FXMLLoader.load(getClass().getResource("../../View/NoteHinzufuegenView.fxml")));
			dialogPane.getStylesheets().add(getClass().getResource("../../View/CSS/Application.css").toExternalForm());
			dialogPane.setStyle(Main.getRoot().getStyle());
		}catch(IOException ignored){}
		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField noteTextField=(TextField) dialogPane.lookup("#noteTextField");
		TextField artTextField=(TextField) dialogPane.lookup("#artTextField");
		TextField bemerkungTextField=(TextField) dialogPane.lookup("#bemerkungTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->dialogPane.lookupButton(ButtonType.OK).setDisable(noteTextField.getText().trim().isEmpty() || artTextField.getText().trim().isEmpty() || bemerkungTextField.getText().trim().isEmpty());
		noteTextField.textProperty().addListener(changeListener);
		artTextField.textProperty().addListener(changeListener);
		bemerkungTextField.textProperty().addListener(changeListener);
		noteTextField.setText(notePrompt);
		artTextField.setText(artPrompt);
		bemerkungTextField.setText(bemerkungPrompt);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) dialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachPrompt, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachPrompt);

		Button fachHinzufuegeButton=(Button) dialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschButton=(Button) dialogPane.lookup("#fachLoeschButton");
		fachLoeschButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Note> dialog=new Dialog<>();
		dialog.setTitle(fensterTitel);
		dialog.setDialogPane(dialogPane);
		dialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}else
			{
				return new Note(noteTextField.getText().trim(),
					artTextField.getText().trim(),
					bemerkungTextField.getText().trim(),
					faecherChoiceBox.getValue());
			}
		});
		dialog.initOwner(Main.getPrimaryStage());

		return dialog.showAndWait();
	}

	private Optional<Notiz> oeffneNotizDialog(String fensterTitel, String buttonTitel, String ueberschriftPrompt, String inhaltPrompt, String fachPrompt)
	{
		DialogPane dialogPane=new DialogPane();
		try
		{
			dialogPane.setContent(FXMLLoader.load(getClass().getResource("../../View/NotizHinzufuegeView.fxml")));
			dialogPane.getStylesheets().add(getClass().getResource("../../View/CSS/Application.css").toExternalForm());
			dialogPane.setStyle(Main.getRoot().getStyle());
		}
		catch(Exception keineGefahrExcepttion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML- bzw. CSS-Datei hartkodiert ist
			keineGefahrExcepttion.printStackTrace();
		}

		dialogPane.setMinSize(300, 200);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) dialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) dialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		dialogPane.lookupButton(ButtonType.OK).setDisable(true);
		System.out.println(((Button) dialogPane.lookupButton(ButtonType.CANCEL)).getScaleX());

		TextField nameTextField=(TextField) dialogPane.lookup("#nameTextField");
		TextArea inhaltTextArea=(TextArea) dialogPane.lookup("#inhaltTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->dialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty() || inhaltTextArea.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		inhaltTextArea.textProperty().addListener(changeListener);
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
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
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

	private Optional<String> fachnameHinzufuegenDialogOeffnen()
	{
		TextInputDialog textInputDialog=new TextInputDialog();
		textInputDialog.setGraphic(null);
		textInputDialog.setHeaderText(null);
		((Button) textInputDialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Hinzufügen");
		textInputDialog.setTitle("Fach hinzufügen");
		textInputDialog.setContentText("Fachname");
		textInputDialog.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> textInputDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(textInputDialog.getEditor().textProperty().getValue().trim().isEmpty())));
		textInputDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		textInputDialog.getDialogPane().getStylesheets().add(getClass().getResource("../../View/CSS/Application.css").toExternalForm());
		textInputDialog.getDialogPane().setStyle(Main.getRoot().getStyle());

		return textInputDialog.showAndWait();
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
			if((mouseEvent.getButton()==MouseButton.SECONDARY) || (mouseEvent.getClickCount()==2))
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