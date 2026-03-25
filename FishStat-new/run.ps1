$JAVAFX = "C:\Users\amine\Downloads\openjfx-21.0.10_windows-x64_bin-sdk\javafx-sdk-21.0.10\lib"

$SRC = Get-ChildItem -Recurse -Filter *.java |
  Where-Object { $_.FullName -notmatch '\\tests\\' } |
  ForEach-Object { $_.FullName }

javac --module-path $JAVAFX `
      --add-modules javafx.controls `
      -d out `
      $SRC

java --module-path $JAVAFX `
     --add-modules javafx.controls `
     -cp out ui.App

