package co.edu.udea.compumovil.gr02_20172.lab1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Locale locale;
    private Configuration config = new Configuration();

    private Singleton_InformacionPersonal informacion;

    private TextView txtEmail_NavBar;
    private TextView txtUser_NavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Lo más seguro es que le hayas dado clic.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        informacion  = Singleton_InformacionPersonal.getInformacion();
        recuperarInformacion();
    }

    private void recuperarInformacion() {

        TextView nombre  = (TextView)findViewById(R.id.txtNombre_Informacion);
        TextView apellido  = (TextView)findViewById(R.id.txtApellido_Informacion);
        TextView telefono  = (TextView)findViewById(R.id.txtTelefono_Informacion);
        TextView direccion  = (TextView)findViewById(R.id.txtDireccion_Informacion);
        TextView email  = (TextView)findViewById(R.id.txtEmail_Informacion);
        TextView ciudad  = (TextView)findViewById(R.id.txtCiudad_Informacion);
        RadioButton genMasculino  = (RadioButton)findViewById(R.id.rbtnSexoMasculino_Informacion);
        RadioButton genFemenino  = (RadioButton)findViewById(R.id.rbtnSexoFemenino_Informacion);
        ImageView foto = (ImageView)findViewById(R.id.imgFoto_Informacion);

        nombre.setText(nombre.getText().toString()+": "+informacion.getData().getNombre());
        apellido.setText(apellido.getText().toString()+": "+informacion.getData().getApellidos());
        telefono.setText(telefono.getText().toString()+": "+informacion.getData().getTelefono());
        direccion.setText(direccion.getText().toString()+": "+informacion.getData().getDireccion());
        email.setText(email.getText().toString()+": "+informacion.getData().getEmail());
        ciudad.setText(ciudad.getText().toString()+": "+informacion.getData().getCiudad());

        if(informacion.getData().getGenero().equals("Masculino")){
            genMasculino.setChecked(true);
        }else{
            genFemenino.setChecked(true);
        }

        foto.setImageURI(Uri.parse(informacion.getData().getRuta_foto()));

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
        if (id == R.id.nav_actualizar) {
            i = new Intent(Principal.this, Registro.class);
            startActivity(i);
        } else if (id == R.id.nav_salir) {
            finish();
            i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
