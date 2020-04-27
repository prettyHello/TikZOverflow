package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.IOException;

/**
 * The DALServiceMock is used to hide the database transaction during test.
 * Since all the methods return void, they are all empty, the point being to test the model (dao) without the need worry about eventual
 * throws the real dalservice could launch.
 */
public class DALServiceMock  implements DALServices {

    public DALServiceMock(){

    }

    @Override
    public void startTransaction() throws FatalException {

    }

    @Override
    public void commit() throws FatalException {

    }

    @Override
    public void rollback() throws FatalException {

    }

    @Override
    public void createTables(String name) throws IOException, FatalException {

    }

    @Override
    public void createTables() throws IOException, FatalException {

    }

    @Override
    public void deleteDB(String name) throws FatalException {

    }
}
