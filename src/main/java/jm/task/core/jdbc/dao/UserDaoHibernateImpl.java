package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public static SessionFactory sessionFactory = Util.getMySessionFactory();
    private static final String CREATEUSERSTABLE = """
                              CREATE TABLE IF NOT EXISTS user(
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50),
                                     lastName VARCHAR(50),
                                     age TINYINT);
                              """;
    private static final String DROPUSERSTABLE = "DROP TABLE IF EXISTS user;";
    private static final String CLEANUSERSTABLE = "DELETE from User;";
    private static final String GETALLUSERS = "SELECT u from User u";
    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(CREATEUSERSTABLE).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(DROPUSERSTABLE).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class,id);
            if (user != null) {
                session.delete("FROM user", user);
                transaction.commit();
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(GETALLUSERS, User.class).list();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(CLEANUSERSTABLE).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
