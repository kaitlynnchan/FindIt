package cmpt276.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Splash screen
 * Plays a short animation and displays game title and authors
 */
public class WelcomeActivity extends AppCompatActivity {

    private int height;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        moveAnimation();
        setupSkipBtn();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = MainActivity.makeLaunchIntent(WelcomeActivity.this);
                startActivity(intent);
                finish();
            }
        }, 9000);
    }

    private void setupSkipBtn() {
        Button skip = findViewById(R.id.buttonSkip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.makeLaunchIntent(WelcomeActivity.this);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
    }

    private void moveAnimation() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        final int NEGATIVE_HEIGHT = height * -1;

        TextView title = findViewById(R.id.textTitle);
        TextView authors = findViewById(R.id.textAuthors);
        ImageView magnifyGlass = findViewById(R.id.imageMagnifyGlass);
        View overlay = findViewById(R.id.viewOverlay);
        title.setY(NEGATIVE_HEIGHT);
        authors.setY(NEGATIVE_HEIGHT);
        magnifyGlass.setY(NEGATIVE_HEIGHT);
        overlay.setY(NEGATIVE_HEIGHT);

        ImageView apple1 = findViewById(R.id.imageApple1);
        ImageView apple2 = findViewById(R.id.imageApple2);
        ImageView apple3 = findViewById(R.id.imageApple3);
        ImageView apple4 = findViewById(R.id.imageApple4);
        ImageView apple5 = findViewById(R.id.imageApple5);
        apple1.setY(NEGATIVE_HEIGHT);
        apple2.setY(NEGATIVE_HEIGHT);
        apple3.setY(NEGATIVE_HEIGHT);
        apple4.setY(NEGATIVE_HEIGHT);
        apple5.setY(NEGATIVE_HEIGHT);

        TranslateAnimation moveDown = new TranslateAnimation(0, 0, 0, height);
        moveDown.setDuration(5000);
        moveDown.setFillAfter(true);

        overlay.startAnimation(moveDown);
        title.startAnimation(moveDown);
        magnifyGlass.startAnimation(moveDown);
        authors.setAnimation(moveDown);
        apple1.startAnimation(moveDown);
        apple2.startAnimation(moveDown);
        apple3.startAnimation(moveDown);
        apple4.startAnimation(moveDown);
        apple5.startAnimation(moveDown);
    }
}