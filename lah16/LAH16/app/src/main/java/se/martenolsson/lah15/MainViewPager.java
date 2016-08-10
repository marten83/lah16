package se.martenolsson.lah15;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.customViewPager.SlidingTabLayout;

/**
 * Created by maroln on 15-08-19.
 */
public class MainViewPager extends AppCompatActivity {

    static ViewPager pager;
    Toolbar toolbar;
    static se.martenolsson.lah15.customViewPager.ViewPagerAdapter adapter;
    static SlidingTabLayout tabs;
    static LinearLayout tabBoarder;
    CharSequence Titles[]={"Artister","Mina artister","Nyheter","Spelschema","Mer"};
    //static int[] color;
    String[] Icons;
    int Numboftabs = 5;
    static TinyDB tinydb;
    static Context baseContext;
    static Window window;
    static Context mContext;
    LinearLayout buttonScheme;
    LinearLayout buttonCont;
    TextView topTitle;
    static ApplicationController appController;

    static TextView ao;
    static TextView cat;
    static TextView stage;
    static TextView time;

    MediaPlayer myMediaPlayer;
    static LinearLayout playBtn;
    static TextView playBtnText;
    LinearLayout playListBtn;

    //Handle backbtn
    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() != 0){
            pager.setCurrentItem(0);
        }else if(Tab_Start.searchHasFocus){
            Tab_Start.removeFocus();
        }
        else {
            thisFinish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        baseContext = getBaseContext();
        setContentView(R.layout.main_viewpager);
        tinydb = new TinyDB(this);

        //tinydb.remove("followList");

        mContext = getApplicationContext();
        appController = (ApplicationController) this.getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonScheme = (LinearLayout) findViewById(R.id.buttonScheme);
        buttonCont = (LinearLayout) findViewById(R.id.buttonCont);
        topTitle = (TextView) findViewById(R.id.topTitle);
        topTitle.setTypeface(((ApplicationController) mContext.getApplicationContext()).geoSans);

        myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;
        playBtn = (LinearLayout) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myMediaPlayer.getCurrentPosition() > 0 && !myMediaPlayer.isPlaying()) {
                    myMediaPlayer.start();
                    playBtnText.setText(Html.fromHtml("&#xf1f9;"));
                } else if (myMediaPlayer.getCurrentPosition() > 0) {
                    myMediaPlayer.pause();
                    playBtnText.setText(Html.fromHtml("&#xf115;"));
                }
            }
        });
        playBtnText = (TextView) findViewById(R.id.playBtnText);


        playListBtn = (LinearLayout) findViewById(R.id.playListBtn);
        playListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, playListView.class);
                startActivity(intent);
            }
        });


        ao = (TextView) findViewById(R.id.ao);
        cat = (TextView) findViewById(R.id.cat);
        ao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_Start.clickAo();
                cat.setAlpha(0.5f);
                ao.setAlpha(1f);
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_Start.clickCat();
                cat.setAlpha(1f);
                ao.setAlpha(0.5f);
            }
        });
        ao.setTypeface(((ApplicationController) mContext.getApplicationContext()).geoSans);
        cat.setTypeface(((ApplicationController) mContext.getApplicationContext()).geoSans);

        stage = (TextView) findViewById(R.id.stage);
        time = (TextView) findViewById(R.id.time);
        stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_Scheme.sortStage();
                stage.setAlpha(1f);
                time.setAlpha(0.5f);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_Scheme.sortTime();
                stage.setAlpha(0.5f);
                time.setAlpha(1f);
            }
        });
        stage.setTypeface(((ApplicationController) mContext.getApplicationContext()).geoSans);
        time.setTypeface(((ApplicationController) mContext.getApplicationContext()).geoSans);



        //Debugg in chrome
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        Icons= new String[] {getString(R.string.iconArtists), getString(R.string.iconMyArtist), getString(R.string.iconNews), getString(R.string.iconSchema), getString(R.string.iconMore)};


        window = getWindow();
        adapter = new se.martenolsson.lah15.customViewPager.ViewPagerAdapter(getSupportFragmentManager(),Titles, Icons, Numboftabs);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        //pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("PAGE1", String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    buttonCont.setVisibility(View.VISIBLE);
                    buttonScheme.setVisibility(View.GONE);
                    topTitle.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                }else if(position == 3){
                    buttonCont.setVisibility(View.GONE);
                    buttonScheme.setVisibility(View.VISIBLE);
                    topTitle.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                } else if(position == 4){
                    toolbar.setVisibility(View.GONE);
                }
                else{
                    buttonCont.setVisibility(View.GONE);
                    buttonScheme.setVisibility(View.GONE);
                    topTitle.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
                String topText = (String) Titles[position];
                topTitle.setText(topText.toUpperCase());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Assiging the Sliding Tab Layout View
        tabBoarder = (LinearLayout) findViewById(R.id.tabBoarder);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.theme_gray);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpTabs();
            }
        }, 100);

    }

    static void setUpTabs(){
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(myMediaPlayer.isPlaying()) {
            playBtn.setVisibility(View.VISIBLE);
            playBtnText.setText(Html.fromHtml("&#xf1f9;"));
        }else{
            playBtn.setVisibility(View.GONE);
        }

        if(pager.getCurrentItem() == 3) {
            Tab_Scheme.updateSchemePages();
        }
    }

    public void thisFinish(){
        this.finish();
    }
}
