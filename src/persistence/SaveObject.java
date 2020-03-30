package persistence;


import business.Canvas.Canvas;
import business.DTO.UserDTO;

import java.io.*;

public class SaveObject {
    private String rootProject = System.getProperty("user.home") + File.separator + "ProjectTikZ" + File.separator  ;

    // Saving the magnificient drawing of the users into a file
    public void save(Canvas canvas, String nameOfTheFile, UserDTO userID) throws IOException, ClassNotFoundException {
        String userHome = userID.getFirstName()+"_"+userID.getLastName()+"_"+ userID.getUserId() ;
        rootProject = rootProject + userHome + File.separator + nameOfTheFile + File.separator;
        //Creating the file to save to
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream(rootProject + nameOfTheFile + ".bin"));
        //Saving the canvas to the file
        objectOutputStream.writeObject(canvas);
        objectOutputStream.close();
        System.out.println("Project saved successfully");
    }

    public Canvas open(String nameOfTheFile,UserDTO userID) throws IOException, ClassNotFoundException {
        //Open a file and return the resulting Canvas
        FileInputStream fileInputStream = new FileInputStream(rootProject +"userid_" +userID.getUserId() + File.separator + nameOfTheFile + File.separator + nameOfTheFile + ".bin");
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Canvas readCanvas = (Canvas) in.readObject();
        in.close();
        System.out.println("Project successfully loaded");
        return readCanvas;
    }
}