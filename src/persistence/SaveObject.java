package persistence;

import business.Canvas.Canvas;
import business.shape.Shape;
import business.DTO.ProjectDTO;
import business.UCC.ProjectUCCImpl;
import javafx.scene.shape.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SaveObject {
    private String rootProject = File.separator + "ProjectTikZ" + File.separator;
    // Saving the magnificient drawing of the users into a file
    public void save(Canvas canvas, String nameOfTheFile) throws IOException, ClassNotFoundException {
        //Iterating into the list of shapes
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream(rootProject + nameOfTheFile + ".bin"));

            objectOutputStream.writeObject(canvas);
            objectOutputStream.close();
            System.out.println("Project saved successfully");
    }
    public List <Shape> open(String nameOfTheFile) throws IOException, ClassNotFoundException {
        ArrayList myList = null;
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