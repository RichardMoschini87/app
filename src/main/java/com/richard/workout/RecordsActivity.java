package com.richard.workout;

import androidx.appcompat.app.AppCompatActivity;
import database.dao.RecordDao;
import database.pojo.Record;
import database.pojo.Utente;
import utils.Constants;
import utils.Converter;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private Utente utente;
    private static SQLiteDatabase mydb;
    private static Record rec = new Record();
    private String SQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        mydb = this.openOrCreateDatabase("Utenti", MODE_PRIVATE, null);
        Bundle b = getIntent().getExtras();
        TextView bestStrongman = (TextView) findViewById(R.id.bestStrongman);
        TextView worstStrongman = (TextView) findViewById(R.id.worstStrongman);
        TextView bestSixPack = (TextView) findViewById(R.id.bestSixPack);
        TextView worstSixPack = (TextView) findViewById(R.id.worstSixPack);


        utente = new Utente();
        utente.setSurname(b.getString("nome"));
        try {
            // Strongman
            rec = new Record();
            rec.setSurname(utente.getSurname());
            rec.setWorkout(Constants.WORKOUT);
            SQL = RecordDao.cercaRecord(rec);
            List<Record> recordList = RecordDao.resultRecord(SQL, mydb);
            Record bestStrong = Converter.maxRecord(recordList);
            Record worstStrong = Converter.minRecord(recordList);
            bestStrongman.setText(bestStrong.getRecord());
            worstStrongman.setText(worstStrong.getRecord());
            // Six Pack
            rec = new Record();
            rec.setSurname(utente.getSurname());
            rec.setWorkout(Constants.SIXPACK);
            SQL = RecordDao.cercaRecord(rec);
            List<Record> recordList1 = RecordDao.resultRecord(SQL, mydb);
            Record bestSixPackRecord = Converter.maxRecord(recordList);
            Record worstSixPackRecord = Converter.minRecord(recordList);
            bestSixPack.setText(bestSixPackRecord.getRecord());
            worstSixPack.setText(worstSixPackRecord.getRecord());
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}