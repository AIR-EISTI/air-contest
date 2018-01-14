package fr.aireisti.aircontest.ressources;


import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Upload;
import fr.aireisti.aircontest.security.Secured;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/upload")
public class UploadRessource {

    public static final String UPLOAD_PATH = "/tmp/";

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Upload postUpload(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context SecurityContext securityContext) {

        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        String fileName = fileDetail.getFileName().replaceAll("\\s+", "");
        if (fileName.length() > 50) {
            fileName = fileName.substring(fileName.length() - 50);
        }
        Upload upload = new Upload();
        upload.setFilename(fileName);

        Serializable.saveObject(upload);

        fileName = upload.getId() + "-" + fileName;

        try {
            Files.copy(uploadedInputStream, Paths.get(UPLOAD_PATH + fileName));
        } catch (IOException e) {
            throw new InternalServerErrorException("Error while uploading file.");
        }

        return upload;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Upload getUpload(@PathParam("id") String id) {
        Integer pk;
        try {
            pk = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Upload upload = session.get(Upload.class, pk);
        if (upload == null) {
            throw new NotFoundException();
        }
        session.close();
        return upload;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Upload> getUploads() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Upload> uploads = session.createQuery("SELECT u FROM Upload u").list();
        session.close();
        return uploads;
    }

    @DELETE
    @Secured
    @Path("{id}")
    public Response deleteUpload(@PathParam("id") String id, @Context SecurityContext securityContext) {
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        Integer pk;
        try {
            pk = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

        Upload upload = session.get(Upload.class, pk);

        if (upload == null) {
            throw new NotFoundException();
        }

        try {
            Files.delete(Paths.get(UPLOAD_PATH + upload.getId() + "-" + upload.getFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.delete(upload);
        session.close();

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
