package dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import entites.Post;
import entites.Topic;
import entites.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{
  private static final Logger log = LogManager.getLogger(HibernateUtil.class);

  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory()
  {
    if(sessionFactory == null)
    {
      try
      {
        Configuration configuration = new Configuration().configure();

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Post.class);
        configuration.addAnnotatedClass(Topic.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties());

        sessionFactory = configuration.buildSessionFactory(builder.build());
      }
      catch (Exception e)
      {
        log.error("{}", e.getMessage(), e);
        throw new RuntimeException(e);
      }
    }
    return sessionFactory;
  }
}
