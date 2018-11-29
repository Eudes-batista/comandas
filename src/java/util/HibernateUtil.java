package util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory=null;
    
    public static SessionFactory getSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            if(sessionFactory==null){
                sessionFactory= configuration.buildSessionFactory();
            }
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
        }
        return sessionFactory;
    }
}
