package Controller.ViewController;



import Controller.InformationsVermittlung.Downloader;
import Controller.InformationsVermittlung.Internetverbindungskontrolleur;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.Datum;
import Model.NutzerdatenModel.*;
import Model.Tag;
import Model.Uhrzeit;

import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

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



public class StundenplanViewController implements Initializable
{
	@FXML private TabPane hauptTabPane;


	@FXML private HBox stundenplanHBox;

	@FXML private TableView<Doppelstunde> montagTableView;
	@FXML private TableColumn<Doppelstunde,String> montagTableColumn;
	private ObservableList<Doppelstunde> montagObservableList;

	@FXML private TableView<Doppelstunde> dienstagTableView;
	@FXML private TableColumn<Doppelstunde,String> dienstagTableColumn;
	private ObservableList<Doppelstunde> dienstagObservableList;

	@FXML private TableView<Doppelstunde> mittwochTableView;
	@FXML private TableColumn<Doppelstunde,String> mittwochTableColumn;
	private ObservableList<Doppelstunde> mittwochObservableList;

	@FXML private TableView<Doppelstunde> donnerstagTableView;
	@FXML private TableColumn<Doppelstunde,String> donnerstagTableColumn;
	private ObservableList<Doppelstunde> donnerstagObservableList;

	@FXML private TableView<Doppelstunde> freitagTableView;
	@FXML private TableColumn<Doppelstunde,String> freitagTableColumn;
	private ObservableList<Doppelstunde> freitagObservableList;

	@FXML private TableView<Doppelstunde> samstagTableView;
	@FXML private TableColumn<Doppelstunde,String> samstagTableColumn;
	private ObservableList<Doppelstunde> samstagObservableList;

	@FXML private TableView<Doppelstunde> ohneTagTableView;
	@FXML private TableColumn<Doppelstunde,String> ohneTagTableColumn;
	private ObservableList<Doppelstunde> ohneTagObservableList;

	@FXML private ProgressIndicator stundenplanZuruecksetzungProgressIndicator;
	@FXML private Button stundenplanZuruecksetzungButton;


	@FXML private TabPane faecherTabPane;

	@FXML private TableView<Aufgabe> aufgabenTableView;
	@FXML private TableColumn<Aufgabe,String> aufgabenNameTableColumn;
	@FXML private TableColumn<Aufgabe,String> aufgabenInhaltTableColumn;
	@FXML private TableColumn<Aufgabe,String> aufgabenDatumTableColumn;
	@FXML private TableColumn<Aufgabe,String> aufgabenZeitTableColumn;
	@FXML private TableColumn<Aufgabe,String> aufgabenFachTableColumn;
	private ObservableList<Aufgabe> aufgabenObservableList;

	@FXML private TableView<Notiz> notizenTableView;
	@FXML private TableColumn<Notiz,String> notizenNameTableColumn;
	@FXML private TableColumn<Notiz,String> notizenInhaltTableColumn;
	@FXML private TableColumn<Notiz,String> notizenFachTableColumn;
	private ObservableList<Notiz> notizObservableList;

	@FXML private TableView<Note> notenTableView;
	@FXML private TableColumn<Note,String> notenNoteTableColumn;
	@FXML private TableColumn<Note,String> notenHerkunftTableColumn;
	@FXML private TableColumn<Note,String> notenBemerkungTableColumn;
	@FXML private TableColumn<Note,String> notenFachTableColumn;
	private ObservableList<Note> notenObservableList;



	@Override public void initialize(URL url, ResourceBundle resourceBundle)
	{
		hauptTabPane.widthProperty().addListener((observable, oldValue, newValue)->
		{
			hauptTabPane.setTabMinWidth((hauptTabPane.getWidth()/hauptTabPane.getTabs().size())-22);
			hauptTabPane.setTabMaxWidth((hauptTabPane.getWidth()/hauptTabPane.getTabs().size())-22);
		});

		hauptTabPane.setTabMinHeight(40);
		faecherTabPane.setTabMinHeight(40);


		BiConsumer<ActionEvent,TableView<Doppelstunde>> aendernBiConsumer=(actionEvent, tableView)->
		{
			oeffneDoppelstundeHinzufuegenDialog("Stunde ändern", "Ändern", tableView.getSelectionModel().getSelectedItem().getName(), tableView.getSelectionModel().getSelectedItem().getDozent(), tableView.getSelectionModel().getSelectedItem().getRaum(), tableView.getSelectionModel().getSelectedItem().getDoppelstundentag(), tableView.getSelectionModel().getSelectedItem().getBeginnUhrzeit(), tableView.getSelectionModel().getSelectedItem().getEndeUhrzeit()).ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().set(SchreiberLeser.getNutzerdaten().getDoppelstunden().indexOf(tableView.getSelectionModel().getSelectedItem()), item));
			stundenplanLaden();
		};

		BiConsumer<ActionEvent,TableView<Doppelstunde>> loeschenBiConsumer=(actionEvent, tableView)->
		{
			SchreiberLeser.getNutzerdaten().getDoppelstunden().remove(tableView.getSelectionModel().getSelectedItem());
			stundenplanLaden();
		};


		montagObservableList=FXCollections.observableArrayList();
		dienstagObservableList=FXCollections.observableArrayList();
		mittwochObservableList=FXCollections.observableArrayList();
		donnerstagObservableList=FXCollections.observableArrayList();
		freitagObservableList=FXCollections.observableArrayList();
		samstagObservableList=FXCollections.observableArrayList();
		ohneTagObservableList=FXCollections.observableArrayList();

		Callback<TableColumn.CellDataFeatures<Doppelstunde,String>,ObservableValue<String>> zelleninhaltCallback=cellData->
		{
			return new SimpleStringProperty(((cellData.getValue().getDoppelstundendatum()==null)?"":(cellData.getValue().getDoppelstundendatum()+" "))+((cellData.getValue().getBeginnUhrzeit()==null)?"":(cellData.getValue().getBeginnUhrzeit()+"-"+cellData.getValue().getEndeUhrzeit()+"\n"))+((cellData.getValue().getRaum()==null)?"":(cellData.getValue().getRaum()+"\n"))+cellData.getValue().getName()+"\n"+cellData.getValue().getDozent());
		};

		tableViewInitialisieren(montagTableView, montagTableColumn, montagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(dienstagTableView, dienstagTableColumn, dienstagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(mittwochTableView, mittwochTableColumn, mittwochObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(donnerstagTableView, donnerstagTableColumn, donnerstagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(freitagTableView, freitagTableColumn, freitagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(samstagTableView, samstagTableColumn, samstagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);
		tableViewInitialisieren(ohneTagTableView, ohneTagTableColumn, ohneTagObservableList, zelleninhaltCallback, aendernBiConsumer, loeschenBiConsumer);

		stundenplanHBox.getChildren().remove(samstagTableView);
		stundenplanHBox.getChildren().remove(ohneTagTableView);

		stundenplanZuruecksetzungProgressIndicator.progressProperty().addListener(((observable, oldValue, newValue)->
		{
			if(newValue.doubleValue()==1)
			{
				stundenplanLaden();

				stundenplanZuruecksetzungButton.setDisable(false);
				stundenplanZuruecksetzungProgressIndicator.setVisible(false);
			}
		}));

		stundenplanLaden();


		notenObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getNoten());
		notenTableView.setItems(notenObservableList);
		aendernLoeschenKontextmenueHinzufuegen(notenTableView, (actionEvent, tabelView)->
		{
			oeffneNoteHinzufuegenDialog("Note ändern", "Ändern", tabelView.getSelectionModel().getSelectedItem().getNote(), tabelView.getSelectionModel().getSelectedItem().getHerkunft(), tabelView.getSelectionModel().getSelectedItem().getBemerkung(), tabelView.getSelectionModel().getSelectedItem().getFach()).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNoten().set(tabelView.getSelectionModel().getSelectedIndex(), item);
				notenObservableList.set(tabelView.getSelectionModel().getSelectedIndex(), item);
				tabelView.refresh();
			});
		}, (actionEvent, tableView)->
		{
			SchreiberLeser.getNutzerdaten().getNoten().remove(tableView.getSelectionModel().getSelectedIndex());
			notenObservableList.remove(tableView.getSelectionModel().getSelectedIndex());
			tableView.refresh();
		});
		notenBemerkungTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getBemerkung());
		});
		notenNoteTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getNote());
		});
		notenHerkunftTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getHerkunft());
		});
		notenFachTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getFach());
		});

		aufgabenObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getAufgaben());
		aufgabenTableView.setItems(aufgabenObservableList);
		aendernLoeschenKontextmenueHinzufuegen(aufgabenTableView, (actionEvent, tableView)->
		{
			oeffneAufgabeHinzufuegenDialog("Aufgabe ändern", "Ändern", tableView.getSelectionModel().getSelectedItem().getName(), tableView.getSelectionModel().getSelectedItem().getInhalt(), tableView.getSelectionModel().getSelectedItem().getFaelligkeitsdatum(), tableView.getSelectionModel().getSelectedItem().getFaelligkeitsuhrzeit(), tableView.getSelectionModel().getSelectedItem().getFach()).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getAufgaben().set(tableView.getSelectionModel().getSelectedIndex(), item);
				aufgabenObservableList.set(tableView.getSelectionModel().getSelectedIndex(), item);
				tableView.refresh();
			});
		}, (actionEvent, tableView)->
		{
			SchreiberLeser.getNutzerdaten().getAufgaben().remove(tableView.getSelectionModel().getSelectedIndex());
			aufgabenObservableList.remove(tableView.getSelectionModel().getSelectedIndex());
			tableView.refresh();
		});
		aufgabenNameTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getName());
		});
		aufgabenInhaltTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getInhalt());
		});
		aufgabenZeitTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getFaelligkeitsuhrzeit().toString());
		});
		aufgabenDatumTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getFaelligkeitsdatum().toString());
		});
		aufgabenFachTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getFach());
		});

		notizObservableList=FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getNotizen());
		notizenTableView.setItems(notizObservableList);
		aendernLoeschenKontextmenueHinzufuegen(notizenTableView, (actionEvent, notizTableView)->
		{
			oeffneNotizHinzufuegenDialog("Notiz ändern", "Ändern", notizTableView.getSelectionModel().getSelectedItem().getTitel(), notizTableView.getSelectionModel().getSelectedItem().getInhalt(), notizTableView.getSelectionModel().getSelectedItem().getFach()).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNotizen().set(notizTableView.getSelectionModel().getSelectedIndex(), item);
				notizObservableList.set(notizTableView.getSelectionModel().getSelectedIndex(), item);
				notizenTableView.refresh();
			});
		}, (actionEvent, notizTableView)->
		{
			SchreiberLeser.getNutzerdaten().getNotizen().remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizObservableList.remove(notizTableView.getSelectionModel().getSelectedIndex());
			notizenTableView.refresh();
		});
		notizenNameTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getTitel());
		});
		notizenInhaltTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getInhalt());
		});
		notizenFachTableColumn.setCellValueFactory((cellData)->
		{
			return new SimpleStringProperty(cellData.getValue().getFach());
		});
	}


	@FXML private void stundenplanZuruecksetzen()
	{
		if(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang()==null&&SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester()==null)
		{
			GrundViewController.oeffneFehlenderStudiengangDialog();
		}
		else
		{
			if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.hof-university.de"))
			{
				Downloader.setDownloadfortschrittProgressIndicator(stundenplanZuruecksetzungProgressIndicator);
				Downloader.stundenplanAbrufen();
				stundenplanZuruecksetzungButton.setDisable(true);
				stundenplanZuruecksetzungProgressIndicator.setVisible(true);
			}
			else
			{
				GrundViewController.oeffneFehlendeInternetverbindungAlertWarnung();
			}
		}
	}

	@FXML private void doppelstundeHinzufuegen(ActionEvent actionEvent)
	{
		oeffneDoppelstundeHinzufuegenDialog("Stunde hinzufügen", "Hinzufügen", "", "", "", Tag.MONTAG, new Uhrzeit(8, 0), new Uhrzeit(8, 0)).ifPresent((item)->SchreiberLeser.getNutzerdaten().getDoppelstunden().add(item));
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
		ohneTagObservableList.clear();

		stundenplanHBox.getChildren().remove(samstagTableView);
		stundenplanHBox.getChildren().remove(ohneTagTableView);


		Comparator<Doppelstunde> doppelstundeComparator=(o1, o2)->
		{
			return o1.getBeginnUhrzeit()==null?0:o1.getBeginnUhrzeit().compareTo(o2.getBeginnUhrzeit());
		};

		SchreiberLeser.getNutzerdaten().getDoppelstunden().forEach(item->
		{
			if(item.getDoppelstundentag()==null)
			{
				if(!stundenplanHBox.getChildren().contains(ohneTagTableView))
				{
					stundenplanHBox.getChildren().add(ohneTagTableView);
				}
				ohneTagObservableList.add(item);
				ohneTagObservableList.sort(doppelstundeComparator);
			}
			else
			{
				switch(item.getDoppelstundentag())
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

	private Optional<Doppelstunde> oeffneDoppelstundeHinzufuegenDialog(String fensterTitel, String buttonTitel, String nameVorschau, String dozentVorschau, String raumVorschau, Tag tagVorschau, Uhrzeit beginnVorschauUhrzeit, Uhrzeit endeVorschauUhrzeit)
	{
		DialogPane doppelstundeHinzufuegenDialogPane=new DialogPane();
		try
		{
			doppelstundeHinzufuegenDialogPane.setContent(FXMLLoader.load(getClass().getResource("/View/DoppelstundenHinzufuegenView.fxml")));
			doppelstundeHinzufuegenDialogPane.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
			doppelstundeHinzufuegenDialogPane.setStyle(Main.getRoot().getStyle());
		}
		catch(IOException keineGefahrExcepttion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML-Datei hartkodiert ist.
			keineGefahrExcepttion.printStackTrace();
		}
		doppelstundeHinzufuegenDialogPane.setMinSize(300, 200);
		doppelstundeHinzufuegenDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) doppelstundeHinzufuegenDialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) doppelstundeHinzufuegenDialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		doppelstundeHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(true);


		TextField nameTextField=(TextField) doppelstundeHinzufuegenDialogPane.lookup("#nameTextField");
		TextField dozentTextField=(TextField) doppelstundeHinzufuegenDialogPane.lookup("#dozentTextField");
		TextField raumTextField=(TextField) doppelstundeHinzufuegenDialogPane.lookup("#raumTextField");

		ChangeListener<String> changeListener=(observable, oldValue, newValue)->doppelstundeHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty()||dozentTextField.getText().trim().isEmpty()||raumTextField.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		dozentTextField.textProperty().addListener(changeListener);
		raumTextField.textProperty().addListener(changeListener);

		nameTextField.setText(nameVorschau);
		dozentTextField.setText(dozentVorschau);
		raumTextField.setText(raumVorschau);


		ChoiceBox<Tag> tagChoiceBox=(ChoiceBox<Tag>) doppelstundeHinzufuegenDialogPane.lookup("#tagChoiceBox");
		tagChoiceBox.setItems(FXCollections.observableArrayList(List.of(Tag.values())));
		tagChoiceBox.getSelectionModel().select(0);
		tagChoiceBox.setConverter(new StringConverter<Tag>()
		{
			@Override public String toString(Tag item)
			{

				return item.toString().substring(0, 1).toUpperCase()+item.toString().substring(1).toLowerCase();
			}

			@Override public Tag fromString(String item)
			{
				return null;
			}
		});
		tagChoiceBox.setValue(tagVorschau);

		Spinner<Integer> beginnStundeUhrzeitSpinner=(Spinner<Integer>) doppelstundeHinzufuegenDialogPane.lookup("#beginnStundeUhrzeitSpinner");
		beginnStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, beginnVorschauUhrzeit.getStunde()));
		beginnStundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> beginnMinuteUhrzeitSpinner=(Spinner<Integer>) doppelstundeHinzufuegenDialogPane.lookup("#beginnMinuteUhrzeitSpinner");
		beginnMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, beginnVorschauUhrzeit.getMinute()));
		beginnMinuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> endeStundeUhrzeitSpinner=(Spinner<Integer>) doppelstundeHinzufuegenDialogPane.lookup("#endeStundeUhrzeitSpinner");
		endeStundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, endeVorschauUhrzeit.getStunde()));
		endeStundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> endeMinuteUhrzeitSpinner=(Spinner<Integer>) doppelstundeHinzufuegenDialogPane.lookup("#endeMinuteUhrzeitSpinner");
		endeMinuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, endeVorschauUhrzeit.getMinute()));
		endeMinuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Dialog<Doppelstunde> doppelstundeHinzufuegenDialog=new Dialog<>();
		doppelstundeHinzufuegenDialog.setTitle(fensterTitel);
		doppelstundeHinzufuegenDialog.setDialogPane(doppelstundeHinzufuegenDialogPane);
		doppelstundeHinzufuegenDialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}
			else
			{
				return new Doppelstunde(tagChoiceBox.getValue(), null, new Uhrzeit(beginnStundeUhrzeitSpinner.getValue(), beginnMinuteUhrzeitSpinner.getValue()), new Uhrzeit(endeStundeUhrzeitSpinner.getValue(), endeMinuteUhrzeitSpinner.getValue()), raumTextField.getText().trim(), nameTextField.getText().trim(), dozentTextField.getText().trim());
			}
		});
		doppelstundeHinzufuegenDialog.initOwner(Main.getPrimaryStage());

		return doppelstundeHinzufuegenDialog.showAndWait();
	}

	private void tableViewInitialisieren(TableView<Doppelstunde> tableView, TableColumn<Doppelstunde,String> tableColumn, ObservableList<Doppelstunde> observableList, Callback<TableColumn.CellDataFeatures<Doppelstunde,String>,ObservableValue<String>> callback, BiConsumer<ActionEvent,TableView<Doppelstunde>> aendernBiConsumer, BiConsumer<ActionEvent,TableView<Doppelstunde>> loeschenBiConsumer)
	{
		tableView.setItems(observableList);
		aendernLoeschenKontextmenueHinzufuegen(tableView, aendernBiConsumer, loeschenBiConsumer);
		tableColumn.setCellValueFactory(callback);
	}


	@FXML private void aufgabeNotizNoteHinzufuegen()
	{
		switch(faecherTabPane.getSelectionModel().getSelectedIndex())
		{
			case 0 -> oeffneAufgabeHinzufuegenDialog("Aufgaben hinzufügen", "Hinzufügen", "", "", new Datum(Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR)), new Uhrzeit(8, 0), SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getAufgaben().add(item);
				aufgabenObservableList.add(item);
				aufgabenTableView.refresh();
			});
			case 1 -> oeffneNotizHinzufuegenDialog("Notiz hinzufügen", "Hinzufügen", "", "", SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNotizen().add(item);
				notizObservableList.add(item);
				notizenTableView.refresh();
			});
			case 2 -> oeffneNoteHinzufuegenDialog("Note hinzufügen", "Hinzufügen", "", "", "", SchreiberLeser.getNutzerdaten().getFaecher().size()==0?"Allgemein":SchreiberLeser.getNutzerdaten().getFaecher().get(0)).ifPresent((item)->
			{
				SchreiberLeser.getNutzerdaten().getNoten().add(item);
				notenObservableList.add(item);
				notizenTableView.refresh();
			});
		}
	}

	private Optional<Aufgabe> oeffneAufgabeHinzufuegenDialog(String fensterTitel, String buttonTitel, String nameVorschau, String inhaltVorschau, Datum datumVorschau, Uhrzeit uhrzeitVorschau, String fachVorschau)
	{
		DialogPane aufgabeHinzufuegenDialogPane=new DialogPane();
		try
		{
			aufgabeHinzufuegenDialogPane.setContent(FXMLLoader.load(getClass().getResource("/View/AufgabeHinzufuegenView.fxml")));
			aufgabeHinzufuegenDialogPane.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
			aufgabeHinzufuegenDialogPane.setStyle(Main.getRoot().getStyle());
		}
		catch(IOException ignored)
		{
		}
		aufgabeHinzufuegenDialogPane.setMinSize(300, 200);
		aufgabeHinzufuegenDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) aufgabeHinzufuegenDialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) aufgabeHinzufuegenDialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		aufgabeHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField nameTextField=(TextField) aufgabeHinzufuegenDialogPane.lookup("#nameTextField");
		TextField inhaltTextField=(TextField) aufgabeHinzufuegenDialogPane.lookup("#inhaltTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->aufgabeHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty()||inhaltTextField.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		inhaltTextField.textProperty().addListener(changeListener);
		nameTextField.setText(nameVorschau);
		inhaltTextField.setText(inhaltVorschau);

		Spinner<Integer> tagDatumSpinner=(Spinner<Integer>) aufgabeHinzufuegenDialogPane.lookup("#tagDatumSpinner");
		tagDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, datumVorschau.getTag()));
		tagDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> monatDatumSpinner=(Spinner<Integer>) aufgabeHinzufuegenDialogPane.lookup("#monatDatumSpinner");
		monatDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, datumVorschau.getMonat()));
		monatDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> jahrDatumSpinner=(Spinner<Integer>) aufgabeHinzufuegenDialogPane.lookup("#jahrDatumSpinner");
		jahrDatumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2402, datumVorschau.getJahr()));
		jahrDatumSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> stundeUhrzeitSpinner=(Spinner<Integer>) aufgabeHinzufuegenDialogPane.lookup("#stundeUhrzeitSpinner");
		stundeUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, uhrzeitVorschau.getStunde()));
		stundeUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		Spinner<Integer> minuteUhrzeitSpinner=(Spinner<Integer>) aufgabeHinzufuegenDialogPane.lookup("#minuteUhrzeitSpinner");
		minuteUhrzeitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, uhrzeitVorschau.getMinute()));
		minuteUhrzeitSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) aufgabeHinzufuegenDialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachVorschau, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachVorschau);

		Button fachHinzufuegeButton=(Button) aufgabeHinzufuegenDialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschenButton=(Button) aufgabeHinzufuegenDialogPane.lookup("#fachLoeschenButton");
		fachLoeschenButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Aufgabe> aufgabeHinzufuegenDialog=new Dialog<>();
		aufgabeHinzufuegenDialog.setTitle(fensterTitel);
		aufgabeHinzufuegenDialog.setDialogPane(aufgabeHinzufuegenDialogPane);
		aufgabeHinzufuegenDialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}
			else
			{
				return new Aufgabe(nameTextField.getText().trim(), inhaltTextField.getText().trim(), new Datum(tagDatumSpinner.getValue(), monatDatumSpinner.getValue(), jahrDatumSpinner.getValue()), new Uhrzeit(stundeUhrzeitSpinner.getValue(), minuteUhrzeitSpinner.getValue()), faecherChoiceBox.getValue());
			}
		});
		aufgabeHinzufuegenDialog.initOwner(Main.getPrimaryStage());

		return aufgabeHinzufuegenDialog.showAndWait();
	}

	private Optional<Note> oeffneNoteHinzufuegenDialog(String fensterTitel, String buttonTitel, String noteVorschau, String artVorschau, String bemerkungVorschau, String fachVorschau)
	{
		DialogPane noteHinzufuegenDialogPane=new DialogPane();
		try
		{
			noteHinzufuegenDialogPane.setContent(FXMLLoader.load(getClass().getResource("/View/NoteHinzufuegenView.fxml")));
			noteHinzufuegenDialogPane.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
			noteHinzufuegenDialogPane.setStyle(Main.getRoot().getStyle());
		}
		catch(IOException ignored)
		{
		}
		noteHinzufuegenDialogPane.setMinSize(300, 200);
		noteHinzufuegenDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) noteHinzufuegenDialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) noteHinzufuegenDialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		noteHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField noteTextField=(TextField) noteHinzufuegenDialogPane.lookup("#noteTextField");
		TextField herkunftTextField=(TextField) noteHinzufuegenDialogPane.lookup("#herkunftTextField");
		TextField bemerkungTextField=(TextField) noteHinzufuegenDialogPane.lookup("#bemerkungTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->noteHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(noteTextField.getText().trim().isEmpty()||herkunftTextField.getText().trim().isEmpty());
		noteTextField.textProperty().addListener(changeListener);
		herkunftTextField.textProperty().addListener(changeListener);
		bemerkungTextField.textProperty().addListener(changeListener);
		noteTextField.setText(noteVorschau);
		herkunftTextField.setText(artVorschau);
		bemerkungTextField.setText(bemerkungVorschau);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) noteHinzufuegenDialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachVorschau, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachVorschau);

		Button fachHinzufuegeButton=(Button) noteHinzufuegenDialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschenButton=(Button) noteHinzufuegenDialogPane.lookup("#fachLoeschenButton");
		fachLoeschenButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Note> noteHinzufuegenDialog=new Dialog<>();
		noteHinzufuegenDialog.setTitle(fensterTitel);
		noteHinzufuegenDialog.setDialogPane(noteHinzufuegenDialogPane);
		noteHinzufuegenDialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}
			else
			{
				return new Note(noteTextField.getText().trim(), herkunftTextField.getText().trim(), bemerkungTextField.getText().trim(), faecherChoiceBox.getValue());
			}
		});
		noteHinzufuegenDialog.initOwner(Main.getPrimaryStage());

		return noteHinzufuegenDialog.showAndWait();
	}

	private Optional<Notiz> oeffneNotizHinzufuegenDialog(String fensterTitel, String buttonTitel, String ueberschriftVorschau, String inhaltVorschau, String fachVorschau)
	{
		DialogPane notizHinzufuegenDialogPane=new DialogPane();
		try
		{
			notizHinzufuegenDialogPane.setContent(FXMLLoader.load(getClass().getResource("/View/NotizHinzufuegenView.fxml")));
			notizHinzufuegenDialogPane.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
			notizHinzufuegenDialogPane.setStyle(Main.getRoot().getStyle());
		}
		catch(Exception keineGefahrExcepttion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML- bzw. CSS-Datei hartkodiert ist
			keineGefahrExcepttion.printStackTrace();
		}

		notizHinzufuegenDialogPane.setMinSize(300, 200);
		notizHinzufuegenDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		((Button) notizHinzufuegenDialogPane.lookupButton(ButtonType.OK)).setText(buttonTitel);
		((Button) notizHinzufuegenDialogPane.lookupButton(ButtonType.CANCEL)).setText("Abbrechen");
		notizHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(true);

		TextField nameTextField=(TextField) notizHinzufuegenDialogPane.lookup("#nameTextField");
		TextArea inhaltTextArea=(TextArea) notizHinzufuegenDialogPane.lookup("#inhaltTextField");
		ChangeListener<String> changeListener=(observable, oldValue, newValue)->notizHinzufuegenDialogPane.lookupButton(ButtonType.OK).setDisable(nameTextField.getText().trim().isEmpty()||inhaltTextArea.getText().trim().isEmpty());
		nameTextField.textProperty().addListener(changeListener);
		inhaltTextArea.textProperty().addListener(changeListener);
		nameTextField.setText(ueberschriftVorschau);
		inhaltTextArea.setText(inhaltVorschau);

		ChoiceBox<String> faecherChoiceBox=(ChoiceBox<String>) notizHinzufuegenDialogPane.lookup("#fachChoiceBox");
		fachnameHinzufuegen(fachVorschau, faecherChoiceBox);
		faecherChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		faecherChoiceBox.getSelectionModel().select(0);
		faecherChoiceBox.setValue(fachVorschau);

		Button fachHinzufuegeButton=(Button) notizHinzufuegenDialogPane.lookup("#fachHinzufuegeButton");
		fachHinzufuegeButton.setOnAction((actionEvent)->
		{
			fachnameHinzufuegenDialogOeffnen().ifPresent((item)->
			{
				fachnameHinzufuegen(item, faecherChoiceBox);
			});
		});

		Button fachLoeschenButton=(Button) notizHinzufuegenDialogPane.lookup("#fachLoeschenButton");
		fachLoeschenButton.setOnAction((actionEvent)->
		{
			fachnameLoeschen(faecherChoiceBox);
		});

		Dialog<Notiz> dialog=new Dialog<>();
		dialog.setTitle(fensterTitel);
		dialog.setDialogPane(notizHinzufuegenDialogPane);
		dialog.setResultConverter((dialogButton)->
		{
			if(dialogButton.getButtonData().isCancelButton())
			{
				return null;
			}
			else
			{
				return new Notiz(nameTextField.getText().trim(), inhaltTextArea.getText().trim(), faecherChoiceBox.getValue());
			}
		});
		dialog.initOwner(Main.getPrimaryStage());

		return dialog.showAndWait();
	}

	private Optional<String> fachnameHinzufuegenDialogOeffnen()
	{
		TextInputDialog hinzufuegenDialog=new TextInputDialog();
		hinzufuegenDialog.setGraphic(null);
		hinzufuegenDialog.setHeaderText(null);
		((Button) hinzufuegenDialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Hinzufügen");
		hinzufuegenDialog.setTitle("Fach hinzufügen");
		hinzufuegenDialog.setContentText("Fachname");
		hinzufuegenDialog.getEditor().textProperty().addListener(((observable, oldValue, newValue)->hinzufuegenDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(hinzufuegenDialog.getEditor().textProperty().getValue().trim().isEmpty())));
		hinzufuegenDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		hinzufuegenDialog.getDialogPane().getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
		hinzufuegenDialog.getDialogPane().setStyle(Main.getRoot().getStyle());

		return hinzufuegenDialog.showAndWait();
	}

	private void fachnameHinzufuegen(String name, ChoiceBox<String> zielChoiceBox)
	{
		SchreiberLeser.getNutzerdaten().getFaecher().add(name);
		SchreiberLeser.getNutzerdaten().setFaecher(new ArrayList<String>(SchreiberLeser.getNutzerdaten().getFaecher().stream().distinct().collect(Collectors.toList())));
		zielChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		zielChoiceBox.getSelectionModel().select(name);
	}

	private void fachnameLoeschen(ChoiceBox<String> zielChoiceBox)
	{
		SchreiberLeser.getNutzerdaten().getFaecher().remove(zielChoiceBox.getValue());
		zielChoiceBox.setItems(FXCollections.observableArrayList(SchreiberLeser.getNutzerdaten().getFaecher()));
		zielChoiceBox.getSelectionModel().select(0);
	}


	private <S> void aendernLoeschenKontextmenueHinzufuegen(TableView<S> zielTableView, BiConsumer<ActionEvent,TableView<S>> aendernBiConsumer, BiConsumer<ActionEvent,TableView<S>> loeschenBiConsumer)
	{
		ContextMenu contextMenu=new ContextMenu();
		contextMenu.setAutoHide(true);

		MenuItem aendernMenuItem=new MenuItem("Ändern");
		aendernMenuItem.setOnAction((actionEvent)->
		{
			aendernBiConsumer.accept(actionEvent, zielTableView);
		});
		contextMenu.getItems().add(aendernMenuItem);

		MenuItem loeschenMenuItem=new MenuItem("Löschen");
		loeschenMenuItem.setOnAction((actionEvent)->
		{
			loeschenBiConsumer.accept(actionEvent, zielTableView);
		});
		contextMenu.getItems().add(loeschenMenuItem);

		zielTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->
		{
			if((mouseEvent.getButton()==MouseButton.SECONDARY)||(mouseEvent.getClickCount()==2))
			{
				contextMenu.show(zielTableView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
			}
			else
			{
				contextMenu.hide();
			}
		});
	}
}