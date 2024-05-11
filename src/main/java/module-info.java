module org.example.notepad {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens org.example.notepad to javafx.fxml;
    exports org.example.notepad;
}