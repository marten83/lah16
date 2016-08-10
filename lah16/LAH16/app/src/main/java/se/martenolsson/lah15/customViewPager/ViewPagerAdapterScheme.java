package se.martenolsson.lah15.customViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import se.martenolsson.lah15.SchemeItem;
import se.martenolsson.lah15.Tab_More;
import se.martenolsson.lah15.Tab_SchemePage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maroln on 15-08-19.
 */
public class ViewPagerAdapterScheme extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;
    //List<String> Urls;
    CharSequence[] Titles;
    int NumbOfTabs;
    JsonArray onsdagJson;
    JsonArray torsdagJson;
    JsonArray fredagJson;
    JsonArray lordagJson;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterScheme(FragmentManager fm, CharSequence[] mTitles, JsonArray onsdagJson, JsonArray torsdagJson, JsonArray fredagJson, JsonArray lordagJson) {
    //public ViewPagerAdapterScheme(FragmentManager fm, List<CharSequence> mTitles, List<String> urls, String department) {
        super(fm);

        this.Titles = mTitles;
        //this.Urls = urls;
        this.NumbOfTabs = this.Titles.length;
        this.onsdagJson = onsdagJson;
        this.torsdagJson = torsdagJson;
        this.fredagJson = fredagJson;
        this.lordagJson = lordagJson;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
            Tab_SchemePage tab = new Tab_SchemePage();
            Bundle args = new Bundle();
            args.putString("title", String.valueOf(Titles[position]));

            if(Titles[position].equals("Onsdag")){
                args.putString("day", String.valueOf(onsdagJson));
            }else if(Titles[position].equals("Torsdag")){
                args.putString("day", String.valueOf(torsdagJson));
            }else if(Titles[position].equals("Fredag")){
                args.putString("day", String.valueOf(fredagJson));
            }else{
                args.putString("day", String.valueOf(lordagJson));
            }

        tab.setArguments(args);
            return tab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
