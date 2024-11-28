module kronologic.kronologic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens Kronologic.kronologic to javafx.fxml;
    exports Kronologic.kronologic;
}