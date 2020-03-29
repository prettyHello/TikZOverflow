package persistence;


import business.Canvas.Canvas;

import java.io.*;

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
    }

    public Canvas open(String nameOfTheFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(rootProject + nameOfTheFile + File.separator + nameOfTheFile + ".bin");
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Canvas readCanvas = (Canvas) in.readObject();
        in.close();
        System.out.println("Project successfully loaded");
        return readCanvas;
    }
}