package se.martenolsson.lah15.classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

/**
 * Created by martenolsson on 15-12-03.
 */
public class getTextWithLinks extends ClickableSpan {
    private String mUrl;
    private Context mContext;

    public getTextWithLinks(String url, Context context) {
        mUrl =url;
        mContext = context;
    }
    @Override
    public void onClick(View widget) {
        Log.e("test", mUrl);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        mContext.startActivity(browserIntent);

    }
}