package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Exercice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;
import java.util.stream.Collector;
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

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Exercice getExerciceById(@PathParam("id") String id, @DefaultValue("md") @QueryParam("markup") String markup) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT e FROM  Exercice e WHERE id=:id");
        query.setParameter("id", Integer.parseInt(id));
        Exercice exercice = (Exercice) query.uniqueResult();
        if (exercice == null) {
            throw new NotFoundException();
        }
        return renderDescription(exercice, markup);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String postExercice(Exercice exercice){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(exercice);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return "{\"id\":" + exercice.getId() + "}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercice> getExercices(@DefaultValue("md") @QueryParam("markup") final String markup) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Exercice> exercices = session.createQuery("SELECT e FROM  Exercice e").list();
        return exercices.stream().map(e -> renderDescription(e, markup)).collect(Collectors.toList());
    }
}
