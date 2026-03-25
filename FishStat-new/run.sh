#!/usr/bin/env bash
JAVAFX="/mnt/c/Users/amine/Downloads/openjfx-21.0.10_linux-x64_bin-sdk/javafx-sdk-21.0.10/lib"


mkdir -p out

javac --module-path "$JAVAFX" \
      --add-modules javafx.controls \
      -d out \
      $(find . -name "*.java" -not -path "./tests/*")

java --module-path "$JAVAFX" \
     --add-modules javafx.controls \
     -cp out ui.App
