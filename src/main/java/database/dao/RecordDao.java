package database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import database.pojo.Record;

public class RecordDao {
    private static String SQL;

    public static String inserisciRecord(Record records) {
        SQL = "";
        if (records != null && records.getSurname() != null && records.getWorkout() != null && records.getRecord() != null)
            SQL = "INSERT INTO records (surname,workout,record,value) VALUES('" + records.getSurname() + "','" + records.getWorkout() + "','" + records.getRecord() + "'," + records.getValue() + ")";
        return SQL;
    }

    public static String cercaRecord(Record records) {
        SQL = "";
        if (records != null && records.getSurname() != null)
            SQL = "SELECT * FROM records WHERE 1=1 AND surname = '" + records.getSurname() + "'";
        if(records.getWorkout() != null)
            SQL += " AND workout = '"+records.getWorkout()+"'";
        return SQL;
    }

    public static List<Record> resultRecord(String SQL, SQLiteDatabase mydb) {
        List<Record> recordList = new ArrayList<>();
        Record r;
        Cursor c = mydb.rawQuery(SQL, null);

        while (c.moveToNext()) {
            int index = 0;
            r = new Record();
            r.setSurname(c.getString(index++));
            r.setWorkout(c.getString(index++));
            r.setRecord(c.getString(index++));
            r.setValue(c.getLong(index));
            recordList.add(r);
        }
        c.close();
        mydb.close();
        return recordList;
    }
}
