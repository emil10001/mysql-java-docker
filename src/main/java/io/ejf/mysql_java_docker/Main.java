package io.ejf.mysql_java_docker;

import io.ejf.mysql_java_docker.models.TagModel;
import io.ejf.mysql_java_docker.models.UserModel;
import io.ejf.mysql_java_docker.models.UserTagModel;
import io.ejf.mysql_java_docker.utils.DatabaseInit;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

    public static void main(String[] args){
        System.out.println("---------------- mysql_java_docker main ----------------");

        seedDb();
        readDb();

        System.out.println("-------------- mysql_java_docker finished --------------");
    }

    private static void readDb() {
        System.out.println("readDb");
        Session session = DatabaseInit.SESSIONS.openSession();

        UserModel userModel = UserModel.getFromDb(session, "user");

        System.out.println("found user: " + userModel.getId() +
            ", " + userModel.getUsername() +
                        ", " + userModel.getDescription() +
                        ", tags: " );

        for (TagModel tag : userModel.getTags())
            System.out.println(tag.getId() + " - " + tag.getTag());

        session.close();
    }

    private static void seedDb() {
        System.out.println("seedDb");
        Session session = DatabaseInit.SESSIONS.openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            TagModel tagModel = TagModel.getFromDb(session, "sample");
            session.persist(tagModel);

            UserModel userModel = UserModel.getFromDb(session, "user");
            userModel.setDescription("just a sample user");
            userModel.getTags().add(tagModel);
            session.persist(userModel);

            UserTagModel userTagModel = UserTagModel.get(session, userModel, tagModel);
            session.persist(userTagModel);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null)
                tx.rollback();

            e.printStackTrace();
        }
        finally {
            session.close();
        }

        System.out.println("seeded");
    }
}
