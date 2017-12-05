package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.InternalServerErrorException;

public class Serializable {
    private static Session session;

    public static String saveObject(InitModel object){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
        return "{\"id\":" + object.getId() + "}";
    }

    public static void updateObject(InitModel object, Integer id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            object.setId(id);
            session.update(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new InternalServerErrorException();
        } finally {
            session.close();
        }
    }
}
