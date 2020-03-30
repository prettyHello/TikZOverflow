package persistence;

import exceptions.FatalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DALServicesImplTest {
    DALServices dal;
    @BeforeEach
    void setUp() {
        dal = new DALServicesImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void deleteInexistantDB(){
        assertThrows(FatalException.class, () -> {
            dal.deleteDB("noDB");
        });
    }

    @Test
    void rollBackNothing(){
        assertThrows(FatalException.class, () -> {
            dal.startTransaction();
            dal.rollback();
        });
    }

    @Test
    void startTransactionWithoutDB(){
        assertThrows(FatalException.class, () -> {
            dal.startTransaction();
        });
    }

}