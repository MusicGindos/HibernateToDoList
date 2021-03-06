package il.ac.shenkar.hibernate.model;

import javax.persistence.*;
/**
 * This class is used to instantiate and manipulate objects that represent as user
 * @author Arel Gindos
 * @author Dassi Rosen
 * @author Lior Lerner
 */
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;

    /**
     * Default Constructor
     */
    public User() {
        super();
    }

    /**
     * Constructor
     * @param username unique username chosen by the user
     * @param password the user chosen password
     */
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * Returns user id
     * @return the desire user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets user id
     * @param id unique autogenerated user id
     */
    public void setId(int id) {this.id = id;}

    /**
     * Returns username of the user
     * @return the desired username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets user name
     * @param username the desired username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns password of the user
     * @return the desired password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets user password
     * @param password the desired password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the object represent as string
     * @return String the toString of the object
     */
    @Override
    public String toString() {
        return "User [id=" + getId() + ", name=" +  ", userName=" + getUsername() + ", password=" + getPassword() + "]";
    }
}
