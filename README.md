
# 🐟 FishStat - Statistical Analysis of Fish Population Health

FishStat is a comprehensive desktop application for statistical analysis of fish population health and parasitic infestation patterns. It processes biometric data (length, weight, size) and correlates it with parasitic infection metrics across different fish species using advanced statistical methods and machine learning techniques.

## 🎯 Overview

This application is designed for marine biologists and data scientists to explore and analyze parasitic infestation patterns in fish populations through:
- **Data Import & Cleaning**: Robust CSV parsing with missing data handling
- **Statistical Analysis**: Regression, correlation, clustering, and descriptive statistics
- **Data Imputation**: Multiple strategies (mean, linear regression, polynomial regression, K-means)
- **Visualization**: SVG-based scatter plots with regression overlays
- **User-Friendly GUI**: JavaFX-based desktop interface supporting dual analysis modes

## ✨ Key Features

- ✅ **CSV Import with Dynamic Header Mapping** - Customize how CSV columns map to fish attributes
- ✅ **4 Data Completion Strategies** - Mean imputation, linear regression, polynomial regression (degree 2), K-means clustering
- ✅ **Outlier Detection** - Box-plot whisker method for statistical cleaning
- ✅ **Comprehensive Statistical Tools**:
  - Linear & Polynomial Regression (with least-squares fitting)
  - Pearson Correlation & Covariance analysis
  - K-Means unsupervised clustering (2D)
  - Mean, Standard Deviation calculations
- ✅ **Data Visualization** - SVG scatter plots with regression lines and customizable grids
- ✅ **Dual Analysis Modes**:
  - Individual fish records (Fish mode)
  - Aggregated population statistics (Population mode)
- ✅ **Robust Error Handling** - Custom exceptions for validation and data integrity

## 📊 Data Processed

The application handles fish biometric measurements:
- **Espèce** (Species): Cod, Herring, Mackerel, Merlu, etc.
- **Longueur_cm** (Length): in centimeters
- **Taille_cm** (Size/Thickness): in centimeters
- **Poids_kg** (Weight): in kilograms
- **Taux_infestation** (Infestation Rate): 0-1 or 0-100%
- **Nombre_parasites** (Parasite Count): integer count
- **Contenu** (Stomach Content): multi-value observations

## 🏗️ Project Structure

```
src/
├── model/                    # Core data models
│   ├── Fish.java            # Individual fish entity
│   ├── Population.java       # Aggregated population statistics
│   └── DataFrame.java        # Generic column-based data container
├── files/
│   └── CsvReader.java        # Robust CSV parsing with validation
├── tools/                    # Statistical computation engines
│   ├── LinearRegression.java
│   ├── RegressionPoly2.java
│   ├── KMeans.java
│   ├── Pearson.java
│   ├── Covariance.java
│   ├── MeanValue.java
│   ├── StandardDeviation.java
│   └── BoiteAMoustaches.java # Outlier detection
├── traitements/              # Data completion strategies
│   ├── Traitement.java       # Abstract base strategy
│   ├── MeanValueCompletion.java
│   ├── LinearRegressionCompletion.java
│   ├── RegressionCompletion.java
│   └── KmeansCompletion.java
├── graphique/
│   └── SvgGenerator.java     # SVG visualization
├── interfaces/               # Contracts
│   ├── Data.java
│   └── Cleanable.java
├── exceptions/               # Custom exception handling
│   ├── InvalidAttribute.java
│   ├── InvalidFileFormat.java
│   ├── InvalidParametreLength.java
│   └── InvalidPopulation.java
├── ui/
│   └── App.java              # JavaFX GUI application
└── tests/                    # Test files and sample data
```

## 🛠️ Technologies & Architecture

- **Language**: Java 11+
- **UI Framework**: JavaFX 17.0.18 / 21.0.2
- **Design Patterns**: 
  - Model-View-Controller (MVC)
  - Strategy Pattern (data completion methods)
  - Abstract Factory
- **Build System**: Java compiler with module-path configuration
- **Data Format**: CSV with customizable delimiters
- **Graphics**: SVG for data visualization
- **Documentation**: Comprehensive JavaDoc in French

## 📦 Installation & Setup

### Prerequisites
- Java 11 or higher
- JavaFX SDK 17.0.18 or 21.0.2
- Linux, macOS, or Windows with Bash/MSYS support

### Building & Running

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd ProjetJava
   ```

2. **Set JavaFX SDK path** (if not in default location):
   ```bash
   export JAVAFX=/path/to/javafx-sdk/lib
   ```

3. **Run the application**:
   ```bash
   JAVAFX=/c/javafx-sdk-21.0.2/lib ./run.sh
   ```

The `run.sh` script automatically searches for JavaFX in common locations and compiles the project before launching the GUI.

## 📖 Usage

### Launching the Application
1. Run the build script as shown above
2. The JavaFX GUI will open with the main application window

### Workflow
1. **Load CSV Data**: Import fish biometric data from CSV file with custom header mapping
2. **Clean Data**: Remove invalid entries, enforce boundary conditions
3. **Complete Missing Values**: Choose one of 4 strategies to fill missing data
4. **Analyze**: Apply regression, correlation, and statistical analysis
5. **Visualize**: Generate SVG scatter plots with regression overlays
6. **Export**: View results in Fish mode (individual records) or Population mode (aggregated stats)

## 🧮 Statistical Methods

### Regression Analysis
- **Linear Regression**: `y = a*x + b` using least-squares fitting
- **Polynomial Regression (degree 2)**: `y = ax² + bx + c`

### Clustering
- **K-Means**: 2D unsupervised clustering for pattern discovery

### Descriptive Statistics
- Pearson Correlation Coefficient
- Covariance analysis
- Mean and Standard Deviation
- Box Plot whisker method for outlier detection

### Data Completion Strategies
Each strategy estimates missing values based on existing correlations:
- **Mean Imputation**: Simple column averages
- **Linear Regression Imputation**: Uses best-fit line relationships
- **Polynomial Regression Imputation**: Uses degree-2 polynomial relationships
- **K-Means Imputation**: Cluster-based nearest-neighbor interpolation

## 📋 Sample Data

The project includes real-world fish datasets:
- `mackerel.97442.csv` - Mackerel population data
- `merlu2018_75164.csv` - Merlu (Hake) population data
- Additional test files with intentional missing values for robustness testing

## 🔍 Documentation

- **JavaDoc**: Full API documentation generated in `/docs/` directory
- **Class Diagram**: See `diag.puml` for PlantUML class structure visualization
- **Presentation**: See `PRESENTATION.md` for project overview and features

## 🚀 Advanced Features

- **Customizable Validation**: Enforce infestation rates between 0-1, reject negative measurements
- **Multi-Delimiter CSV Support**: Handles various CSV formats (comma, semicolon, tab)
- **Percentage Conversion**: Automatic percentage-to-decimal conversion
- **Filter & Group Operations**: Species-based grouping and filtering utilities
- **Box Plot Analysis**: Statistical whisker-based outlier detection

## 📄 Exceptions & Error Handling

Custom exceptions for robust data validation:
- `InvalidAttribute` - Unknown or invalid column/attribute
- `InvalidFileFormat` - Malformed CSV or unsupported format
- `InvalidParametreLength` - Incorrect array/parameter dimensions
- `InvalidPopulation` - Invalid population statistics or aggregation

## 👥 Architecture Highlights

### Interfaces
- **Data**: Common contract for DataFrame entities
- **Cleanable**: Contract for cleaning/completion strategies (clean + complete methods)

### Strategy Pattern
Data completion strategies implement `Cleanable` interface:
- Pluggable algorithms for missing data imputation
- Easy to add new strategies without modifying core code

## 📝 Building & Contributing

All source files follow comprehensive JavaDoc standards with detailed French comments.

To build with custom JavaFX location:
```bash
export JAVAFX=/your/javafx/lib/path
./run.sh
```

## 📧 Support & Issues

For bugs, feature requests, or questions, please refer to the project documentation or open an issue in the repository.

---

**FishStat** - Bridging marine biology with advanced statistical analysis and machine learning techniques for parasitic infestation research.
