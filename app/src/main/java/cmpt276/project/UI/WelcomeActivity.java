package cmpt276.project.UI;

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

import cmpt276.project.R;

/**
 * Splash screen
 * Plays a short animation and displays game title and authors
 */
public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        final int HEIGHT = displayMetrics.heightPixels;
        final int NEGATIVE_HEIGHT = HEIGHT * -1;

        View overlay = findViewById(R.id.viewOverlay);
        TextView title = findViewById(R.id.textTitle);
        TextView authors = findViewById(R.id.textAuthors);
        ImageView magnifyGlass = findViewById(R.id.imageMagnifyGlass);

        overlay.setY(NEGATIVE_HEIGHT);
        title.setY(NEGATIVE_HEIGHT);
        authors.setY(NEGATIVE_HEIGHT);
        magnifyGlass.setY(NEGATIVE_HEIGHT);

        ImageView apple = findViewById(R.id.imageApple);
        ImageView lettuce = findViewById(R.id.imageLettuce);
        ImageView carrot = findViewById(R.id.imageCarrot);
        ImageView eggplant = findViewById(R.id.imageEggplant);
        ImageView appleGreen = findViewById(R.id.imageAppleGreen);
        ImageView broccoli = findViewById(R.id.imageBroccoli);
        ImageView lemon = findViewById(R.id.imageLemon);
        ImageView mango = findViewById(R.id.imageMango);
        ImageView mushroom = findViewById(R.id.imageMushroom);
        ImageView onion = findViewById(R.id.imageOnion);
        ImageView orange = findViewById(R.id.imageOrange);
        ImageView pumpkin = findViewById(R.id.imagePumpkin);
        ImageView radish = findViewById(R.id.imageRadish);
        ImageView watermelon = findViewById(R.id.imageWatermelon);

        // Animation
        TranslateAnimation moveDown = new TranslateAnimation(0, 0, 0, HEIGHT);
        moveDown.setDuration(5000);
        moveDown.setFillAfter(true);

        overlay.startAnimation(moveDown);
        title.startAnimation(moveDown);
        magnifyGlass.startAnimation(moveDown);
        authors.setAnimation(moveDown);
        apple.startAnimation(moveDown);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
        finish();
    }
}