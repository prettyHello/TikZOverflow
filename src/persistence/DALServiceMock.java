package persistence;

import exceptions.FatalException;

import java.io.IOException;

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
