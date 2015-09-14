package io.ejf.mysql_java_docker.models;

import org.hibernate.Session;

import javax.persistence.*;

import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "tags")
public class TagModel {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "tag", length = 255, unique = true)
    private String tag;

    public TagModel(String tag) {
        this.tag = tag;
    }

    public TagModel(){}

    public static TagModel getFromDb(Session session, String tag){

        List tags = null;
        try {
            tags = session
                            .createSQLQuery("SELECT * FROM tags WHERE tag = :tag")
                            .addEntity(TagModel.class)
                            .setParameter("tag", tag)
                            .list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (null == tags)
            return new TagModel(tag);

        Iterator iterator = tags.iterator();
        if(iterator.hasNext())
            return (TagModel) iterator.next();

        return new TagModel(tag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
