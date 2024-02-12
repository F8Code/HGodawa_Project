package restful;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Named
@ApplicationScoped
@Path("/PPTFiles")
public class FileResource implements Serializable {
    
    @PersistenceContext(unitName = "PPTFile")
    private EntityManager entityManager;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response uploadPPTFile(jpa.PPTFiles pptFile) {
        try {
            entityManager.persist(pptFile);
            entityManager.flush(); // Flush changes to the database
            return Response.status(Response.Status.CREATED).entity(pptFile).build();
        } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error uploading file").build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllPPTFiles")
    @Transactional
    public List<jpa.PPTFiles> getAllPPTFiles() {
        try {
            // Create and execute a query to fetch all PPTFiles entities
            TypedQuery<jpa.PPTFiles> query = entityManager.createQuery("SELECT p FROM PPTFiles p", jpa.PPTFiles.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception if needed
            return null;
        }
    }
    
    public jpa.PPTFiles getFileContentById(int fileId) {
        jpa.PPTFiles pptFile = entityManager.find(jpa.PPTFiles.class, fileId);
        if (pptFile != null) {
            return pptFile;
        } else {
            //Error message?
            return null;
        }
    }

    public void deletePPTFileById(int fileId) {
        jpa.PPTFiles pptFile = entityManager.find(jpa.PPTFiles.class, fileId);
        if (pptFile != null) {
            entityManager.remove(pptFile);
        } else {
            //Error message?
        }
    }
}


