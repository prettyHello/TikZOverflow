package persistence;

import exceptions.FatalException;

import java.io.IOException;

/**
 * This interface is part of the DAL (Data Access Layer). It is exposed to the UCCs (Use-case
 * Controllers) to manage the connections to the database.
 */
public interface DALServices {
    /**
     * Opens a connection and starts a transaction.
     */
    void startTransaction() throws FatalException;

    /**
     * Commits all operations performed on the database and closes the connection.
     */
    void commit() throws FatalException;

    /**
     * Rolls back all operations performed on the database and closes the connection.
     */
    void rollback() throws FatalException;

    /**
     * This method creates all the necessary tables if they don't exist
     */
    void createTables(String name) throws IOException, FatalException;

    void createTables() throws IOException, FatalException;

    /**
     * This method delete the database
     * Usefull for tests
     **/
    void deleteDB(String name) throws FatalException;
}