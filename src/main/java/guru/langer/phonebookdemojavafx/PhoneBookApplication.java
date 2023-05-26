package guru.langer.phonebookdemojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PhoneBookApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PhoneBookApplication.class.getResource("phone-book-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PhoneBookJavaFX demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}