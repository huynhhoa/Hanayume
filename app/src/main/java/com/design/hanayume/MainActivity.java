package com.design.hanayume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    //Variables
    private LinearLayout cvLesson1, cvLesson2,cvLesson3, cvLesson4, cvLesson5,cvLesson6;
    private Intent intent;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Quang cao bieu ngu
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AnhXa();

        cvLesson1.setOnClickListener(v -> {
            if(!isConnect(this)){
                showInternetDialog();
            }else {

            intent = new Intent(MainActivity.this,VocaListActivity.class);
            intent.putExtra("levelMain", 5);
            startActivity(intent);
            }
        });
        cvLesson2.setOnClickListener(v -> {
            if(!isConnect(this)){
                showInternetDialog();
            }else {
                intent = new Intent(MainActivity.this, VocaListActivity.class);
                intent.putExtra("levelMain", 4);
                startActivity(intent);
            }
        });
        cvLesson3.setOnClickListener(v -> {
            if(!isConnect(this)){
                showInternetDialog();
            }else {

            intent = new Intent(MainActivity.this,VocaListActivity.class);
            intent.putExtra("levelMain", 3);
            startActivity(intent);
            }
        });
        cvLesson4.setOnClickListener(v ->{
                showError();
        });
        cvLesson5.setOnClickListener(v -> {
            showError();
        });

        cvLesson6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                final String appPackName = getApplicationContext().getPackageName();
                String strAppLink = "";
                try {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackName;
                } catch (android.content.ActivityNotFoundException e) {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackName;
                }
                shareIntent.setType("text/link");
                String shareBody = "Mình đang học 2136 Kanji bằng ứng dụng này, Mình nghĩ nó có ích cho bạn đấy!:" + "\n" + "" + strAppLink;
                String shareSub = "2136 Kanji Flashcard";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });
    }

    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Chức năng đang được xây dựng. Xin vui lòng quay lại sau!")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vui lòng kết nối Internet")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private boolean isConnect(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCo = connectivityManager.getActiveNetworkInfo();
        if (wifiCo != null) {
            // connected to the internet
            if (wifiCo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
            } else if (wifiCo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
            }
            return true;
        } else {
            // not connected to the internet
            return false;
        }

    }

    public void AnhXa(){
        cvLesson1 = findViewById(R.id.cv_lesson1);
        cvLesson2 = findViewById(R.id.cv_lesson2);
        cvLesson3 = findViewById(R.id.cv_lesson3);
        cvLesson4 = findViewById(R.id.cv_lesson4);
        cvLesson5 = findViewById(R.id.cv_lesson5);
        cvLesson6 = findViewById(R.id.cv_lesson6);
    }

}