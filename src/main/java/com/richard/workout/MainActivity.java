package com.richard.workout;

import androidx.appcompat.app.AppCompatActivity;
import database.pojo.Utente;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase mydb;
    Utente utente ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS records (surname VARCHAR, workout VARCHAR, record VARCHAR, value INTEGER)");
        TextView textTraining = (TextView)findViewById(R.id.training);
        WebView web = (WebView) findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        WebSettings w1 = web.getSettings();
        w1.setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/sfondo.html");
        web.setVisibility(View.VISIBLE);
        Bundle b = getIntent().getExtras();
        b.getString("nome");
        utente = new Utente();
        utente.setSurname(b.getString("nome"));
        textTraining.setText("Welcome "+ b.getString("nome")+"\n are you ready ?");
    }

    public void getStrongMan(View view) {
        Intent intent2 = new Intent(this, StrongManActivity.class);
        intent2.putExtra("nome",utente.getSurname());
        startActivity(intent2);
    }

    public void getRecords(View view) {
        Intent intent2 = new Intent(this, RecordsActivity.class);
        intent2.putExtra("nome",utente.getSurname());
        startActivity(intent2);
    }

    public void getSixPack(View view) {
        Intent intent2 = new Intent(this, SixPackActivity.class);
        intent2.putExtra("nome",utente.getSurname());
        startActivity(intent2);

    }
}