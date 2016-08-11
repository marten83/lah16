package se.martenolsson.lah15.news;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import io.realm.Realm;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import se.martenolsson.lah15.R;
import se.martenolsson.lah15.classes.ObservableScrollView;
import se.martenolsson.lah15.classes.ReturnSpannableString;
import se.martenolsson.lah15.db.RealmArticle;

/**
 * Created by martenolsson on 16-06-24.
 */
public class NewsArticleView extends SwipeBackActivity implements ObservableScrollView.OnScrollChangedListener{

    String mId;
    ObservableScrollView mScrollView;
    float density;
    ImageView imageView;
    int screenWidth;
    LinearLayout closeBtn;
    FrameLayout imageHolder;

    @Override
    public void onBackPressed(){
        thisFinish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsarticle);
        overridePendingTransition(R.anim.slide_in_in, R.anim.slide_in);

        if(this.getIntent().getExtras() != null) {
            mId = this.getIntent().getExtras().getString("articleId");
        }

        closeBtn = (LinearLayout) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFinish();
            }
        });

        mScrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        mScrollView.setOnScrollChangedListener(this);
        density  = getResources().getDisplayMetrics().density;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        TextView titleView = (TextView) findViewById(R.id.title);
        TextView bodyView = (TextView) findViewById(R.id.body);
        imageView = (ImageView) findViewById(R.id.thumbnail);
        imageHolder = (FrameLayout) findViewById(R.id.imageHolder);

        FrameLayout.LayoutParams topImgHeight = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth * 0.5625));
        imageView.setLayoutParams(topImgHeight);

        Realm realm = Realm.getDefaultInstance();
        final RealmArticle jsonFromDb = realm.where(RealmArticle.class).equalTo("id", mId).findFirst();

        if(jsonFromDb != null) {
            Log.i("REALM", String.valueOf(jsonFromDb.json));

            try {
                JSONObject article = new JSONObject(jsonFromDb.json);
                if (article.has("title")) {
                    titleView.setText(article.getString("title"));
                }
                if (article.has("image") && !article.getString("image").equals("")) {
                    Glide.with(getApplicationContext())
                            .load(article.getString("image"))
                            .asBitmap()
                            .into(imageView);
                }else{
                    imageHolder.setVisibility(View.GONE);
                }
                if (article.has("body")) {
                    ReturnSpannableString returnSpan = new ReturnSpannableString();
                    bodyView.setMovementMethod(LinkMovementMethod.getInstance());
                    bodyView.setText(returnSpan.ReturnSpannableString(article.getString("body"), this));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        int scrollY = mScrollView.getScrollY();
        int ScrollValue = (int) ((scrollY * density) / 7);
        if(scrollY < (int) (screenWidth * 0.5625)){
            imageView.setTranslationY(ScrollValue);
        }
    }
    void thisFinish(){
        this.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
