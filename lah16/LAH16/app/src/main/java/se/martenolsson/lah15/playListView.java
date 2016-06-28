package se.martenolsson.lah15;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.classes.mediaPlayer;

/**
 * Created by martenolsson on 16-04-22.
 */
public class playListView extends SwipeBackActivity {
    ArrayList<String> playList = new ArrayList<>();
    RecyclerView mRecyclerView;
    public static MyRecyclerAdapter adapter;
    TinyDB tinydb;
    LinearLayout clear;
    LinearLayout play;
    TextView playBtn;
    MediaPlayer myMediaPlayer;
    ProgressBar progressBar;
    Context mContext;
    Handler h;
    TextView infoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        mContext = this;
        tinydb = new TinyDB(this);
        playList = tinydb.getList("playList");
        myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        infoText = (TextView) findViewById(R.id.infoText);

        clear = (LinearLayout) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Rensa låtkö?")
                        .setMessage("Du kommer inte att kunna ångra dig.")
                        .setNeutralButton("Rensa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                playList.clear();
                                tinydb.putList("playList", playList);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton("Stäng", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //close dialog
                            }
                        })
                        .show();
            }
        });

        play = (LinearLayout) findViewById(R.id.play);
        playBtn = (TextView) findViewById(R.id.playBtn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myMediaPlayer.getCurrentPosition() > 0 && !myMediaPlayer.isPlaying()) {
                    myMediaPlayer.start();
                    playBtn.setText(Html.fromHtml("&#xf1f9;"));
                } else if (myMediaPlayer.getCurrentPosition() > 0) {
                    myMediaPlayer.pause();
                    playBtn.setText(Html.fromHtml("&#xf115;"));
                }
            }
        });

        final LinearLayoutManager linearLayoutManagerComment = new LinearLayoutManager(this);
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerViewComments);

        mRecyclerView.setLayoutManager(linearLayoutManagerComment);
        mRecyclerView.hasFixedSize();
        adapter=new MyRecyclerAdapter(this,this.playList);
        mRecyclerView.setAdapter(adapter);

        if(playList.size() > 0){
            infoText.setVisibility(View.GONE);
        }else{
            infoText.setVisibility(View.VISIBLE);
        }

        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myMediaPlayer.getCurrentPosition() > 0 && myMediaPlayer.isPlaying()) {
                    int progress = (int) (((double) myMediaPlayer.getCurrentPosition() / (double) myMediaPlayer.getDuration()) * 1000);

                    progressBar.setMax(1000);
                    progressBar.setProgress(progress);

                    Log.i("time", String.valueOf(progress));
                }

                h.postDelayed(this, 200);
            }
        }, 200);
    }


    @Override
    public void onResume() {
        super.onResume();

        if(myMediaPlayer.isPlaying()) {
            playBtn.setText(Html.fromHtml("&#xf1f9;"));
        }else{
            playBtn.setText(Html.fromHtml("&#xf115;"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacksAndMessages(null);
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
        ArrayList<String> playList = new ArrayList<>();
        Context mContext;
        Typeface titleText;

        class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView artist;
            protected LinearLayout removeFromPlayList;
            protected SwipeLayout swipe;

            public ViewHolder(View view) {
                super(view);
                titleText = ((ApplicationController) getApplicationContext()).geoSans;
                this.artist = (TextView) view.findViewById(R.id.artist);
                this.artist.setTypeface(titleText);
                this.removeFromPlayList = (LinearLayout) view.findViewById(R.id.removeFromPlayList);
                this.swipe = (SwipeLayout) view.findViewById(R.id.swipe);
            }
        }

        public MyRecyclerAdapter(Context context, ArrayList playList) {
            setHasStableIds(true);
            this.playList = playList;
            this.mContext = context;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return playList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.playlist_row, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        View.OnClickListener viewClickListner = new View.OnClickListener() {
            public void onClick(View view) {
                HashMap click = (HashMap) view.getTag();
                int index = (int) click.get("index");
                SwipeLayout swipeView = (SwipeLayout) click.get("swipeView");

                if(swipeView.getOpenStatus()==SwipeLayout.Status.Open){
                    swipeView.close();
                }else {
                    String[] listitem = playList.get(index).split(";;");
                    String txtArtist = listitem[0];
                    String mp3 = listitem[1];
                    new mediaPlayer(mContext, txtArtist, mp3, false, null, null, null, false);

                    playBtn.setText(Html.fromHtml("&#xf1f9;"));
                    notifyDataSetChanged();
                }
            }
        };

        View.OnClickListener remove = new View.OnClickListener() {
            public void onClick(View view) {
                HashMap remove = (HashMap) view.getTag();
                int index = (int) remove.get("index");
                SwipeLayout swipeView = (SwipeLayout) remove.get("swipeView");
                swipeView.close();
                playList.remove(index);
                tinydb.putList("playList", playList);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, 300);
            }
        };

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final float density  = mContext.getResources().getDisplayMetrics().density;
            String[] listitem = playList.get(i).split(";;");
            String txtArtist = listitem[0];
            String mp3 = listitem[1];

            if(i==(playList.size()-1)){
                viewHolder.swipe.setPadding(0, 0, 0, 70 * (int) density);
            }

            if (((ApplicationController) mContext.getApplicationContext()).currentSong != null) {
                if(((ApplicationController) mContext.getApplicationContext()).currentSong.equals(mp3)){
                    viewHolder.artist.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_default_primary));
                    viewHolder.artist.setText(txtArtist.toUpperCase());
                }else {
                    viewHolder.artist.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
                    viewHolder.artist.setText(txtArtist.toUpperCase());
                }
            }else {
                viewHolder.artist.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
                viewHolder.artist.setText(txtArtist.toUpperCase());
            }
            viewHolder.artist.setOnClickListener(viewClickListner);
            HashMap<String, Object> click = new HashMap<>();
            click.put("index", i);
            click.put("swipeView", viewHolder.swipe);
            viewHolder.artist.setTag(click);

            viewHolder.removeFromPlayList.setOnClickListener(remove);
            HashMap<String, Object> remove = new HashMap<>();
            remove.put("index", i);
            remove.put("swipeView", viewHolder.swipe);
            viewHolder.removeFromPlayList.setTag(remove);

        }

    }
}
