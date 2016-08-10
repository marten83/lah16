package se.martenolsson.lah15;

import android.app.Application;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.onesignal.OneSignal;
import se.martenolsson.lah15.classes.Foreground;
import se.martenolsson.lah15.classes.TinyDB;

public class ApplicationController extends Application {

    TinyDB tinydb;
	public Typeface geoSans;
    public Typeface geoSansBold;
    public Typeface caviar;
    public Typeface icons;
    public MediaPlayer myMediaPlayer;
    public String currentSong = null;
    public String currentArtist = null;
    public String currentStageSort = "stage";

	@Override
	public void onCreate() {
		super.onCreate();

        Foreground.init(this);
        tinydb = new TinyDB(this);

        myMediaPlayer = new MediaPlayer();
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		geoSans = Typeface.createFromAsset(this.getAssets(), "Cabin-Bold.ttf");
        geoSansBold = Typeface.createFromAsset(this.getAssets(), "Cabin-Bold.ttf");
        caviar = Typeface.createFromAsset(this.getAssets(), "lane.ttf");
        icons = Typeface.createFromAsset(this.getAssets(), "Flaticon.ttf");

        OneSignal.startInit(this).init();
        OneSignal.enableNotificationsWhenActive(false);

        if(!tinydb.getBoolean("firstOpen")){
            OneSignal.sendTag("news", "1");
            OneSignal.sendTag("artists", "1");
            tinydb.putBoolean("firstOpen", true);
        }

	}

}