module kronologic.kronologic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires org.chocosolver.solver;


    opens Kronologic to javafx.fxml;
    exports Kronologic;
}