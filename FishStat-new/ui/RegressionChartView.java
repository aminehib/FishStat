package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.Fish;
import tools.LinearRegression;

public class RegressionChartView {

    public static void showRegressionChart(List<Fish> fishes) {
        List<Fish> valid = filterValidPoints(fishes);
        if (valid.size() < 2) {
            InfoDialogs.showInfo("Régression", "Pas assez de points valides pour tracer la régression.");
            return;
        }

        double minX = valid.stream().mapToDouble(f -> f.getSize()).min().orElse(0);
        double maxX = valid.stream().mapToDouble(f -> f.getSize()).max().orElse(0);
        if (minX == maxX) {
            InfoDialogs.showInfo("Régression", "Toutes les tailles sont identiques, droite impossible.");
            return;
        }

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        for (Fish f : valid) {
            x.add(f.getSize());
            y.add(f.getInfestationRate());
        }
        LinearRegression reg = new LinearRegression(x, y);
        Double a = reg.getCoeff();
        Double b = reg.getIntercept();
        if (a == null || b == null || Double.isNaN(a) || Double.isNaN(b)) {
            InfoDialogs.showInfo("Régression", "Régression impossible (division par zéro).");
            return;
        }

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Size");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Infestation Rate");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Nuage de points + droite de régression");
        chart.setCreateSymbols(true);

        XYChart.Series<Number, Number> points = new XYChart.Series<>();
        points.setName("Données");
        for (Fish f : valid) {
            points.getData().add(new XYChart.Data<>(f.getSize(), f.getInfestationRate()));
        }

        XYChart.Series<Number, Number> line = new XYChart.Series<>();
        line.setName("Régression");
        line.getData().add(new XYChart.Data<>(minX, a * minX + b));
        line.getData().add(new XYChart.Data<>(maxX, a * maxX + b));

        chart.getData().addAll(points, line);

        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();
            if (points.getNode() != null) {
                points.getNode().setStyle("-fx-stroke: transparent;");
            }
            if (line.getNode() != null) {
                line.getNode().setStyle("-fx-stroke: #1f77b4; -fx-stroke-width: 2px;");
            }
            line.getData().forEach(d -> {
                if (d.getNode() != null) d.getNode().setVisible(false);
            });
        });

        Stage stage = new Stage();
        stage.setTitle("Régression linéaire");
        stage.setScene(new Scene(chart, 800, 500));
        stage.show();
    }

    private static List<Fish> filterValidPoints(List<Fish> fishes) {
        List<Fish> valid = new ArrayList<>();
        for (Fish f : fishes) {
            if (f.getSize() != null && f.getInfestationRate() != null) {
                valid.add(f);
            }
        }
        return valid;
    }
}
