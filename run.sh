
#!/usr/bin/env bash

set -e

JAVAFX="/home/anis/Downloads/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18/lib"

if [[ ! -d "$JAVAFX" ]]; then
  echo "Erreur: dossier JavaFX introuvable: $JAVAFX"
  echo "Modifie la variable JAVAFX dans run.sh pour pointer vers javafx-sdk-17/lib."
  exit 1
fi



mkdir -p out

javac --module-path "$JAVAFX" \
      --add-modules javafx.controls \
      -d out \
      $(find . -name "*.java" -not -path "./src/tests/*")

java --module-path "$JAVAFX" \
     --add-modules javafx.controls \
     -cp out ui.App