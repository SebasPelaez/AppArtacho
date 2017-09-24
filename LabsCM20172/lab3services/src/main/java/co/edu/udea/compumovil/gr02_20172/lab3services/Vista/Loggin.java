package co.edu.udea.compumovil.gr02_20172.lab3services.Vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20172.lab3services.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab3services.R;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User;
import retrofit2.Call;

public class Loggin extends AppCompatActivity implements View.OnClickListener{

    private EditText txtUsuario;
    private EditText txtClave;
    private static final String PREF_USER = "UserPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        txtClave = (EditText)findViewById(R.id.txtPassword);
        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
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
        Intent i;
        String username = txtUsuario.getText().toString();
        String password = txtClave.getText().toString();
        User user = new User();
        if(!username.equals("") && !password.equals("")){

            Map<String, String> mapa = new HashMap<String, String>();
            mapa.put("nombre", username);
            mapa.put("contrasena",password);

            RestClient restClient = RestClient.retrofit.create(RestClient.class);
            Call<User> call = restClient.loginUser(mapa);
            try {
                user = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                i = new Intent(Loggin.this, Principal.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SharedPreferences settings = getSharedPreferences(PREF_USER, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("userId",user.getId());
                editor.commit();
                startActivity(i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
        }else{
           Toast.makeText(getApplicationContext(),"Usuario y contraseña requeridos",Toast.LENGTH_SHORT).show();
        }
    }
}
