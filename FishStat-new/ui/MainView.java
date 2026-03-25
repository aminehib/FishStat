package ui;

import java.io.File;
import java.util.ArrayList;

import exceptions.InvalidFileFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.DataFrame;
import model.Fish;
import tools.BoiteAMoustaches;
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;
import tools.LinearRegression;
import traitements.Traitement;

public class MainView {

    private final BorderPane root = new BorderPane();
    private final DataFrame dataFrame = new DataFrame();
    private final ObservableList<Fish> data = FXCollections.observableArrayList();

    private final TableView<Fish> table = new TableView<>();
    private final TextArea statsArea = new TextArea();
    private final TextArea detailArea = new TextArea();
    private String lastCompletion = "Aucune";

    public MainView() {
        buildLayout();
        buildTable();
        hookSelection();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        Label title = new Label("FishStat");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button loadBtn = new Button("Charger un fichier CSV");
        loadBtn.setOnAction(e -> onLoadCsv());

        ToolBar topBar = new ToolBar(title, new Label("  "), loadBtn);
        topBar.setStyle("-fx-background-color: #f5f5f5;");

        Button meanBtn = new Button("Complétion sans régression");
        meanBtn.setOnAction(e -> onMeanValue());

        Button regBtn = new Button("Complétion avec régression");
        regBtn.setOnAction(e -> onRegression());

        Button cleanBtn = new Button("Boîte à moustaches");
        cleanBtn.setOnAction(e -> onBoxPlot());

        HBox actions = new HBox(10, meanBtn, regBtn, cleanBtn);
        actions.setPadding(new Insets(10));
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(topBar, actions);
        root.setTop(top);

        statsArea.setEditable(false);
        statsArea.setPromptText("Résultats / statistiques...");

        detailArea.setEditable(false);
        detailArea.setPromptText("Détails du poisson sélectionné...");

        VBox right = new VBox(10, new Label("Résultats"), statsArea, new Label("Détails"), detailArea);
        right.setPadding(new Insets(10));
        right.setPrefWidth(320);
        VBox.setVgrow(statsArea, Priority.ALWAYS);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        root.setCenter(table);
        root.setRight(right);
        BorderPane.setMargin(table, new Insets(10));
    }

    private void buildTable() {
        TableColumn<Fish, String> speciesCol = new TableColumn<>("Species");
        speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));
        speciesCol.setPrefWidth(150);

        TableColumn<Fish, Double> lengthCol = new TableColumn<>("Length");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthCol.setPrefWidth(90);

        TableColumn<Fish, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightCol.setPrefWidth(90);

        TableColumn<Fish, Double> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(90);

        TableColumn<Fish, Double> rateCol = new TableColumn<>("Infestation");
        rateCol.setCellValueFactory(new PropertyValueFactory<>("infestationRate"));
        rateCol.setPrefWidth(120);

        table.getColumns().addAll(speciesCol, lengthCol, weightCol, sizeCol, rateCol);
        table.setItems(data);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void hookSelection() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel == null) {
                detailArea.clear();
                return;
            }
            detailArea.setText(buildFishDetails(newSel));
        });
    }

    private void onLoadCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir un fichier CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

        File file = chooser.showOpenDialog(root.getScene().getWindow());
        if (file == null) return;

        try {
            dataFrame.readAuto(file.getAbsolutePath());
            ArrayList<Fish> poissons = dataFrame.getData();
            data.setAll(poissons);
            statsArea.setText("Fichier chargé: " + file.getName() + "\nLignes: " + poissons.size());
        } catch (InvalidFileFormat e) {
            showError("Dataset non reconnu", e.getMessage());
        }
    }

    private void onMeanValue() {
        if (data.isEmpty()) {
            showInfo("Aucune donnée", "Chargez un fichier CSV d'abord.");
            return;
        }
        MeanValueCompletion mean = new MeanValueCompletion();
        try {
            mean.completeWithReference(dataFrame, "tests/D_collectees/processed_data_anisakis.csv");
        } catch (InvalidFileFormat e) {
            showError("Erreur référence", e.getMessage());
            return;
        }
        table.refresh();
        statsArea.setText("Complétion par moyenne terminée.");
        lastCompletion = "Moyenne";
    }

    private void onRegression() {
        if (data.isEmpty()) {
            showInfo("Aucune donnée", "Chargez un fichier CSV d'abord.");
            return;
        }
        LinearRegressionCompletion reg = new LinearRegressionCompletion();
        reg.complete(dataFrame);
        table.refresh();
        LinearRegression model = new LinearRegression(dataFrame.getSizes(), dataFrame.getInfestationRates());
        if (model.getCoeff() == null || model.getIntercept() == null) {
            statsArea.setText("Régression linéaire appliquée.\nCoefficients indisponibles.");
        } else {
            statsArea.setText("Régression linéaire appliquée.\nCoeff a=" + model.getCoeff() + " b=" + model.getIntercept());
        }
        lastCompletion = "Régression linéaire";
        RegressionChartView.showRegressionChart(data);
    }

    private void onBoxPlot() {
        if (data.isEmpty()) {
            showInfo("Aucune donnée", "Chargez un fichier CSV d'abord.");
            return;
        }
        ArrayList<Double> rates = dataFrame.getInfestationRates();
        ArrayList<Double> validRates = new ArrayList<>();
        for (Double r : rates) {
            if (r != null) validRates.add(r);
        }
        if (validRates.isEmpty()) {
            showInfo("Boîte à moustaches", "Aucune valeur d'infestation disponible.");
            return;
        }

        BoiteAMoustaches box = new BoiteAMoustaches(validRates);
        int nullBefore = countNullInfestation(rates);
        Traitement t = new MeanValueCompletion();
        t.clean(dataFrame);
        table.refresh();
        statsArea.setText(
            "Boîte à moustaches:\n" +
            "Q1=" + box.getPremierQuantile() + "\n" +
            "Q2=" + box.getMediane() + "\n" +
            "Q3=" + box.getDernierQuantile() + "\n" +
            "Moustache inf=" + box.getMoustacheInf() + "\n" +
            "Moustache sup=" + box.getMoustacheSup() + "\n" +
            "Outliers nettoyés (taux null)."
        );
        int nullAfter = countNullInfestation(dataFrame.getInfestationRates());
        String details =
            "Résumé boîte à moustaches:\n" +
            "Q1=" + box.getPremierQuantile() + "\n" +
            "Q2=" + box.getMediane() + "\n" +
            "Q3=" + box.getDernierQuantile() + "\n" +
            "Moustache inf=" + box.getMoustacheInf() + "\n" +
            "Moustache sup=" + box.getMoustacheSup() + "\n\n" +
            "Infestation null avant nettoyage: " + nullBefore + "\n" +
            "Infestation null après nettoyage: " + nullAfter + "\n";
        BoxPlotView.showBoxPlot(
            data,
            "Complétion: " + lastCompletion + " | Données après nettoyage",
            details
        );
    }

    private String buildFishDetails(Fish f) {
        StringBuilder sb = new StringBuilder();
        sb.append("Species: ").append(f.getSpecies()).append("\n");
        sb.append("Length: ").append(f.getLength()).append("\n");
        sb.append("Weight: ").append(f.getWeight()).append("\n");
        sb.append("Size: ").append(f.getSize()).append("\n");
        sb.append("Infestation Rate: ").append(f.getInfestationRate()).append("\n");
        sb.append("Content: ").append(f.getContent());
        return sb.toString();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int countNullInfestation(ArrayList<Double> values) {
        int count = 0;
        for (Double v : values) {
            if (v == null) count++;
        }
        return count;
    }
}
