package persistence;


import business.Canvas.Canvas;
import business.shape.Shape;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class SaveObject {
    private String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator;
    // Saving the magnificient drawing of the users into a file
    public void save(Canvas canvas, String nameOfTheFile) throws IOException, ClassNotFoundException {
        rootProject = rootProject + nameOfTheFile + File.separator;
        //Creating the file to save to
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream(rootProject + nameOfTheFile + ".bin"));
        //Saving the canvas to the file
        objectOutputStream.writeObject(canvas);
        objectOutputStream.close();
        System.out.println("Project saved successfully");

        //Read out the canvas and output the content of it
        FileInputStream fileInputStream = new FileInputStream(rootProject + nameOfTheFile + ".bin");
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Canvas readCanvas = (Canvas) in.readObject();

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