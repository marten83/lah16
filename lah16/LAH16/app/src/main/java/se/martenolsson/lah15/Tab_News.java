package se.martenolsson.lah15;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.db.RealmArticle;
import se.martenolsson.lah15.news.FeedItem;
import se.martenolsson.lah15.news.PreCachingLayoutManager;
import se.martenolsson.lah15.news.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martenolsson on 16-06-24.
 */
public class Tab_News extends Fragment {

    View v;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private List<FeedItem> feedItemList = new ArrayList<>(); //items from json
    Context mContext;
    static Realm realm;
    RequestQueue queue;
    TinyDB tinydb;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tab_news, container, false);
        mContext = container.getContext();
        queue = Volley.newRequestQueue(mContext);
        tinydb = new TinyDB(mContext);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new PreCachingLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter(getActivity(), feedItemList);
        mRecyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJson("http://lah16.bastardcreative.se/api/news");
            }
        });

        if(!tinydb.getString("news").isEmpty() && tinydb.getString("news") != null){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(tinydb.getString("news"));
            }catch (JSONException e) {e.printStackTrace();}
            //Log.e("test", String.valueOf(jsonObject));
            loadInList(jsonObject, true);
        }
        getJson("http://lah16.bastardcreative.se/api/news");

        return v;
    }

    void loadInList(JSONObject json, Boolean fromSaved){
        feedItemList.clear();
        try {
            JSONArray allItems = json.getJSONArray("payload");
            for (int i = 0; i < allItems.length(); i++) {
                JSONObject article = allItems.getJSONObject(i);

                String mId = article.getString("mid");
                String image = article.getString("image");
                String title = article.getString("title");
                String body = article.getString("body");

                FeedItem item = new FeedItem();
                item.setMid(mId);
                item.setImage(image);
                item.setTitle(title);
                item.setBody(body);
                feedItemList.add(item);

                //Log.i("title", title);


                /*Save in Realm*/
                if(!fromSaved) {
                    final RealmArticle obj = new RealmArticle();
                    obj.id = mId;
                    obj.json = article.toString();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(obj);
                        }
                    });
                }
                /*Save in Realm*/

            }
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    void getJson(final String url) {
        MainViewPager.cat.setAlpha(0.5f);
        MainViewPager.ao.setAlpha(1f);
        final JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        loadInList(json, false);
                        tinydb.putString("news", json.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        queue.add(stringRequest);
    }
}
