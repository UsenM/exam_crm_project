package dao;

import dao.daoutil.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface CrudDao<Model> {

    List<Model> findAll();
    Optional<Model> save(Model model);


    default Connection getConnection() throws SQLException {
        final String URL = "jdbc:postgresql://localhost:5432/crm";
        final String USERNAME = "postgres";
        final String PASSWORD = "'";
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }

    default void close(AutoCloseable closeable) {

        try {
            Log.info(this.getClass().getSimpleName(), closeable.getClass().getSimpleName(), "Closing connection");
            closeable.close();
        } catch (Exception e) {

            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
    }

}
