package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * This implementation of DALServices and DALBackEndServices is meant to be used in the production environment
 */
public class DALServicesImpl implements DALServices, DALBackEndServices {
    private static Connection connection;
    private static String db_name = "database";
    private static String db_path;

    public DALServicesImpl() {

    }

    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
        this.openConnection();
        return this.connection.prepareStatement(query);
    }

    @Override
    public void startTransaction() {
        this.openConnection();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        try {
            this.connection.commit();
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeConnection();
    }

    @Override
    public void rollback() {
        if (this.connection == null)
            return;
        try {
            this.connection.rollback();
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeConnection();
    }

    @Override
    public void createTables(String name) throws IOException {
        this.db_name = name;
        this.db_path = "jdbc:sqlite:" + this.db_name + ".db";
        String scriptFilePath = "script.sql";
        BufferedReader reader = null;
        Statement statement = null;
        String text = "";
        try {
            this.openConnection();
            statement = this.connection.createStatement();
            // initialize file reader
            reader = new BufferedReader(new FileReader(scriptFilePath));
            String line = null;
            // read script line by line
            while ((line = reader.readLine()) != null) {
                // execute query
                text += line;
                text += '\n';
            }
            statement.executeUpdate(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close file reader
            if (reader != null) {
                reader.close();
            }
            this.closeConnection();
        }
    }

    @Override
    public void createTables() throws IOException {
        createTables(this.db_name);

    }

    @Override
    public void deleteDB(String name) {
        this.closeConnection();
        try {
            File f = new File("./" + this.db_name + ".db");
            if (!f.delete()) {
                System.out.println("./" + this.db_name + ".db can't be deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This private method open a connexion with the database
     * Note that if the file doesn't exist, SQLite will create one
     */
    private void openConnection() {
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.db_path);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

        }
    }

    /**
     * This private method safely close the connexion with the database
     */
    private void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }
}
