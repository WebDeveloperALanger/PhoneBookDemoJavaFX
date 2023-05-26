module guru.langer.phonebookdemojavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens guru.langer.phonebookdemojavafx to javafx.fxml;
    exports guru.langer.phonebookdemojavafx;
}