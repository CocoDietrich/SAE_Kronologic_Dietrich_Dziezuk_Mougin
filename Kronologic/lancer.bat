@echo off
java --enable-preview ^
     --module-path "Kronologic-distrib/javafx-sdk-24/lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp "Kronologic-distrib/Kronologic.jar;Kronologic-distrib/choco-solver.jar" ^
     Kronologic.MainMVC

pause
