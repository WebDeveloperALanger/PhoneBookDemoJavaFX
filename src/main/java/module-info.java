module guru.langer.phonebookdemojavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens guru.langer.phonebookdemojavafx to javafx.fxml;
    exports guru.langer.phonebookdemojavafx;
    exports guru.langer.phonebookdemojavafx.model;
    opens guru.langer.phonebookdemojavafx.model to javafx.fxml;
    exports guru.langer.phonebookdemojavafx.controller;
    opens guru.langer.phonebookdemojavafx.controller to javafx.fxml;
}