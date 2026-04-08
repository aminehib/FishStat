package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.crypto.Data;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import exceptions.InvalidParametreLength;
import files.CsvReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;
import traitements.Traitement;

public class MainView {

    private final BorderPane root = new BorderPane();
    private DataFrame<Fish> dataFrame = new DataFrame<>();
    private final ObservableList<Fish> data = FXCollections.observableArrayList();

    private final TableView<Fish> table = new TableView<>();
    private final TextArea statsArea = new TextArea();
    private final TextArea detailArea = new TextArea();
    private File currentFile;
    private String lastCompletion = "Aucune";

    public MainView() {
        buildLayout();
        buildTable();
        hookSelection();
        refreshView();
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

        Button meanBtn = new Button("Completion moyenne");
        meanBtn.setOnAction(e -> onMeanValue());

        Button regBtn = new Button("Completion regression");
        regBtn.setOnAction(e -> onRegression());

        Button cleanBtn = new Button("Nettoyage");
        cleanBtn.setOnAction(e -> onClean());

        Button boxPlotBtn = new Button("Boite a moustaches");
        boxPlotBtn.setOnAction(e -> onBoxPlot());

        Button chartBtn = new Button("Graphique regression");
        chartBtn.setOnAction(e -> onRegressionChart());

        HBox actions = new HBox(10, meanBtn, regBtn, cleanBtn, boxPlotBtn, chartBtn);
        actions.setPadding(new Insets(10));
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(topBar, actions);
        root.setTop(top);

        statsArea.setEditable(false);
        statsArea.setWrapText(true);
        detailArea.setEditable(false);
        detailArea.setWrapText(true);

        VBox right = new VBox(10, new Label("Resume"), statsArea, new Label("Details"), detailArea);
        right.setPadding(new Insets(10));
        right.setPrefWidth(340);
        VBox.setVgrow(statsArea, Priority.ALWAYS);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        root.setCenter(table);
        root.setRight(right);
        BorderPane.setMargin(table, new Insets(10));
    }

    private void buildTable() {
        TableColumn<Fish, String> speciesCol = new TableColumn<>("Espece");
        speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));
        speciesCol.setPrefWidth(150);

        TableColumn<Fish, Double> lengthCol = new TableColumn<>("Longueur");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthCol.setPrefWidth(100);

        TableColumn<Fish, Double> weightCol = new TableColumn<>("Poids");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightCol.setPrefWidth(100);

        TableColumn<Fish, Double> sizeCol = new TableColumn<>("Taille");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(100);

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
            currentFile = file;
            String separator = detectSeparator(file);
            
            dataFrame.setData(CsvReader.readCsv(file.getAbsolutePath(), separator, ",",100,buildFishHeaders(), Fish.class));
        } catch (IOException e) {
            InfoDialogs.showError("Erreur de lecture", "Impossible de lire le fichier : " + e.getMessage());
        } catch (InvalidFileFormat e) {
            InfoDialogs.showError("Format invalide", "Le fichier ne respecte pas le format attendu : " + e.getMessage());
        }catch (InvalidAttribute e) {
            InfoDialogs.showError("Attribut invalide", "Le fichier contient un attribut inconnu : " + e.getMessage());
        }
        refreshView();
    }

    private void onMeanValue() {
        if (!hasData()) return;

        MeanValueCompletion mean = new MeanValueCompletion();
        mean.complete(dataFrame);
        lastCompletion = "Moyenne";
        refreshView();
        statsArea.setText(buildSummary());
    }

    private void onRegression() {
        if (!hasData()) return;

        LinearRegressionCompletion regression = new LinearRegressionCompletion();
        regression.complete(dataFrame);
        lastCompletion = "Regression lineaire";
        refreshView();
        statsArea.setText(buildSummary());
    }

    private void onClean() {
        if (!hasData()) return;

        Traitement traitement = new MeanValueCompletion();
        Double[] errors = {0.0, 0.0, 0.0, 0.0};
        try {
            traitement.clean(dataFrame, errors);
            refreshView();
            statsArea.setText(buildSummary());
        } catch (InvalidParametreLength e) {
            InfoDialogs.showError("Erreur de nettoyage", e.getMessage());
        }
    }

    private void onBoxPlot() {
        if (!hasData()) return;
        BoxPlotView.showBoxPlot(dataFrame.getData(), "Taux d'infestation des poissons charges");
    }

    private void onRegressionChart() {
        if (!hasData()) return;
        RegressionChartView.showRegressionChart(dataFrame.getData());
    }

    private boolean hasData() {
        if (!data.isEmpty()) return true;
        InfoDialogs.showInfo("Aucune donnee", "Chargez un fichier CSV d'abord.");
        return false;
    }

    private void refreshView() {
        data.setAll(dataFrame.getData());
        table.refresh();
        Fish selected = table.getSelectionModel().getSelectedItem();
        detailArea.setText(selected == null ? "" : buildFishDetails(selected));
    }

    private LinkedHashMap<String, String> buildFishHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Espèce", "Species");
        headers.put("Species", "Species");
        headers.put("longueur", "Length");
        headers.put("Length", "Length");
        headers.put("Masse g", "Weight");
        headers.put("Weight", "Weight");
        headers.put("LT mm", "Size");
        headers.put("Size", "Size");
        headers.put("Prevalence", "InfestationRate");
        headers.put("InfestationRate", "InfestationRate");
        headers.put("contenu", "Content");
        headers.put("Content", "Content");
        return headers;
    }

    private String detectSeparator(File file) throws IOException {
        String firstLine = Files.readAllLines(file.toPath()).stream().findFirst().orElse("");
        if (firstLine.contains(";")) return ";";
        return ",";
    }

    private String buildSummary() {
        ArrayList<Fish> fishes = dataFrame.getData();
        int total = fishes.size();
        int missingRate = 0;
        int missingLength = 0;
        int missingWeight = 0;
        int missingSize = 0;

        for (Fish fish : fishes) {
            if (fish.getInfestationRate() == null) missingRate++;
            if (fish.getLength() == null) missingLength++;
            if (fish.getWeight() == null) missingWeight++;
            if (fish.getSize() == null) missingSize++;
        }

        String fileName = currentFile == null ? "Aucun" : currentFile.getName();

        return "Fichier : " + fileName + "\n"
            + "Lignes : " + total + "\n"
            + "Especes : " + dataFrame.getSpecies().size() + "\n"
            + "Completion : " + lastCompletion + "\n"
            + "TI manquants : " + missingRate + "\n"
            + "Longueurs manquantes : " + missingLength + "\n"
            + "Poids manquants : " + missingWeight + "\n"
            + "Tailles manquantes : " + missingSize;
    }

    private String buildFishDetails(Fish fish) {
        return "Espece : " + fish.getSpecies() + "\n"
            + "Longueur : " + fish.getLength() + "\n"
            + "Poids : " + fish.getWeight() + "\n"
            + "Taille : " + fish.getSize() + "\n"
            + "Taux d'infestation : " + fish.getInfestationRate() + "\n"
            + "Contenu : " + fish.getContent();
    }
}
