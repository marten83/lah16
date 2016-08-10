package se.martenolsson.lah15.customViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import se.martenolsson.lah15.*;

/**
 * Created by maroln on 15-08-19.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static String[] Icons;
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], String[] icons, int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        Icons = icons;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            Tab_Start tab = new Tab_Start();
            return tab;
        }
        else if(position == 1){
            Tab_Follow tab = new Tab_Follow();
            return tab;
        }
        else if(position == 2){
            Tab_News tab = new Tab_News();
            return tab;
        }
        else if(position == 3){
            Tab_Scheme tab = new Tab_Scheme();
            return tab;
        }
        else{
            Tab_More tab = new Tab_More();
            return tab;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    public static String getPageIcons(int position) {
        return Icons[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
