package business.DTO;

import java.util.ArrayList;
import java.util.Date;

public class ProjectDTO {
    private ArrayList<String> projectName = new ArrayList<String>();
    private ArrayList<String> projectReference = new ArrayList<String>() ;
    private ArrayList<String> projectPath = new ArrayList<String>();
    private ArrayList<String> createDate = new ArrayList<String>();
    private ArrayList<String> modificationDate = new ArrayList<String>();

    public ArrayList<String> getProjectName() {
        return projectName;
    }

    public ProjectDTO setProjectName(String projectName) {
        this.projectName.add(projectName) ;
        return  this;
    }

    public ArrayList<String> getProjectPath() {
        return projectPath;
    }

    public ProjectDTO setProjectPath(String projectPath) {
        this.projectPath.add(projectPath);
        return  this;
    }

    public ArrayList<String> getCreateDate() {
        return createDate;
    }

    public ProjectDTO setCreateDate(String createDate) {
        this.createDate.add(createDate);
        return this;
    }

    public ArrayList<String> getModificationDate() {
        return modificationDate;
    }

    public ProjectDTO setModificationDate(String modificationDate) {
        this.modificationDate.add(modificationDate);
        return  this;
    }

    public ArrayList<String> getProjectReference() {
        return projectReference;
    }

    public ProjectDTO setProjectReference(String projectReference) {
        this.projectReference.add(projectReference);
        return  this;
    }
}
