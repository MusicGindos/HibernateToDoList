package il.ac.shenkar.hibernate;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateToDoListDAO implements IToDoListDAO{
    private static HibernateToDoListDAO instance = null;
    private SessionFactory factory;
    private Session session;

    public static HibernateToDoListDAO getInstance() {  // singleton
        if (instance == null) {
            instance = new HibernateToDoListDAO();
        }
        return instance;
    }

    private HibernateToDoListDAO() {
        factory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void createUser(User user) throws ToiListException{  // create user using Hibernate
        try{
            session = factory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - createUser with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

//    @Override
//    public void deleteUser(User user) {  // delete user using Hibernate
//        session = factory.openSession();
//        session.beginTransaction();
//        session.delete(user);
//        session.getTransaction().commit();
//    }
//
//    @Override
//    public void updateUser(User user) {  // update user using Hibernate
//        session = factory.openSession();
//        session.beginTransaction();
//        session.update(user);
//        session.getTransaction().commit();
//    }

    @Override
    public void addItem(Item item) throws ToiListException{   // add item and attach it to existing user
        try{
            session = factory.openSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - addItem with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public void updateItem(Item item) throws ToiListException{  // update item
        try{
            session = factory.openSession();
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - updateItem with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public void deleteItem(Item item) throws ToiListException{  // delete item
        try{
            session = factory.openSession();
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - deleteItem with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

//    @Override
//    public List<User> getAllUsers() {  // get all users from the database
//        session = factory.openSession();
//        session.beginTransaction();
//        Query query = session.createQuery("from User");
//        List users = query.list();
//        //TODO: add if users is empty
//        return users;
//    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Item> getAllItemsByUserId(int userId) throws ToiListException{
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Item where userId = :userId");
            query.setParameter("userId", userId);
            return query.list();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getAllItemsByUserId with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public User getUserById(int userID) throws ToiListException{
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User where ID = :userId");
            query.setParameter("userId", userID);
            List list = query.list();
            return (User)list.get(0);
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getUserById with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

//    @Override
//    public Boolean checkIfDBExist() {
//        try{
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "todo", "1234");
//            ResultSet resultSet = conn.getMetaData().getCatalogs();
//            while (resultSet.next()) {
//                String databaseName = resultSet.getString(1);                // Get the database name, which is at position 1
//                if(databaseName.equals("todo")){
//                    return true;
//                }
//
//            }
//            return true;
//        }
//        catch(Exception SQLException){
//            return false;
//        }
//    }

    @Override
    public Item getItemByID(int itemID) throws ToiListException{
        Item item;
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Item where ID = :itemID");
            query.setParameter("itemID", itemID);
            List list = query.list();
            item = (Item)list.get(0);
            return item;
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getItemByID with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public boolean checkIfUserExists(User user) throws ToiListException{  // return if user exist in the database or not
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User where USERNAME= :username and PASSWORD= :password");
            query.setParameter("username",user.getUsername());
            query.setParameter("password",user.getPassword());
            List list = query.list();
            return !list.isEmpty();  // true if user exist
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - checkIfUserExists with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public int getUserIdByEmail(User user) throws ToiListException{
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User where USERNAME= :username");
            query.setParameter("username", user.getUsername());
            List list = query.list();
            User tempUser = (User)list.get(0);
            return tempUser.getId();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getUserIdByEmail with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public String getNameById(int userId) throws ToiListException{
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User where ID= :userId");
            query.setParameter("userId", userId);
            List list = query.list();
            User user = (User)list.get(0);
            return user.getUsername();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getNameById with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }

    @Override
    public List getAllTasks() throws ToiListException{
        try{
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Item");
            return query.list();
        }
        catch (HibernateException e){
            throw new ToiListException("Hibernate - getAllTasks with error:" + e.getMessage());
        }
        finally{
            session.close();
        }
    }
}
