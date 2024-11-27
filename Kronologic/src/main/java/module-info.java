module org.example.kronologic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens org.example.kronologic to javafx.fxml;
    exports org.example.kronologic;
}