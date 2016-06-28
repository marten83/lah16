package se.martenolsson.lah15.classes;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.Log;

/**
 * Created by martenolsson on 16-01-27.
 */
public class ReturnSpannableString {
    Context mContext;
    public CharSequence ReturnSpannableString(String textString, Context context) {
        mContext = context;
        CharSequence chartext = Html.fromHtml(textString);
        if (chartext instanceof Spannable) {
            int end = chartext.length();
            Spannable sp = (Spannable) Html.fromHtml(textString);
            Object spansToRemove[] = sp.getSpans(0, end, Object.class);
            for(Object span: spansToRemove){
                if(span instanceof URLSpan) {
                    String url = ((URLSpan) span).getURL();
                    getTextWithLinks click = new getTextWithLinks(url, mContext);
                    sp.setSpan(click, sp.getSpanStart(span), sp.getSpanEnd(span), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sp.removeSpan(span);
                }
            }

            return noTrailingWhiteLines(sp);
        } else{
            return null;
        }
    }

    private CharSequence noTrailingWhiteLines(CharSequence text) {
        if(text.length()>2) {
            while (text.charAt(text.length() - 1) == '\n') {
                text = text.subSequence(0, text.length() - 1);
            }
        }
        return text;
    }

}
