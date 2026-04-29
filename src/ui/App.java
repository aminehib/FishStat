package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import exceptions.InvalidParametreLength;
import files.CsvReader;
import interfaces.Data;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DataFrame;
import model.Fish;
import model.Population;
import tools.BoiteAMoustaches;
import tools.Coords;
import tools.KMeans;
import tools.LinearRegression;
import tools.Pearson;
import tools.RegressionPoly2;
import traitements.KmeansCompletion;
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;
import traitements.RegressionCompletion;
import traitements.Traitement;

public class App extends Application {

    private enum Mode {
        FISH, POPULATION
    }

    private interface TwoColumnAction {
        void run(String xColumn, String yColumn);
    }

    private interface OneColumnAction {
        void run(String column);
    }

    private interface HeaderMappingAction {
        void run(LinkedHashMap<String, String> headers, String split, String multipleSplit, int percentage);
    }

    private final BorderPane root = new BorderPane();
    private final DataFrame<Fish> fishFrame = new DataFrame<>();
    private final DataFrame<Population> populationFrame = new DataFrame<>();
    private final ObservableList<Data> rows = FXCollections.observableArrayList();
    private final TableView<Data> table = new TableView<>();
    private final TextArea resumeArea = new TextArea();
    private final TextArea detailArea = new TextArea();
    private final Label fileLabel = new Label("Aucun fichier");
    private final Label modeLabel = new Label("Mode : aucun");
    private boolean selectionListenerAdded = false;

    private Mode mode = Mode.FISH;
    private File currentFile;
    private String fileFormat = "Aucun";
    private String lastTraitement = "Aucun";

    @Override
    public void start(Stage stage) {
        buildLayout();
        buildFishTable();
        refreshView();

        Scene scene = new Scene(root, 1050, 650);
        stage.setTitle("FishStat");
        stage.setScene(scene);
        stage.show();
    }

    private void buildLayout() {
        Label title = new Label("FishStat");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button fishExampleButton = new Button("Exemple poissons");
        fishExampleButton.setOnAction(e -> showFishMapping(new File("src/tests.csv")));

        Button populationExampleButton = new Button("Exemple populations");
        populationExampleButton.setOnAction(e -> showPopulationMapping(new File("src/test3.csv")));

        Button loadFishButton = new Button("Charger poissons");
        loadFishButton.setOnAction(e -> chooseFishCsv());

        Button loadPopulationButton = new Button("Charger populations");
        loadPopulationButton.setOnAction(e -> choosePopulationCsv());

        ToolBar toolBar = new ToolBar(
                title,
                fishExampleButton,
                populationExampleButton,
                loadFishButton,
                loadPopulationButton,
                modeLabel,
                fileLabel);

        Button cleanButton = new Button("Nettoyer");
        cleanButton.setOnAction(e -> cleanFishData());

        Button meanButton = new Button("Completion moyenne");
        meanButton.setOnAction(e -> completeFishWith(new MeanValueCompletion(), "moyenne"));

        Button linearButton = new Button("Completion lineaire");
        linearButton.setOnAction(e -> completeFishWith(new LinearRegressionCompletion(), "regression lineaire"));

        Button polyCompletionButton = new Button("Completion poly2");
        polyCompletionButton.setOnAction(e -> completeFishWith(new RegressionCompletion(), "regression poly2"));

        Button kmeansCompletionButton = new Button("Completion KMeans");
        kmeansCompletionButton.setOnAction(e -> completeFishWith(new KmeansCompletion(), "KMeans"));

        HBox treatments = new HBox(10, cleanButton, meanButton, linearButton, polyCompletionButton, kmeansCompletionButton);
        treatments.setAlignment(Pos.CENTER_LEFT);
        treatments.setPadding(new Insets(10, 10, 0, 10));

        Button graphButton = new Button("Graphique regression");
        graphButton.setOnAction(e -> showRegressionCanvas());

        // Outils statistiques affiches en JavaFX avec des colonnes choisies par l'utilisateur.
        Button polyButton = new Button("Regression poly2");
        polyButton.setOnAction(e -> showPoly2Canvas());

        Button pearsonButton = new Button("Pearson");
        pearsonButton.setOnAction(e -> showPearson());

        Button kmeansButton = new Button("KMeans");
        kmeansButton.setOnAction(e -> showKMeansCanvas());

        Button boxButton = new Button("Boite a moustaches");
        boxButton.setOnAction(e -> showBoxPlot());

        HBox actions = new HBox(10, graphButton, polyButton, pearsonButton, kmeansButton, boxButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPadding(new Insets(10));

        root.setTop(new VBox(toolBar, treatments, actions));

        resumeArea.setEditable(false);
        resumeArea.setWrapText(true);
        detailArea.setEditable(false);
        detailArea.setWrapText(true);

        VBox right = new VBox(8, new Label("Resume"), resumeArea, new Label("Selection"), detailArea);
        right.setPadding(new Insets(10));
        right.setPrefWidth(320);
        VBox.setVgrow(resumeArea, Priority.ALWAYS);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        root.setCenter(table);
        root.setRight(right);
        BorderPane.setMargin(table, new Insets(10));
    }

    private void buildFishTable() {
        table.getColumns().clear();
        addColumn("Espece", "species", 190);
        addColumn("Longueur", "length", 100);
        addColumn("Poids", "weight", 100);
        addColumn("Taille", "size", 100);
        addColumn("Parasites", "parasites", 90);
        addColumn("Infestation", "infestationRate", 110);
        connectTable();
    }

    private void buildPopulationTable() {
        table.getColumns().clear();
        addColumn("Espece", "species", 190);
        addColumn("Total", "number", 80);
        addColumn("Longueur moy.", "length", 110);
        addColumn("Poids moy.", "weight", 110);
        addColumn("Taille moy.", "size", 110);
        addColumn("Parasites", "parasites", 90);
        addColumn("Infestation", "infestationRate", 110);
        addColumn("Intensite", "intensity", 90);
        addColumn("Abondance", "abondance", 90);
        connectTable();
    }

    private void connectTable() {
        table.setItems(rows);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if (!selectionListenerAdded) {
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                detailArea.setText(newValue == null ? "" : details(newValue));
            });
            selectionListenerAdded = true;
        }
    }

    private void addColumn(String title, String property, int width) {
        TableColumn<Data, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        table.getColumns().add(column);
    }

    private void chooseFishCsv() {
        File selected = chooseCsv("Choisir un fichier CSV poissons");
        if (selected != null) {
            showFishMapping(selected);
        }
    }

    private void choosePopulationCsv() {
        File selected = chooseCsv("Choisir un fichier CSV populations");
        if (selected != null) {
            showPopulationMapping(selected);
        }
    }

    private File chooseCsv(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        return chooser.showOpenDialog(root.getScene().getWindow());
    }

    private void showFishMapping(File file) {
        showHeaderMapping(
                "Mapping colonnes poissons",
                file,
                fishMappingLabels(),
                fishHeaders(),
                1,
                (headers, split, multipleSplit, percentage) -> loadFishFile(file, headers, split, multipleSplit, percentage));
    }

    private void showPopulationMapping(File file) {
        Stage stage = new Stage();
        stage.setTitle("Format population");

        Label label = new Label("Choisis le format du fichier population.");

        Button simpleButton = new Button("Format simple");
        simpleButton.setOnAction(e -> {
            stage.close();
            showSimplePopulationMapping(file);
        });

        Button longButton = new Button("Format 2");
        longButton.setOnAction(e -> {
            stage.close();
            showLongPopulationHeaderMapping(file);
        });

        HBox buttons = new HBox(10, simpleButton, longButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(10, label, buttons);
        box.setPadding(new Insets(12));

        stage.setScene(new Scene(box, 350, 120));
        stage.show();
    }

    private void showSimplePopulationMapping(File file) {
        showHeaderMapping(
                "Mapping colonnes populations",
                file,
                populationMappingLabels(),
                populationHeaders(),
                100,
                (headers, split, multipleSplit, percentage) -> loadPopulationFile(file, headers, split, multipleSplit, percentage));
    }

    private void showHeaderMapping(String title, File file, LinkedHashMap<String, String> labels,
            LinkedHashMap<String, String> defaults, int defaultPercentage, HeaderMappingAction action) {
        String detectedSplit;
        ArrayList<String> columns;
        try {
            detectedSplit = detectSeparator(file);
            columns = csvColumns(file, detectedSplit);
        } catch (IOException e) {
            showError("Lecture impossible", e.getMessage());
            return;
        }

        Stage stage = new Stage();
        stage.setTitle(title);

        LinkedHashMap<String, ComboBox<String>> choices = new LinkedHashMap<>();
        VBox box = new VBox(8, new Label("Choisis le vrai nom des colonnes du fichier."));
        box.setPadding(new Insets(12));

        for (String internal : labels.keySet()) {
            ComboBox<String> combo = columnCombo(columns);
            combo.setValue(defaultColumnFor(internal, columns, defaults));
            choices.put(internal, combo);
            box.getChildren().add(new HBox(8, new Label(labels.get(internal)), combo));
        }

        TextField splitField = new TextField(detectedSplit);
        TextField multipleSplitField = new TextField(";");
        TextField percentageField = new TextField(String.valueOf(defaultPercentage));
        box.getChildren().add(new HBox(8, new Label("Separateur colonnes"), splitField));
        box.getChildren().add(new HBox(8, new Label("Separateur multiple"), multipleSplitField));
        box.getChildren().add(new HBox(8, new Label("Diviseur infestation"), percentageField));

        Label result = new Label("");
        Button readButton = new Button("Lire le fichier");
        readButton.setOnAction(e -> {
            LinkedHashMap<String, String> headers = buildHeaderMapping(choices);
            if (headers.isEmpty()) {
                result.setText("Choisis au moins une colonne.");
                return;
            }
            int percentage;
            try {
                percentage = Integer.parseInt(percentageField.getText());
            } catch (NumberFormatException ex) {
                result.setText("Le diviseur doit etre un entier.");
                return;
            }
            if (splitField.getText().isEmpty() || multipleSplitField.getText().isEmpty() || percentage == 0) {
                result.setText("Parametres de lecture invalides.");
                return;
            }
            stage.close();
            action.run(headers, splitField.getText(), multipleSplitField.getText(), percentage);
        });

        box.getChildren().addAll(readButton, result);
        stage.setScene(new Scene(box, 470, 520));
        stage.show();
    }

    private void showLongPopulationHeaderMapping(File file) {
        String detectedSplit;
        ArrayList<String> columns;
        try {
            detectedSplit = detectSeparator(file);
            columns = csvColumns(file, detectedSplit);
        } catch (IOException e) {
            showError("Lecture impossible", e.getMessage());
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Mapping colonnes populations");

        LinkedHashMap<String, String> labels = longPopulationMappingLabels();
        LinkedHashMap<String, ComboBox<String>> choices = new LinkedHashMap<>();
        VBox box = new VBox(8, new Label("Premier mapping : colonnes du fichier."));
        box.setPadding(new Insets(12));

        for (String internal : labels.keySet()) {
            ComboBox<String> combo = columnCombo(columns);
            combo.setValue(defaultColumnFor(internal, columns, longPopulationHeaders()));
            choices.put(internal, combo);
            box.getChildren().add(new HBox(8, new Label(labels.get(internal)), combo));
        }

        TextField splitField = new TextField(detectedSplit);
        TextField multipleSplitField = new TextField(";");
        TextField percentageField = new TextField("100");
        box.getChildren().add(new HBox(8, new Label("Separateur colonnes"), splitField));
        box.getChildren().add(new HBox(8, new Label("Separateur multiple"), multipleSplitField));
        box.getChildren().add(new HBox(8, new Label("Diviseur infestation"), percentageField));

        Label result = new Label("");
        Button nextButton = new Button("Suivant");
        nextButton.setOnAction(e -> {
            LinkedHashMap<String, String> headers = buildHeaderMapping(choices);
            String parameterColumn = selectedColumnFor(choices, "Parameter");
            String valueColumn = selectedColumnFor(choices, "Value");

            if (parameterColumn == null || valueColumn == null) {
                result.setText("Il faut choisir Parametre et Valeur.");
                return;
            }
            int percentage;
            try {
                percentage = Integer.parseInt(percentageField.getText());
            } catch (NumberFormatException ex) {
                result.setText("Le diviseur doit etre un entier.");
                return;
            }
            if (splitField.getText().isEmpty() || multipleSplitField.getText().isEmpty() || percentage == 0) {
                result.setText("Parametres de lecture invalides.");
                return;
            }

            stage.close();
            showLongPopulationParameterMapping(file, headers, parameterColumn, splitField.getText(), multipleSplitField.getText(), percentage);
        });

        box.getChildren().addAll(nextButton, result);
        stage.setScene(new Scene(box, 470, 360));
        stage.show();
    }

    private void showLongPopulationParameterMapping(File file, LinkedHashMap<String, String> headers,
            String parameterColumn, String split, String multipleSplit, int percentage) {
        ArrayList<String> params;
        try {
            params = csvColumnValues(file, parameterColumn, split);
        } catch (IOException e) {
            showError("Lecture impossible", e.getMessage());
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Mapping parametres populations");

        LinkedHashMap<String, String> labels = populationParameterLabels();
        LinkedHashMap<String, ComboBox<String>> choices = new LinkedHashMap<>();
        VBox box = new VBox(8, new Label("Deuxieme mapping : valeurs de la colonne Parametre."));
        box.setPadding(new Insets(12));

        for (String internal : labels.keySet()) {
            ComboBox<String> combo = columnCombo(params);
            combo.setValue(defaultColumnFor(internal, params, populationParameters()));
            choices.put(internal, combo);
            box.getChildren().add(new HBox(8, new Label(labels.get(internal)), combo));
        }

        TextField nField = new TextField("7");
        box.getChildren().add(new HBox(8, new Label("Lignes par population"), nField));

        Label result = new Label("");
        Button readButton = new Button("Lire le fichier");
        readButton.setOnAction(e -> {
            int n;
            try {
                n = Integer.parseInt(nField.getText());
            } catch (NumberFormatException ex) {
                result.setText("N doit etre un entier.");
                return;
            }

            LinkedHashMap<String, String> paramsMapping = buildHeaderMapping(choices);
            if (paramsMapping.isEmpty()) {
                result.setText("Choisis au moins un parametre.");
                return;
            }

            stage.close();
            loadLongPopulationFile(file, headers, paramsMapping, split, multipleSplit, percentage, n);
        });

        box.getChildren().addAll(readButton, result);
        stage.setScene(new Scene(box, 470, 470));
        stage.show();
    }

    private ComboBox<String> columnCombo(ArrayList<String> values) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add("");
        combo.getItems().addAll(values);
        combo.setPrefWidth(230);
        return combo;
    }

    private String defaultColumnFor(String internal, ArrayList<String> available, LinkedHashMap<String, String> defaults) {
        for (String column : available) {
            if (internal.equals(defaults.get(column))) {
                return column;
            }
        }
        return "";
    }

    private String selectedColumnFor(LinkedHashMap<String, ComboBox<String>> choices, String internal) {
        ComboBox<String> combo = choices.get(internal);
        if (combo == null || combo.getValue() == null || combo.getValue().isEmpty()) {
            return null;
        }
        return combo.getValue();
    }

    private LinkedHashMap<String, String> buildHeaderMapping(LinkedHashMap<String, ComboBox<String>> choices) {
        LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
        for (String internal : choices.keySet()) {
            String column = choices.get(internal).getValue();
            if (column != null && !column.isEmpty()) {
                mapping.put(column, internal);
            }
        }
        return mapping;
    }

    private void loadFishFile(File file, LinkedHashMap<String, String> headers,
            String split, String multipleSplit, int percentage) {
        if (!file.exists()) {
            refreshView();
            return;
        }

        try {
            ArrayList<Fish> loaded = CsvReader.readCsv(
                    file.getAbsolutePath(),
                    split,
                    multipleSplit,
                    percentage,
                    headers,
                    Fish.class);

            if (loaded == null) {
                showError("CSV invalide", "Aucune colonne poisson reconnue.");
                return;
            }

            mode = Mode.FISH;
            fileFormat = "Poissons";
            lastTraitement = "Aucun";
            currentFile = file;
            fishFrame.setData(loaded);
            buildFishTable();
            refreshView();
        } catch (InvalidFileFormat | InvalidAttribute | NumberFormatException e) {
            showError("CSV invalide", e.getMessage());
        }
    }

    private void loadPopulationFile(File file, LinkedHashMap<String, String> headers,
            String split, String multipleSplit, int percentage) {
        if (!file.exists()) {
            refreshView();
            return;
        }

        try {
            ArrayList<Population> loaded = CsvReader.readCsv(
                    file.getAbsolutePath(),
                    split,
                    multipleSplit,
                    percentage,
                    headers,
                    Population.class);

            if (loaded == null) {
                showError("CSV invalide", "Aucune colonne population reconnue.");
                return;
            }

            mode = Mode.POPULATION;
            fileFormat = "Populations simples";
            lastTraitement = "Non applique aux populations";
            currentFile = file;
            populationFrame.setData(loaded);
            buildPopulationTable();
            refreshView();
        } catch (InvalidFileFormat | InvalidAttribute | NumberFormatException e) {
            showError("CSV invalide", e.getMessage());
        }
    }

    private void loadLongPopulationFile(File file, LinkedHashMap<String, String> headers,
            LinkedHashMap<String, String> params, String split, String multipleSplit, int percentage, int n) {
        if (!file.exists()) {
            refreshView();
            return;
        }

        try {
            ArrayList<Population> loaded = CsvReader.readCsv(
                    file.getAbsolutePath(),
                    split,
                    multipleSplit,
                    percentage,
                    headers,
                    params,
                    n,
                    Population.class);

            if (loaded == null) {
                showError("CSV invalide", "Aucune colonne population reconnue.");
                return;
            }

            mode = Mode.POPULATION;
            fileFormat = "Populations par parametres";
            lastTraitement = "Non applique aux populations";
            currentFile = file;
            populationFrame.setData(loaded);
            buildPopulationTable();
            refreshView();
        } catch (InvalidFileFormat | InvalidAttribute | NumberFormatException e) {
            showError("CSV invalide", e.getMessage());
        }
    }

    private LinkedHashMap<String, String> fishHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        addSpeciesHeaders(headers);
        headers.put("espece", "Species");
        headers.put("TrueHost", "Species");

        headers.put("longueur_cm", "Length");
        headers.put("longueur", "Length");
        headers.put("Length", "Length");
        headers.put("Standard_LengthCalc", "Length");

        headers.put("poids_kg", "Weight");
        headers.put("poids", "Weight");
        headers.put("Masse g", "Weight");
        headers.put("Weight", "Weight");

        headers.put("taille_cm", "Size");
        headers.put("taille", "Size");
        headers.put("Size", "Size");
        headers.put("LT mm", "Size");
        headers.put("StandardLength", "Size");

        headers.put("nombre_parasites", "Total_parasites");
        headers.put("NParasitesTotal", "Total_parasites");
        headers.put("Total_parasites", "Total_parasites");

        headers.put("taux_infestation", "InfestationRate");
        headers.put("InfestationRate", "InfestationRate");
        headers.put("Prevalence", "InfestationRate");

        headers.put("contenu", "Content");
        headers.put("Content", "Content");
        return headers;
    }

    private LinkedHashMap<String, String> populationHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        addSpeciesHeaders(headers);
        headers.put("TrueHost", "Species");

        headers.put("N", "Total");
        headers.put("Total", "Total");
        headers.put("Total_Fish_Examined", "Total");

        headers.put("Prevalence", "InfestationRate");
        headers.put("InfestationRate", "InfestationRate");
        headers.put("Pr\u00e9valence (%)", "InfestationRate");
        headers.put("Pr\u00c3\u00a9valence (%)", "InfestationRate");

        headers.put("Intensity", "Intensity");
        headers.put("Intensit\u00e9 moyenne (\u00e9tendue)", "Intensity");
        headers.put("Intensit\u00c3\u00a9 moyenne (\u00c3\u00a9tendue)", "Intensity");

        headers.put("LT mm", "MeanLength");
        headers.put("MeanLength", "MeanLength");
        headers.put("Longueur moyenne \u00b1 SD (cm)", "MeanLength");
        headers.put("Longueur moyenne \u00c2\u00b1 SD (cm)", "MeanLength");
        headers.put("Standard_LengthCalc", "MeanLength");

        headers.put("Masse g", "MeanWeight");
        headers.put("MeanWeight", "MeanWeight");
        headers.put("Poids moyen \u00b1 SD (g)", "MeanWeight");
        headers.put("Poids moyen \u00c2\u00b1 SD (g)", "MeanWeight");

        headers.put("MeanSize", "MeanSize");
        headers.put("Size", "MeanSize");

        headers.put("NParasitesTotal", "Total_parasites");
        headers.put("Total_parasites", "Total_parasites");
        headers.put("nombre_parasites", "Total_parasites");

        headers.put("Content", "Content");
        headers.put("contenu", "Content");
        return headers;
    }

    private LinkedHashMap<String, String> longPopulationHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        addSpeciesHeaders(headers);
        headers.put("Parametre", "Parameter");
        headers.put("Param\u00e8tre", "Parameter");
        headers.put("Param\u00c3\u00a8tre", "Parameter");
        headers.put("Parameter", "Parameter");
        headers.put("Total", "Value");
        return headers;
    }

    private LinkedHashMap<String, String> populationParameters() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("N", "Total");
        params.put("Longueur moyenne \u00b1 SD (cm)", "MeanLength");
        params.put("Longueur moyenne \u00c2\u00b1 SD (cm)", "MeanLength");
        params.put("Poids moyen \u00b1 SD (g)", "MeanWeight");
        params.put("Poids moyen \u00c2\u00b1 SD (g)", "MeanWeight");
        params.put("Pr\u00e9valence (%)", "InfestationRate");
        params.put("Pr\u00c3\u00a9valence (%)", "InfestationRate");
        params.put("Intensit\u00e9 moyenne (\u00e9tendue)", "Intensity");
        params.put("Intensit\u00c3\u00a9 moyenne (\u00c3\u00a9tendue)", "Intensity");
        return params;
    }

    private LinkedHashMap<String, String> fishMappingLabels() {
        LinkedHashMap<String, String> labels = new LinkedHashMap<>();
        labels.put("Species", "Espece");
        labels.put("Length", "Longueur");
        labels.put("Weight", "Poids");
        labels.put("Size", "Taille");
        labels.put("Total_parasites", "Parasites");
        labels.put("InfestationRate", "Infestation");
        labels.put("Content", "Contenu");
        return labels;
    }

    private LinkedHashMap<String, String> populationMappingLabels() {
        LinkedHashMap<String, String> labels = new LinkedHashMap<>();
        labels.put("Species", "Espece");
        labels.put("Total", "Total");
        labels.put("MeanLength", "Longueur moyenne");
        labels.put("MeanWeight", "Poids moyen");
        labels.put("MeanSize", "Taille moyenne");
        labels.put("Total_parasites", "Parasites");
        labels.put("InfestationRate", "Infestation");
        labels.put("Intensity", "Intensite");
        labels.put("Content", "Contenu");
        return labels;
    }

    private LinkedHashMap<String, String> longPopulationMappingLabels() {
        LinkedHashMap<String, String> labels = new LinkedHashMap<>();
        labels.put("Species", "Espece");
        labels.put("Parameter", "Colonne des parametres");
        labels.put("Value", "Colonne des valeurs");
        return labels;
    }

    private LinkedHashMap<String, String> populationParameterLabels() {
        LinkedHashMap<String, String> labels = new LinkedHashMap<>();
        labels.put("Total", "Total");
        labels.put("MeanLength", "Longueur moyenne");
        labels.put("MeanWeight", "Poids moyen");
        labels.put("MeanSize", "Taille moyenne");
        labels.put("Total_parasites", "Parasites");
        labels.put("InfestationRate", "Infestation");
        labels.put("Intensity", "Intensite");
        labels.put("Content", "Contenu");
        return labels;
    }

    private void addSpeciesHeaders(LinkedHashMap<String, String> headers) {
        headers.put("Espece", "Species");
        headers.put("Esp\u00e8ce", "Species");
        headers.put("Esp\u00c3\u00a8ce", "Species");
        headers.put("Species", "Species");
    }

    private String detectSeparator(File file) throws IOException {
        return firstLine(file).contains(";") ? ";" : ",";
    }

    private String firstLine(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line == null ? "" : line;
        }
    }

    private ArrayList<String> csvColumns(File file, String split) throws IOException {
        String[] parts = firstLine(file).split(split, -1);
        ArrayList<String> columns = new ArrayList<>();
        for (String part : parts) {
            String column = cleanCsvName(part);
            if (!column.isEmpty()) {
                columns.add(column);
            }
        }
        return columns;
    }

    private ArrayList<String> csvColumnValues(File file, String columnName, String split) throws IOException {
        String[] headers = firstLine(file).split(split, -1);
        int index = -1;
        for (int i = 0; i < headers.length; i++) {
            if (cleanCsvName(headers[i]).equals(columnName)) {
                index = i;
                break;
            }
        }

        ArrayList<String> values = new ArrayList<>();
        if (index == -1) {
            return values;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(split, -1);
                if (index < parts.length) {
                    String value = cleanCsvName(parts[index]);
                    if (!value.isEmpty() && !values.contains(value)) {
                        values.add(value);
                    }
                }
            }
        }
        return values;
    }

    private String cleanCsvName(String value) {
        String result = value == null ? "" : value.trim();
        if (result.startsWith("\"") && result.endsWith("\"") && result.length() >= 2) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }

    private DataFrame<? extends Data> currentFrame() {
        return mode == Mode.FISH ? fishFrame : populationFrame;
    }

    private boolean hasData() {
        if (!currentFrame().getData().isEmpty()) {
            return true;
        }
        showInfo("Aucune donnee", "Charge un fichier CSV avant d'afficher un graphique.");
        return false;
    }

    private boolean hasFishDataForTreatment() {
        if (mode != Mode.FISH) {
            showInfo("Traitement", "Les classes de nettoyage et completion du projet sont definies pour DataFrame<Fish>.");
            return false;
        }
        if (!fishFrame.getData().isEmpty()) {
            return true;
        }
        showInfo("Traitement", "Charge un fichier poissons avant de nettoyer ou completer.");
        return false;
    }

    private Double[] treatmentErrors() {
        return new Double[]{0.0, 0.1, 0.0, 0.1, 0.1};
    }

    private void cleanFishData() {
        if (!hasFishDataForTreatment()) {
            return;
        }

        try {
            new KmeansCompletion().clean(fishFrame, treatmentErrors());
            lastTraitement = "Nettoyage";
            refreshView();
        } catch (InvalidParametreLength e) {
            showError("Nettoyage impossible", e.getMessage());
        }
    }

    private void completeFishWith(Traitement traitement, String name) {
        if (!hasFishDataForTreatment()) {
            return;
        }

        try {
            traitement.clean(fishFrame, treatmentErrors());
            traitement.complete(fishFrame);
            lastTraitement = "Nettoyage + completion " + name;
            refreshView();
        } catch (InvalidParametreLength e) {
            showError("Traitement impossible", e.getMessage());
        } catch (RuntimeException e) {
            showError("Traitement impossible", e.getMessage());
        }
    }

    private void prepareFishForAnalysis(Traitement traitement, String name) {
        if (mode != Mode.FISH || fishFrame.getData().isEmpty()) {
            return;
        }

        try {
            traitement.clean(fishFrame, treatmentErrors());
            traitement.complete(fishFrame);
            lastTraitement = "Auto : nettoyage + completion " + name;
            refreshView();
        } catch (InvalidParametreLength e) {
            showError("Preparation impossible", e.getMessage());
        } catch (RuntimeException e) {
            showError("Preparation impossible", e.getMessage());
        }
    }

    private void cleanFishForAnalysis() {
        if (mode != Mode.FISH || fishFrame.getData().isEmpty()) {
            return;
        }

        try {
            new KmeansCompletion().clean(fishFrame, treatmentErrors());
            lastTraitement = "Auto : nettoyage";
            refreshView();
        } catch (InvalidParametreLength e) {
            showError("Preparation impossible", e.getMessage());
        }
    }

    private void refreshView() {
        rows.setAll(currentFrame().getData());
        table.refresh();
        fileLabel.setText(currentFile == null ? "Aucun fichier" : currentFile.getName());
        modeLabel.setText(mode == Mode.FISH ? "Mode : poissons" : "Mode : populations");
        resumeArea.setText(summary());

        Data selected = table.getSelectionModel().getSelectedItem();
        detailArea.setText(selected == null ? "" : details(selected));
    }

    private String summary() {
        ArrayList<? extends Data> data = currentFrame().getData();
        int missingLength = 0;
        int missingWeight = 0;
        int missingSize = 0;
        int missingParasites = 0;
        int missingRate = 0;
        HashSet<String> species = new HashSet<>();

        for (Data item : data) {
            if (item.getSpecies() != null) {
                species.add(item.getSpecies());
            }
            if (item.getLength() == null) {
                missingLength++;
            }
            if (item.getWeight() == null) {
                missingWeight++;
            }
            if (item.getSize() == null) {
                missingSize++;
            }
            if (item.getParasites() == null) {
                missingParasites++;
            }
            if (item.getInfestationRate() == null) {
                missingRate++;
            }
        }

        String label = mode == Mode.FISH ? "poissons" : "populations";
        return "Fichier : " + (currentFile == null ? "Aucun" : currentFile.getName()) + "\n"
                + "Format : " + fileFormat + "\n"
                + "Traitement : " + lastTraitement + "\n"
                + "Nombre de " + label + " : " + data.size() + "\n"
                + "Especes : " + species.size() + "\n\n"
                + "Longueurs manquantes : " + missingLength + "\n"
                + "Poids manquants : " + missingWeight + "\n"
                + "Tailles manquantes : " + missingSize + "\n"
                + "Parasites manquants : " + missingParasites + "\n"
                + "Infestations manquantes : " + missingRate;
    }

    private String details(Data item) {
        String text = "Espece : " + value(item.getSpecies()) + "\n"
                + "Longueur : " + value(item.getLength()) + "\n"
                + "Poids : " + value(item.getWeight()) + "\n"
                + "Taille : " + value(item.getSize()) + "\n"
                + "Parasites : " + value(item.getParasites()) + "\n"
                + "Infestation : " + value(item.getInfestationRate()) + "\n"
                + "Contenu : " + value(item.getContent());

        if (item instanceof Population) {
            Population population = (Population) item;
            text += "\nTotal : " + value(population.getNumber())
                    + "\nIntensite : " + value(population.getIntensity())
                    + "\nAbondance : " + value(population.getAbondance());
        }
        return text;
    }

    private String value(Object object) {
        return object == null ? "manquant" : object.toString();
    }

    private void chooseTwoColumns(String title, String defaultX, String defaultY, TwoColumnAction action) {
        Stage stage = new Stage();
        stage.setTitle(title);

        ComboBox<String> xChoice = new ComboBox<>();
        xChoice.getItems().addAll(numericColumns());
        xChoice.setValue(defaultX);

        ComboBox<String> yChoice = new ComboBox<>();
        yChoice.getItems().addAll(numericColumns());
        yChoice.setValue(defaultY);

        Label result = new Label("Choisis deux colonnes numeriques.");

        Button validateButton = new Button("Valider");
        validateButton.setOnAction(e -> {
            String xColumn = xChoice.getValue();
            String yColumn = yChoice.getValue();

            if (xColumn == null || yColumn == null) {
                result.setText("Choisis deux colonnes.");
                return;
            }

            if (xColumn.equals(yColumn)) {
                result.setText("Choisis deux colonnes differentes.");
                return;
            }

            stage.close();
            action.run(xColumn, yColumn);
        });

        VBox box = new VBox(10,
                new Label("Colonne X"),
                xChoice,
                new Label("Colonne Y"),
                yChoice,
                validateButton,
                result);
        box.setPadding(new Insets(12));

        stage.setScene(new Scene(box, 320, 240));
        stage.show();
    }

    private void chooseOneColumn(String title, String defaultColumn, OneColumnAction action) {
        Stage stage = new Stage();
        stage.setTitle(title);

        ComboBox<String> columnChoice = new ComboBox<>();
        columnChoice.getItems().addAll(numericColumns());
        columnChoice.setValue(defaultColumn);

        Label result = new Label("Choisis une colonne numerique.");

        Button validateButton = new Button("Valider");
        validateButton.setOnAction(e -> {
            String column = columnChoice.getValue();
            if (column == null) {
                result.setText("Choisis une colonne.");
                return;
            }

            stage.close();
            action.run(column);
        });

        VBox box = new VBox(10,
                new Label("Colonne"),
                columnChoice,
                validateButton,
                result);
        box.setPadding(new Insets(12));

        stage.setScene(new Scene(box, 300, 180));
        stage.show();
    }

    private void showRegressionCanvas() {
        if (!hasData()) {
            return;
        }

        chooseTwoColumns("Graphique regression", "Length", "InfestationRate", (xColumn, yColumn) -> {
            prepareFishForAnalysis(new LinearRegressionCompletion(), "regression lineaire");

            Canvas canvas = new Canvas(800, 520);
            drawRegression(canvas, currentFrame(), xColumn, yColumn);

            Stage stage = new Stage();
            stage.setTitle("Graphique regression - " + xColumn + " / " + yColumn);
            stage.setScene(new Scene(new BorderPane(canvas), 800, 520));
            stage.show();
        });
    }

    private void showPoly2Canvas() {
        if (!hasData()) {
            return;
        }

        chooseTwoColumns("Regression poly2", "Length", "InfestationRate", (xColumn, yColumn) -> {
            prepareFishForAnalysis(new RegressionCompletion(), "regression poly2");

            // On dessine la regression polynomiale dans une nouvelle fenetre.
            Canvas canvas = new Canvas(800, 520);
            drawPoly2(canvas, currentFrame(), xColumn, yColumn);

            Stage stage = new Stage();
            stage.setTitle("Regression Poly2 - " + xColumn + " / " + yColumn);
            stage.setScene(new Scene(new BorderPane(canvas), 800, 520));
            stage.show();
        });
    }

    private void showPearson() {
        if (!hasData()) {
            return;
        }

        chooseTwoColumns("Pearson", "Length", "InfestationRate", (xColumn, yColumn) -> {
            cleanFishForAnalysis();

            ArrayList<Double> xValues = new ArrayList<>(currentFrame().getColumn(xColumn));
            ArrayList<Double> yValues = new ArrayList<>(currentFrame().getColumn(yColumn));
            Double value = Pearson.pearson(xValues, yValues);

            if (value == null) {
                showInfo("Pearson", "Coefficient impossible a calculer.");
                return;
            }

            showInfo("Pearson", "Pearson " + xColumn + " / " + yColumn + " = " + format(value));
        });
    }

    private ArrayList<String> numericColumns() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Length");
        columns.add("Weight");
        columns.add("Size");
        columns.add("Parasites");
        columns.add("InfestationRate");
        return columns;
    }

    private void showKMeansCanvas() {
        if (!hasData()) {
            return;
        }

        chooseTwoColumns("KMeans", "Length", "InfestationRate", (xColumn, yColumn) -> {
            if (mode == Mode.FISH) {
                prepareFishForAnalysis(new KmeansCompletion(), "KMeans");
            }

            ArrayList<Data> valid = new ArrayList<>();
            ArrayList<? extends Data> data = currentFrame().getData();
            ArrayList<Double> originalX = new ArrayList<>(currentFrame().getColumn(xColumn));
            ArrayList<Double> originalY = new ArrayList<>(currentFrame().getColumn(yColumn));
            for (int i = 0; i < data.size(); i++) {
                if (originalX.get(i) != null && originalY.get(i) != null) {
                    valid.add(data.get(i));
                }
            }

            if (valid.size() < 2) {
                showInfo("KMeans", "Il faut au moins deux lignes avec " + xColumn + " et " + yColumn + ".");
                return;
            }

            HashSet<String> validSpecies = new HashSet<>();
            for (Data item : valid) {
                if (item.getSpecies() != null && !item.getSpecies().isEmpty()) {
                    validSpecies.add(item.getSpecies());
                }
            }

            int speciesCount = validSpecies.size();
            if (speciesCount == 0) {
                showInfo("KMeans", "Aucune espece trouvee pour choisir le nombre de groupes.");
                return;
            }
            int k = speciesCount + 1;

            DataFrame<Data> validFrame = new DataFrame<>(valid);
            ArrayList<Double> xValues = new ArrayList<>(validFrame.getColumn(xColumn));
            ArrayList<Double> yValues = new ArrayList<>(validFrame.getColumn(yColumn));

            // Le nombre de centres KMeans est le nombre d'especes presentes + 1.
            Coords[] centers = initialKMeansCenters(xValues, yValues, k);
            ArrayList<Integer> labels = runKMeansTool(centers, validFrame, xColumn, yColumn);
            if (labels == null) {
                showInfo("KMeans", "KMeans impossible avec ces donnees.");
                return;
            }

            Canvas canvas = new Canvas(800, 520);
            drawKMeans(canvas, xValues, yValues, labels, centers, xColumn, yColumn);

            Stage stage = new Stage();
            stage.setTitle("KMeans - " + k + " centres - " + speciesCount + " especes - " + xColumn + " / " + yColumn);
            stage.setScene(new Scene(new BorderPane(canvas), 800, 520));
            stage.show();
        });
    }

    private Coords[] initialKMeansCenters(ArrayList<Double> xValues, ArrayList<Double> yValues, int k) {
        Coords[] centers = new Coords[k];
        double minX = min(xValues);
        double maxX = max(xValues);
        double minY = min(yValues);
        double maxY = max(yValues);

        for (int i = 0; i < k; i++) {
            double ratio = k == 1 ? 0.5 : (double) i / (k - 1);
            centers[i] = new Coords(
                    minX + (maxX - minX) * ratio,
                    minY + (maxY - minY) * ratio);
        }
        return centers;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Integer> runKMeansTool(Coords[] centers, DataFrame<? extends Data> frame,
            String xColumn, String yColumn) {
        // La classe KMeans du dossier tools est typee avec DataFrame<Fish>.
        // Ici on ne modifie pas tools : KMeans lit seulement les colonnes numeriques du DataFrame.
        DataFrame<Fish> fishTypedFrame = (DataFrame<Fish>) (DataFrame<?>) frame;
        return KMeans.Kmeans(centers, 100, 0.001, fishTypedFrame, xColumn, yColumn);
    }

    private void drawRegression(Canvas canvas, DataFrame<? extends Data> frame, String xName, String yName) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        int marge = 70;
        int nbl = 10;
        int nbc = 10;

        g.clearRect(0, 0, width, height);

        ArrayList<Double> xValues = new ArrayList<>(frame.getColumn(xName));
        ArrayList<Double> yValues = new ArrayList<>(frame.getColumn(yName));
        for (int i = xValues.size() - 1; i >= 0; i--) {
            if (xValues.get(i) == null || yValues.get(i) == null) {
                xValues.remove(i);
                yValues.remove(i);
            }
        }

        if (xValues.size() < 2) {
            g.fillText("Donnees manquantes pour afficher la regression.", 20, 30);
            return;
        }

        LinearRegression model = new LinearRegression(xValues, yValues);
        if (model.getCoeff() == null || model.getIntercept() == null) {
            g.fillText("Regression impossible avec ces donnees.", 20, 30);
            return;
        }

        double minX = min(xValues);
        double maxX = max(xValues);
        double minY = Math.min(0.0, min(yValues));
        double maxY = max(yValues);
        double predictedMin = model.predict(minX);
        double predictedMax = model.predict(maxX);
        if (predictedMin < minY) {
            minY = predictedMin;
        }
        if (predictedMax < minY) {
            minY = predictedMax;
        }
        if (predictedMin > maxY) {
            maxY = predictedMin;
        }
        if (predictedMax > maxY) {
            maxY = predictedMax;
        }
        if (minX == maxX) {
            minX -= 1;
            maxX += 1;
        }
        if (minY == maxY) {
            minY -= 1;
            maxY += 1;
        }

        g.setStroke(Color.LIGHTGRAY);
        g.setFill(Color.BLACK);
        for (int i = 0; i <= nbl; i++) {
            double y = marge + i * (height - 2 * marge) / nbl;
            g.strokeLine(marge, y, width - marge, y);
            g.fillText(format(minY + (maxY - minY) * (nbl - i) / nbl), 8, y + 4);
        }

        for (int i = 0; i <= nbc; i++) {
            double x = marge + i * (width - 2 * marge) / nbc;
            g.strokeLine(x, marge, x, height - marge);
            g.fillText(format(minX + (maxX - minX) * i / nbc), x - 18, height - marge + 22);
        }

        g.setFill(Color.DARKGREEN);
        String title = (mode == Mode.FISH ? "Poissons : " : "Populations : ") + xName + " / " + yName;
        g.fillText(title, width / 2 - 120, 30);
        g.setFill(Color.DARKBLUE);
        g.fillText("x(" + xName + ")", width / 2 - 35, height - 15);
        g.fillText("y(" + yName + ")", width - marge + 8, height / 2);

        g.setFill(Color.BLUE);
        for (int i = 0; i < xValues.size(); i++) {
            double x = map(xValues.get(i), minX, maxX, marge, width - marge);
            double y = map(yValues.get(i), minY, maxY, height - marge, marge);
            g.fillOval(x - 4, y - 4, 8, 8);
        }

        double x1 = map(minX, minX, maxX, marge, width - marge);
        double y1 = map(model.predict(minX), minY, maxY, height - marge, marge);
        double x2 = map(maxX, minX, maxX, marge, width - marge);
        double y2 = map(model.predict(maxX), minY, maxY, height - marge, marge);
        g.setStroke(Color.RED);
        g.setLineWidth(2);
        g.strokeLine(x1, y1, x2, y2);
    }

    private void drawPoly2(Canvas canvas, DataFrame<? extends Data> frame, String xName, String yName) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        int marge = 70;
        int nbl = 10;
        int nbc = 10;

        g.clearRect(0, 0, width, height);

        // On recupere les colonnes et on ignore les lignes incompletes.
        ArrayList<Double> xValues = new ArrayList<>(frame.getColumn(xName));
        ArrayList<Double> yValues = new ArrayList<>(frame.getColumn(yName));
        for (int i = xValues.size() - 1; i >= 0; i--) {
            if (xValues.get(i) == null || yValues.get(i) == null) {
                xValues.remove(i);
                yValues.remove(i);
            }
        }

        if (xValues.size() < 3) {
            g.fillText("Il faut au moins trois points pour RegressionPoly2.", 20, 30);
            return;
        }

        // RegressionPoly2 calcule y = ax^2 + bx + c.
        RegressionPoly2 model = new RegressionPoly2(xValues, yValues);
        Double[] coeffs = model.getCoeffs();
        if (coeffs[0] == null || coeffs[1] == null || model.getIntercept() == null) {
            g.fillText("RegressionPoly2 impossible avec ces donnees.", 20, 30);
            return;
        }

        double minX = min(xValues);
        double maxX = max(xValues);
        double minY = Math.min(0.0, min(yValues));
        double maxY = max(yValues);
        int steps = 60;

        // Les bornes Y prennent aussi en compte la courbe predite.
        for (int i = 0; i <= steps; i++) {
            double x = minX + (maxX - minX) * i / steps;
            Double predicted = model.predict(x);
            if (predicted != null) {
                if (predicted < minY) {
                    minY = predicted;
                }
                if (predicted > maxY) {
                    maxY = predicted;
                }
            }
        }
        if (minX == maxX) {
            minX -= 1;
            maxX += 1;
        }
        if (minY == maxY) {
            minY -= 1;
            maxY += 1;
        }

        drawGrid(g, width, height, marge, nbl, nbc, minX, maxX, minY, maxY);

        g.setFill(Color.DARKGREEN);
        g.fillText("RegressionPoly2 : " + xName + " / " + yName, width / 2 - 130, 30);
        g.setFill(Color.BLUE);
        for (int i = 0; i < xValues.size(); i++) {
            double x = map(xValues.get(i), minX, maxX, marge, width - marge);
            double y = map(yValues.get(i), minY, maxY, height - marge, marge);
            g.fillOval(x - 4, y - 4, 8, 8);
        }

        g.setStroke(Color.PURPLE);
        g.setLineWidth(2);
        double previousX = 0;
        double previousY = 0;
        boolean hasPrevious = false;

        // La courbe est tracee comme une suite de petits segments.
        for (int i = 0; i <= steps; i++) {
            double realX = minX + (maxX - minX) * i / steps;
            Double realY = model.predict(realX);
            if (realY == null) {
                continue;
            }
            double x = map(realX, minX, maxX, marge, width - marge);
            double y = map(realY, minY, maxY, height - marge, marge);
            if (hasPrevious) {
                g.strokeLine(previousX, previousY, x, y);
            }
            previousX = x;
            previousY = y;
            hasPrevious = true;
        }
    }

    private void drawKMeans(Canvas canvas, ArrayList<Double> xValues, ArrayList<Double> yValues,
            ArrayList<Integer> labels, Coords[] centers, String xName, String yName) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        int marge = 70;
        int nbl = 10;
        int nbc = 10;

        g.clearRect(0, 0, width, height);

        // Les bornes du graphique incluent les points et les centres calcules.
        double minX = min(xValues);
        double maxX = max(xValues);
        double minY = Math.min(0.0, min(yValues));
        double maxY = max(yValues);
        for (Coords center : centers) {
            if (center.getX() < minX) {
                minX = center.getX();
            }
            if (center.getX() > maxX) {
                maxX = center.getX();
            }
            if (center.getY() < minY) {
                minY = center.getY();
            }
            if (center.getY() > maxY) {
                maxY = center.getY();
            }
        }
        if (minX == maxX) {
            minX -= 1;
            maxX += 1;
        }
        if (minY == maxY) {
            minY -= 1;
            maxY += 1;
        }

        drawGrid(g, width, height, marge, nbl, nbc, minX, maxX, minY, maxY);
        g.setFill(Color.DARKGREEN);
        g.fillText("KMeans : " + centers.length + " centres - " + xName + " / " + yName, width / 2 - 150, 30);

        Color[] colors = {
                Color.BLUE,
                Color.ORANGE,
                Color.DARKCYAN,
                Color.PURPLE,
                Color.DARKGREEN,
                Color.DEEPPINK,
                Color.BROWN,
                Color.DARKGOLDENROD,
                Color.DARKVIOLET,
                Color.CORNFLOWERBLUE
        };
        // Chaque label KMeans recoit une couleur.
        for (int i = 0; i < xValues.size(); i++) {
            int label = labels.get(i);
            Color color = colors[Math.max(0, label) % colors.length];
            double x = map(xValues.get(i), minX, maxX, marge, width - marge);
            double y = map(yValues.get(i), minY, maxY, height - marge, marge);
            g.setFill(color);
            g.fillOval(x - 4, y - 4, 8, 8);
        }

        g.setFill(Color.RED);
        // Les centres finaux sont affiches avec des carres rouges.
        for (Coords center : centers) {
            double x = map(center.getX(), minX, maxX, marge, width - marge);
            double y = map(center.getY(), minY, maxY, height - marge, marge);
            g.fillRect(x - 6, y - 6, 12, 12);
        }
    }

    private void drawGrid(GraphicsContext g, double width, double height, int marge, int nbl, int nbc,
            double minX, double maxX, double minY, double maxY) {
        g.setStroke(Color.LIGHTGRAY);
        g.setFill(Color.BLACK);

        // Grille horizontale et graduations de l'axe Y.
        for (int i = 0; i <= nbl; i++) {
            double y = marge + i * (height - 2 * marge) / nbl;
            g.strokeLine(marge, y, width - marge, y);
            g.fillText(format(minY + (maxY - minY) * (nbl - i) / nbl), 8, y + 4);
        }

        // Grille verticale et graduations de l'axe X.
        for (int i = 0; i <= nbc; i++) {
            double x = marge + i * (width - 2 * marge) / nbc;
            g.strokeLine(x, marge, x, height - marge);
            g.fillText(format(minX + (maxX - minX) * i / nbc), x - 18, height - marge + 22);
        }
    }

    private void showBoxPlot() {
        if (!hasData()) {
            return;
        }

        chooseOneColumn("Boite a moustaches", "InfestationRate", column -> {
            cleanFishForAnalysis();

            ArrayList<Double> values = new ArrayList<>();
            for (Double value : currentFrame().getColumn(column)) {
                if (value != null) {
                    values.add(value);
                }
            }

            Canvas canvas = new Canvas(760, 320);
            drawBoxPlot(canvas, values, column);

            Stage stage = new Stage();
            stage.setTitle("Boite a moustaches - " + column);
            stage.setScene(new Scene(new BorderPane(canvas), 760, 320));
            stage.show();
        });
    }

    private void drawBoxPlot(Canvas canvas, ArrayList<Double> values, String column) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        g.clearRect(0, 0, width, height);

        if (values.isEmpty()) {
            g.fillText("Aucune valeur a afficher pour " + column + ".", 20, 30);
            return;
        }

        BoiteAMoustaches box = new BoiteAMoustaches(values);
        Double q1 = box.getPremierQuantile();
        Double q2 = box.getMediane();
        Double q3 = box.getDernierQuantile();
        Double low = box.getMoustacheInf();
        Double high = box.getMoustacheSup();
        if (q1 == null || q2 == null || q3 == null || low == null || high == null) {
            g.fillText("Pas assez de valeurs pour dessiner la boite.", 20, 30);
            return;
        }

        double min = min(values);
        double max = max(values);
        if (min == max) {
            min -= 1;
            max += 1;
        }

        double whiskerLow = max;
        double whiskerHigh = min;
        for (Double value : values) {
            if (value >= low && value <= high) {
                if (value < whiskerLow) {
                    whiskerLow = value;
                }
                if (value > whiskerHigh) {
                    whiskerHigh = value;
                }
            }
        }

        double xWhiskerLow = map(whiskerLow, min, max, 70, width - 70);
        double xWhiskerHigh = map(whiskerHigh, min, max, 70, width - 70);
        double xQ1 = map(q1, min, max, 70, width - 70);
        double xQ2 = map(q2, min, max, 70, width - 70);
        double xQ3 = map(q3, min, max, 70, width - 70);
        double y = height / 2.0;

        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeLine(xWhiskerLow, y, xWhiskerHigh, y);
        g.strokeLine(xWhiskerLow, y - 15, xWhiskerLow, y + 15);
        g.strokeLine(xWhiskerHigh, y - 15, xWhiskerHigh, y + 15);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(xQ1, y - 35, xQ3 - xQ1, 70);
        g.setStroke(Color.BLACK);
        g.strokeRect(xQ1, y - 35, xQ3 - xQ1, 70);
        g.setStroke(Color.DARKRED);
        g.strokeLine(xQ2, y - 35, xQ2, y + 35);

        g.setFill(Color.BLACK);
        g.fillText("Boite a moustaches : " + column, width / 2 - 90, 30);
        g.fillText("Q1=" + format(q1), xQ1 - 15, y - 45);
        g.fillText("Mediane=" + format(q2), xQ2 - 25, y - 60);
        g.fillText("Q3=" + format(q3), xQ3 - 15, y - 45);
        g.fillText("Min=" + format(whiskerLow), xWhiskerLow - 20, y + 50);
        g.fillText("Max=" + format(whiskerHigh), xWhiskerHigh - 20, y + 50);
    }

    private double min(ArrayList<Double> values) {
        double result = values.get(0);
        for (Double value : values) {
            if (value < result) {
                result = value;
            }
        }
        return result;
    }

    private double max(ArrayList<Double> values) {
        double result = values.get(0);
        for (Double value : values) {
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    private double map(double value, double min, double max, double outMin, double outMax) {
        return outMin + (value - min) * (outMax - outMin) / (max - min);
    }

    private String format(double value) {
        return String.format("%.2f", value);
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message == null ? "Erreur inconnue" : message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
