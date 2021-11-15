package com.design.hanayume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import com.design.hanayume.Adapter.VocaListGetSet;
import com.design.hanayume.Database.InitDatabase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.Random;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class FashcardActivity extends AppCompatActivity {
    private TextView txtFrtKanji, txtFrtHira, txtFrtMean, txtFrtEx1,txtFrtEx2 ,
            txtBackKanji,txtBackHira,txtBackMean,txtBackEx1,txtBackEx2 ,txtFCPos;
    private CardView cardFront,cardBack;
    private final int duration = 350;
    private boolean flipped = false, isReplace =false,isReplace2 =false;
    private GestureDetector gestureDetector; //nhan biet cu chi cua nguoi dung
    private ArrayList<VocaListGetSet> arrayVocal;
    private Cursor dataKanji;
    private String defineFrBa;
    private int pos = 0, arrSize, widthCardFront;
    private InterstitialAd mInterstitialAd;
    private Button btnResult1, btnResult2, btnResult3;
    private String question1, question2, question3, txtAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashcard);
        //Quang cao bieu ngu
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //QC full
        //test ca-app-pub-3940256099942544/1033173712
        //live ca-app-pub-2847353116904733/8512464276

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

        Intent intent = getIntent();
        int lesson = intent.getIntExtra("lesson", 0);
        int level = intent.getIntExtra("level", 0);
        int idTranfer = intent.getIntExtra("vocalPosition", 0);
        int rememberCheck = intent.getIntExtra("rememberCheck",0);
        AnhXa();

        defineFrBa = "Front";
        arrayVocal = new ArrayList<>();

        SQLiteDatabase database = InitDatabase.initDatabase(this,"hanayume.db");

        getSupportActionBar().setTitle("Flashcard N"+ level +"");

        dataKanji = database.rawQuery("SELECT * FROM N5 WHERE Id >= "+ idTranfer +" and Remember = "+ rememberCheck +" AND JLPT = "+ level +" AND Lession = "+lesson +"",null);
        getDatabase();

        //tinh chieu cao cua card
        cardFront.getViewTreeObserver().addOnGlobalLayoutListener(() -> widthCardFront = cardFront.getWidth());

        //event for swipe
        swipe();
        //event for answer question
        answerQuestion();
    }

    private void answerQuestion() {

        btnResult1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnResult1.getText() == txtAnswer){
                    btnResult1.setBackgroundColor(getColor(R.color.green));
                }else {
                    btnResult1.setBackgroundColor(getColor(R.color.red));
                }
            }
        });
        btnResult2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnResult2.getText() == txtAnswer){
                    btnResult2.setBackgroundColor(getColor(R.color.green));
                }else {
                    btnResult2.setBackgroundColor(getColor(R.color.red));
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FashcardActivity.this);
                }

            }
        });
        btnResult3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnResult3.getText() == txtAnswer){
                    btnResult3.setBackgroundColor(getColor(R.color.green));
                }else {
                    btnResult3.setBackgroundColor(getColor(R.color.red));
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FashcardActivity.this);
                }

            }
        });
    }

    public void getDatabase(){
        arrayVocal.clear();
        while (dataKanji.moveToNext()){

            int id = dataKanji.getInt(0);
            String kanji = dataKanji.getString(1);
            String tuHan = dataKanji.getString(2);
            String description = dataKanji.getString(3);
            String meaning = dataKanji.getString(4);
            String hiragana = dataKanji.getString(5);
            boolean remember = dataKanji.getInt(6) > 0;
            String example1 = dataKanji.getString(7);
            String example2 = dataKanji.getString(8);
            String example3 = dataKanji.getString(9);
            String example4 = dataKanji.getString(10);
            shuffleArray(kanji,example2,example3);
            arrayVocal.add(new VocaListGetSet(id,kanji, tuHan,description,meaning,hiragana,remember,example1,example4,question1,question2,question3));
        }

        btnResult1.setText(arrayVocal.get(0).getQuestion1());
        btnResult2.setText(arrayVocal.get(0).getQuestion2());
        btnResult3.setText(arrayVocal.get(0).getQuestion3());
        txtAnswer = arrayVocal.get(0).getKanji();

        arrSize = arrayVocal.size();
        txtFCPos.setText(pos+1 + "/"+ arrSize);
        txtFrtKanji.setText(arrayVocal.get(0).getTuHan()); //get means positon = pos
        txtFrtMean.setText(arrayVocal.get(0).getDescription());
    }

    private void shuffleArray(String randomKanji,String randomEx1, String randomEx2) {
        String[] arr = {randomKanji,randomEx1, randomEx2};
        //create object for random
        Random random = new Random();
        for(int i = 2; i > 0; i--){
            //pick random index from 0 to i
            int j = random.nextInt(i+1);
            //swap arrayTemp with the element at random index
            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        question1 = arr[0];
        question2 = arr[1];
        question3 = arr[2];
    }

    public void AnhXa(){
        cardFront = findViewById(R.id.card_Front);
        cardBack = findViewById(R.id.card_Back);
        txtFrtKanji = findViewById(R.id.txtFrtKanji);
        txtFrtMean = findViewById(R.id.txtFrtMean);
        txtFrtHira = findViewById(R.id.txtFrtHira);

        txtFrtEx1 = findViewById(R.id.txtFrtEx1);
        txtFrtEx2 = findViewById(R.id.txtFrtEx2);

        txtBackKanji = findViewById(R.id.txtBackKanji);
        txtBackHira = findViewById(R.id.txtBackHira);
        txtBackMean = findViewById(R.id.txtBackMean);
        txtBackEx1 = findViewById(R.id.txtBackEx1);
        txtBackEx2 = findViewById(R.id.txtBackEx2);
        txtFCPos = findViewById(R.id.txtFCPos);

        btnResult1 = findViewById(R.id.btnResult1);
        btnResult2 = findViewById(R.id.btnResult2);
        btnResult3 = findViewById(R.id.btnResult3);

        Toolbar toolbar = findViewById(R.id.tbFashcard);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }
    @SuppressLint("ClickableViewAccessibility")
    public void swipe(){
        gestureDetector = new GestureDetector(this,new MyGesture());
        cardFront.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }
    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        //xoay
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(defineFrBa.equals("Front")){
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardFront, "scaleX", 1f, 0f); //oa1 : mat truoc
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardFront, "scaleX", 0f, 1f); //oa2 : mat sau
                oa1.setInterpolator(new DecelerateInterpolator());   //Decelerate: giam toc do them vao
                oa2.setInterpolator(new AccelerateDecelerateInterpolator()); //Accelerate : tang toc
                oa1.addListener(new AnimatorListenerAdapter() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animator animation) { //ket thuc xoay
                        super.onAnimationEnd(animation);
                        if (!isReplace) { // neu da xoay roi (fasle)
                            txtFrtMean.setText(arrayVocal.get(pos).getMeaning());
                            txtFrtKanji.setText(arrayVocal.get(pos).getKanji());
                            txtFrtHira.setText("Âm Kun: " + arrayVocal.get(pos).getHiragana());
                            txtFrtEx2.setText("Ví dụ: " + arrayVocal.get(pos).getExample1());
                            txtFrtEx1.setText("Bộ: " + arrayVocal.get(pos).getExample4());
                            oa2.start();
                        } else { // back lai
                            txtFrtMean.setText(arrayVocal.get(pos).getDescription());
                            txtFrtKanji.setText(arrayVocal.get(pos).getTuHan());
                            txtFrtHira.setText(null);
                            txtFrtEx1.setText(null);
                            txtFrtEx2.setText(null);

                            oa2.start();
                        }
                        isReplace = !isReplace;
                    }
                });
                oa1.setDuration(200);
                oa2.setDuration(200);
                oa1.start();
            }
            if(defineFrBa.equals("Back")){
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardBack, "scaleX", 1f, 0f); //oa1 : mat truoc
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardBack, "scaleX", 0f, 1f); //oa2 : mat sau
                oa1.setInterpolator(new DecelerateInterpolator());   //Decelerate: giam toc do them vao
                oa2.setInterpolator(new AccelerateDecelerateInterpolator()); //Accelerate : tang toc
                oa1.addListener(new AnimatorListenerAdapter() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animator animation) { //ket thuc xoay
                        super.onAnimationEnd(animation);
                        if(!isReplace2){ // neu da xoay roi
                            txtBackKanji.setText(arrayVocal.get(pos).getKanji());
                            txtBackHira.setText("Âm kun: " + arrayVocal.get(pos).getHiragana());
                            txtBackMean.setText(arrayVocal.get(pos).getMeaning());
                            txtBackEx2.setText("Ví dụ: " + arrayVocal.get(pos).getExample1());
                            txtBackEx1.setText("Bộ: " + arrayVocal.get(pos).getExample4());
                            oa2.start();
                        }else { // back lai
                            txtBackKanji.setText(arrayVocal.get(pos).getTuHan());
                            txtBackMean.setText(arrayVocal.get(pos).getDescription());
                            txtBackHira.setText(null);
                            txtBackEx1.setText(null);
                            txtBackEx2.setText(null);
                            oa2.start();
                        }
                        isReplace2 = !isReplace2;
                    }
                });
                oa1.setDuration(200);
                oa2.setDuration(200);
                oa1.start();
            }
            return super.onSingleTapUp(e);
        }
        //keo
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int swipe_threshold = 100;
            if(e2.getX() - e1.getX() > swipe_threshold || e1.getX() - e2.getX() > swipe_threshold){
                pos +=1;
                if(pos == arrSize)
                    pos = 0;
                txtFCPos.setText(pos+1 + "/" + arrSize);
                isReplace = false;
                isReplace2 = false;
                //reset button
                btnResult1.setBackgroundResource(R.drawable.vien_button);
                btnResult2.setBackgroundResource(R.drawable.vien_button);
                btnResult3.setBackgroundResource(R.drawable.vien_button);

                //gan gia tri button
                btnResult1.setText(arrayVocal.get(pos).getQuestion1());
                btnResult2.setText(arrayVocal.get(pos).getQuestion2());
                btnResult3.setText(arrayVocal.get(pos).getQuestion3());
                txtAnswer = arrayVocal.get(pos).getKanji();
            }
            //Lan dau cham e1, keo den = e2
            //keo tu trai sang phai
            //khoang cach thuc hien cu chi vuot

            if(e2.getX() - e1.getX() > swipe_threshold){
                cardFront.animate().setDuration(duration).translationX(widthCardFront).start(); //will move above layout slightly below
                //endaction called once the animation is completed
                cardBack.animate().setDuration(duration).translationX(-widthCardFront).withEndAction(() -> {
                    //translate 'z' values of layout and reserve animation (thay doi bong + gia tri cua card view)
                    //tach back vs font ra 50
                    if(!flipped){ // = flip it
                        cardFront.animate().translationZ(-50).start();
                        cardBack.animate().translationZ(0).start();
                        defineFrBa = "Back";
                        txtBackKanji.setText(arrayVocal.get(pos).getTuHan());
                        txtBackMean.setText(arrayVocal.get(pos).getDescription());
                        txtBackHira.setText(null);
                        txtBackEx1.setText(null);
                        txtBackEx2.setText(null);
                    }else { //else flip back
                        cardFront.animate().translationZ(0).start();
                        cardBack.animate().translationZ(-50).start();
                        defineFrBa = "Front";
                        //sau moi lan vuot, id txtFont +1
                        txtFrtMean.setText(arrayVocal.get(pos).getDescription());
                        txtFrtKanji.setText(arrayVocal.get(pos).getTuHan());
                        txtFrtHira.setText(null);
                        txtFrtEx1.setText(null);
                        txtFrtEx2.setText(null);
                    }
                    //dua back vs font ve lai vi tri
                    cardFront.animate().setDuration(duration).translationX(0).start();
                    cardBack.animate().setDuration(duration).translationX(0).start();
                    flipped = !flipped;
                    cardFront.animate().translationX(0).start();
                    cardBack.animate().translationX(0).start();
                }).start(); //now animate back layout in opposite direction, so put -value
            }
            //keo tu phai san trai
            if(e1.getX() - e2.getX() > swipe_threshold){
                cardFront.animate().setDuration(duration).translationX(widthCardFront).start(); //will move above layout slightly below
                //endaction called once the animation is completed
                cardBack.animate().setDuration(duration).translationX(-widthCardFront).withEndAction(() -> {
                    //translate 'z' values of layout and reserve animation (thay doi bong + gia tri cua card view)
                    //tach back vs font ra 50
                    if(!flipped){ // = True means the view is not flipped so flip it
                        cardFront.animate().translationZ(-50).start();
                        cardBack.animate().translationZ(0).start();
                        defineFrBa = "Back";
                        txtBackKanji.setText(arrayVocal.get(pos).getTuHan());
                        txtBackMean.setText(arrayVocal.get(pos).getDescription());
                        txtBackHira.setText(null);
                        txtBackEx1.setText(null);
                        txtBackEx2.setText(null);
                    }else { //else flip back
                        cardFront.animate().translationZ(0).start();
                        cardBack.animate().translationZ(-50).start();
                        defineFrBa = "Front";
                        //sau moi lan vuot, id txtFont +1
                        txtFrtMean.setText(arrayVocal.get(pos).getDescription());
                        txtFrtKanji.setText(arrayVocal.get(pos).getTuHan());
                        txtFrtHira.setText(null);
                        txtFrtEx1.setText(null);
                        txtFrtEx2.setText(null);
                    }
                    //dua back vs font ve lai vi tri
                    cardFront.animate().setDuration(duration).translationX(0).start();
                    cardBack.animate().setDuration(duration).translationX(0).start();

                    flipped = !flipped;
                }).start(); //now animate back layout in opposite direction, so put -value
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(FashcardActivity.this);
        }
        finish();
        return true;
    }
}