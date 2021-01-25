package com.github.mvollebregt.graphqldemoserver;

import org.hsqldb.persist.HsqlProperties;
import org.springframework.stereotype.Component;
import org.hsqldb.Server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class EmbeddedDatabase {

    final String dbLocation = "./";
    Server server;
    Connection dbConn = null;

    @PostConstruct
    public void startDBServer() {
        HsqlProperties props = new HsqlProperties();
        props.setProperty("server.database.0", "file:" + dbLocation + "mydb;");
        props.setProperty("server.dbname.0", "xdb");
        server = new org.hsqldb.Server();
        try {
            server.setProperties(props);
        } catch (Exception e) {
            return;
        }
        server.start();
    }

    @PreDestroy
    public void stopDBServer() {
        server.shutdown();
    }

    public Connection getDBConn() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            dbConn = DriverManager.getConnection(
                    "jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConn;
    }
}
