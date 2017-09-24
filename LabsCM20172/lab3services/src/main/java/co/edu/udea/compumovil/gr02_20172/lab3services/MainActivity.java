package co.edu.udea.compumovil.gr02_20172.lab3services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.udea.compumovil.gr02_20172.lab3services.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab3services.Vista.Loggin;
import co.edu.udea.compumovil.gr02_20172.lab3services.Vista.Principal;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User_Singleton;
import retrofit2.Call;

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
                        RestClient restClient = RestClient.retrofit.create(RestClient.class);
                        Call<User> call = restClient.getUser(userId);
                        User currentUser = null;
                        try {
                            currentUser = call.execute().body();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        User_Singleton.getInstance().setId(currentUser.getId());
                        User_Singleton.getInstance().setImage(currentUser.getImage());
                        User_Singleton.getInstance().setCity(currentUser.getCity());
                        User_Singleton.getInstance().setName(currentUser.getName());
                        User_Singleton.getInstance().setEmail(currentUser.getEmail());
                        User_Singleton.getInstance().setAddress(currentUser.getAddress());
                        User_Singleton.getInstance().setBirthday(currentUser.getBirthday());
                        User_Singleton.getInstance().setGender(currentUser.getGender());
                        User_Singleton.getInstance().setLastname(currentUser.getLastname());
                        User_Singleton.getInstance().setPassword(currentUser.getPassword());
                        User_Singleton.getInstance().setPhone(currentUser.getPhone());
                        User_Singleton.getInstance().setUsername(currentUser.getUsername());

                         Intent mainIntent = new Intent().setClass(
                            MainActivity.this, Principal.class);
                         startActivity(mainIntent);
                         finish();
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
        };

    }

