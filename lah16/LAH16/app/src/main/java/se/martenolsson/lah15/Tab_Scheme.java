package se.martenolsson.lah15;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.customViewPager.SlidingTabLayoutText;
import se.martenolsson.lah15.customViewPager.ViewPagerAdapterScheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martenolsson on 16-06-24.
 */
public class Tab_Scheme extends Fragment {
    View v;
    static ViewPager pager;
    static ViewPagerAdapterScheme adapter;
    static SlidingTabLayoutText tabs;
    static Context mContext;
    RequestQueue queue;
    TinyDB tinydb;

    CharSequence Titles[]={"Onsdag","Torsdag","Fredag","Lördag"};

    private List<SchemeItem> onsdag = new ArrayList<>();
    private List<SchemeItem> torsdag = new ArrayList<>();
    private List<SchemeItem> fredag = new ArrayList<>();
    private List<SchemeItem> lordag = new ArrayList<>();

    JsonArray onsdagJson;
    JsonArray torsdagJson;
    JsonArray fredagJson;
    JsonArray lordagJson;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tabscheme, container, false);
        mContext = container.getContext();
        queue = Volley.newRequestQueue(mContext);
        tinydb = new TinyDB(mContext);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayoutText) v.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizerText(new SlidingTabLayoutText.TabColorizerText() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(mContext, R.color.theme_default_primary);
            }
        });

        getJson("http://lah16.bastardcreative.se/api/schedule");

        return v;
    }

    void setUpFromConfig(JsonArray onsdagJson, JsonArray torsdagJson, JsonArray fredagJson, JsonArray lordagJson){
        //adapter =  new ViewPagerAdapterScheme(getChildFragmentManager(), Titles, onsdagJson, torsdagJson, fredagJson, lordagJson);
        adapter =  new ViewPagerAdapterScheme(getChildFragmentManager(), Titles, onsdagJson, torsdagJson, fredagJson, lordagJson);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    void getJson(final String url) {
        MainViewPager.cat.setAlpha(0.5f);
        MainViewPager.ao.setAlpha(1f);
        final JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        if(json != null) {
                            sortToDay(json);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        queue.add(stringRequest);
    }

    void sortToDay(JSONObject json){
        try {
        JSONArray allmarkers = json.getJSONArray("payload");
            for (int i = 0; i < allmarkers.length(); i++) {
                JSONObject c = allmarkers.getJSONObject(i);

                SchemeItem item = new SchemeItem();
                item.setStage(c.getString("stage"));
                item.setDate(c.getString("date"));
                item.setOrgDate(c.getString("date"));
                item.setArtistId(c.getString("artistid"));
                item.setArtist(c.getString("artist"));

                if(c.getString("date").contains("2016-08-31") || c.getString("date").contains("2016-09-01T00") || c.getString("date").contains("2016-09-01T01")){
                    onsdag.add(item);
                }else if(c.getString("date").contains("2016-09-01") || c.getString("date").contains("2016-09-02T00")  || c.getString("date").contains("2016-09-02T01")){
                    torsdag.add(item);
                }else if(c.getString("date").contains("2016-09-02") || c.getString("date").contains("2016-09-03T00") || c.getString("date").contains("2016-09-03T01")){
                    fredag.add(item);
                }else{
                    lordag.add(item);
                }

            }

            Gson gson = new GsonBuilder().create();
            onsdagJson = gson.toJsonTree(onsdag).getAsJsonArray();
            torsdagJson = gson.toJsonTree(torsdag).getAsJsonArray();
            fredagJson = gson.toJsonTree(fredag).getAsJsonArray();
            lordagJson = gson.toJsonTree(lordag).getAsJsonArray();

            setUpFromConfig(onsdagJson, torsdagJson, fredagJson, lordagJson);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static void sortStage(){
        int currentPage = pager.getCurrentItem();

        ((ApplicationController) mContext.getApplicationContext()).currentStageSort = "stage";
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        pager.setCurrentItem(currentPage);
    }

    static void sortTime(){
        int currentPage = pager.getCurrentItem();

        ((ApplicationController) mContext.getApplicationContext()).currentStageSort = "time";
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        pager.setCurrentItem(currentPage);
    }

    static void updateSchemePages(){
        if(adapter != null) {
            int currentPage = pager.getCurrentItem();
            pager.setAdapter(adapter);
            tabs.setViewPager(pager);
            pager.setCurrentItem(currentPage);
        }
    }
}
