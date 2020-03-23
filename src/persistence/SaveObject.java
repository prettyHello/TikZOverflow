package persistence;

import business.shape.Shape;
import business.DTO.ProjectDTO;
import business.UCC.ProjectUCCImpl;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class SaveObject {
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    // Saving the magnificient drawing of the users into a file
    public void save(List<Shape> myList, String nameOfTheFile) throws IOException, ClassNotFoundException {
        //Iterating into the list of shapes
        for (Shape shape : myList) {
            //Creating an object to save into the file
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(new FileOutputStream(rootProject + nameOfTheFile + ".bin"));
            objectOutputStream.writeObject(shape);
            objectOutputStream.close();
        }
    }
    public List <Shape> open(String nameOfTheFile) throws IOException, ClassNotFoundException {
        List myList = null;
        // We still need to work here
        // Adding the object read into the list
        // But we need to make sure the save method is working first

        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(nameOfTheFile));

        Shape shape = (Shape) objectInputStream.readObject();

        objectInputStream.close();

        return myList;
    }

}