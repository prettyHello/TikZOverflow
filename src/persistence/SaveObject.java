package persistence;

import java.io.*;


public class SaveObject {

    public < E > void save( E inputObject ) throws IOException, ClassNotFoundException {
        // Save elements
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream("data/person.bin"));
        objectOutputStream.writeObject(inputObject);
        objectOutputStream.close();
    }

}
