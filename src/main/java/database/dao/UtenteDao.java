package database.dao;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import database.pojo.Utente;

public class UtenteDao {
    private Utente utente;
    private static String SQL;

    public UtenteDao() {
        super();
        this.utente = utente;
    }

    public static String cercaUtente(Utente utente) {
        SQL = "";
        if (utente != null)
            SQL = "SELECT surname,email,password FROM utente WHERE password = '" + utente.getPassword() + "'";
        return SQL;
    }

    public static Utente resultUtente(String SQL, SQLiteDatabase mydb) {
        int index = 0;
        Utente u = new Utente();
        Cursor c = mydb.rawQuery(SQL, null);
        if (c.moveToFirst()) {
            do {
                u.setSurname(c.getString(index++));
                u.setEmail(c.getString(index++));
                u.setPassword(c.getString(index));
            } while (c.moveToNext());
            c.close();
            mydb.close();
        }
        return u;
    }

    public static String inserisciUtente(Utente utente) {
        SQL = "";
        if (utente != null && utente.getSurname() != null && utente.getEmail() != null && utente.getPassword() != null)
            SQL = "INSERT INTO utente (surname,email,password) VALUES('" + utente.getSurname() + "','" + utente.getEmail() + "','" + utente.getPassword() + "')";
        return SQL;
    }

}
