package co.edu.udea.compumovil.gr02_20172.lab4fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Loggin;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Principal;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                if(userFirebase == null){//PREGUNTO PPR DONDE ESTA LOGUEADO
                    goLogin();
                }else{
                    User_Singleton.getInstance().setName(userFirebase.getDisplayName());
                    User_Singleton.getInstance().setEmail(userFirebase.getEmail());
                    User_Singleton.getInstance().setImage(String.valueOf(userFirebase.getPhotoUrl()));
                     Intent mainIntent = new Intent().setClass(
                        MainActivity.this, Principal.class);
                     startActivity(mainIntent);
                     finish();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        }

    private void goLogin() {
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, Loggin.class);
        startActivity(mainIntent);
        finish();
    }

    }

