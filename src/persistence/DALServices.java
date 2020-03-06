package persistence;

import java.io.IOException;

/**
 * This interface is part of the DAL (Data Access Layer). It is exposed to the UCCs (Use-case
 * Controllers) to manage the connections to the database.
 */
public interface DALServices {
    /**
     * Opens a connection and starts a transaction.
     */
    void startTransaction();

    /**
     * Commits all operations performed on the database and closes the connection.
     */
    void commit();

    /**
     * Rolls back all operations performed on the database and closes the connection.
     */
    void rollback();

    /**
     * This method creates all the necessary tables if they don't exist
     */
    void createTables(String name) throws IOException;
    void createTables() throws IOException;

    /**
     * This method delete the database
     * Usefull for tests
     **/
    void deleteDB(String name);
}