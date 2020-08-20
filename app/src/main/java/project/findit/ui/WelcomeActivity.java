package project.findit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import project.findit.R;

/**
 * SPLASH SCREEN
 * Plays a short animation and displays game title and authors
 */
public class WelcomeActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = MainActivity.makeLaunchIntent(WelcomeActivity.this);
                startActivity(intent);
                finish();
            }
        }, 9000);

        moveAnimation();
        setupSkipButton();
    }

    private void moveAnimation() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int HEIGHT = displayMetrics.heightPixels;
        final int NEGATIVE_HEIGHT = HEIGHT * -1;

        View overlay = findViewById(R.id.view_overlay);
        TextView title = findViewById(R.id.text_title);
        TextView authors = findViewById(R.id.text_authors);
        ImageView magnifyGlass = findViewById(R.id.image_magnify_glass);

        overlay.setY(NEGATIVE_HEIGHT);
        title.setY(NEGATIVE_HEIGHT);
        authors.setY(NEGATIVE_HEIGHT);
        magnifyGlass.setY(NEGATIVE_HEIGHT);

        ImageView appleRed = findViewById(R.id.image_red_apple);
        ImageView lettuce = findViewById(R.id.image_lettuce);
        ImageView carrot = findViewById(R.id.image_carrot);
        ImageView eggplant = findViewById(R.id.image_eggplant);
        ImageView appleGreen = findViewById(R.id.image_green_apple);
        ImageView broccoli = findViewById(R.id.image_broccoli);
        ImageView lemon = findViewById(R.id.image_lemon);
        ImageView mango = findViewById(R.id.image_mango);
        ImageView mushroom = findViewById(R.id.image_mushroom);
        ImageView onion = findViewById(R.id.image_onion);
        ImageView orange = findViewById(R.id.image_orange);
        ImageView pumpkin = findViewById(R.id.image_pumpkin);
        ImageView radish = findViewById(R.id.image_radish);
        ImageView watermelon = findViewById(R.id.image_watermelon);

        // Animation
        TranslateAnimation moveDown = new TranslateAnimation(0, 0, 0, HEIGHT);
        moveDown.setDuration(5000);
        moveDown.setFillAfter(true);

        overlay.startAnimation(moveDown);
        title.startAnimation(moveDown);
        magnifyGlass.startAnimation(moveDown);
        authors.setAnimation(moveDown);
        appleRed.startAnimation(moveDown);
        broccoli.startAnimation(moveDown);
        carrot.startAnimation(moveDown);
        eggplant.startAnimation(moveDown);
        appleGreen.startAnimation(moveDown);
        lemon.startAnimation(moveDown);
        lettuce.startAnimation(moveDown);
        mango.startAnimation(moveDown);
        mushroom.startAnimation(moveDown);
        onion.startAnimation(moveDown);
        orange.startAnimation(moveDown);
        pumpkin.startAnimation(moveDown);
        radish.startAnimation(moveDown);
        watermelon.startAnimation(moveDown);
    }

    private void setupSkipButton() {
        Button buttonSkip = findViewById(R.id.button_skip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.makeLaunchIntent(WelcomeActivity.this);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
    }
}