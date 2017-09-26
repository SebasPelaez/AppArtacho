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
import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20172.lab3services.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab3services.R;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User_Singleton;
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
        List<User> userList = null;
        if(!username.equals("") && !password.equals("")){

            RestClient restClient = RestClient.retrofit.create(RestClient.class);
            Call<List<User>> call = restClient.getUsers();
            try {
                userList = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            User user = null;
            for(User u: userList){
                if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                    user=u;
                    break;
                }
            }
            if (user!=null) {
                setUser(user);
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

    private void setUser(User u) {
        User_Singleton.getInstance().setId(u.getId());
        User_Singleton.getInstance().setUsername(u.getUsername());
        User_Singleton.getInstance().setName(u.getName());
        User_Singleton.getInstance().setLastname(u.getLastname());
        User_Singleton.getInstance().setGender(u.getGender());
        User_Singleton.getInstance().setBirthday(u.getBirthday());
        User_Singleton.getInstance().setPhone(u.getPhone());
        User_Singleton.getInstance().setAddress(u.getAddress());
        User_Singleton.getInstance().setEmail(u.getEmail());
        User_Singleton.getInstance().setCity(u.getCity());
        User_Singleton.getInstance().setImage(u.getImage());
    }
}
