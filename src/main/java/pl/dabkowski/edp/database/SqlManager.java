package pl.dabkowski.edp.database;

import org.greenrobot.eventbus.EventBus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.dabkowski.edp.database.entities.Busstop;
import pl.dabkowski.edp.database.entities.Location;
import pl.dabkowski.edp.database.entities.Notification;
import pl.dabkowski.edp.database.entities.ZtmRide;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    public String getNameFromId(String id, String nr){
        Busstop b = sessionFactory.openSession().createQuery("from Busstop  b where b.stop_id = :id and b.stop_nr = :nr", Busstop.class).setParameter("id", id).setParameter("nr", nr).getSingleResult();
        return b.getStreet();
    }

    public List<Notification> loadNotifications() {
        return sessionFactory.openSession().createQuery("from  Notification", Notification.class).list();
    }

    public List<Busstop> selectQuery(String name) {
        List<Busstop> objects;
        Session session = sessionFactory.openSession();
        objects = session.createQuery("From Busstop b where b.street = :name", Busstop.class).setParameter("name", name).list();
        return objects;
    }

    public List<Busstop> selectAllBusstops() {
        return sessionFactory.openSession().createQuery("From Busstop", Busstop.class).list();
    }

    public void removeObject(Object obj) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.remove(obj);
            tx.commit();
        } catch (Exception e) {
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

/*    public void removeOldNotifications(){
        List<Notification> notifications = loadNotifications();
        for (Notification notification: notifications){
            if (notification.getTime().after(Time.valueOf(LocalTime.now().plusHours(1)))){
                removeObject(notification);
            }
        }
    }*/

    public void scheduleNotifications(){
        for (Notification notification : loadNotifications()){
            TimerTask task = new TimerTask() {
                public void run() {
                    System.out.println("Task performed on: " + Date.from(notification.getTime().toLocalTime().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC)) + "n" +
                            "Thread's name: " + Thread.currentThread().getName());
                    EventBus.getDefault().post(notification);
                    SqlManager.getInst().removeObject(notification);
                }
            };

            Timer timer = new Timer("Notification");

            timer.schedule(task, Date.from(notification.getTime().toLocalTime().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC)));
        }
    }
}
