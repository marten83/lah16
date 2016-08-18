package se.martenolsson.lah15;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    LinearLayout textWrapper;
    LinearLayout scheduleWrapper;
    LinearLayout border;
    LinearLayout youtubeHolder;
    private WebView webView;

	ArrayList<String> followList = new ArrayList<>();
	TinyDB tinydb;

	Toolbar toolbar;

    ArrayList<HashMap<String, String>> scheduleList = new ArrayList<>();

    private myWebChromeClient mWebChromeClient;
    private FrameLayout customViewContainer;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback customViewCallback;

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
        new mediaPlayer(this, title, mp3, fromPlayBtn, vanta, stopBtn, playBtn, true, false);
	}

	public void onstopButtonFollow(View v) {
		followList = tinydb.getList("followList2");
		for(int i = 0; i < followList.size(); i++) {
			if(followList.get(i).contains(title)){
				followList.remove(i);
				tinydb.putList("followList2", followList);
				followBtn.setVisibility(View.VISIBLE);
				stopFollowBtn.setVisibility(View.GONE);

				//Log.i("FollowID", mId);
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

			//Log.i("FollowID", mId);
			OneSignal.sendTag(mId, "1");
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
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
        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
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
        //Log.e("mId", "mId"+mId);

        Realm realm = Realm.getDefaultInstance();

        if(title == null){
            final RealmArticle jsonFromDb = realm.where(RealmArticle.class).equalTo("id", mId).findFirst();
            if(jsonFromDb != null) {
                try {
                    JSONObject article = new JSONObject(jsonFromDb.json);
                    title = article.getString("name");
                    musik = article.getString("category");
                    place = article.getString("city");
                    text = article.getString("description");
                    //image = article.getString("image");
                    image = "null";
                    mp3 = article.getString("songurl");

                    if (article.has("image_medium")) {
                        if (!article.getString("image_medium").equals("")) {
                            image = article.getString("image_medium");
                        } else if (article.has("image")) {
                            image = article.getString("image");
                        }
                    } else if (article.has("image")) {
                        image = article.getString("image");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //Get Youtube ID
        youtubeHolder = (LinearLayout) findViewById(R.id.youtubeHolder);
        final RealmArticle getYouTube = realm.where(RealmArticle.class).equalTo("id", mId).findFirst();
        try {

            JSONObject youtube = new JSONObject(getYouTube.json);
            String youTubeLink = youtube.getString("youtubeurl");
            String res = "";
            String pattern = "(?<=watch\\?v=|/videos/|/youtu[.]be/|embed\\/)[^#\\&\\?]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youTubeLink);
            if(matcher.find()){
                res = matcher.group();
            }
            String baseUrl = "http://martenolsson.se/";
            if(!res.equals("")){
                mWebChromeClient = new myWebChromeClient();
                webView = new WebView(getApplicationContext());
                webView.setWebViewClient(new WebViewClient());
                webView.setWebChromeClient(mWebChromeClient);
                youtubeHolder.addView(webView);
                webView.getSettings().setJavaScriptEnabled(true);
                youtubeHolder.setPadding(15*(int)density,15*(int)density,15*(int)density,15*(int)density);
                if(youTubeLink.contains("facebook")){
                    webView.loadDataWithBaseURL(baseUrl, "<style>body{margin:0}</style><div id=\"fb-root\"></div><script>window.fbAsyncInit=function(){FB.init({appId:\"1119237631465992\",xfbml:!0,version:\"v2.5\"});var e;FB.Event.subscribe(\"xfbml.ready\",function(n){\"video\"===n.type&&(e=n.instance)})},function(e,n,t){var c,i=e.getElementsByTagName(n)[0];e.getElementById(t)||(c=e.createElement(n),c.id=t,c.src=\"//connect.facebook.net/en_US/sdk.js\",i.parentNode.insertBefore(c,i))}(document,\"script\",\"facebook-jssdk\");</script><div  class=\"fb-video\" data-href='https://www.facebook.com/selmagustaf/videos/1007298852695775/' data-width=\"100%\" data-allowfullscreen=\"true\"></div>\n", "text/html", null, null);
                }else{
                    webView.loadData("<style>body{margin:0}</style><div style='position:relative; height:0; overflow:hidden; padding-bottom:56.25%; width:100%;'><iframe frameborder='0' width=\"100%\" height=\"0\" style='position:absolute; width:100%; height:100%;' src='https://www.youtube.com/embed/"+res+"' frameborder=\"0\" allowfullscreen></iframe></div>", "text/html", null);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //end Get Youtube ID

        border = (LinearLayout) findViewById(R.id.border);
        textWrapper = (LinearLayout) findViewById(R.id.textWrapper);
        scheduleWrapper = (LinearLayout) findViewById(R.id.scheduleWrapper);
        String schedule = tinydb.getString("schedule");
        if(schedule != null) {
            if(!schedule.isEmpty()) {
                JsonObject scheduleJson = new JsonParser().parse(schedule).getAsJsonObject();
                JsonArray schemeArray = scheduleJson.get("payload").getAsJsonArray();
                LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                for (int index = 0; index < schemeArray.size(); index++) {
                    if (schemeArray.get(index).getAsJsonObject().get("artistid").getAsString().equals(mId)) {

                        HashMap<String, String> scheduleItem = new HashMap<>();
                        scheduleItem.put("date", schemeArray.get(index).getAsJsonObject().get("date").getAsString());
                        scheduleItem.put("stage", schemeArray.get(index).getAsJsonObject().get("stage").getAsString());
                        scheduleList.add(scheduleItem);

                    }
                    Collections.sort(scheduleList, new DateComparator("date"));
                }

                if(scheduleList.size() == 0){
                    border.setVisibility(View.GONE);
                }

                for (int index = 0; index < scheduleList.size(); index++) {
                    HashMap item = (HashMap) scheduleList.get(index);
                    String stageTxt = (String) item.get("stage");
                    String timeTxt = (String) item.get("date");

                    View child = inflater.inflate(R.layout.single_schemeview_item, null);
                    scheduleWrapper.addView(child);

                    TextView time = (TextView) child.findViewById(R.id.time);
                    TextView stage = (TextView) child.findViewById(R.id.stage);

                    //Convert Date
                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    Date dateObj = null;
                    try {
                        dateObj = curFormater.parse(timeTxt);
                    } catch (ParseException e) {e.printStackTrace();}
                    SimpleDateFormat postFormater = new SimpleDateFormat("EEE HH:mm");
                    String newDateStr = postFormater.format(dateObj);
                    //end Convert Date

                    time.setText(newDateStr);
                    stage.setText(stageTxt);
                }
            }
        }

        mp3 = mp3.replace("https","http");
		/*Player*/
        if(mp3 != null) {
            myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;
            new mediaPlayer(this, title, mp3, fromPlayBtn, vanta, stopBtn, playBtn, true, false);
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
            }else {
                txtText.setVisibility(View.GONE);
                border.setVisibility(View.GONE);
                //arrowDown.setVisibility(View.GONE);
            }

            final Context mContext = this;
            Glide.with(this)
                    .load(image)
                    .centerCrop()
                    .bitmapTransform(new BlurTransformation(this, Glide.get(this).getBitmapPool()))
                    .listener(
                            GlidePalette.with(image)
                                    .use(GlidePalette.Profile.MUTED_DARK)
                                    .intoBackground(textWrapper)
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

    class myWebChromeClient extends WebChromeClient {
        private View mVideoProgressView;
        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            //if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            //Hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(SingleItemView.this);
                mVideoProgressView = inflater.inflate(R.layout.video_progress, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView(); //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Show status bar
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class DateComparator implements Comparator<Map<String, String>> {
        private final String key;

        public DateComparator(String key){
            this.key = key;
        }

        public int compare(Map<String, String> first, Map<String, String> second){
            String firstValue = first.get(key);
            String secondValue = second.get(key);
            return firstValue.compareTo(secondValue);
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
        if (this.isFinishing()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(webView != null) {
                        webView.loadUrl("about:blank");
                        webView.setVisibility(View.GONE);
                        webView.clearHistory();
                        youtubeHolder.removeAllViews();
                        webView.destroy();
                    }
                }
            }, 500);
        }
    }

    void thisFinish(){
        this.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(webView != null) {
                    webView.loadUrl("about:blank");
                    webView.setVisibility(View.GONE);
                    webView.clearHistory();
                    youtubeHolder.removeAllViews();
                    webView.destroy();
                }
            }
        }, 500);
    }
}