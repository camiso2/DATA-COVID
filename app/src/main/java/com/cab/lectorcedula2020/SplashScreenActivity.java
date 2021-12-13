package com.cab.lectorcedula2020;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by jaiver-camiso on 16/06/2017.
 */

/**
 * Created by jaiver-camiso on 16/06/2017.
 */

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2000;

    private TextView logo;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
        ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar_splash);
        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(pg.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(SplashScreenActivity.this, R.color.colorPrim));
            pg.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            pg.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(SplashScreenActivity.this, R.color.colorPrim), PorterDuff.Mode.SRC_IN);
        }
        logo = (TextView) findViewById(R.id.logo);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.setAnimation(fade_in);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                animacionActivity();
            }
        };
        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
    public void animacionActivity() {
        overridePendingTransition(R.anim.fade_in, R.anim.alpha);
        overridePendingTransition(R.anim.fade_in, R.anim.alpha);
    }


}
