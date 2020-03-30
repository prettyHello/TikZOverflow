package persistence;

import exceptions.FatalException;

/**
 * This interface exposes the basic CRUD operations performed on the database that are shared by all
 * the DAOs (Data Access Objects). It is intended to by extended by more specific DAOs if those
 * operations are not sufficient.
 *
 * @param <T> a generic object representing an entry in the database.
 */
public interface DAO<T> {
    /**
     * Finds an object in the database by its id.
     *
     * @param obj a generic type object defined in children interfaces (a DTO).
     * @return a generic type object (a DTO).
     */
    T find(T obj);

    /**
     * Creates an entry in the database.
     *
     * @param obj a generic type object defined in children interfaces (a DTO).
     */
    void create(T obj) throws FatalException;

    /**
     * Updates an entry in the database.
     *
     * @param obj a generic type object defined in children interfaces (a DTO).
     */
    void update(T obj);

    /**
     * Deletes an entry in the database.
     *
     * @param obj a generic type object defined in children interfaces (a DTO).
     */
    void delete(T obj);


    T get(T obj);

}
