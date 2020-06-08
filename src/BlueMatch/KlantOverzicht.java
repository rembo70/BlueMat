package BlueMatch;

import BlueMatch.model.Datasource;
import BlueMatch.model.Klant;
import BlueMatch.model.Medewerker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class KlantOverzicht {

    private Scene ParentScene;
    @FXML
    private TableColumn columnklantnaam;
    @FXML
    private TableColumn columnklantcontactpersoon;
    @FXML
    private TableColumn columnklantcontacttelnr;
    @FXML
    private TableColumn columnklantcontactemail;
    @FXML
    private TableColumn columnklantopmerking;


    private Controller parentController;

    public void setParentScene(Scene scene) {
        this.ParentScene = scene;
    }

    public void setParentController(Controller controller) {
        this.parentController = controller;
    }

    public void changeSceneMain(ActionEvent event) throws IOException {

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene((ParentScene));
        window.show();
        parentController.updateMainView();
        Main.windowWidth = (int) window.getWidth();
    }


    @FXML
    public void addKlant(ActionEvent event) throws IOException, SQLException {
        //System.out.println("add medewerker");

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addKlant.fxml"));
        dialog.getDialogPane().setContent(loader.load());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AddKlantController addKlantController = loader.getController();
                Klant klant = addKlantController.getNewKlant();
                Datasource.getInstance().klantToevoegen(klant);
            }
        }
        //updateMainView();
        ObservableList<Klant> Klantlist = FXCollections.observableArrayList(Datasource.getInstance().queryKlant());
        klantTable.itemsProperty().unbind();
        klantTable.setItems(Klantlist);
        updateView();


    }

    @FXML
    private TableView<Klant> klantTable;

    public void tableViewMouseClicked(MouseEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Main.windowWidth = (int) window.getWidth();
        updateView();
    }

    public void updateView() {
        double Kolumnwidthklantnaam = (columnklantnaam.widthProperty().getValue()) / 4.9;
        double Kolumnwidthklantcontactpersoon = (columnklantcontactpersoon.widthProperty().getValue()) / 4.9;
        double Kolumnwidthklantcontacttelnr = (columnklantcontacttelnr.widthProperty().getValue()) / 4.9;
        double Kolumnwidthklantcontactemail = (columnklantcontactemail.widthProperty().getValue()) / 4.9;
        double Kolumnwidthklantopmerking = (columnklantopmerking.widthProperty().getValue()) / 4.9;


        ObservableList<Klant> klantenlist = FXCollections.observableArrayList(Datasource.getInstance().queryKlant());

        for (Klant huidigeklant : klantenlist) {
            if (!(huidigeklant.getKlantnaam() == null))
                huidigeklant.setKlantnaam(Editaanvraag.lineWrap(huidigeklant.getKlantnaam(), (int) Kolumnwidthklantnaam));
            if (!(huidigeklant.getKlantcontactpersoon() == null)) {
                huidigeklant.setKlantcontactpersoon(Editaanvraag.lineWrap(huidigeklant.getKlantcontactpersoon(), (int) Kolumnwidthklantcontactpersoon));
            }
            if (!(huidigeklant.getKlantcontacttelnr() == null)) {
                huidigeklant.setKlantcontacttelnr(Editaanvraag.lineWrap(huidigeklant.getKlantcontacttelnr(), (int) Kolumnwidthklantcontacttelnr));
            }
            if (!(huidigeklant.getKlantcontactemail() == null)) {
                huidigeklant.setKlantcontactemail(Editaanvraag.lineWrap(huidigeklant.getKlantcontactemail(), (int) Kolumnwidthklantcontactemail));
            }
            if (!(huidigeklant.getKlantopmerking() == null)) {
                huidigeklant.setKlantopmerking(Editaanvraag.lineWrap(huidigeklant.getKlantopmerking(), (int) Kolumnwidthklantopmerking));
            }

            klantTable.itemsProperty().unbind();
            klantTable.setItems(klantenlist);
        }
    }

    public void listKlanten() {
        Task<ObservableList<Klant>> task = new GetAllKlantenTask();
        klantTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
}
}
    class GetAllKlantenTask extends Task {

        @Override
        public ObservableList<Klant> call() {

            ObservableList<Klant> klantenList = FXCollections.observableArrayList(Datasource.getInstance().queryKlant());
            return klantenList;

        }
    }

