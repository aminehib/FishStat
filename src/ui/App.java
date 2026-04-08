package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView view = new MainView();
        Scene scene = new Scene(view.getRoot(), 1100, 700);
        stage.setTitle("FishStat");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
