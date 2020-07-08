package BlueMatch;

import BlueMatch.model.Aanvraag;
import BlueMatch.model.Broker;
import BlueMatch.model.Datasource;
import BlueMatch.model.Medewerker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static java.time.LocalDate.parse;

public class AddAanvraagController {
    String statusklanttmp;

    @FXML
    private ChoiceBox<String> statusklantBox;
    @FXML
    private ChoiceBox<String> selectBrokerBox;
    @FXML
    private DatePicker datePickAanvraagDate;
    @FXML
    private DatePicker datePickerStartDate;
    @FXML
    private Label Brokerlbl;

    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Vrijblijvend aanbieden",
                    "Aanbieden via broker",
                    "Aanbieden bij eindklant",
                    "(Nog) niet aanbieden",
                    "Anders"

            );

    @FXML
    private void initialize() {
if (Controller.typeofaddaanvraag=="new") {
    ChoiceDialog<String> dialog = new ChoiceDialog("Vrijblijvend aanbieden", options);
    dialog.setTitle("");
    dialog.setHeaderText("Hoe ga je  op deze aanvraag aanbieden ");
    //dialog.setContentText("contenttext:");

// Traditional way to get the response value.
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
        if (result.get().equals("Aanbieden bij eindklant")) {
            System.out.println("laat broker niet zien");
            selectBrokerBox.setVisible(false);
            BrokerField.setText("");
            BrokerField.setEditable(false);
            selectBrokerBox.setValue("");
        } else {
            System.out.println("laat broker zien");
            selectBrokerBox.setVisible(true);
            ObservableList<String> optionsbrkr = populateBrokerNameList();
            selectBrokerBox.setItems(optionsbrkr);
        }
        statusklanttmp=result.get();
        statusklantBox.setVisible(false);
    }
}else {
    statusklantBox.setItems(options);
//    statusklantBox.setValue("Nieuw");
    statusklantBox.setVisible(true);
}
        ObservableList<String> optionsbrkr = populateBrokerNameList();
        selectBrokerBox.setItems(optionsbrkr);
    }


    @FXML
    private TextField BrokerField;
    @FXML
    private TextField ContactField;
    @FXML
    private TextField FunctieField;
    @FXML
    private TextField UrenPerWeekField;
    //    @FXML
//    private TextField StatusKlantField;
    @FXML
    private TextField DatumAanvraagField;
    @FXML
    private TextField LocatieField;
    @FXML
    private TextField StartDatumField;
    @FXML
    private TextArea OpmerkingField;
    @FXML
    private TextField KlantField;
    @FXML
    private TextField LinkField;
    @FXML
    private TextField TariefField;
    @FXML
    private Label Dialogue;
    @FXML
    private TableView<Aanvraag> aanvraagTable;

    private ObservableList populateBrokerNameList() {
        ArrayList<String> brokernaamlist = new ArrayList<String>();
        ObservableList<Broker> medewerkerslijst = FXCollections.observableArrayList(Datasource.getInstance().queryBroker());

        for (Broker huidigebroker : medewerkerslijst) {
            brokernaamlist.add(huidigebroker.getBrokernaam());
        }
        ObservableList<String> options =
                FXCollections.observableArrayList(brokernaamlist
                );

        return options;
    }


    Aanvraag getNewAanvraag() {
        //SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");

        String datumaanvraag = "";
        String startdatum = "";

        String broker = selectBrokerBox.getValue();
        String contact = ContactField.getText();
        String functie = FunctieField.getText();
        String urenperweek = UrenPerWeekField.getText();
        //String statusklant = statusklantBox.getValue();
        if (datePickAanvraagDate.getValue() != null) {
            datumaanvraag = datePickAanvraagDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        String locatie = LocatieField.getText();
        if (datePickerStartDate.getValue() != null) {
            startdatum = datePickerStartDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        String opmerking = OpmerkingField.getText();

        String klant = KlantField.getText();

        String link = LinkField.getText();
        String tarief = TariefField.getText();

        Aanvraag newAanvraag = new Aanvraag();

        newAanvraag.setRefbroker(broker);
        System.out.println(broker);
        newAanvraag.setRefcontact(contact);
        newAanvraag.setFunctie(functie);
        newAanvraag.setVraagurenweek(urenperweek);
        newAanvraag.setStatusklant(statusklanttmp);
        newAanvraag.setDatumaanvraag(datumaanvraag);
        newAanvraag.setLocatie(locatie);
        newAanvraag.setStartdatum(startdatum);
        newAanvraag.setOpmerking(opmerking);
        newAanvraag.setRefklant(klant);
        newAanvraag.setLinkaanvraag(link);
        newAanvraag.setTariefaanvraag(tarief);

        return newAanvraag;
    }
    void editAanvraag(Aanvraag aanvraag, String type) {
        //get values for showing in dialogue
        if (type=="update"){
            Dialogue.setText("Aanvraag wijzigen");
            //System.out.println("update selected");
        }
        else {
            Dialogue.setText("Aanvraag verwijderen ?");
            //System.out.println("delete selected");
        }

        BrokerField.setText(aanvraag.getRefbroker());
        ContactField.setText(aanvraag.getRefcontact());


        FunctieField.setText(aanvraag.getFunctie());
        selectBrokerBox.setValue(aanvraag.getRefbroker());
        System.out.println(aanvraag.getRefbroker());
        UrenPerWeekField.setText(aanvraag.getVraagurenweek());
        statusklantBox.setValue(aanvraag.getStatusklant());
        //StartDatumField.setText(aanvraag.getStartdatum());
        LocatieField.setText(aanvraag.getLocatie());
        //DatumAanvraagField.setText(aanvraag.getDatumaanvraag());
        if ((aanvraag.getStartdatum()).isEmpty()==true) {} else {

            datePickerStartDate.setValue(LOCAL_DATE(aanvraag.getStartdatum()));}

        if (aanvraag.getDatumaanvraag().isEmpty()==true) {}else{

            datePickAanvraagDate.setValue(LOCAL_DATE(aanvraag.getDatumaanvraag()));}

        OpmerkingField.setText(aanvraag.getOpmerking());

        KlantField.setText(aanvraag.getRefklant());
        LinkField.setText(aanvraag.getLinkaanvraag());
        TariefField.setText(aanvraag.getTariefaanvraag());
    }

    void updateAanvraag(Aanvraag aanvraag){

          aanvraag.setRefbroker(selectBrokerBox.getValue());
        //System.out.println(selectBrokerBox.getValue());
          aanvraag.setRefcontact(ContactField.getText());
          aanvraag.setFunctie(FunctieField.getText());
          aanvraag.setVraagurenweek(UrenPerWeekField.getText());
          aanvraag.setStatusklant(statusklantBox.getValue());
          if (datePickAanvraagDate.getValue() != null) {
              aanvraag.setDatumaanvraag(datePickAanvraagDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
          }
          aanvraag.setLocatie(LocatieField.getText());
        if (datePickerStartDate.getValue() != null) {
            aanvraag.setStartdatum(datePickerStartDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
          aanvraag.setOpmerking(OpmerkingField.getText());

          aanvraag.setRefklant(KlantField.getText());
          aanvraag.setLinkaanvraag(LinkField.getText());
          aanvraag.setTariefaanvraag(TariefField.getText());
    }

    private static LocalDate LOCAL_DATE(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate localDate = parse(dateString, formatter);
        return localDate;
    }
}

