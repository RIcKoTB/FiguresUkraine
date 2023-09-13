module com.example.outstandingfiguresofukraine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.outstandingfiguresofukraine to javafx.fxml;
    exports com.example.outstandingfiguresofukraine;
    exports com.example.outstandingfiguresofukraine.ui;
    opens com.example.outstandingfiguresofukraine.ui to javafx.fxml;
}