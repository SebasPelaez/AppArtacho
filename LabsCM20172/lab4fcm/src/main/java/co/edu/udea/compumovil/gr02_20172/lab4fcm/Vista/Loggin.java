package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista;

import android.content.Intent;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;

public class Loggin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private EditText txtUsuario;
    private EditText txtClave;

    private LoginButton loginButtonFacebook; //BOTÓN DE INICIO DE FACEBOOK
    private SignInButton loginButtonGoogle; //BOTÓN DE INICIO DE GOOGLE

    private CallbackManager callbackManager; //CALLBACK PARA LOS LLAMADOS DE FACEBOOK
    private FirebaseAuth firebaseAuth; //VARIABLE QUE MANEJARA EL LOGUEO RELACIONADO A FIREBASE
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //LISTENER DE FIREBASE QUE REGISTRA EVENTOS
    private GoogleApiClient googleApiClient; //COMPONENTE PARA USAR LOS SERVICIOS DE GOOGLE

    private ProgressBar progressBar;
    private static final int SIGN_IN_GOOGLE_CODE = 777; //CÓGIDO PARA VALIDAR SI ESCOGÍ CORRECTAMENTE UNA CUENTA DE GOOGLE

    private DatabaseReference databaseReference;//REFERENCIAS A LA BASE DE DATOS DE FIREBASE
    private DatabaseReference userReference;//REFERENCIA A UN HIJO EN LA BASE DE DATOS.

    private List<User> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        progressBar = (ProgressBar)findViewById(R.id.progressBarLoging); //SE INFLA EL PROGRESS BAR
        loginButtonGoogle = (SignInButton) findViewById(R.id.login_button_google); //SE INFLA EL BOTON DE GOOGE
        loginButtonFacebook = (LoginButton)findViewById(R.id.login_button_facebook); //SE INFLA EL BOTON DE FACEBOOK
        txtClave = (EditText)findViewById(R.id.txtPassword); //SE INFLA EL CAMPO DE PASSWORD
        txtUsuario = (EditText)findViewById(R.id.txtUsuario); //SE INFLA EL CAMPO DE USUARIO
        firebaseAuth = FirebaseAuth.getInstance(); //SE INICIALIZA LA VARIABLE DE FIREBASE QUE MANEJA TODOS LOS LOGUEOS
        callbackManager = CallbackManager.Factory.create(); //SE INICIALIZA LA VARIABLE DE FACEBOOK QUE MANEJA LOS LOGUEOS

        loginButtonGoogle.setColorScheme(loginButtonGoogle.COLOR_DARK);
        loginButtonGoogle.setSize(loginButtonGoogle.SIZE_WIDE);

        databaseReference = FirebaseDatabase.getInstance().getReference();//INSTANCIA LA BASE DE DATOS DE FIREBASE
        userReference = databaseReference.child("Usuario");//SE PARA EN EL HIJO USUARIO

        /**
         * QUIZA EL LISTENER MAS IMPORTANTE, ES EL QUE VA A ESTAR A CARGO DE TODO LO QUE SE PUEDA
         * ESCUCHAR EN CUANTO A FIREBASE SE REFIERE
         */
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();//TOMAMOS EL USUARIO ACTUAL
                if(user!=null){
                    setUser(user);//SETEA UN USUARIO QUE ENTRA POR RED SOCIAL
                    validarUsuarioExistente();//VERIFICA SI EXISTE EL USUARIO, SI NO CREA UN NUEVO REGISTRO
                    goPrincipalScreen();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuth(loginResult.getAccessToken());
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

        loginButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i,SIGN_IN_GOOGLE_CODE);
            }
        });

        listaUsuarios = new ArrayList<>();
        listarUsuarios();

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

    private void firebaseAuth(Object objectCredetial){
        hideButtons(); //OCULTA LOS COMPONENTES BOTONES
        AuthCredential credential;
        if(objectCredetial instanceof AccessToken){ //VERIFICA DE QUE TIPO VA A SER LA CREDENCIAL
            //SI ENTRA AQUÍ ES PORQUE LA FORMA DE LOGGEO FUE CON FACEBOOK
            AccessToken accessToken = (AccessToken)objectCredetial;
            credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        }else{
            //SI ENTRA AQUÍ ES PORQUE LA FORMA DE LOGGUEO FUE CON GOOGLE
            GoogleSignInAccount signInAccount = (GoogleSignInAccount) objectCredetial;
            credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        }
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"PASO ALGO MALO", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void hideButtons() {
        progressBar.setVisibility(View.VISIBLE);
        loginButtonGoogle.setVisibility(View.GONE);
        loginButtonFacebook.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener!=null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
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
        String username = txtUsuario.getText().toString();
        String password = txtClave.getText().toString();
        if(!username.equals("") && !password.equals("")){
            User uLogueado=validarUsuarioContrasena(username,password);
            if(uLogueado!=null){
                User_Singleton.getInstance().setId(uLogueado.getId());
                User_Singleton.getInstance().setUsername(uLogueado.getUsername());
                User_Singleton.getInstance().setPassword(uLogueado.getPassword());
                User_Singleton.getInstance().setName(uLogueado.getName());
                User_Singleton.getInstance().setLastname(uLogueado.getLastname());
                User_Singleton.getInstance().setGender(uLogueado.getGender());
                User_Singleton.getInstance().setBirthday(uLogueado.getBirthday());
                User_Singleton.getInstance().setPhone(uLogueado.getPhone());
                User_Singleton.getInstance().setAddress(uLogueado.getAddress());
                User_Singleton.getInstance().setEmail(uLogueado.getEmail());
                User_Singleton.getInstance().setCity(uLogueado.getCity());
                User_Singleton.getInstance().setImage(String.valueOf(uLogueado.getImage()));
                goPrincipalScreen();
            }
        }
    }

    private void goPrincipalScreen() {
        Intent i;
        i = new Intent(Loggin.this, Principal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void validarUsuarioExistente() {
        User singleUser = User_Singleton.getInstance();
        for(User u: listaUsuarios){
            if(u.getName().equals(singleUser.getName()) || u.getEmail().equals(singleUser.getEmail())){
                singleUser.setId(u.getId());
                break;
            }
        }
        if(singleUser.getId().equals(""))
            registerUser();
    }

    public User validarUsuarioContrasena(String username,String password){
        for(User u: listaUsuarios){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                return u;
            }
        }
        return null;
    }

    private void setUser(FirebaseUser user) {
        User_Singleton.getInstance().setId("");
        User_Singleton.getInstance().setUsername("");
        User_Singleton.getInstance().setPassword("");
        User_Singleton.getInstance().setName(user.getDisplayName());
        User_Singleton.getInstance().setLastname("");
        User_Singleton.getInstance().setGender(-1);
        User_Singleton.getInstance().setBirthday("");
        User_Singleton.getInstance().setPhone("");
        User_Singleton.getInstance().setAddress("");
        User_Singleton.getInstance().setEmail(user.getEmail());
        User_Singleton.getInstance().setCity("");
        User_Singleton.getInstance().setImage(String.valueOf(user.getPhotoUrl()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == SIGN_IN_GOOGLE_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            firebaseAuth(result.getSignInAccount());
        }else{
            Toast.makeText(getApplicationContext(),"PASO ALGO MALO LOGUEO CON GOOGLE",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void registerUser(){
        String key = userReference.push().getKey();
        User u = new User();
        u.setId(key);
        u.setEmail(User_Singleton.getInstance().getEmail());
        u.setImage(User_Singleton.getInstance().getImage());
        u.setName(User_Singleton.getInstance().getName());
        u.setUsername("");
        u.setPassword("");
        u.setLastname("");
        u.setGender(0);//Prueba con un Integer
        u.setBirthday("");
        u.setPhone("");
        u.setAddress("");
        u.setCity("");
        userReference.child(key).setValue(u);
    }
}