package se.martenolsson.lah15.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import se.martenolsson.lah15.R;

import java.util.List;

/**
 * Created by martenolsson on 15-12-08.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<FeedItem> feedItemList;
    private Context mContext;
    int viewTypeCell;

    class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView title;
        protected LinearLayout linearLayout;

        public ViewHolder(View view, Boolean isAd) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            this.linearLayout.setOnClickListener(teaserClicklistner);
        }

        View.OnClickListener teaserClicklistner = new View.OnClickListener() {
            public void onClick(View view) {
                FeedItem feedItem = feedItemList.get(getAdapterPosition());
                Intent intent = new Intent(mContext, NewsArticleView.class);
                intent.putExtra("articleId", feedItem.getMid());
                mContext.startActivity(intent);
            }
        };

    }

    public RecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        setHasStableIds(true);
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewTypeCell = viewType;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_cell, viewGroup, false);
        return new ViewHolder(v, false);
    }


    @Override
    public int getItemViewType(int position) {
        return 1000;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public FeedItem getItem(int position) {
        return feedItemList.get(position);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final float density  = mContext.getResources().getDisplayMetrics().density;
        FeedItem feedItem = getItem(i);


            viewHolder.title.setText(feedItem.getTitle());

            Log.i("sasa",feedItem.getTitle());

            if(!feedItem.getImage().equals("")) {
                viewHolder.image.setVisibility(View.VISIBLE);
                String setImage = feedItem.getImage();

                Glide.with(viewHolder.image.getContext())
                        .load(setImage)
                        .asBitmap()
                        .into(viewHolder.image);
            }else{
                viewHolder.image.setVisibility(View.GONE);
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