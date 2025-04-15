@echo off
set JAVA_FX_PATH="C:\Program Files\Java\javafx-sdk-21.0.6\lib"
set SOURCE_PATH="D:\path\to\your\java\files"   REM Modify this with the correct path to your `.java` files

cd %SOURCE_PATH%

rem Compile the Java program
javac --module-path %JAVA_FX_PATH% --add-modules javafx.controls,javafx.fxml,javafx.web CompilerUI.java

rem Run the Java program
java --module-path %JAVA_FX_PATH% --add-modules javafx.controls,javafx.fxml,javafx.web CompilerUI
