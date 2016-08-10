package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmFieldType;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.martenolsson.lah15.db.RealmArticle;

public class RealmArticleRealmProxy extends RealmArticle
    implements RealmObjectProxy, RealmArticleRealmProxyInterface {

    static final class RealmArticleColumnInfo extends ColumnInfo {

        public final long idIndex;
        public final long jsonIndex;

        RealmArticleColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(2);
            this.idIndex = getValidColumnIndex(path, table, "RealmArticle", "id");
            indicesMap.put("id", this.idIndex);

            this.jsonIndex = getValidColumnIndex(path, table, "RealmArticle", "json");
            indicesMap.put("json", this.jsonIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final RealmArticleColumnInfo columnInfo;
    private final ProxyState proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("json");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    RealmArticleRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (RealmArticleColumnInfo) columnInfo;
        this.proxyState = new ProxyState(RealmArticle.class, this);
    }

    @SuppressWarnings("cast")
    public String realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idIndex);
    }

    public void realmSet$id(String value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.idIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.idIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$json() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.jsonIndex);
    }

    public void realmSet$json(String value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.jsonIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.jsonIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_RealmArticle")) {
            Table table = transaction.getTable("class_RealmArticle");
            table.addColumn(RealmFieldType.STRING, "id", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "json", Table.NULLABLE);
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_RealmArticle");
    }

    public static RealmArticleColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_RealmArticle")) {
            Table table = transaction.getTable("class_RealmArticle");
            if (table.getColumnCount() != 2) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 2 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final RealmArticleColumnInfo columnInfo = new RealmArticleColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("id") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'id' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.idIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(),"@PrimaryKey field 'id' does not support null values in the existing Realm file. Migrate using RealmObjectSchema.setNullable(), or mark the field as @Required.");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id' in existing Realm file. Add @PrimaryKey.");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
            }
            if (!columnTypes.containsKey("json")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'json' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("json") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'json' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.jsonIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'json' is required. Either set @Required to field 'json' or migrate using RealmObjectSchema.setNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The RealmArticle class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_RealmArticle";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static RealmArticle createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        RealmArticle obj = null;
        if (update) {
            Table table = realm.getTable(RealmArticle.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = TableOrView.NO_MATCH;
            if (json.isNull("id")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("id"));
            }
            if (rowIndex != TableOrView.NO_MATCH) {
                obj = new RealmArticleRealmProxy(realm.schema.getColumnInfo(RealmArticle.class));
                ((RealmObjectProxy)obj).realmGet$proxyState().setRealm$realm(realm);
                ((RealmObjectProxy)obj).realmGet$proxyState().setRow$realm(table.getUncheckedRow(rowIndex));
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (RealmArticleRealmProxy) realm.createObject(RealmArticle.class, null);
                } else {
                    obj = (RealmArticleRealmProxy) realm.createObject(RealmArticle.class, json.getString("id"));
                }
            } else {
                obj = (RealmArticleRealmProxy) realm.createObject(RealmArticle.class);
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                ((RealmArticleRealmProxyInterface) obj).realmSet$id(null);
            } else {
                ((RealmArticleRealmProxyInterface) obj).realmSet$id((String) json.getString("id"));
            }
        }
        if (json.has("json")) {
            if (json.isNull("json")) {
                ((RealmArticleRealmProxyInterface) obj).realmSet$json(null);
            } else {
                ((RealmArticleRealmProxyInterface) obj).realmSet$json((String) json.getString("json"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static RealmArticle createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        RealmArticle obj = realm.createObject(RealmArticle.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((RealmArticleRealmProxyInterface) obj).realmSet$id(null);
                } else {
                    ((RealmArticleRealmProxyInterface) obj).realmSet$id((String) reader.nextString());
                }
            } else if (name.equals("json")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((RealmArticleRealmProxyInterface) obj).realmSet$json(null);
                } else {
                    ((RealmArticleRealmProxyInterface) obj).realmSet$json((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static RealmArticle copyOrUpdate(Realm realm, RealmArticle object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        RealmArticle realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(RealmArticle.class);
            long pkColumnIndex = table.getPrimaryKey();
            String value = ((RealmArticleRealmProxyInterface) object).realmGet$id();
            long rowIndex = TableOrView.NO_MATCH;
            if (value == null) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, value);
            }
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new RealmArticleRealmProxy(realm.schema.getColumnInfo(RealmArticle.class));
                ((RealmObjectProxy)realmObject).realmGet$proxyState().setRealm$realm(realm);
                ((RealmObjectProxy)realmObject).realmGet$proxyState().setRow$realm(table.getUncheckedRow(rowIndex));
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static RealmArticle copy(Realm realm, RealmArticle newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmArticle realmObject = realm.createObject(RealmArticle.class, ((RealmArticleRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((RealmArticleRealmProxyInterface) realmObject).realmSet$id(((RealmArticleRealmProxyInterface) newObject).realmGet$id());
        ((RealmArticleRealmProxyInterface) realmObject).realmSet$json(((RealmArticleRealmProxyInterface) newObject).realmGet$json());
        return realmObject;
    }

    public static RealmArticle createDetachedCopy(RealmArticle realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        RealmArticle unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (RealmArticle)cachedObject.object;
            } else {
                unmanagedObject = (RealmArticle)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new RealmArticle();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((RealmArticleRealmProxyInterface) unmanagedObject).realmSet$id(((RealmArticleRealmProxyInterface) realmObject).realmGet$id());
        ((RealmArticleRealmProxyInterface) unmanagedObject).realmSet$json(((RealmArticleRealmProxyInterface) realmObject).realmGet$json());
        return unmanagedObject;
    }

    static RealmArticle update(Realm realm, RealmArticle realmObject, RealmArticle newObject, Map<RealmModel, RealmObjectProxy> cache) {
        ((RealmArticleRealmProxyInterface) realmObject).realmSet$json(((RealmArticleRealmProxyInterface) newObject).realmGet$json());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("RealmArticle = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{json:");
        stringBuilder.append(realmGet$json() != null ? realmGet$json() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealmArticleRealmProxy aRealmArticle = (RealmArticleRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aRealmArticle.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aRealmArticle.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aRealmArticle.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
