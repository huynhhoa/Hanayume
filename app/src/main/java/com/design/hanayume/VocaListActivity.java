package com.design.hanayume;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.design.hanayume.Adapter.VocaAdapter;
import com.design.hanayume.Adapter.VocaListGetSet;
import com.design.hanayume.Database.InitDatabase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class VocaListActivity extends AppCompatActivity {
    private GridView listVocal;
    private int isCheckedReme=0, lesson = 1, positionOfRecord=0, level = 5, rememberCheck;
    private ArrayList<VocaListGetSet> arrayVocal;
    private VocaAdapter adapter;
    private Cursor cursor;
    private String table=null;
    private InterstitialAd mInterstitialAd;
    private SQLiteDatabase database;
    private CheckBox cbRememberList;
    private Spinner spinner;
    private List<String> spnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocalist);
        //Quang cao bieu ngu
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //quang cao full
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

        //Nhan du lieu tu Intent
        Intent intent = getIntent();
        level = intent.getIntExtra("levelMain", 0);
        //Anh xa
        AnhXa();

        //lay database
        database = InitDatabase.initDatabase(this,"hanayume.db");

        Objects.requireNonNull(getSupportActionBar()).setTitle("N"+level);
        cursor = database.rawQuery("SELECT * FROM N5 WHERE Remember = 0 AND JLPT = "+level+" AND Lession = "+lesson +"",null);
        getDatabase();
        adapter.notifyDataSetChanged();

        showFashCard();
        //cbRememberlist check
        cbRememberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRememberList.isChecked()){
                    cursor = database.rawQuery("SELECT * FROM N5 WHERE Remember = 1 AND JLPT = "+level+" AND Lession = "+lesson +"", null);
                }else {
                    cursor = database.rawQuery("SELECT * FROM N5 WHERE Remember = 0 AND JLPT = "+level+" AND Lession = "+lesson +"",null);
                }
                getDatabase();
                adapter.notifyDataSetChanged();
            }
        });
        //spinner event
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lesson = position +1 ;
                if(cbRememberList.isChecked()){
                    rememberCheck = 1;
                }else rememberCheck = 0;
                cursor = database.rawQuery("SELECT * FROM N5 WHERE Remember = "+ rememberCheck +" AND JLPT = "+level+" AND Lession = "+lesson +"", null);
                getDatabase();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void AnhXa(){
        cbRememberList = findViewById(R.id.cbRememberList);
        listVocal = findViewById(R.id.lstVoca);
        Toolbar toolbar = findViewById(R.id.tbVoca);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        arrayVocal = new ArrayList<>();
        System.out.println("Arrvay vocal : "+ arrayVocal.size());
        adapter = new VocaAdapter(this,R.layout.line_vocalist,arrayVocal);
        listVocal.setAdapter(adapter);
        //spinner
        spinner = findViewById(R.id.spinner);
        spnList = new ArrayList<>();
        spnList.add("Bài 1");
        if(level < 5 ){
            spnList.add("Bài 2");
            if(level < 4){
                spnList.add("Bài 3");
            }
        }
        ArrayAdapter spnArray = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spnList);
        spinner.setAdapter(spnArray);
    }
    public void showFashCard(){
      //su kien click vao item
        listVocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idTranfer = arrayVocal.get(position).getId();
                if(cbRememberList.isChecked()){
                    rememberCheck = 1;
                }else rememberCheck = 0;
                if(arrayVocal.size() > 0 ){

                    Intent intent = new Intent(VocaListActivity.this, FashcardActivity.class);
                    intent.putExtra("vocalPosition", idTranfer);
                    intent.putExtra("lesson",lesson);
                    intent.putExtra("level",level);
                    intent.putExtra("rememberCheck", rememberCheck);
                    startActivity(intent);
                }
            }
        });
    }
    public void updateData(int id,int isCheck){
        database.execSQL("UPDATE N5 SET Remember = "+isCheck+" WHERE ID = "+id+"");
        if (mInterstitialAd != null) {
            mInterstitialAd.show(VocaListActivity.this);
        }

        if(isCheck==1){
            Toast.makeText(this,"Đã lưu!", Toast.LENGTH_SHORT).show();
        }

    }
    public void getDatabase(){
        arrayVocal.clear();
        while (cursor.moveToNext()){
            int idData = cursor.getInt(0);
            String kanji = cursor.getString(1);
            String tuHan = cursor.getString(2);
            boolean remember = cursor.getInt(6) > 0;
            arrayVocal.add(new VocaListGetSet(idData,kanji, tuHan,remember));
        }
        if (mInterstitialAd != null) {
            mInterstitialAd.show(VocaListActivity.this);
        }
    }
}
