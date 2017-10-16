package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;

public class Loggin extends AppCompatActivity implements View.OnClickListener{

    private EditText txtUsuario;
    private EditText txtClave;

    private LoginButton loginButtonFacebook;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        progressBar = (ProgressBar)findViewById(R.id.progressBarLoging);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    goPrincipal(user);
                }
            }
        };

        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton)findViewById(R.id.login_button_facebook);
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"PASO ALGO MALO",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"PASO ALGO MALO",Toast.LENGTH_SHORT).show();
            }
        });

        txtClave = (EditText)findViewById(R.id.txtPassword);
        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButtonFacebook.setVisibility(View.GONE);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"PASO ALGO MALO",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnIngesar:
                loginUser();
                break;
            case R.id.txtRegistrar:
                i = new Intent(Loggin.this, Registro.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }


    private void loginUser(){
    }

    private void goPrincipal(FirebaseUser user) {
        Intent i;
        setUser(user);
        i = new Intent(Loggin.this, Principal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void setUser(FirebaseUser user) {
        User_Singleton.getInstance().setName(user.getDisplayName());
        User_Singleton.getInstance().setEmail(user.getEmail());
        User_Singleton.getInstance().setImage(String.valueOf(user.getPhotoUrl()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
