
#!/usr/bin/env bash

set -e

JAVAFX="${JAVAFX:-}"

if [[ -z "$JAVAFX" ]]; then
  for dir in \
    "/home/anis/Downloads/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18/lib" \
    "$HOME/Downloads/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18/lib" \
    "/c/javafx-sdk-21.0.2/lib" \
    "/mnt/c/javafx-sdk-21.0.2/lib"
  do
    if [[ -d "$dir" ]]; then
      JAVAFX="$dir"
      break
    fi
  done
fi

if [[ ! -d "$JAVAFX" ]]; then
  echo "Erreur: dossier JavaFX introuvable: $JAVAFX"
  echo "Modifie la variable JAVAFX dans run.sh pour pointer vers le dossier lib de JavaFX."
  exit 1
fi



mkdir -p out

javac --module-path "$JAVAFX" \
      --add-modules javafx.controls \
      -d out \
      $(find src -name "*.java" -not -path "src/tests/*" -not -path "src/unitaires/*")

java --module-path "$JAVAFX" \
     --add-modules javafx.controls \
     -cp out ui.App
