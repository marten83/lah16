package se.martenolsson.lah15;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import se.martenolsson.lah15.classes.TinyDB;

import java.util.ArrayList;

/**
 * Created by martenolsson on 16-06-21.
 */
public class Tab_Follow extends Fragment {
    View v;

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    Toolbar toolbar;
    TextView infoText;

    MediaPlayer myMediaPlayer;
    LinearLayout playBtn;
    TextView playBtnText;

    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

    ArrayList<String> followList = new ArrayList<>();
    TinyDB tinydb;

    Context mContext;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Log.e("VISIBLE","VISIBLE");
            arraylist.clear();
            followList = tinydb.getList("followList2");

            if(followList.size() > 0){
                infoText.setVisibility(View.GONE);
            }else{
                infoText.setVisibility(View.VISIBLE);
            }

            for(int i = 0; i < followList.size(); i++) {

                String[] listitem = followList.get(i).split(";;");
                String title = listitem[0];
                String musik = listitem[1];
                String place = listitem[2];
                String text = listitem[3];
                String mp3 = listitem[4];
                String image = listitem[5];
                String mId = listitem[6];

                WorldPopulation wp = new WorldPopulation(mId, title, musik, text,  place, mp3, image);
                arraylist.add(wp);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tab_follow, container, false);

        mContext = container.getContext();

        myMediaPlayer = ((ApplicationController) mContext.getApplicationContext()).myMediaPlayer;
        playBtn = (LinearLayout) v.findViewById(R.id.playBtn);
        playBtnText = (TextView) v.findViewById(R.id.playBtnText);
        infoText = (TextView) v.findViewById(R.id.infoText);

        tinydb = new TinyDB(mContext);
        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);


        loadInList();

        return v;
    }
    void loadInList(){
        list = (ListView) v.findViewById(R.id.listview);
        arraylist.clear();
        list.setAdapter(null);
        tinydb = new TinyDB(mContext);
        followList = tinydb.getList("followList2");
        for(int i = 0; i < followList.size(); i++) {

            String[] listitem = followList.get(i).split(";;");
            String title = listitem[0];
            String musik = listitem[1];
            String place = listitem[2];
            String text = listitem[3];
            String mp3 = listitem[4];
            String image = listitem[5];
            String mId = listitem[6];

            WorldPopulation wp = new WorldPopulation(mId, title, musik, text,  place, mp3, image);
            arraylist.add(wp);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(mContext, arraylist, MainViewPager.playBtn, MainViewPager.playBtnText, myMediaPlayer);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


    }


}

