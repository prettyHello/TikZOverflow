package controller.UCC;

import controller.ProjectImpl;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import utilities.exceptions.BizzException;
import view.dashboard.DashboardController;

import java.io.File;
import java.io.IOException;

//TODO LES FAMEUX USECASE THE VEIWOPTION, TU FAIS UNE VIEWOPTION, ILS FONT DES VIEWOPTIONS....
//TODO MERGE AVEC PROJECT...
public interface ViewOptionUCC {




    //TODO BIEN SUR, DANS UN AUTRE USECASECONTROLLER, C'EST EVIDENT
     void deleteProject (ProjectImpl project, DashboardController dashboard ) ;
}
