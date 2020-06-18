package com.richard.workout;

import androidx.appcompat.app.AppCompatActivity;
import database.dao.UtenteDao;
import database.pojo.Utente;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Utente utente;
    UtenteDao utenteDao;
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        WebView web = (WebView) findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        WebSettings w1 = web.getSettings();
        w1.setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/sfondo.html");
        web.setVisibility(View.VISIBLE);
    }

    public void getLogin(View view) {
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextpassword = (EditText) findViewById(R.id.editTextPassword);
        TextView readyExist2 = (TextView) findViewById(R.id.readyExist);
        if (!editTextEmail.getText().toString().isEmpty() && !editTextpassword.getText().toString().isEmpty()) {
            utente = new Utente();
            utente.setEmail(editTextEmail.getText().toString());
            utente.setPassword(editTextpassword.getText().toString());
        }
        if (utente != null && utente.getEmail() != null && utente.getPassword() != null) {
            try {
                utente = UtenteDao.resultUtente(UtenteDao.cercaUtente(utente), mydb);
                if (utente != null && utente.getSurname() != null) {
                    Intent intent2 = new Intent(this, MainActivity.class);
                    intent2.putExtra("nome", utente.getSurname());
                    startActivity(intent2);
                } else {
                    readyExist2.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                readyExist2.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getRegister(View view) {
        Intent intent1 = new Intent(this, RegisterActivity.class);
        startActivity(intent1);
    }
}