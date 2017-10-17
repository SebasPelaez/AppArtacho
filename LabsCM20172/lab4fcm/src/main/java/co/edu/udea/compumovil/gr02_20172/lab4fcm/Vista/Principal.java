package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.Locale;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.IComunicaFragments;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.MainActivity;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment.Apartamentos;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment.DetalleApartamento;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment.Perfil;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment.RegistroApartamento;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment.SettingsActivity;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.User_Singleton;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IComunicaFragments,GoogleApiClient.OnConnectionFailedListener {

    private CircularImageView userImage;
    private TextView userNameView;
    private Locale locale;
    private Configuration config = new Configuration();
    private Fragment fragmentGenerico;
    private static final String PREF_USER = "UserPref";
    private static final String PREF_GENERAL = "pref_general";

    private GoogleApiClient googleApiClient;

    private IComunicaFragments actualizarApartamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentGenerico = new Apartamentos();
        changeFragment(fragmentGenerico);
        View headerLayout = navigationView.getHeaderView(0);
        userImage = (CircularImageView)headerLayout.findViewById(R.id.imageCircleUser);
        userNameView = (TextView)headerLayout.findViewById(R.id.txtUser_NavBar);
        User user = User_Singleton.getInstance();
        userNameView.setText(user.getName()+" "+ user.getLastname());
        String userI = user.getImage();
        if(userI != null && !userI.equals("")) {
            Glide.with(Principal.this)
                    .load(Uri.parse(user.getImage()))
                    .into(userImage);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(fragmentGenerico!=null){
            if(fragmentGenerico instanceof Apartamentos){
                return true;
            }
        }
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cambiarIdioma) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.cambiarIdioma));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("pt");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(Principal.this, Loggin.class);
                startActivity(refresh);
                finish();
            }
        });
        b.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.nav_apartamentos:
                fragmentGenerico = new Apartamentos();
                changeFragment(fragmentGenerico);
                break;
            case R.id.nav_perfil:
                fragmentGenerico = new Perfil();
                changeFragment(fragmentGenerico);
                break;
            case R.id.nav_configuraciones:
                i = new Intent(Principal.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_cerrarSesion:
                User_Singleton.destroyInstance();
                SharedPreferences settings = getSharedPreferences(PREF_USER, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("userId",0);
                editor.commit();
                logOut();
                break;
            case R.id.nav_actualizar:
                if(fragmentGenerico instanceof Apartamentos){
                    actualizarApartamentos = (Apartamentos)fragmentGenerico;
                    actualizarApartamentos.generarAccion("Actualizar_Aptos");
                }else{
                    Toast.makeText(this,"Se actualizaron los apartamentos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_acercaDe:
                i = new Intent(Principal.this, AboutActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment){
        // Create transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        // Create new fragment
        //Fragment fragmentA = new FragmentA();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // add the transaction to the back stack (optional)
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void enviarApartamento(Apartament apto) {
        DetalleApartamento detalleApartamento=new DetalleApartamento();
        Bundle bundleEnvio=new Bundle();
        bundleEnvio.putSerializable("objeto",apto);
        detalleApartamento.setArguments(bundleEnvio);
        changeFragment(detalleApartamento);
    }

    @Override
    public void generarAccion(String tag) {
        switch (tag){
            case "add_Apartamento":
                Fragment fragmentRegistroApartamento = new RegistroApartamento();
                changeFragment(fragmentRegistroApartamento);
                break;
            default:
                break;
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut(); //CIERRO SESIÓN EN FIREBASE SEA, CUAL SEA EL MÉTODO
        LoginManager.getInstance().logOut(); //CIERRO SESIÓN EN FACEBOOK
        logOutGoogle(); //CIERRO SESIÓN DE GOOGLE DIRECTAMENTE
        revokeGoogle(); // QUITO TODOS RASTRO DE QUE ENTRE CON GOOGLE
        returnLoginScreen(); //VUELVO A LA VENTANA DE LOGIN
    }

    private void returnLoginScreen(){
        Intent i;
        i = new Intent(Principal.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void logOutGoogle(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(!status.isSuccess()){
                    Toast.makeText(getApplicationContext(),"Ocurrió un error en logOutGoogle",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void revokeGoogle(){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(!status.isSuccess()){
                    Toast.makeText(getApplicationContext(),"Ocurrió un error en revokeGoogle",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}
