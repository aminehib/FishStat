# 🐟 FishStatistics

> Application Java d'analyse de données biologiques marines avec gestion complète du cycle de traitement des données

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-11%2B-blue)](https://www.java.com)

## 📋 Description

**FishStatistics** est une application Java modulable permettant de :
- 📊 Charger et parser des relevés CSV de populations de poissons
- 🧹 Nettoyer et valider les données biologiques marines
- 📈 Compléter les valeurs manquantes (moyenne arithmétique ou régression linéaire)
- 📉 Générer des visualisations graphiques (boîtes à moustaches, graphiques SVG)
- ✅ Tester et valider l'intégrité des données via une suite de tests complète

## 🎯 Caractéristiques Principales

- **Gestion robuste des erreurs** : Exceptions personnalisées pour validation des données
- **Architecture modulaire** : Séparation claire des responsabilités (model, tools, traitements, files)
- **Traitement flexible** : Plusieurs stratégies de complètion de données
- **Visualisation** : Export SVG des résultats d'analyse
- **Tests complets** : Tests unitaires et d'intégration pour tous les modules

## 📁 Structure du Projet

```
src/
├── model/              # Modèles de données (Fish, Population, DataFrame, Data)
├── tools/              # Outils d'analyse (MeanValue, LinearRegression, BoiteAMoustaches)
├── traitements/        # Stratégies de traitement et complètion
├── files/              # Parsers et lecteurs (CsvReader)
├── graphique/          # Génération de graphiques (SvgGenerator)
├── exceptions/         # Exceptions personnalisées
├── interfaces/         # Contrats d'implémentation (Cleanable)
├── tests/              # Tests unitaires et d'intégration
└── resources/          # Fichiers de données CSV
```

## 🛠️ Technologies

- **Java 11+**
- **JUnit** - Tests unitaires
- **Maven** - Gestion des dépendances (optionnel)

## 🚀 Démarrage Rapide

### Prérequis
- Java 11 ou supérieur
- Un IDE Java (IntelliJ IDEA, Eclipse, VS Code avec Extension Pack for Java)

### Compilation

```bash
javac -d bin src/**/*.java
```

### Exécution

```bash
java -cp bin tests.Main
```

### Tests

```bash
# Tests unitaires
java -cp bin:lib/junit-4.13.jar org.junit.runner.JUnitCore tests.unit.*

# Tests d'intégration
java -cp bin:lib/junit-4.13.jar org.junit.runner.JUnitCore tests.integration.*
```

## 📊 Exemples d'Utilisation

### Chargement de données

```java
DataFrame df = new DataFrame();
df.readCSV("data/poissons.csv");
```

### Complètion de données manquantes

```java
Traitement traitement = new MeanValueCompletion();
df.traiter(traitement);
```

### Génération de visualisation

```java
SvgGenerator generator = new SvgGenerator();
generator.generateChart(df, "output.svg");
```

## 📝 Fichiers de Données

Le projet inclut des fichiers CSV d'exemple :
- `anis.csv`
- `mackerel.97442.csv` - Données sur les maquereaux
- `merlu2018_75164.csv` - Données sur le merlu
- `valid.csv` - Données valides pour les tests

## 🧪 Tests

La suite de tests couvre :
- **Parsing CSV** : `TestCsvParser`, `DataFrameReadCsvTest`, `FilesTest`
- **Calculs statistiques** : `MeanValueTest`, `BoiteAMoustachesTest`
- **Intégration complète** : `IntegrationFilesToMeanValueTest`

## 🔧 Architecture et Design

### Interfaces Principales
- `Cleanable` : Contrat pour le nettoyage des données
- `Traitement` : Contrat pour les stratégies de traitement

### Hiérarchie des Modèles
```
Data (abstract)
  ├── Fish
  └── Population
```

### Gestion des Erreurs
- `InvalidAttribute` - Attribut invalide
- `InvalidFileFormat` - Format de fichier incorrect
- `InvalidParametreLength` - Longueur de paramètre invalide
- `InvalidPopulation` - Population invalide

## 📄 Licence

Ce projet est sous licence **MIT**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

```
MIT License
Copyright (c) 2026 HIBAOUI Mohammed Amine
```

## 👤 Auteur

**HIBAOUI Mohammed Amine**  
- GitHub: [@anis0yahi](https://github.com/anis0yahi)
- Project: [FishStatistics](https://github.com/anis0yahi/FishStatistics)

## 🤝 Contribution

Les contributions sont bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commiter vos changements (`git commit -m 'Add AmazingFeature'`)
4. Pusher vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une [issue](https://github.com/anis0yahi/FishStatistics/issues).

---

**Dernière mise à jour**: Avril 2026
