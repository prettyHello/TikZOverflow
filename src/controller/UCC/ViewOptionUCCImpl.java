package controller.UCC;

import config.ConfigurationSingleton;
import controller.DTO.ProjectDTO;
import controller.ProjectImpl;
import controller.factories.ProjectFactory;
import controller.factories.ProjectFactoryImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import model.DALServices;
import model.DALServicesImpl;
import model.ProjectDAO;
import model.ProjectDAOImpl;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import utilities.Utility;
import utilities.exceptions.BizzException;
import view.dashboard.DashboardController;

import java.io.*;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;


//TODO refactor to be MVC compliant (everything related to javaFX should be in the javaFX controller)
//TODO once MVC compliant move into projectUCC
public class ViewOptionUCCImpl implements ViewOptionUCC {


    private ProjectDAO projectDAO = ConfigurationSingleton.getProjectDAO();






    //TODO CLEAN ET MOVE DANS PROJECT, SMIPLIFY, DIVIDE
    public void deleteProject(ProjectImpl project, DashboardController dashboard ) {
        File dir = new File(project.getProjectPath()) ;
        if (dir.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirmation ?");
            alert.setHeaderText("once deleted, the "+ project.getProjectName()+" project can no longer be restored");
            alert.setContentText("are you sure you want to delete");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DALServices dal = new DALServicesImpl();
                ProjectFactory projectFactory = new ProjectFactoryImpl();
                ProjectDAO dao = new ProjectDAOImpl(dal, projectFactory);
                //TODO WHHHHYYYYYYY
                ((ProjectDAO) dao).delete(project);
                Utility.deleteFile(dir);
                dashboard.delete(project);
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR, "This project doesn't exit in this computer. It will be deleted from your projects").showAndWait();
            DALServices dal = new DALServicesImpl();
            ProjectFactory projectFactory = new ProjectFactoryImpl();
            ProjectDAO dao = new ProjectDAOImpl(dal, projectFactory);
            ((ProjectDAO) dao).delete(project);
            dashboard.delete(project);
        }
    }
}

