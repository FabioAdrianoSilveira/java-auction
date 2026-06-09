module org.openjfx.auction {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.auction to javafx.fxml;
    exports org.openjfx.auction;
}
