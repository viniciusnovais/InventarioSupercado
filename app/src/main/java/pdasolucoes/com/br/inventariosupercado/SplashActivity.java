package pdasolucoes.com.br.inventariosupercado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView pda = findViewById(R.id.splashmove);
        final Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.up);
        pda.startAnimation(logoMoveAnimation);


        try {

            Bundle bundle = getIntent().getBundleExtra("bundle");
            Log.i("bundle", bundle.getString("teste"));

        }catch (Exception e){
            e.printStackTrace();
        }


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }, 3000);
    }

}
