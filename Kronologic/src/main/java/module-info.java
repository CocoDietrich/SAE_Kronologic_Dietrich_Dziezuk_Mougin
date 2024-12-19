module kronologic.kronologic {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.chocosolver.solver;
    requires jdk.unsupported.desktop;


    opens Kronologic to javafx.fxml;
    exports Kronologic;
}