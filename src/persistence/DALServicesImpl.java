package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;

/**
 * This implementation of DALServices and DALBackEndServices is meant to be used in the production environment
 */
public class DALServicesImpl implements DALServices, DALBackEndServices {
    private static Connection connection;

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
        try {
            this.connection.rollback();
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeConnection();
    }

    @Override
    public void createTables() throws IOException {
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
                text+=line;
                text+='\n';

            }
            //System.out.println(text);
            statement.execute(text);
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

    /**
     * This private method open a connexion with the database
     * Note that if the file doesn't exist, SQLite will create one
     */
    private void openConnection(){
        if(this.connection == null){
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }

        }
    }

    /**
     * This private method safely close the connexion with the database
     */
    private  void closeConnection(){
        if (this.connection  != null) {
            try {
                this.connection .close();
                this.connection = null;
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
        }
    }
}
