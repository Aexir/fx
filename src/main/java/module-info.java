module pl.aexir.edp {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires javafx.web;
    requires java.naming;
    requires com.google.gson;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens pl.dabkowski.edp to javafx.fxml;
    exports pl.dabkowski.edp;
    opens pl.dabkowski.edp.controllers to javafx.fxml;
    exports pl.dabkowski.edp.controllers;
    exports pl.dabkowski.edp.utils;
    opens pl.dabkowski.edp.utils to javafx.fxml;
    opens pl.dabkowski.edp.database.entities to org.hibernate.orm.core;
    exports pl.dabkowski.edp.database.entities;

}