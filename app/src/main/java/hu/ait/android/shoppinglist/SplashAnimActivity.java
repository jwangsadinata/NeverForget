package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashAnimActivity extends AppCompatActivity {

    AnimationDrawable myAnimationDrawable;

    Timer timer;
    MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_anim);

        ImageView myAnimation = (ImageView) findViewById(R.id.logoAnimation);
        myAnimationDrawable
                = (AnimationDrawable)myAnimation.getDrawable();

        myAnimation.post(
                new Runnable(){

                    @Override
                    public void run() {
                        myAnimationDrawable.start();
                    }
                });

        //Calculate the total duration
        int duration = 0;
        for(int i = 0; i < myAnimationDrawable.getNumberOfFrames(); i++){
            duration += myAnimationDrawable.getDuration(i);
        }

        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, duration);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            timer.cancel();
            Intent intent = new Intent(
                    SplashAnimActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}