package se.martenolsson.lah15.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by martenolsson on 16-05-28.
 */
public class RealmArticle extends RealmObject {
    @PrimaryKey
    public String id;
    public String json;

}
