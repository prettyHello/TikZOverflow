package be.ac.ulb.infof307.g09.model.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This interface is part of the DAL (Data Access Layer). It is only exposed to the DAOs (Data
 * Access Objects) and is used to fetch prepared statements in order to execute queries. It
 * shouldn't be used by UCCs (Use-case controllers).
 */
public interface DALBackEndServices {
    /**
     * Uses the connection to the database to create a prepared statement. The returned object is
     * expected to have its parameters `(?)` filled through setObject() calls if needed.
     *
     * @param query the query as a string, potentially with `?` denoting placeholders for parameters.
     * @return the PreparedStatement object.
     * @throws SQLException if a preparedStatement can't be retrieved from the Connection object.
     */
    PreparedStatement prepareStatement(String query) throws SQLException;
}
