package io.ejf.mysql_java_docker.models;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "userTags",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "user_id", "tag_id"}))
public class UserTagModel {
    @Id
    @GeneratedValue
    public Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "user_id"), referencedColumnName = "id", nullable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(name = "tag_id"), referencedColumnName = "id", nullable = false)
    private TagModel tag;

    public UserTagModel(UserModel user, TagModel tag) {
        this.user = user;
        this.tag = tag;
    }

    public UserTagModel(){}

    public static UserTagModel get(Session session,
                                        UserModel user,
                                        TagModel tag) {
        List userTags = null;
        try {
            userTags = session
                    .createSQLQuery("SELECT * FROM userTags WHERE user_id = :user_id AND tag_id = :tag_id")
                    .addEntity(UserTagModel.class)
                    .setParameter("user_id", user.getId())
                    .setParameter("tag_id", tag.getId())
                    .list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (null == userTags)
            return new UserTagModel(user, tag);

        Iterator iterator = userTags.iterator();
        if(iterator.hasNext())
            return (UserTagModel) iterator.next();

        return new UserTagModel(user, tag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public TagModel getTag() {
        return tag;
    }

    public void setTag(TagModel tag) {
        this.tag = tag;
    }
}
