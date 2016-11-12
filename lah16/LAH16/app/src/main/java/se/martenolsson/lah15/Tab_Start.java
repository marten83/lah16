package se.martenolsson.lah15;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import io.realm.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.martenolsson.lah15.classes.CleanTextString;
import se.martenolsson.lah15.classes.TinyDB;
import se.martenolsson.lah15.db.RealmArticle;

import java.util.*;

/**
 * Created by martenolsson on 16-06-21.
 */
public class Tab_Start extends Fragment {

    View v;

    static ListView list;
    static ListViewAdapter adapter;
    static EditText editsearch;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout sok;
    float density;
    static CleanTextString returnCleanString;

    static Realm realm;
    RequestQueue queue;

    static MediaPlayer myMediaPlayer;

    static Boolean searchHasFocus = false;

    //ArrayList<String> listofitems = new ArrayList<>();
    static TinyDB tinydb;

    static ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

    private static final String TAG_ID = "id";
    private static final String TAG_ARTICLE = "payload";
    private static final String TAG_TITLE = "namenormalized";
    private static final String TAG_MUSIK = "category";
    private static final String TAG_PLACE = "city";
    private static final String TAG_TEXT = "description";
    private static final String TAG_IMAGE = "image_medium";

    static JSONArray allmarkers = null;

    Typeface titleText;
    static Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tab_start, container, false);

        mContext = container.getContext();
        queue = Volley.newRequestQueue(mContext);
        density  = getResources().getDisplayMetrics().density;
        returnCleanString = new CleanTextString();

        //Debugg in chrome
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        list = (ListView) v.findViewById(R.id.listview);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        myMediaPlayer = ((ApplicationController) mContext.getApplicationContext()).myMediaPlayer;

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJson("http://martenolsson.se/lah16/artists/artists.json");
            }
        });

        tinydb = new TinyDB(mContext);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();

        /*String sb = tinydb.getString("listofitems");
        String TAG = "sadsds";
        if (sb.length() > 4000) {
            Log.v(TAG, "sb.length = " + sb.length());
            int chunkCount = sb.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    Log.v(TAG, sb.substring(4000 * i));
                } else {
                    Log.v(TAG, sb.substring(4000 * i, max));
                }
            }
        } else {
            Log.v(TAG, sb.toString());
        }*/


        if(!tinydb.getString("listofitems").isEmpty() && tinydb.getString("listofitems") != null){
            JSONArray jsonObject = null;
            try {
                jsonObject = new JSONArray(tinydb.getString("listofitems"));
            }catch (JSONException e) {e.printStackTrace();}
            //Log.e("test", String.valueOf(jsonObject));
            loadInList(jsonObject, true);
        }
        getJson("http://martenolsson.se/lah16/artists/artists.json");

        /*File cacheDir = mContext.getCacheDir();
        File tab1 = new File(cacheDir, "tab1.html");
        File tab2 = new File(cacheDir, "tab2.html");
        File tab3 = new File(cacheDir, "tab3.html");
        File tab4 = new File(cacheDir, "tab4.html");
        if(!tab1.exists()){
            new AlarmReceiver.saveFile(mContext, "tab1").execute("http://martenolsson.se/lah15/tab1.php?3");
        }
        if(!tab2.exists()){
            new AlarmReceiver.saveFile(mContext, "tab2").execute("http://martenolsson.se/lah15/tab2.php?3");
        }
        if(!tab3.exists()){
            new AlarmReceiver.saveFile(mContext, "tab3").execute("http://martenolsson.se/lah15/tab3.php?3");
        }
        if(!tab4.exists()){
            new AlarmReceiver.saveFile(mContext, "tab4").execute("http://martenolsson.se/lah15/tab4.php?3");
        }*/

		/*BackgroundTask*/
        /*Intent alarmIntent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        int interval = 1800000;*/
        //int interval = 30000; //30sek
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
		/*end BackgroundTask*/

        //LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, list, false);
        list.addHeaderView(header);

        titleText = ((ApplicationController) mContext.getApplicationContext()).geoSans;
        sok = (LinearLayout) header.findViewById(R.id.sok);
        editsearch = (EditText) header.findViewById(R.id.search);
        editsearch.setTypeface(titleText);
        editsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchHasFocus = true;
                    list.smoothScrollToPosition(0);

                } else {
                    searchHasFocus = false;
                }
            }
        });
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });

        //isKeyBoardVisible();

        return v;
    }

    static void loadInList(final JSONArray json, Boolean fromSaved){
        try {
            arraylist.clear();
            list.setAdapter(null);
            if(json != null) {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = json.getJSONObject(i);
                    String mId = c.getString(TAG_ID);
                    String image = "http://martenolsson.se/lah16/images/" + mId + ".jpg";
                    String title = c.getString(TAG_TITLE).toUpperCase();
                    String musik = c.getString(TAG_MUSIK);
                    String place = c.getString(TAG_PLACE);
                    String text = c.getString(TAG_TEXT);
                    String mp3 = "http://martenolsson.se/lah16/songs/" + mId + ".mp3";


                    WorldPopulation wp = new WorldPopulation(mId, title, musik, text, place, mp3, image);
                    arraylist.add(wp);
                }
                if (!fromSaved) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject c = null;
                                try {
                                    c = allmarkers.getJSONObject(i);
                                    final RealmArticle obj = new RealmArticle();
                                    String mId = c.getString(TAG_ID);
                                    obj.id = mId;
                                    obj.json = c.toString();
                                    realm.copyToRealmOrUpdate(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }


                // Pass results to ListViewAdapter Class
                adapter = new ListViewAdapter(mContext, arraylist, MainViewPager.playBtn, MainViewPager.playBtnText, myMediaPlayer);
                // Binds the Adapter to the ListView
                list.setAdapter(adapter);

                mSwipeRefreshLayout.setRefreshing(false);
            }

        }
        catch (JSONException e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    void getJson(final String url) {
        MainViewPager.cat.setAlpha(0.5f);
        MainViewPager.ao.setAlpha(1f);
        final JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            allmarkers = json.getJSONArray(TAG_ARTICLE);
                            loadInList(allmarkers, false);
                            tinydb.putString("listofitems", allmarkers.toString());

                            /*sort*/
                            JSONArray sortedByCat = sortJsonArray(allmarkers);
                            tinydb.putString("listsorted", sortedByCat.toString());
                            /*sort*/

                        } catch (JSONException e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        queue.add(stringRequest);
    }

    public static JSONArray sortJsonArray(JSONArray array) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString("category");
                    rid = rhs.getString("category");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    public static void clickAo(){
        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(tinydb.getString("listofitems"));
        }catch (JSONException e) {e.printStackTrace();}
        loadInList(jsonObject, true);
    }

    public static void clickCat(){
        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(tinydb.getString("listsorted"));
        }catch (JSONException e) {e.printStackTrace();}
        loadInList(jsonObject, true);
    }

    public static void removeFocus() {
        editsearch.clearFocus();
        editsearch.getText().clear();
    }

    /*void isKeyBoardVisible(){
        ViewTreeObserver observer = v.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);
                int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100 * (int) density) {
                    keyBoardVisible();
                } else {
                    keyBoardHidden();
                }
            }
        });
    }
    void keyBoardVisible(){
        MainViewPager.tabs.setVisibility(View.GONE);
        MainViewPager.tabBoarder.setVisibility(View.GONE);
        MainViewPager.pager.setPadding(0,0,0,0);
    }

    void keyBoardHidden(){
        MainViewPager.tabs.setVisibility(View.VISIBLE);
        MainViewPager.tabBoarder.setVisibility(View.VISIBLE);
        MainViewPager.pager.setPadding(0, 0, 0, 55 * (int) density);
    }*/
}
