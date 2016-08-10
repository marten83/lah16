package se.martenolsson.lah15.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.martenolsson.lah15.ApplicationController;
import se.martenolsson.lah15.R;
import se.martenolsson.lah15.SchemeItem;
import se.martenolsson.lah15.SingleItemView;
import se.martenolsson.lah15.news.FeedItem;

/**
 * Created by martenolsson on 15-12-08.
 */
public class SchemeRecyclerAdapter extends RecyclerView.Adapter<SchemeRecyclerAdapter.ViewHolder> {

    private List<SchemeItem> feedItemList;
    //private Boolean isToggled = false;
    private Context mContext;
    int viewTypeCell;
    //ApplicationController appController;

    ArrayList<String> followList = new ArrayList<>();
    TinyDB tinydb;

    class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView header;
        protected TextView title;
        protected TextView time;
        protected TextView stage;
        protected TextView heart;
        protected RelativeLayout teaser;
        protected Typeface geoSans;


        public ViewHolder(View view) {
            super(view);
            geoSans = ((ApplicationController) mContext.getApplicationContext()).geoSans;
            this.header = (TextView) view.findViewById(R.id.header);
            this.header.setTypeface(geoSans);
            this.title = (TextView) view.findViewById(R.id.title);
            this.title.setTypeface(geoSans);
            this.time = (TextView) view.findViewById(R.id.time);
            this.stage = (TextView) view.findViewById(R.id.stage);
            this.teaser = (RelativeLayout) view.findViewById(R.id.teaser);
            this.teaser.setOnClickListener(teaserClicklistner);
            this.heart = (TextView) view.findViewById(R.id.heart);
        }

        View.OnClickListener teaserClicklistner = new View.OnClickListener() {
            public void onClick(View view) {
                SchemeItem feedItem = feedItemList.get(getAdapterPosition());
                Intent intent = new Intent(mContext, SingleItemView.class);
                intent.putExtra("mId", feedItem.getArtistId());
                mContext.startActivity(intent);
            }
        };


    }

    public SchemeRecyclerAdapter(Context context, List<SchemeItem> feedItemList) {
        setHasStableIds(true);
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    /*public void setIsToggled(boolean value) {
        this.isToggled = value;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewTypeCell = viewType;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schemeview_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public SchemeItem getItem(int position) {
        return feedItemList.get(position);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        tinydb = new TinyDB(mContext);
        SchemeItem feedItem = getItem(i);

        if(((ApplicationController) mContext.getApplicationContext()).currentStageSort.equals("stage")) {
            String currentHeader = "";
            if (i > 0) {
                currentHeader = getItem(i - 1).getStage();
            }
            if (!feedItem.getStage().equals(currentHeader)) {
                viewHolder.header.setVisibility(View.VISIBLE);
            } else {
                viewHolder.header.setVisibility(View.GONE);
            }
        }

        viewHolder.title.setText(feedItem.getArtist().toUpperCase());
        viewHolder.time.setText(feedItem.getDate());
        viewHolder.stage.setText(feedItem.getStage());
        viewHolder.header.setText(feedItem.getStage());


        ArrayList<Integer> follow = new ArrayList<>();
        followList = tinydb.getList("followList2");
        for(int index = 0; index < followList.size(); index++) {
            if(followList.get(index).contains(feedItem.getArtist())){
                follow.add(i);
            }
        }
        if(follow.contains(i)) {
            viewHolder.heart.setVisibility(View.VISIBLE);
        }else{
            viewHolder.heart.setVisibility(View.GONE);
        }

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}