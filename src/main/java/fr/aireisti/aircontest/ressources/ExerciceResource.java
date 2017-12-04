package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Exercice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.text.TextContentRenderer;

import org.hibernate.HibernateException;
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

    private Exercice renderDatasetLink(Exercice exercice) {
        exercice.setOutputFile("/api/exercice/" + exercice.getId() + "/outputFile");
        exercice.setInputFile("/api/exercice/" + exercice.getId() + "/inputFile");
        return exercice;
    }

    private Exercice getExercice(Integer id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT e FROM  Exercice e WHERE id=:id");
        query.setParameter("id", id);
        Exercice exercice = (Exercice) query.uniqueResult();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postExercice(Exercice exercice){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(exercice);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        return "{\"id\":" + exercice.getId() + "}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercice> getExercices(@DefaultValue("md") @QueryParam("markup") final String markup,
                                       @QueryParam("search") String search,
                                       @QueryParam("start") Integer start,
                                       @QueryParam("limit") Integer limit) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Exercice> exercices;
        if(search != null) {
            String sql ="SELECT e FROM  Exercice e WHERE e.title LIKE :search ORDER BY e.creatingDate ASC";
            Query query = session.createQuery(sql);
            query.setParameter("search", '%' + search + '%');
            if (start == null) {
                start = 0;
            }
            if (limit == null){
                limit = 11;
            }
            exercices = query.setFirstResult(start).setMaxResults(limit).list();
        } else {
            exercices = session.createQuery("SELECT e FROM  Exercice e").list();
        }
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
    public String getQuantity(){
        session = HibernateUtil.getSessionFactory().openSession();
        String quantity = session.createQuery("SELECT COUNT(e) FROM Exercice e").uniqueResult().toString();
        return "{\"quantity\":" + quantity + "}";
    }

    @DELETE
    @Path("{id}")
    public void deleteExerciceById(@PathParam("id") String id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Exercice e WHERE e.id = " + id).executeUpdate();
        tx.commit();
        session.close();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void patchExerciceById(Exercice exercice, @PathParam("id") Integer id){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            exercice.setId(id);
            session.update(exercice);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
    }


}
