package persistence;

import business.Canvas.Canvas;
import business.Canvas.CanvasImpl;
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
    private String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator;
    // Saving the magnificient drawing of the users into a file
    public void save(CanvasImpl canvas, String nameOfTheFile) throws IOException, ClassNotFoundException {
        //Iterating into the list of shapes
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream(rootProject + nameOfTheFile + ".bin"));

        objectOutputStream.writeObject(canvas);
        objectOutputStream.close();
        System.out.println("Project saved successfully");


        FileInputStream fileInputStream = new FileInputStream(rootProject + nameOfTheFile + ".bin");
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        CanvasImpl readCanvas = (CanvasImpl) in.readObject();

        in.close();
        ArrayList<Shape> shapes = (ArrayList<Shape>) readCanvas.getShapes();
        for (Shape x : shapes){
            System.out.println(x.print());
        }
    }
    //Method still has to change
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