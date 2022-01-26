module com.example.labyrinth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;

    opens com.example.labyrinth to javafx.fxml;
    exports com.example.labyrinth;
    exports com.example.labyrinth.classes;
    opens com.example.labyrinth.classes to javafx.fxml;
}