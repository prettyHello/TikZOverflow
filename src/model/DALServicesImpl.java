package model;

import utilities.exceptions.FatalException;

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
    private static String DBName = "database";
    private static String DBPath;

    public DALServicesImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
        this.openConnection();
        return this.connection.prepareStatement(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTransaction() throws  FatalException {
        this.openConnection();
        try {
            this.connection.setAutoCommit(false);
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
            this.connection.commit();
            this.connection.setAutoCommit(true);
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
        if (this.connection != null){
            try {
                this.connection.rollback();
                this.connection.setAutoCommit(true);
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
        this.DBName = name;
        this.DBPath = "jdbc:sqlite:" + this.DBName + ".db";
        String scriptFilePath = "script.sql";
        BufferedReader reader = null;
        Statement statement = null;
        StringBuilder text = new StringBuilder();
        try {
            this.openConnection();
            statement = this.connection.createStatement();
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
        createTables(this.DBName);

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
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.DBPath);
            } catch (Exception e) {
                throw new FatalException("Database ./"+this.DBName + " open connection impossible: \n\t: "+e.getMessage());
            }

        }
    }

    /**
     * This private method safely close the connexion with the database
     */
    private void closeConnection() throws FatalException {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (Exception e) {
                throw new FatalException("Database ./"+this.DBName + " close connection impossible: \n\t: "+e.getMessage());
            }
        }
    }
}
