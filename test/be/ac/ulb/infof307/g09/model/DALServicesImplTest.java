package be.ac.ulb.infof307.g09.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DALServicesImplTest {
    DALServices dal;

    @BeforeEach
    void setUp() {
        dal = new DALServicesImpl();
    }

    @Test
    void deleteDB_wrongParameter() {
        String path = "thisdbdoesnotexist";
        File f = new File(path);
        dal.deleteDB(path);
        assertFalse(f.exists());
    }
}