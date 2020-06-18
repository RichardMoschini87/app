package com.richard.workout;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import database.dao.RecordDao;
import database.pojo.Record;
import database.pojo.Utente;
import utils.Constants;


public class SixPackActivity extends AppCompatActivity {

    private Chronometer cronometroStart;
    private Chronometer cronometroStart2;
    private Chronometer cronometroDuration;
    private TextView cronometroRest;
    private CountDownTimer countDownTimer;
    private long timeRestMill = 30000;//un 30 sec in millesimi
    private boolean runningDuration;
    private boolean running;
    private boolean runningRest = false;
    public static String records = "";
    Button buttonStart;
    private Utente utente;
    SQLiteDatabase mydb;
    public static int numberOfRest = 0;
    private long pauseOffset;
    boolean flex = false;
    boolean climb = false;
    boolean hand = false;
    boolean starfish = false;
    boolean crunch = false;
    boolean finishFirst = false;
    private MediaPlayer mediaOne;
    private MediaPlayer mediaRest;

    private AudioAttributes audioAttributes;
    private SoundPool sound;
    private static int sound1;
    private String ONEDOWN_URL = "file:///android_asset/OneDownTwoUps.html";
    private String HANDS_URL = "file:///android_asset/HandsFreeTucks.html";
    private String CRUNCH_URL = "file:///android_asset/Crunch.html";
    private String STARFISH_URL = "file:///android_asset/StarFishCrunch.html";
    private String TWISTING_URL = "file:///android_asset/TwistingPistons.html";
    private String REST_URL = "file:///android_asset/Rest.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_pack);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle b = getIntent().getExtras();
        // Creo la webView
        CreateWeb(ONEDOWN_URL);
        WebView web = (WebView) findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        WebSettings w1 = web.getSettings();
        w1.setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/sfondo.html");
        web.setVisibility(View.VISIBLE);
        // carico il suono
      mediaOne = MediaPlayer.create(this, R.raw.oneDown);
        mediaRest = MediaPlayer.create(this, R.raw.rest);
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
        utente = new Utente();
        utente.setSurname(b.getString("nome"));
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        cronometroStart = findViewById(R.id.cronometro);
        cronometroStart2 = findViewById(R.id.cronometro2);
        cronometroDuration = findViewById(R.id.cronometroDuration);
        cronometroDuration.setBase(SystemClock.elapsedRealtime());
        buttonStart = findViewById(R.id.buttonStart);
        cronometroRest = (TextView) findViewById(R.id.cronometroRest);


    }


    // metodo che avvia il Rest
    public void start() {
        if (!runningRest)
            startRest();
    }

    //metodo del Rest
    public void startRest() {
        mediaRest.start();
        cronometroStart.setVisibility(View.INVISIBLE);
        cronometroStart2.setVisibility(View.INVISIBLE);
        cronometroRest.setVisibility(View.VISIBLE);
        numberOfRest = numberOfRest + 1;
        countDownTimer = new CountDownTimer(timeRestMill, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                cronometroRest.setText("" + v + ":" + String.format("%02d", va));
            }

            @Override
            public void onFinish() {
                cronometroRest.setVisibility(View.INVISIBLE);
                running = false;
                runningRest = false;
                if (crunch) {
                    CreateWeb(HANDS_URL);
                } else {
                    CreateWeb(STARFISH_URL);
                }
                autoStart();
            }
        }.start();
    }

    //metodo del start  cronometro di 60 secondi
    public void autoStart() {
        if (!running) {
            cronometroStart.setVisibility(View.VISIBLE);
            cronometroStart.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            cronometroStart.start();
            mediaOne.start();
            running = true;
            cronometroStart.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //    minuti =  ( ( SystemClock . elapsedRealtime ( )  - matchChron . getBase ( ) )  /  1000 )  /  60 ; questo per calcolare i minuti
                    long seconds = ((SystemClock.elapsedRealtime() - cronometroStart.getBase()) / 1000) % 60;
                    if (seconds == 59) {
                        if (running) {
                            cronometroStart.setBase(SystemClock.elapsedRealtime());
                            pauseOffset = 0;
                            if (!flex) {
                                flex = true;
                                CreateWeb(TWISTING_URL);
                                cronometroStart.stop();
                            } else if (!climb) {
                                climb = true;
                                cronometroStart.stop();
                                finishFirst = true;
                                CreateWeb(REST_URL);
                                start();
                            } else if (!hand) {
                                cronometroStart.stop();
                                hand = true;
                                cronometroStart.setVisibility(View.INVISIBLE);
                                finishFirst = true;
                                running = false;
                                autoStart2();
                            } else if (!crunch) {
                                crunch = true;
                                cronometroStart.stop();
                                CreateWeb(REST_URL);
                                start();
                            }
                            //Qui finisce il workout
                            else if (!starfish) {
                                cronometroStart.stop();
                                cronometroStart2.stop();
                                cronometroStart2.setVisibility(View.INVISIBLE);
                                cronometroStart.setVisibility(View.INVISIBLE);
                            }
                        }
                        if (!finishFirst) {
                            cronometroStart.start();
                        }
                    }
                }
            });
            if (!runningDuration) {
                cronometroDuration.start();
                runningDuration = true;
            }
            buttonStart.setVisibility(View.INVISIBLE);
        }
    }

    // metodo del start cronometro da 30 secondi
    public void autoStart2() {
        if (!running) {
            cronometroStart.setVisibility(View.INVISIBLE);
            cronometroStart2.setVisibility(View.VISIBLE);
            cronometroStart2.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            cronometroStart2.start();
            CreateWeb(STARFISH_URL);
            running = true;
            cronometroStart2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    //    minuti =  ( ( SystemClock . elapsedRealtime ( )  - matchChron . getBase ( ) )  /  1000 )  /  60 ; questo per calcolare i minuti
                    long seconds = ((SystemClock.elapsedRealtime() - cronometroStart2.getBase()) / 1000) % 60;
                    if (seconds == 30) {
                        if (running) {
                            cronometroStart2.setBase(SystemClock.elapsedRealtime());
                            pauseOffset = 0;
                            cronometroStart2.stop();
                            cronometroStart2.setVisibility(View.INVISIBLE);
                            running = false;
                            CreateWeb(CRUNCH_URL);
                            autoStart();
                        }
                    }
                }
            });
        }
    }

    // onclisck start
    public void startCronometro(View view) {
        autoStart();
    }

    // onclick finish
    public void stopCronometro(View view) {
        cronometroStart2.setVisibility(View.INVISIBLE);
        cronometroStart.setVisibility(View.INVISIBLE);
        cronometroRest.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        cronometroStart.stop();
        cronometroStart2.stop();
        records = cronometroDuration.getContentDescription().toString();
        long time = SystemClock.elapsedRealtime() - cronometroDuration.getBase();
// qui tento di registrare il record
        records += " " + numberOfRest + " di Rest";
        Record rec = new Record();
        rec.setValue(time);
        rec.setRecord(records);
        rec.setWorkout(Constants.SIXPACK);
        rec.setSurname(utente.getSurname());
        try {
            mydb.execSQL(RecordDao.inserisciRecord(rec));
        } catch (Exception e) {
            System.out.println("Query Sbagliata");
        }
// cambio Activity
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putExtra("nome", utente.getSurname());
        startActivity(intent2);
    }

    private void CreateWeb(String url) {
        WebView webGif1 = (WebView) findViewById(R.id.webGif1);
        webGif1.setWebViewClient(new WebViewClient());
        WebSettings w1 = webGif1.getSettings();
        w1.setJavaScriptEnabled(true);
        webGif1.loadUrl(url);
        webGif1.setVisibility(View.VISIBLE);
    }


}
