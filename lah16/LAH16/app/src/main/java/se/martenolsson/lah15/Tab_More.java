package se.martenolsson.lah15;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by martenolsson on 16-06-24.
 */
public class Tab_More extends Fragment {
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tabmore, container, false);

        LinearLayout buyBtn = (LinearLayout) v.findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.trippus.se/web/registration/registration.aspx?view=registration&idcategory=AB0ILBAXOh8FnA31Ag3AyzpgBodwXivoxkSC4q7zb4OVZU79V8pkkAf9GyufgHnA5BKywOjnERb-&ln=swe"));
                startActivity(browserIntent);
            }
        });

        TextView privacy_policy = (TextView) v.findViewById(R.id.privacy_policy);
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://onesignal.com/privacy_policy"));
                startActivity(browserIntent);
            }
        });

        return v;
    }
}
