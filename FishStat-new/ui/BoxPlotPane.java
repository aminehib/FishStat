package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Fish;
import tools.BoiteAMoustaches;

public class BoxPlotPane extends VBox {
    private final Canvas canvas = new Canvas(700, 220);
    private final Label emptyLabel = new Label("Aucune donnée pour la boîte à moustaches.");
    private ArrayList<Double> values = new ArrayList<>();

    public BoxPlotPane() {
        super(10);
        getChildren().add(canvas);
        canvas.widthProperty().addListener((obs, o, n) -> draw());
        canvas.heightProperty().addListener((obs, o, n) -> draw());
    }

    public void setData(List<Fish> fishes) {
        values = new ArrayList<>();
        for (Fish f : fishes) {
            Double v = f.getInfestationRate();
            if (v != null && !v.isNaN() && !v.isInfinite()) values.add(v);
        }
        draw();
    }

    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, w, h);

        if (values == null || values.isEmpty()) {
            getChildren().remove(emptyLabel);
            getChildren().add(emptyLabel);
            return;
        } else {
            getChildren().remove(emptyLabel);
        }

        BoiteAMoustaches box = new BoiteAMoustaches(values);
        double q1 = box.getPremierQuantile();
        double q2 = box.getMediane();
        double q3 = box.getDernierQuantile();
        if (Double.isNaN(q1) || Double.isNaN(q2) || Double.isNaN(q3)) {
            getChildren().remove(emptyLabel);
            getChildren().add(new Label("Valeurs invalides (NaN) pour la boîte à moustaches."));
            return;
        }
        double whiskerMin = box.getMoustacheInf();
        double whiskerMax = box.getMoustacheSup();

        double min = values.stream().mapToDouble(v -> v).min().orElse(q1);
        double max = values.stream().mapToDouble(v -> v).max().orElse(q3);
        if (whiskerMin > min) whiskerMin = min;
        if (whiskerMax < max) whiskerMax = max;

        double padding = 30;
        double plotLeft = padding;
        double plotRight = w - padding;
        double plotTop = padding;
        double plotBottom = h - padding;
        double plotHeight = plotBottom - plotTop;

        double scale = (max - min) == 0 ? 1 : plotHeight / (max - min);
        double yQ1 = plotBottom - (q1 - min) * scale;
        double yQ2 = plotBottom - (q2 - min) * scale;
        double yQ3 = plotBottom - (q3 - min) * scale;
        double yMin = plotBottom - (whiskerMin - min) * scale;
        double yMax = plotBottom - (whiskerMax - min) * scale;

        double centerX = (plotLeft + plotRight) / 2.0;
        double boxWidth = 120;

        g.setStroke(Color.BLACK);
        g.setLineWidth(2);

        // whiskers
        g.strokeLine(centerX, yMin, centerX, yQ1);
        g.strokeLine(centerX, yQ3, centerX, yMax);
        g.strokeLine(centerX - boxWidth / 4, yMin, centerX + boxWidth / 4, yMin);
        g.strokeLine(centerX - boxWidth / 4, yMax, centerX + boxWidth / 4, yMax);

        // box
        g.setFill(Color.web("#d0e3f3"));
        g.fillRect(centerX - boxWidth / 2, yQ3, boxWidth, yQ1 - yQ3);
        g.setStroke(Color.BLACK);
        g.strokeRect(centerX - boxWidth / 2, yQ3, boxWidth, yQ1 - yQ3);

        // median
        g.setStroke(Color.web("#1f77b4"));
        g.setLineWidth(3);
        g.strokeLine(centerX - boxWidth / 2, yQ2, centerX + boxWidth / 2, yQ2);
    }
}
