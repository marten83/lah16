package se.martenolsson.lah15;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import java.io.IOException;
import java.util.ArrayList;

import com.onesignal.OneSignal;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import org.json.JSONException;
import org.json.JSONObject;
import se.martenolsson.lah15.classes.ObservableScrollView;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.classes.mediaPlayer;
import se.martenolsson.lah15.db.RealmArticle;

public class SingleItemView extends SwipeBackActivity implements ObservableScrollView.OnScrollChangedListener {
	// Declare Variables
	TextView txtTitle;
	TextView txtText;
	TextView txtMusik;
	TextView txtPlace;
    Typeface titleText;
    Typeface smallText;

    TextView button1;
    TextView button2;
    TextView button3;
    TextView button4;
    TextView button5;

    FrameLayout hiddenImageCont;
    ImageView hiddenImage;

	ImageView largeImage;
	ImageView smallImage;

	private ObservableScrollView mScrollView;

	String title;
	String musik;
	String place;
	String text;
	String mp3;
	String image;
    String mId;

	float density;

    MediaPlayer myMediaPlayer;
    //String mediaUrl;
    Boolean fromPlayBtn = false;

	LinearLayout followBtn;
	LinearLayout stopFollowBtn;

	LinearLayout playBtn;
	LinearLayout stopBtn;
	LinearLayout vanta;
    //LinearLayout closeBtn;

	ArrayList<String> followList = new ArrayList<>();
	TinyDB tinydb;

	Toolbar toolbar;

	@Override
	public void onBackPressed(){
        thisFinish();
	}
	public void onButtonStop(View v) throws IOException {
		playBtn.setVisibility(View.VISIBLE);
		stopBtn.setVisibility(View.GONE);
		myMediaPlayer.pause();
	}
	public void onButtonLyssna(View v) throws IOException {
		playBtn.setVisibility(View.GONE);
        fromPlayBtn = true;
        new mediaPlayer(this, title, mp3, fromPlayBtn, vanta, stopBtn, playBtn, true);
	}

	public void onstopButtonFollow(View v) {
		followList = tinydb.getList("followList2");
		for(int i = 0; i < followList.size(); i++) {
			if(followList.get(i).contains(title)){
				followList.remove(i);
				tinydb.putList("followList2", followList);
				followBtn.setVisibility(View.VISIBLE);
				stopFollowBtn.setVisibility(View.GONE);

				Log.i("FollowID", mId);
				OneSignal.deleteTag(mId);
			}

		}

	}
	public void onButtonFollow(View v) {
		followList = tinydb.getList("followList2");
		Boolean foundInList = false;
		//Toast.makeText(getApplicationContext(), "Du kommer att bli påmind när saker händer kring artisten", Toast.LENGTH_LONG).show();
		for(int i = 0; i < followList.size(); i++) {
			if(followList.get(i).contains(title)){
				foundInList = true;
			}

		}
		if(foundInList == false){
			followList.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image + ";;" + mId);
			tinydb.putList("followList2", followList);
			followBtn.setVisibility(View.GONE);
			stopFollowBtn.setVisibility(View.VISIBLE);

			Log.i("FollowID", mId);
			OneSignal.sendTag(mId, "1");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview);
        overridePendingTransition(R.anim.slide_in_in, R.anim.slide_in);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
        smallText = ((ApplicationController) getApplicationContext()).caviar;
        titleText = ((ApplicationController) getApplicationContext()).geoSansBold;

		followBtn = (LinearLayout) findViewById(R.id.followBtn);
		stopFollowBtn = (LinearLayout) findViewById(R.id.stopfollowBtn);
		playBtn = (LinearLayout) findViewById(R.id.play);
		stopBtn = (LinearLayout) findViewById(R.id.stop);
		vanta = (LinearLayout) findViewById(R.id.vanta);
        /*closeBtn = (LinearLayout) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFinish();
            }
        });*/

        button1 = (TextView) findViewById(R.id.button1);
        button2 = (TextView) findViewById(R.id.button2);
        button3 = (TextView) findViewById(R.id.button3);
        button4 = (TextView) findViewById(R.id.button4);
        button5 = (TextView) findViewById(R.id.laddarlat);

        button1.setTypeface(titleText);
        button2.setTypeface(titleText);
        button3.setTypeface(titleText);
        button4.setTypeface(titleText);
        button5.setTypeface(titleText);

        hiddenImageCont = (FrameLayout) findViewById(R.id.imgCont2);
        hiddenImage = (ImageView) findViewById(R.id.image2);

		mScrollView = (ObservableScrollView)findViewById(R.id.scrollview);
		mScrollView.setOnScrollChangedListener(this);

		tinydb = new TinyDB(this);

		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		density  = getResources().getDisplayMetrics().density;
		Point size = new Point();
		display.getSize(size);
		int screenHeight = size.y / (int)density;
		int textHeight = 400;
		int paddingTop = (screenHeight - textHeight) * (int)density;
		//Log.e("test", String.valueOf(paddingTop));
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.paddinLayout);
		layout.setPadding(0,paddingTop,0,0);

		// Retrieve data from MainActivity on item click event
		Intent i = getIntent();
		title = i.getStringExtra("title");
		musik = i.getStringExtra("musik");
		place = i.getStringExtra("place");
		text = i.getStringExtra("text");
		image = i.getStringExtra("image");
		mp3 = i.getStringExtra("mp3");
        mId = i.getStringExtra("mId");
        Log.e("mId", "mId"+mId);

        if(title == null){
            Realm realm = Realm.getDefaultInstance();
            final RealmArticle jsonFromDb = realm.where(RealmArticle.class).equalTo("id", mId).findFirst();

            if(jsonFromDb != null) {
                try {
                    JSONObject article = new JSONObject(jsonFromDb.json);
                    title = article.getString("name");
                    musik = article.getString("category");
                    place = article.getString("city");
                    text = article.getString("description");
                    image = article.getString("image");
                    mp3 = article.getString("songurl");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        mp3 = mp3.replace("https","http");
		/*Player*/
        if(mp3 != null) {
            myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;
            new mediaPlayer(this, title, mp3, fromPlayBtn, vanta, stopBtn, playBtn, true);
        }
		/*end Player*/

        if(title != null) {
            followList = tinydb.getList("followList2");
            Boolean foundInList = false;
            for (int index = 0; index < followList.size(); index++) {
                if (followList.get(index).contains(title)) {
                    foundInList = true;
                }

            }
            if (foundInList == true) {
                followBtn.setVisibility(View.GONE);
                stopFollowBtn.setVisibility(View.VISIBLE);
            }


            txtMusik = (TextView) findViewById(R.id.musik);
            txtMusik.setTypeface(smallText);
            txtTitle = (TextView) findViewById(R.id.title);
            txtTitle.setTypeface(titleText);
            txtPlace = (TextView) findViewById(R.id.place);
            txtPlace.setTypeface(smallText);
            txtText = (TextView) findViewById(R.id.text);
            txtText.setTypeface(smallText);
            largeImage = (ImageView) findViewById(R.id.image);
            smallImage = (ImageView) findViewById(R.id.imageamall);
            ImageView arrowDown = (ImageView) findViewById(R.id.arrowdown);


            // Load the results into the TextViews
            txtMusik.setText(musik.toUpperCase());
            txtTitle.setText(title);
            txtPlace.setText(place);

            if (!text.isEmpty()) {
                txtText.setText(text);
            } else {
                txtText.setVisibility(View.GONE);
                arrowDown.setVisibility(View.GONE);
            }

            final Context mContext = this;
            Glide.with(this)
                    .load(image)
                    .centerCrop()
                    .bitmapTransform(new BlurTransformation(this, Glide.get(this).getBitmapPool()))
                    .listener(
                            GlidePalette.with(image)
                                    .use(GlidePalette.Profile.MUTED_DARK)
                                    .intoBackground(txtText)
                            //.intoTextColor(txtText)
                    )
                    .into(largeImage);

            Glide.with(this)
                    .load(image)
                    .bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool()))
                    .into(smallImage);

            Glide.with(this)
                    .load(image)
                    .into(hiddenImage);


            smallImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        hiddenImageCont.setVisibility(View.VISIBLE);
                        smallImage.setVisibility(View.INVISIBLE);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        hiddenImageCont.setVisibility(View.INVISIBLE);
                        smallImage.setVisibility(View.VISIBLE);
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        hiddenImageCont.setVisibility(View.INVISIBLE);
                        smallImage.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });
        }
	}

    Rect scrollBounds = new Rect();

	@Override
	public void onScrollChanged(int deltaX, int deltaY) {
		int scrollY = mScrollView.getScrollY();
		int ScrollValue = (int) ((scrollY * (int)density)/7.5);

		LinearLayout btnCont = (LinearLayout) findViewById(R.id.btnCont);
        FrameLayout blurImageCont =  (FrameLayout) findViewById(R.id.imgCont);
        hiddenImageCont.setTranslationY(-ScrollValue);
        blurImageCont.setTranslationY(-ScrollValue);


        mScrollView.getHitRect(scrollBounds);
        if(scrollY-btnCont.getTop() > 0){
            btnCont.setTranslationY(scrollY-btnCont.getTop());
            btnCont.bringToFront();
        }else{
            btnCont.setTranslationY(0);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myMediaPlayer != null) {
            if (!myMediaPlayer.isPlaying()) {
                myMediaPlayer.stop();
                myMediaPlayer.reset();
            }
        }
    }

    void thisFinish(){
        this.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}