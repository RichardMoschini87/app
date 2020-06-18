package com.richard.workout;

import androidx.appcompat.app.AppCompatActivity;
import database.dao.RecordDao;
import database.pojo.Record;
import database.pojo.Utente;
import utils.Constants;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

public class StrongManActivity extends AppCompatActivity {

    private Chronometer cronometroStart;
    private Chronometer cronometroDuration;
    private TextView cronometroRest;
    private ImageView imgPull;
    private ImageView imgFlex;
    private CountDownTimer countDownTimer;
    private long timeRestMill = 90000;//un minuto e mezzo in millesimi
    private boolean runningDuration;
    private boolean running;
    private boolean runningRest;
    public static String records = "";
    Button buttonStart;
    Button buttonRest;
    Button button;
    private Utente utente;
    SQLiteDatabase mydb;
    AudioAttributes audioAttributes;
    private SoundPool sound;
    private SoundPool soundCount;
    private int sound1;
    private int sound2;
    MediaPlayer media;
    public static int numberOfRest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strong_man);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle b = getIntent().getExtras();
        WebView web = (WebView) findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        WebSettings w1 = web.getSettings();
        w1.setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/sfondo.html");
        web.setVisibility(View.VISIBLE);
        //prova con Mediaplayer
        media = MediaPlayer.create(this, R.raw.soundtry);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                audioAttributes = new AudioAttributes.Builder().
                        setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).
                        build();
                sound = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
                //  soundCount = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        }
        sound1 = sound.load(this, R.raw.prova, 1);
        // sound2 = soundCount.load(this, R.raw.soundtry, 1);
        utente = new Utente();
        utente.setSurname(b.getString("nome"));
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        cronometroStart = findViewById(R.id.cronometro);
        cronometroDuration = findViewById(R.id.cronometroDuration);
        cronometroDuration.setBase(SystemClock.elapsedRealtime());
        buttonStart = findViewById(R.id.buttonStart);
        buttonRest = findViewById(R.id.buttonRest);
        cronometroRest = (TextView) findViewById(R.id.cronometroRest);
        imgPull = findViewById(R.id.imgPull);
        imgFlex = findViewById(R.id.imgAds);
        buttonRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    public void start() {
        if (!runningRest)
            startRest();
    }

    public void startRest() {
        cronometroStart.setVisibility(View.INVISIBLE);
        cronometroRest.setVisibility(View.VISIBLE);
        buttonRest.setVisibility(View.INVISIBLE);
        numberOfRest = numberOfRest + 1;
        countDownTimer = new CountDownTimer(timeRestMill, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                cronometroRest.setText("" + v + ":" + String.format("%02d", va));
                // controllo la stringa per settare volume e attivare audio countDown
                if (cronometroRest.getText().toString().contains("00:06")) {
                    sound.setVolume(sound1, 0.2f, 0.2f);
                    media.start();
                }
                if (cronometroRest.getText().toString().contains("00:00")) {
                    sound.setVolume(sound1, 1.0f, 1.0f);
                    media.stop();
                }
            }

            @Override
            public void onFinish() {
                cronometroRest.setVisibility(View.INVISIBLE);
                cronometroStart.setVisibility(View.VISIBLE);
                buttonRest.setVisibility(View.VISIBLE);
                running = false;
            }
        }.start();
    }

    public void startCronometro(View view) {
        if (!running) {
            sound.play(sound1, 1.0f, 1.0f, 1, -1, 1);
            cronometroStart.start();
            cronometroStart.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //    minuti =  ( ( SystemClock . elapsedRealtime ( )  - matchChron . getBase ( ) )  /  1000 )  /  60 ; questo per calcolare i minuti
                    long seconds = ((SystemClock.elapsedRealtime() - cronometroStart.getBase()) / 1000) % 60;
                    if (seconds > 30) {
                        imgFlex.setVisibility(View.INVISIBLE);
                        imgPull.setVisibility(View.VISIBLE);
                    }
                    if (seconds < 30) {
                        imgFlex.setVisibility(View.VISIBLE);
                        imgPull.setVisibility(View.INVISIBLE);
                    }
                }
            });
            running = true;
            if (!runningDuration) {
                cronometroDuration.start();
                runningDuration = true;
            }
            buttonStart.setVisibility(View.INVISIBLE);
            buttonRest.setVisibility(View.VISIBLE);
        }
    }

    public void stopCronometro(View view) {
        sound.release();
        sound = null;
        cronometroStart.setVisibility(View.INVISIBLE);
        cronometroRest.setVisibility(View.INVISIBLE);
        buttonRest.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        imgFlex.setVisibility(View.INVISIBLE);
        imgPull.setVisibility(View.INVISIBLE);
        cronometroStart.stop();

        records = cronometroDuration.getContentDescription().toString();
        long time = SystemClock.elapsedRealtime() - cronometroDuration.getBase();
        records += " " + numberOfRest + " di Rest";
        Record rec = new Record();
        rec.setValue(time);
        rec.setRecord(records);
        rec.setWorkout(Constants.WORKOUT);
        rec.setSurname(utente.getSurname());
        if (time < 180000) {
            try {
                mydb.execSQL(RecordDao.inserisciRecord(rec));
            } catch (Exception e) {
                System.out.println("Query Sbagliata");
            }
        }

        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putExtra("nome", utente.getSurname());
        startActivity(intent2);
    }
}