package guru.langer.phonebookdemojavafx.controller;

import guru.langer.phonebookdemojavafx.model.ContactModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
public class PhoneBookController {

    @FXML
    public TextField inputName;
    @FXML
    public TextField inputPhone;
    @FXML
    public TextField inputEmail;
    @FXML
    public Button btnSave;
    @FXML
    public TextField filterInput;
    @FXML
    public Button btnCancelEditMode;
    @FXML
    ListView<ContactModel> listViewContacts;
    private ContactModel editableContact;
    private final ObservableList<ContactModel> itemsList = FXCollections.observableArrayList();


    @FXML
    public void onClickBtnSave() {

        /*
         * --- Wenn Edit Mode aktiv ist
         */
        if (isEditMode()) {

            /*
             * --- dann update
             */
            actionUpdateContact();

        } else {
            /*
             * --- sonst speichern
             */
            actionSaveContact();
        }

        /*
         * --- Button Label aktualisieren
         */
        toggleBtnSaveLabel();
    }

    private void actionUpdateContact() {

        /*
         * --- Werte von den Inputfeldern in ContactModel übernehmen.
         */
        editableContact.setName(inputName.getText());
        editableContact.setPhone(inputPhone.getText());
        editableContact.setEmail(inputEmail.getText());

        /*
         * --- Edit Mode verlassen
         */
        clearEditMode();

        /*
         * --- Liste aktualisieren, damit der aktualisierte Kontakt neu gerendert wird.
         */
        listViewContacts.refresh();

    }

    private void actionSaveContact() {

        /*
         * --- Wenn Input Name leer
         */
        if (inputName.getLength() < 1) {
            /*
             * --- Fehlermeldung & Abbruch
             */
            showInfoAlert("Bitte geben Sie einen Namen ein um den Kontakt zu speichern");
            return;
        }

        /*
         * --- Neuen Kontakt mit den Texten aus den Inputfeldern erstellen
         */
        ContactModel contactModel = new ContactModel(
                inputName.getText(),
                inputPhone.getText(),
                inputEmail.getText()
        );

        /*
         * --- Kontakt der Liste hinzufügen
         */
        itemsList.add(contactModel);

        /*
         * --- Alle Inputfelder löschen
         */
        clearInputFields();
    }

    @FXML
    void initialize() {
        btnCancelEditMode.setVisible(false);
        initListViewContacts();
        addDemoContacts();
    }

    private void initListViewContacts() {

        /*
         * --- dem ListView die ObservableList<ContactModel> übergeben
         */
        listViewContacts.setItems(itemsList);


        listViewContacts.setCellFactory(new Callback<ListView<ContactModel>, ListCell<ContactModel>>() {
            @Override
            public ListCell<ContactModel> call(ListView<ContactModel> param) {
                return new ListCell<>() {

                    /*
                     * --- Überschreiben der updateItem Methode um die Darstellung
                     * --- der Attribute aus ContactModel zu definieren
                     */
                    @Override
                    protected void updateItem(ContactModel contactModel, boolean empty) {
                        super.updateItem(contactModel, empty);

                        if (empty || contactModel == null) {
                            setText(null);
                        } else {

                            /*
                             * --- wenn kein Filtertext vorhanden
                             */
                            if (filterInput.getText().isEmpty()) {
                                /*
                                 * dann wird der Kontakt gerendert
                                 */
                                setText(contactModel.getContactAsStringWithLineBreak());

                                /*
                                 * sonst wird geprüft, ob der Kontakt den Filtertext enthält
                                 */
                            } else if (contactModel.contains(filterInput.getText().toLowerCase())) {
                                /*
                                 * --- wenn ja, dann wird der Kontakt auch gerendert, sonst nicht
                                 */
                                setText(contactModel.getContactAsStringWithLineBreak());
                            }

                        }
                    }
                };
            }
        });

        /*
         * --- Auf einen Doppelklick auf einen Listeneintrag reagieren
         */
        listViewContacts.setOnMouseClicked(event -> {
            /*
             * --- Wenn 2mal geklickt
             */
            if (event.getClickCount() == 2) {
                /*
                 * laden des geklickten Eintrags
                 */
                ContactModel selectedContact = listViewContacts.getSelectionModel().getSelectedItem();
                /*
                 * --- wenn der Eitrag nicht leer ist
                 */
                if (selectedContact != null) {
                    /*
                     * --- dann User fragen, ob dieser Kontakt bearbeitet werden soll
                     */
                    alertInfoEditContact(selectedContact);
                }
            }
        });
    }


    /**
     * Fragt den User, ob der übergene Kontakt (ContactModel) bearbeitet werden soll.
     * Wenn der User mit Ja antwortet, wird dieser zum Bearbeiten geladen (
     *
     * @param contact
     */
    private void alertInfoEditContact(ContactModel contact) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kontakt bearbeiten?");
        alert.setHeaderText(null);
        alert.setContentText(contact.getContactAsStringWithLineBreak());

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nein");

        alert.getButtonTypes().setAll(buttonTypeNo, buttonTypeYes);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                /*
                 * --- Wenn User mit Ja Button reagiert, wird der zu bearbeitende Kontakt geladen.
                 */
                loadEditableContact(contact);
            } else {
                if (isEditMode()) {
                    clearEditMode();
                }
            }
        });
    }

    private void loadEditableContact(ContactModel contact) {
        /*
         * --- speichert die Referenz des übergebenen Kontakts zum Editieren in die Klassenvariable editableContact
         */
        setEditMode(contact);

        /*
         * --- schreibt die Werte auf den Klassenvariablen von editableContact in die Textfelder
         */
        updateInputFieldsByEditableContact();

        /*
         * aktualisiert das Label des Speichern Buttons auf "Aktualisieren", da EditMode aktiv
         */
        toggleBtnSaveLabel();
    }

    /*
     * Aktualisiert das Label des Speichern Buttons.
     * Wenn EditMode Aktiv ist dann "Aktualisieren", sonst "Speichern"
     */
    private void toggleBtnSaveLabel() {
        btnSave.setText(
                isEditMode()
                        ?
                        "Aktualisieren"
                        :
                        "Speichern"

        );
    }

    /**
     * --- schreibt die Werte auf den Klassenvariablen von editableContact in die Textfelder
     */
    private void updateInputFieldsByEditableContact() {
        inputName.setText(editableContact.getName());
        inputPhone.setText(editableContact.getPhone());
        inputEmail.setText(editableContact.getEmail());
    }

    /**
     * leert alle Textfelder
     */
    private void clearInputFields() {
        inputEmail.clear();
        inputName.clear();
        inputPhone.clear();
    }


    /**
     * fügt Demo Einträge hinzu
     */
    private void addDemoContacts() {
        itemsList.add(new ContactModel("Max Mustermann", "+4912345678", "mustermann@example.org"));
        itemsList.add(new ContactModel("John Doe", "+112345678", "johndoe@example.org"));
        itemsList.add(new ContactModel("Anton", "01781111111", "Anton@example.org"));
        itemsList.add(new ContactModel("Berta", "01782222222", "Berta@example.org"));
        itemsList.add(new ContactModel("Cäsar", "01783333333", "Caesar@example.org"));
        itemsList.add(new ContactModel("Dora", "01784444444", "Dora@example.org"));
    }

    /**
     * Zeigt ein Alert Info Fenster mit der übergebenen Message an
     *
     * @param msg
     */
    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * verlässt den Edit Mode
     */
    private void clearEditMode() {
        editableContact = null;
        btnCancelEditMode.setVisible(false);
        clearInputFields();
    }

    /**
     * zeigt an, ob der Edit Mode aktiv ist
     *
     * @return true wenn Edit Mode aktiv, sonst false
     */
    private boolean isEditMode() {
        return editableContact != null;
    }

    /**
     * Aktviert den Edit Mode mit dem übergebenen Kontakt
     *
     * @param contactModel
     */
    private void setEditMode(ContactModel contactModel) {
        editableContact = contactModel;
        btnCancelEditMode.setVisible(true);
    }


    /**
     * Filter
     */
    @FXML
    private void onFilterInput() {
        String filterText = filterInput.getText().toLowerCase();

        if (filterText.isEmpty()) {
            listViewContacts.setItems(itemsList);
            return;
        }

        ObservableList<ContactModel> filteredList = FXCollections.observableArrayList();

        for (ContactModel contactModel : this.itemsList) {
            if (contactModel.getName().toLowerCase().contains(filterText)
                    ||
                    contactModel.getPhone().toLowerCase().contains(filterText)
                    ||
                    contactModel.getEmail().toLowerCase().contains(filterText)
            ) {
                filteredList.add(contactModel);
            }
        }

        listViewContacts.setItems(filteredList);

    }

    public void onCancelEditMode() {
        clearEditMode();
    }

    public void onClickHyperlink() {

        String url = "https://github.com/WebDeveloperALanger/PhoneBookDemoJavaFX";

        String os = System.getProperty("os.name").toLowerCase();

        try {

            ProcessBuilder processBuilder;
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
            } else if (os.contains("mac")) {
                processBuilder = new ProcessBuilder("open", url);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                processBuilder = new ProcessBuilder("xdg-open", url);
            } else {
                System.out.println("Your OS is not support!!");
                return;
            }
            processBuilder.start();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}