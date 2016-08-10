package se.martenolsson.lah15;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import se.martenolsson.lah15.classes.SchemeRecyclerAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by martenolsson on 16-06-24.
 */
public class Tab_SchemePage extends Fragment {
    View v;
    Context mContext;
    String schemeTitle;
    String scheme;
    JsonArray dayScheme;
    private RecyclerView mRecyclerView;
    private SchemeRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<SchemeItem> feedItemList = new ArrayList<>();
    String stageSort;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tabschemepage, container, false);
        mContext = container.getContext();

        if(getArguments() != null) {
            schemeTitle = getArguments().getString("title");
            scheme = getArguments().getString("day");
        }

        stageSort =  ((ApplicationController) mContext.getApplicationContext()).currentStageSort;

        dayScheme = new JsonParser().parse(scheme).getAsJsonArray();
        Log.i("dayScheme", String.valueOf(dayScheme));

        /* Initialize recyclerview */
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < dayScheme.size(); i++) {
            JsonObject c = dayScheme.get(i).getAsJsonObject();
            SchemeItem item = new SchemeItem();
            item.setStage(c.get("stage").getAsString());

            //Convert Date
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date dateObj = null;
            try {
                dateObj = curFormater.parse(c.get("date").getAsString());
            } catch (ParseException e) {e.printStackTrace();}
            SimpleDateFormat postFormater = new SimpleDateFormat("HH:mm");
            String newDateStr = postFormater.format(dateObj);
            //end Convert Date

            item.setDate(newDateStr);
            item.setOrgDate(c.get("date").getAsString());
            item.setArtistId(c.get("artistId").getAsString());
            item.setArtist(c.get("artist").getAsString());

            feedItemList.add(item);
        }

        for (int i = 0; i < feedItemList.size(); i++) {
            Log.i("sort", feedItemList.get(i).getDate());
        }

        adapter = new SchemeRecyclerAdapter(getActivity(), feedItemList);
        mRecyclerView.setAdapter(adapter);

        if(stageSort.equals("stage")){
            sortStage();
            MainViewPager.time.setAlpha(0.5f);
            MainViewPager.stage.setAlpha(1f);
        }else{
            sortTime();
            MainViewPager.time.setAlpha(1f);
            MainViewPager.stage.setAlpha(0.5f);
        }


        return v;
    }

    void sortTime(){
        Collections.sort(feedItemList, new Comparator<SchemeItem>() {
            @Override
            public int compare(SchemeItem lhs, SchemeItem rhs) {
                return  lhs.getOrgDate().compareTo(rhs.getOrgDate());
            }
        });

        adapter.notifyDataSetChanged();
    }

    void sortStage(){
        Collections.sort(feedItemList, new Comparator<SchemeItem>() {
            @Override
            public int compare(SchemeItem lhs, SchemeItem rhs) {
                return  lhs.getStage().compareTo(rhs.getStage());
            }
        });

        adapter.notifyDataSetChanged();
    }
}
