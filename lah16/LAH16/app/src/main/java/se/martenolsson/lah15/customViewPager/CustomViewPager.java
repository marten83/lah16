package se.martenolsson.lah15.customViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by maroln on 15-09-17.
 */
public class CustomViewPager extends ViewPager {

    private static boolean swipe = true;

    public static void setSwipe(boolean swipeValue) {
        swipe = swipeValue;
    }

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        /*if(arg0.getAction()== MotionEvent.ACTION_DOWN){
            swipe = true;
        }
        if (swipe) {
            //Tab_MostRead.isSwipe = true;
            return super.onInterceptTouchEvent(arg0);
        }else{
            return false;
        }*/
        return false;  //No swipe
    }

}