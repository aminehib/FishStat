package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.DataFrame;
import model.Fish;
import tools.LinearRegression;

public class RegressionChartView {

    public static void showRegressionChart(List<Fish> fishes) {
        ArrayList<Fish> valid = new ArrayList<>();
        for (Fish fish : fishes) {
            if (fish.getSize() != null && fish.getInfestationRate() != null) {
                valid.add(fish);
            }
        }

        if (valid.size() < 2) {
            InfoDialogs.showInfo("Regression", "Pas assez de points valides pour afficher la regression.");
            return;
        }

        DataFrame<Fish> frame = new DataFrame<>(valid);
        LinearRegression regression = new LinearRegression(frame.getSizes(), frame.getInfestationRates());
        Double a = regression.getCoeff();
        Double b = regression.getIntercept();

        if (a == null || b == null) {
            InfoDialogs.showInfo("Regression", "Regression impossible avec les donnees actuelles.");
            return;
        }

        double minX = valid.get(0).getSize();
        double maxX = valid.get(0).getSize();
        for (Fish fish : valid) {
            double x = fish.getSize();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
        }

        if (minX == maxX) {
            InfoDialogs.showInfo("Regression", "Toutes les tailles sont identiques.");
            return;
        }

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Taille");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Taux d'infestation");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Nuage de points et droite de regression");
        chart.setCreateSymbols(true);

        XYChart.Series<Number, Number> points = new XYChart.Series<>();
        points.setName("Poissons");
        for (Fish fish : valid) {
            points.getData().add(new XYChart.Data<>(fish.getSize(), fish.getInfestationRate()));
        }

        XYChart.Series<Number, Number> line = new XYChart.Series<>();
        line.setName("Regression");
        line.getData().add(new XYChart.Data<>(minX, regression.predict(minX)));
        line.getData().add(new XYChart.Data<>(maxX, regression.predict(maxX)));

        chart.getData().add(points);
        chart.getData().add(line);

        Stage stage = new Stage();
        stage.setTitle("Regression lineaire");
        stage.setScene(new Scene(chart, 800, 500));
        stage.show();
    }
}
