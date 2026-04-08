package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Fish;
import tools.BoiteAMoustaches;

public class BoxPlotPane extends Pane {

    private final Canvas canvas = new Canvas();
    private final ArrayList<Double> values = new ArrayList<>();

    public BoxPlotPane() {
        getChildren().add(canvas);
        widthProperty().addListener((obs, oldValue, newValue) -> draw());
        heightProperty().addListener((obs, oldValue, newValue) -> draw());
    }

    public void setData(List<Fish> fishes) {
        values.clear();
        for (Fish fish : fishes) {
            if (fish.getInfestationRate() != null) {
                values.add(fish.getInfestationRate());
            }
        }
        draw();
    }

    @Override
    protected void layoutChildren() {
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        draw();
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();
        if (width <= 0 || height <= 0) return;

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        if (values.isEmpty()) {
            g.setFill(Color.GRAY);
            g.fillText("Aucune donnee valide.", 20, 20);
            return;
        }

        BoiteAMoustaches box = new BoiteAMoustaches(new ArrayList<>(values));
        Double q1 = box.getPremierQuantile();
        Double q2 = box.getMediane();
        Double q3 = box.getDernierQuantile();
        Double low = box.getMoustacheInf();
        Double high = box.getMoustacheSup();

        if (q1 == null || q2 == null || q3 == null || low == null || high == null) {
            g.setFill(Color.GRAY);
            g.fillText("Donnees insuffisantes pour la boite a moustaches.", 20, 20);
            return;
        }

        double min = values.get(0);
        double max = values.get(0);
        for (Double value : values) {
            if (value < min) min = value;
            if (value > max) max = value;
        }

        if (min == max) {
            min -= 1;
            max += 1;
        }

        double minInside = max;
        double maxInside = min;
        for (Double value : values) {
            if (value >= low && value <= high) {
                if (value < minInside) minInside = value;
                if (value > maxInside) maxInside = value;
            }
        }

        double pad = 70;
        double centerY = height / 2.0;
        double boxHeight = 60;

        double xMinInside = map(minInside, min, max, pad, width - pad);
        double xMaxInside = map(maxInside, min, max, pad, width - pad);
        double xQ1 = map(q1, min, max, pad, width - pad);
        double xQ2 = map(q2, min, max, pad, width - pad);
        double xQ3 = map(q3, min, max, pad, width - pad);

        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeLine(xMinInside, centerY, xMaxInside, centerY);
        g.strokeLine(xMinInside, centerY - 12, xMinInside, centerY + 12);
        g.strokeLine(xMaxInside, centerY - 12, xMaxInside, centerY + 12);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(xQ1, centerY - boxHeight / 2, xQ3 - xQ1, boxHeight);
        g.setStroke(Color.BLACK);
        g.strokeRect(xQ1, centerY - boxHeight / 2, xQ3 - xQ1, boxHeight);

        g.setStroke(Color.DARKRED);
        g.strokeLine(xQ2, centerY - boxHeight / 2, xQ2, centerY + boxHeight / 2);

        g.setFill(Color.DARKRED);
        for (Double value : values) {
            if (value < low || value > high) {
                double x = map(value, min, max, pad, width - pad);
                g.fillOval(x - 3, centerY - 3, 6, 6);
            }
        }

        g.setFill(Color.BLACK);
        g.fillText("Q1=" + format(q1), clampLabelX(xQ1, width), centerY - boxHeight / 2 - 8);
        g.fillText("Q2=" + format(q2), clampLabelX(xQ2, width), centerY - boxHeight / 2 - 8);
        g.fillText("Q3=" + format(q3), clampLabelX(xQ3, width), centerY - boxHeight / 2 - 8);
        g.fillText("Min=" + format(minInside), clampLabelX(xMinInside, width), centerY + boxHeight / 2 + 20);
        g.fillText("Max=" + format(maxInside), clampLabelX(xMaxInside, width), centerY + boxHeight / 2 + 20);
    }

    private double map(double value, double min, double max, double outMin, double outMax) {
        return outMin + (value - min) * (outMax - outMin) / (max - min);
    }

    private String format(double value) {
        return String.format("%.4f", value);
    }

    private double clampLabelX(double x, double width) {
        if (x < 10) return 10;
        if (x > width - 60) return width - 60;
        return x - 10;
    }
}
