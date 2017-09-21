package co.edu.udea.compumovil.gr02_20172.lab3services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan on 1/09/2017.
 */

public class SQLiteConnectionHelper extends SQLiteOpenHelper {

    private final String CREATE_USER_TABLE= "CREATE TABLE user(" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "username text NOT NULL," +
            "password text NOT NULL," +
            "name text NOT NULL," +
            "last_name text NOT NULL," +
            "gender integer NOT NULL," +
            "birthday text," +
            "phone text," +
            "address text," +
            "email text," +
            "city text," +
            "image text" +
            ")";

    private final String CREATE_APARTAMENT_TABLE= "CREATE TABLE apartament(" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "apartamentname text NOT NULL," +
            "type text NOT NULL," +
            "value integer NOT NULL," +
            "id_user integer NOT NULL," +
            "area real NOT NULL," +
            "description text NOT NULL," +
            "location text NOT NULL," +
            "num_rooms integer NOT NULL," +
            "FOREIGN KEY (id_user) REFERENCES user(id)" +
            ")";

    private final String CREATE_RESOURCE_TABLE= "CREATE TABLE resource(" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "id_apartament integer NOT NULL," +
            "image text NOT NULL," +
            "FOREIGN KEY (id_apartament) REFERENCES apartament(id)" +
            ")";


    public SQLiteConnectionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_APARTAMENT_TABLE);
        db.execSQL(CREATE_RESOURCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
