module org.example.notepad {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.notepad to javafx.fxml;
    exports org.example.notepad;
}