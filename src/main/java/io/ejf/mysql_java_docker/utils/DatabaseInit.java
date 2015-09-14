package io.ejf.mysql_java_docker.utils;

import io.ejf.mysql_java_docker.models.TagModel;
import io.ejf.mysql_java_docker.models.UserModel;
import io.ejf.mysql_java_docker.models.UserTagModel;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DatabaseInit {
    public static final SessionFactory SESSIONS = getSessions();

    private DatabaseInit() {
    }

    private static SessionFactory getSessions() {
        // Probably could pass in an environment variable that keeps track of the address
        String url = "jdbc:mysql://192.168.99.100:3306/sample";
        String driverClass = "com.mysql.jdbc.Driver";

        Configuration cfg = new Configuration()
                .addAnnotatedClass(UserModel.class)
                .addAnnotatedClass(TagModel.class)
                .addAnnotatedClass(UserTagModel.class)
                .setProperty("hibernate.order_updates", "true")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.driver_class", driverClass)
                .setProperty("hibernate.connection.database", "sample")
                .setProperty("hibernate.connection.username", "testapp")
                .setProperty("hibernate.connection.password", "rJQaj5B46ofWjZo2o2648izNCGUcui9o")
                .setProperty("hibernate.connection.autoReconnect", "true");

        StandardServiceRegistryBuilder ssrb
                = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());

        return cfg.buildSessionFactory(ssrb.build());
    }

}
