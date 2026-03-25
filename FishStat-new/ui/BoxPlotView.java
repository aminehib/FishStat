package ui;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Fish;

public class BoxPlotView {

    public static void showBoxPlot(List<Fish> fishes, String subtitle, String details) {
        if (fishes == null || fishes.isEmpty()) {
            InfoDialogs.showInfo("Boîte à moustaches", "Aucune donnée à afficher.");
            return;
        }

        Label label = new Label(subtitle == null ? "Boîte à moustaches" : subtitle);
        label.setWrapText(true);

        BoxPlotPane pane = new BoxPlotPane();
        pane.setData(fishes);

        TextArea area = new TextArea(details == null ? "" : details);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefHeight(120);

        BorderPane root = new BorderPane();
        root.setTop(label);
        root.setCenter(pane);
        root.setBottom(area);
        Scene scene = new Scene(root, 800, 420);

        Stage stage = new Stage();
        stage.setTitle("Boîte à moustaches");
        stage.setScene(scene);
        stage.show();
    }
}
