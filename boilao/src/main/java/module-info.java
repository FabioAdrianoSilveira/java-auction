module org.openjfx.boilao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.openjfx.boilao to javafx.fxml;
    exports org.openjfx.boilao;
}
