package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.utilities.exceptions.FatalException;

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
    private static String databaseName = "database";
    private static String databasePath;

    public DALServicesImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
        this.openConnection();
        return connection.prepareStatement(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTransaction() throws  FatalException {
        this.openConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new FatalException("DalServices error - transaction: \n\t"+e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws  FatalException {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new FatalException("DalServices error - unable to commit: \n\t"+e.getMessage());
        }
        this.closeConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws FatalException {
        if (connection != null){
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new FatalException("DalServices error - impossible to rollback: \n\t"+e.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTables(String name) throws IOException, FatalException {
        databaseName = name;
        databasePath = "jdbc:sqlite:" + databaseName + ".db";
        String scriptFilePath = "script.sql";
        BufferedReader reader = null;
        Statement statement = null;
        StringBuilder text = new StringBuilder();
        try {
            this.openConnection();
            statement = connection.createStatement();
            // initialize file reader
            reader = new BufferedReader(new FileReader(scriptFilePath));
            String line = null;
            // read script line by line
            while ((line = reader.readLine()) != null) {
                // execute query
                text.append(line);
                text.append('\n');
            }
            statement.executeUpdate(text.toString());
        } catch (Exception e) {
            throw new FatalException("Database creation impossible: \n\t"+e.getMessage());
        } finally {
            // close file reader
            if (reader != null) {
                reader.close();
            }
            this.closeConnection();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTables() throws IOException, FatalException {
        createTables(databaseName);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDB(String name) throws FatalException {
        this.closeConnection();
        try {
            File f = new File("./" + name + ".db");
            if (!f.exists() || !f.delete()) {
                throw new FatalException("Database ./"+name + " deletion impossible: \n\t");
            }
        } catch (Exception e) {
            throw new FatalException("Database ./"+name + " deletion impossible: \n\t: "+e.getMessage());
        }
    }

    /**
     * This private method open a connexion with the database
     * Note that if the file doesn't exist, SQLite will create one
     */
    private void openConnection() throws FatalException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(databasePath);
            } catch (Exception e) {
                throw new FatalException("Database ./"+ databaseName + " open connection impossible: \n\t: "+e.getMessage());
            }

        }
    }

    /**
     * This private method safely close the connexion with the database
     */
    private void closeConnection() throws FatalException {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                throw new FatalException("Database ./"+ databaseName + " close connection impossible: \n\t: "+e.getMessage());
            }
        }
    }
}
