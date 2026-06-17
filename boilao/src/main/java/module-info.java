module org.openjfx.boilao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.openjfx.boilao to javafx.fxml;
    opens org.openjfx.boilao.model to javafx.base;

    exports org.openjfx.boilao;
    exports org.openjfx.boilao.model;
}