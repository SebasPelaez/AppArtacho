package co.edu.udea.compumovil.gr02_20172.lab3services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import co.edu.udea.compumovil.gr02_20172.lab3services.Vista.Loggin;
import co.edu.udea.compumovil.gr02_20172.lab3services.Vista.Principal;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    private static final String PREF_USER = "UserPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREF_USER, 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final boolean keepS = prefs.getBoolean("keep_session",true);
        final int  userId = settings.getInt("userId",0);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(userId == 0){
                    Intent mainIntent = new Intent().setClass(
                            MainActivity.this, Loggin.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    if(keepS == true){
                        User user = User.getInstance();
                        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(getApplicationContext(),"db_lab",null,1);
                        SQLiteDatabase db = connectionDb.getReadableDatabase();
                        String[] params = {Integer.toString(userId)};
                        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE id=?",params);
                        if (cursor.moveToFirst()) {
                            do {
                                user.setId(userId);
                                user.setUsername(cursor.getString(1));
                                user.setName(cursor.getString(3));
                                user.setLastname(cursor.getString(4));
                                user.setGender(cursor.getInt(5));
                                user.setBirthday(cursor.getString(6));
                                user.setPhone(cursor.getString(7));
                                user.setAddress(cursor.getString(8));
                                user.setEmail(cursor.getString(9));
                                user.setCity(cursor.getString(10));
                                user.setImage(cursor.getString(11));
                            } while(cursor.moveToNext());
                            Intent mainIntent = new Intent().setClass(
                                    MainActivity.this, Principal.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    }else{
                        Intent mainIntent = new Intent().setClass(
                                MainActivity.this, Loggin.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(this,"db_lab",null,1);
        };

    }

