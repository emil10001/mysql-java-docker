package io.ejf.mysql_java_docker.models;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username", length = 255, unique = true)
    private String username;

    @Column(name = "description", length = 255, unique = true)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id", cascade = {CascadeType.REMOVE})
    private Set<TagModel> tags = new HashSet<>();

    public UserModel(String username) {
        this.username = username;
    }

    public UserModel() {}

    public static UserModel getFromDb(Session session, String username){
        List users = null;
        try {
            users = session
                    .createSQLQuery("SELECT * FROM users WHERE username = :username")
                    .addEntity(UserModel.class)
                    .setParameter("username", username)
                    .list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (null == users)
            return new UserModel(username);

        Iterator iterator = users.iterator();
        if(iterator.hasNext())
            return (UserModel) iterator.next();

        return new UserModel(username);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }

}
