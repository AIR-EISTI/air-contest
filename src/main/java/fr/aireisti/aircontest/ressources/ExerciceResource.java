package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Exercice;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fr.aireisti.aircontest.security.Secured;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.text.TextContentRenderer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

@Path("/exercice")
public class ExerciceResource {

    private Session session;

    private Exercice renderDescription(Exercice exercice, String markup) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(exercice.getDescription());
        Renderer renderer = null;

        if (markup.equals("html")) {
            renderer = HtmlRenderer.builder().build();
            exercice.setDescription(renderer.render(document));
        } else if (markup.equals("txt")) {
            renderer = TextContentRenderer.builder().build();
            exercice.setDescription(renderer.render(document));
        } else if (!markup.equals("md")) {
            throw new BadRequestException();
        }

        return exercice;
    }

    public static Exercice renderDatasetLink(Exercice exercice) {
        exercice.setOutputFile("/api/exercice/" + exercice.getId() + "/outputFile");
        exercice.setInputFile("/api/exercice/" + exercice.getId() + "/inputFile");
        return exercice;
    }

    private Exercice getExercice(Integer id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT e FROM  Exercice e WHERE id=:id");
        query.setParameter("id", id);
        Exercice exercice = (Exercice) query.uniqueResult();
        session.close();
        if (exercice == null) {
            throw new NotFoundException();
        }
        return exercice;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Exercice getExerciceById(@PathParam("id") String id, @DefaultValue("md") @QueryParam("markup") String markup) {
        return renderDatasetLink(renderDescription(getExercice(Integer.parseInt(id)), markup));
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postExercice(Exercice exercice, @Context SecurityContext securityContext){
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        return Serializable.saveObject(exercice);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercice> getExercices(@DefaultValue("md") @QueryParam("markup") final String markup,
                                       @QueryParam("search") String search,
                                       @DefaultValue("0") @QueryParam("start") Integer start,
                                       @DefaultValue("11") @QueryParam("limit") Integer limit,
                                       @QueryParam("group") Integer group) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Exercice> exercices;
        String sql = "";
        String sqlEnd = "";
        Query query;

        if(group != null) {
            sql = "SELECT a FROM Group b JOIN b.exercices a WHERE b.id = :group_number AND a.id IN (";
            sqlEnd = ")";
        }
        if(search != null) {
            sql = sql + "SELECT e FROM  Exercice e WHERE e.title LIKE :search ORDER BY e.creatingDate DESC" + sqlEnd;
            query = session.createQuery(sql);
            if(group != null) query.setParameter("group_number", group);
            query.setParameter("search", '%' + search + '%');
            exercices = query.setFirstResult(start).setMaxResults(limit).list();
        } else {
            sql = sql + "SELECT e FROM  Exercice e" + sqlEnd;
            query = session.createQuery(sql);
            if(group != null) query.setParameter("group_number", group);
            exercices = query.setFirstResult(start).setMaxResults(limit).list();
        }
        session.close();
        return exercices
                .stream()
                .map(e -> renderDatasetLink(renderDescription(e, markup)))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}/inputFile")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getInput(@PathParam("id") String id) {
        Exercice exercice = getExercice(Integer.parseInt(id));
        Response.ResponseBuilder response = Response.ok(exercice.getInputFile());
        response.header("Content-Disposition", "attachment; filename=\"input_exercice_" + id + ".txt\"");
        return response.build();
    }

    @GET
    @Path("{id}/outputFile")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getOutput(@PathParam("id") String id) {
        Exercice exercice = getExercice(Integer.parseInt(id));
        Response.ResponseBuilder response = Response.ok(exercice.getOutputFile());
        response.header("Content-Disposition", "attachment; filename=\"output_exercice_" + id + ".txt\"");
        return response.build();
    }

    @GET
    @Path("quantity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuantity(@DefaultValue("") @QueryParam("search") String search){
        session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT COUNT(e) FROM Exercice e ";
        if (search != null){
            sql += "WHERE e.title LIKE :search";
        }
        Query query = session.createQuery(sql);
        query.setParameter("search", '%' + search + '%');
        String quantity = query.uniqueResult().toString();
        session.close();
        return "{\"quantity\":" + quantity + "}";
    }

    @DELETE
    @Secured
    @Path("{id}")
    public void deleteExerciceById(@PathParam("id") Integer id, @Context SecurityContext securityContext) {
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM Exercice e WHERE e.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    @PUT
    @Secured
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putExerciceById(Exercice exercice,
                                @PathParam("id") Integer id,
                                @Context SecurityContext securityContext){

        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        Serializable.updateObject(exercice, id);
    }
}
