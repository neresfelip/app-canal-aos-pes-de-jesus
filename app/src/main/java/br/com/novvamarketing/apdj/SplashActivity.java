package br.com.novvamarketing.apdj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import br.com.novvamarketing.apdj.utils.TypeWriter;

public class SplashActivity extends AppCompatActivity {

    private View splash1;
    private View splash2;

    private boolean abrirApp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String novoVideo = getIntent().getStringExtra("novo");

        if(novoVideo != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        TypeWriter tw = findViewById(R.id.typewriter);
        tw.setCharacterDelay(25);
        tw.animateText(R.string.splash_frase);

        splash1 = findViewById(R.id.splash_1);
        splash2 = findViewById(R.id.splash_2);

        new FadeIn().execute();
        new TrocaSplash().execute();

    }

    public void acessarSiteNovva(View view) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.novvamarketing.com.br"));
        abrirApp = false;
        startActivity(new Intent(this, MainActivity.class));
        startActivity(it);
        finish();
    }

    class FadeIn extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            View v = findViewById(R.id.logo_novva);
            v.setVisibility(View.VISIBLE);
            v.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));
        }
    }

    class TrocaSplash extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(abrirApp) {
                splash1.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_out));
                splash1.setVisibility(View.GONE);
                splash2.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));
                splash2.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(4000);
                            finish();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

}
