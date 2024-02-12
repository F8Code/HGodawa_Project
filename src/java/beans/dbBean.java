package beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean(name="dbBean")
@ViewScoped
public class dbBean implements Serializable {
    private String name;
    private Part file;
    private String description;
    
    private List<jpa.PPTFiles> pptFiles;

    @EJB
    private ejb.PPTFileController pptFileController;

    @PostConstruct
    public void init() {
        loadPPTFiles();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }

    // Getters and setters for filePath
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    // Getters and setters for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<jpa.PPTFiles> getPPTFiles() {
        return pptFiles;
    }

    public void setPPTFiles(List<jpa.PPTFiles> pptFiles) {
        this.pptFiles = pptFiles;
    }

    // Method to load all PPTFiles entries from the database
    public void loadPPTFiles() {
        pptFiles = pptFileController.getAllPPTFiles();
    }

    public void uploadFile() {
        pptFileController.setFile(file);
        pptFileController.setFileName(name);
        pptFileController.setDescription(description);

        if(file != null)
            pptFileController.uploadPPTFile();
        
        loadPPTFiles();
    }
    
    public void downloadFile(int fileId) {
        try {
            jpa.PPTFiles pptFile = pptFileController.getFileContentById(fileId);
            String fileName = pptFile.getFileName();
            byte[] fileContent = pptFile.getFileContent();

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            externalContext.responseReset();
            externalContext.setResponseContentType("application/octet-stream");
            externalContext.setResponseContentLength(fileContent.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            externalContext.getResponseOutputStream().write(fileContent);
            externalContext.getResponseOutputStream().flush();
            facesContext.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(int fileId) {
        try {
            pptFileController.deletePPTFileById(fileId);
            
            loadPPTFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
