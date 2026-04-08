package ui;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Fish;

public class BoxPlotView {

    public static void showBoxPlot(List<Fish> fishes, String subtitle) {
        if (fishes == null || fishes.isEmpty()) {
            InfoDialogs.showInfo("Boîte à moustaches", "Aucune donnée à afficher.");
            return;
        }

        BoxPlotPane pane = new BoxPlotPane();
        pane.setData(fishes);

        BorderPane root = new BorderPane(pane);
        if (subtitle != null && !subtitle.isEmpty()) {
            root.setTop(new javafx.scene.control.Label(subtitle));
            BorderPane.setMargin(root.getTop(), new javafx.geometry.Insets(8, 10, 0, 10));
        }
        Scene scene = new Scene(root, 800, 300);

        Stage stage = new Stage();
        stage.setTitle("Boîte à moustaches");
        stage.setScene(scene);
        stage.show();
    }
}
