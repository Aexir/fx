package pl.dabkowski.edp.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.dabkowski.edp.database.entities.*;

import java.util.List;

public class SqlManager {
    private static SqlManager inst;
    private final SessionFactory sessionFactory;

    private SqlManager() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Busstop.class);
        configuration.addAnnotatedClass(ZtmRide.class);
        configuration.addAnnotatedClass(Notification.class);


        sessionFactory = configuration.buildSessionFactory();
    }

    public static SqlManager getInst() {
        if (inst == null) {
            inst = new SqlManager();
        }
        return inst;
    }


//    public List<Object> selectQuery(String query){
//        List<Object> objects;
//        Session session = sessionFactory.openSession();
//        objects = (List<Object>) session.createQuery(query).list();
//        return objects;
//    }

    public List<Notification> loadNotifications(){
        return sessionFactory.openSession().createQuery("from  Notification", Notification.class).list();
    }

    public List<Busstop> selectQuery(String name) {
        List<Busstop> objects;
        Session session = sessionFactory.openSession();
        objects = session.createQuery("From Busstop b where b.street = :name", Busstop.class).setParameter("name", name).list();
        return objects;
    }

    public List<Busstop> selectAllBusstops(){
        return sessionFactory.openSession().createQuery("From Busstop", Busstop.class).list();
    }

    public void removeObject(Object obj){
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.remove(obj);
            tx.commit();
        } catch (Exception e){
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void saveObject(Object obj) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
