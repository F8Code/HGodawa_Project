/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.Part;
import sun.misc.IOUtils;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PPTFileController implements Serializable {
    @Inject
    private restful.FileResource fileResource;
    
    private Part file;
    private String fileName;
    private String description;
    
    private byte[] fileContent;
    
    public void setFile(Part file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<jpa.PPTFiles> getAllPPTFiles() {
        try {
            return fileResource.getAllPPTFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadPPTFile() {
        jpa.PPTFiles pptFile = new jpa.PPTFiles();
            
        try (InputStream inputStream = file.getInputStream()) {
            fileContent = IOUtils.readFully(inputStream, -1, true);
            pptFile.setFileContent(fileContent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        String fileNameToSet = fileName != null && !fileName.isEmpty() ? fileName : Paths.get(file.getSubmittedFileName()).getFileName().toString();
        pptFile.setFileName(fileNameToSet.endsWith(".ppt") ? fileNameToSet.substring(0, fileNameToSet.length() - 4) : fileNameToSet);
        pptFile.setDescription(description);

        try {
            fileResource.uploadPPTFile(pptFile);

            System.out.println("File uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during file upload: " + e.getMessage());
        }
    }
    
    public jpa.PPTFiles getFileContentById(int fileId) {
        try {
            return fileResource.getFileContentById(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePPTFileById(int fileId) {
        try {
            fileResource.deletePPTFileById(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


