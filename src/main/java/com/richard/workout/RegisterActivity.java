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

public class RegisterActivity extends AppCompatActivity {

    Utente utente;
    UtenteDao utenteDao;
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        WebView web = (WebView) findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        WebSettings w1 = web.getSettings();
        w1.setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/sfondo.html");
        web.setVisibility(View.VISIBLE);
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS utente (surname VARCHAR, email VARCHAR, password VARCHAR)");
    }

    public void getRegistrazione(View view) {
        EditText editTextTextPersonName = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextpassword = (EditText) findViewById(R.id.editTextPassword);
        TextView readyExist = (TextView) findViewById(R.id.readyExist);

        if (!editTextTextPersonName.getText().toString().isEmpty() && !editTextEmail.getText().toString().isEmpty() && !editTextpassword.getText().toString().isEmpty()) {
            utente = new Utente();
            utente.setSurname(editTextTextPersonName.getText().toString());
            utente.setEmail(editTextEmail.getText().toString());
            utente.setPassword(editTextpassword.getText().toString());

            if (utente != null && utente.getSurname() != null && !utente.getSurname().contains("Surname")
                    && utente.getEmail() != null && utente.getPassword() != null && !utente.getEmail().contains("Email")) {
                try {
                    mydb.execSQL(UtenteDao.inserisciUtente(utente));
                    mydb.close();
                    Intent intent2 = new Intent(this, LoginActivity.class);
                    startActivity(intent2);
                } catch (Exception e) {
                    readyExist.setVisibility(View.VISIBLE);
                }
            } else {
                readyExist.setVisibility(View.VISIBLE);
            }
        }
    }
}