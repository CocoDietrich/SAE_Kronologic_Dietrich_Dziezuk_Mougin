module org.example.kronologic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.kronologic to javafx.fxml;
    exports org.example.kronologic;
}