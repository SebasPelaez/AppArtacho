package co.edu.udea.compumovil.gr02_20172.lab4fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private static final String PREF_USER = "UserPref";
    private List<User> listaUsuarios;
    private DatabaseReference databaseReference;//REFERENCIAS A LA BASE DE DATOS DE FIREBASE
    private DatabaseReference userReference;//REFERENCIA A UN HIJO EN LA BASE DE DATOS.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();//INSTANCIA LA BASE DE DATOS DE FIREBASE
        userReference = databaseReference.child("Usuario");//SE PARA EN EL HIJO USUARIO
        listaUsuarios = new ArrayList<>();
        listarUsuarios();

        FacebookSdk.sdkInitialize(getApplicationContext()); //INICIALIZAMOS EL SDK DE FACEBOOK PARA GUARDAR REGISTRO DE LO QUE PASE CON ESTA API
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
                if(SesionParams()){
                    FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                    if(userFirebase == null){//PREGUNTO PPR DONDE ESTA LOGUEADO
                        goLogin();
                    }else{
                        usuarioActivo(userFirebase);
                        Intent mainIntent = new Intent().setClass(
                                MainActivity.this, Principal.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }else{
                    goLogin();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        }

    private boolean SesionParams() {
        SharedPreferences settings = getSharedPreferences(PREF_USER,0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final boolean keepS = prefs.getBoolean("keep_session",true);
        return keepS;
    }

    private void goLogin() {
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, Loggin.class);
        startActivity(mainIntent);
        finish();
    }

    private void usuarioActivo(FirebaseUser userFirebase){
        User_Singleton.getInstance().setName(userFirebase.getDisplayName());
        User_Singleton.getInstance().setEmail(userFirebase.getEmail());
        User_Singleton.getInstance().setImage(String.valueOf(userFirebase.getPhotoUrl()));

        encontrarUsuario();
    }

    private void encontrarUsuario() {
        User singleUser = User_Singleton.getInstance();
        for(User u: listaUsuarios){
            if(u.getName().equals(singleUser.getName()) || u.getEmail().equals(singleUser.getEmail())){
                setUser(u);
                break;
            }
        }
    }

    private void listarUsuarios() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    User note = noteSnapshot.getValue(User.class);
                    listaUsuarios.add(note);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });
    }

    private void setUser(User u){
        User_Singleton.getInstance().setId(u.getId());
        User_Singleton.getInstance().setUsername(u.getName());
        User_Singleton.getInstance().setPassword(u.getPassword());
        User_Singleton.getInstance().setLastname(u.getLastname());
        User_Singleton.getInstance().setGender(u.getGender());
        User_Singleton.getInstance().setBirthday(u.getBirthday());
        User_Singleton.getInstance().setPhone(u.getPhone());
        User_Singleton.getInstance().setAddress(u.getAddress());
        User_Singleton.getInstance().setCity(u.getCity());
    }

}

